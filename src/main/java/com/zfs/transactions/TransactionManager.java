package com.zfs.transactions;

import java.io.IOException;
import java.util.HashMap;

/**
 * Manager für Transaktionen mit ZFS-Snapshots und Konflikterkennung basierend auf Hash-Werten.
 */
public class TransactionManager {

    // Speichert die initialen Hash-Werte der Dateien
    private final HashMap<String, String> initialHashes = new HashMap<>();

    /**
     * Startet eine neue Transaktion und erstellt einen Snapshot.
     *
     * @param snapshotName Name des Snapshots
     * @param filePath Pfad der zu bearbeitenden Datei
     * @throws Exception falls ein Fehler beim Erstellen des Snapshots auftritt
     */
    public void startTransaction(String snapshotName, String filePath) throws Exception {
        // Hash der Datei berechnen und speichern
        String initialHash = FileOperations.calculateHash(filePath);
        initialHashes.put(filePath, initialHash);

        // Snapshot erstellen
        ZfsManager.createSnapshot(snapshotName);
        System.out.println("Transaktion gestartet: " + snapshotName + " mit initialem Hash: " + initialHash);
    }

    /**
     * Führt einen Commit durch, falls kein Konflikt vorliegt.
     *
     * @param snapshotName Name des Snapshots
     * @param filePath Pfad der Datei
     * @throws Exception falls ein Konflikt erkannt wird oder ein anderer Fehler auftritt
     */
    public void commitTransaction(String snapshotName, String filePath, String existingContent) throws Exception {
        // Aktuellen Hash der Datei berechnen
        String currentHash = FileOperations.calculateHash(filePath);

        // Initialen Hash abrufen
        String initialHash = initialHashes.get(filePath);

        // Konflikt prüfen
        if (!initialHash.equals(currentHash)) {
	    rollbackTransaction(snapshotName);
            throw new Exception("Konflikt erkannt! Die Datei wurde seit dem Start der Transaktion verändert.");
        } else {
	    FileOperations.writeFile(filePath, existingContent);
	    System.out.println("Kommentar hinzugefügt.");
	}

        // Snapshot für Commit behalten (kein zusätzlicher Schritt nötig, falls alles ok ist)
        System.out.println("Transaktion erfolgreich abgeschlossen: " + snapshotName);
        // Hash löschen (optional, falls keine weitere Speicherung nötig ist)
        initialHashes.remove(filePath);
    }

    /**
     * Setzt eine Transaktion auf den Zustand des Snapshots zurück.
     *
     * @param snapshotName Name des Snapshots
     * @throws Exception falls ein Fehler beim Rollback auftritt
     */
    public void rollbackTransaction(String snapshotName) throws Exception {
        ZfsManager.rollbackSnapshot(snapshotName);
        System.out.println("Transaktion zurückgesetzt: " + snapshotName);
    }

    /**
     * Löscht einen existierenden Snapshot, falls er vorhanden ist.
     *
     * @param snapshotName Name des Snapshots
     * @throws IOException falls ein Fehler beim Löschen auftritt
     */
    public void deleteSnapshotIfExists(String snapshotName) throws IOException {
        ZfsManager.deleteSnapshot(snapshotName);
    }
}

