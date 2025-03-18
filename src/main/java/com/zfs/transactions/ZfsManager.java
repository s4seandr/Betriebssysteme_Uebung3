package com.zfs.transactions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manager für die Interaktion mit ZFS-Snapshots.
 */
public class ZfsManager {

    /**
     * Generiert einen eindeutigen Snapshot-Namen mit Zeitstempel.
     *
     * @param baseName Der Basisname für den Snapshot
     * @return Ein eindeutiger Snapshot-Name mit Zeitstempel
     */
    public static String generateSnapshotName(String baseName) {
        // Erstelle Zeitstempel mit Millisekunden
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        return baseName + "_" + timestamp;
    }

    /**
     * Erstellt einen Snapshot mit dynamischem Namen basierend auf dem Basisnamen.
     *
     * @param baseName Der Basisname für den Snapshot
     * @throws IOException Falls der Snapshot-Befehl fehlschlägt
     */
    public static void createSnapshot(String baseName) throws IOException {
        // Generiere den Snapshot-Namen
        String snapshotName = generateSnapshotName(baseName);
        // ZFS-Befehl ausführen
        executeCommand("sudo zfs snapshot testpool/testdir@" + snapshotName);
        System.out.println("Snapshot erstellt: " + snapshotName);
    }

    /**
     * Setzt das Verzeichnis auf den Zustand des letzten Snapshots zurück.
     *
     * @throws IOException Falls der Rollback-Befehl fehlschlägt oder kein Snapshot existiert
     */
    public static void rollbackSnapshot(String snapshotName) throws IOException {
        // Letzten Snapshot abrufen
        String lastSnapshot = getLastSnapshot("testpool/testdir");
        if (lastSnapshot != null) {
            // Rollback auf den letzten Snapshot
            executeCommand("sudo zfs rollback -r " + lastSnapshot);
            System.out.println("Rollback erfolgreich für letzten Snapshot: " + lastSnapshot);
        } else {
            System.err.println("Kein Snapshot gefunden. Rollback nicht möglich.");
        }
    }

    /**
     * Löscht einen Snapshot.
     *
     * @param snapshotName Name des Snapshots
     * @throws IOException Falls der Lösch-Befehl fehlschlägt
     */
    public static void deleteSnapshot(String snapshotName) throws IOException {
        executeCommand("sudo zfs destroy -r testpool/testdir@" + snapshotName);
        System.out.println("Snapshot gelöscht: " + snapshotName);
    }

    /**
     * Ermittelt den Namen des zuletzt erstellten Snapshots für ein Zielverzeichnis.
     *
     * @param targetPath Der ZFS-Pfad, z. B. "testpool/testdir"
     * @return Der Name des letzten Snapshots oder null, falls keiner gefunden wurde
     * @throws IOException Falls ein Fehler beim Abrufen der Snapshots auftritt
     */
    private static String getLastSnapshot(String targetPath) throws IOException {
        String command = "zfs list -t snapshot -o name -s creation | grep " + targetPath;
        Process process = Runtime.getRuntime().exec(command);

        List<String> snapshots = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                snapshots.add(line.trim());
            }
        }

        // Den letzten Snapshot finden
        if (!snapshots.isEmpty()) {
            // Sortierung ist durch ZFS "creation" bereits gewährleistet
            return snapshots.get(snapshots.size() - 1); // Letztes Element
        } else {
            return null; // Kein Snapshot gefunden
        }
    }

    /**
     * Führt einen Systembefehl aus und verarbeitet Fehler.
     *
     * @param command Der auszuführende Systembefehl
     * @throws IOException Falls der Befehl fehlschlägt
     */
    private static void executeCommand(String command) throws IOException {
        System.out.println("Ausgeführter Befehl: " + command);
        Process process = Runtime.getRuntime().exec(command);

        // Fehlerausgabe lesen
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println("ZFS-Fehler: " + line);
            }
        }

        // Auf den Abschluss warten
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Fehler beim Ausführen des Befehls: " + command);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Wiederherstellung des Interrupt-Status
            throw new IOException("Der ZFS-Befehl wurde unterbrochen", e);
        }
    }
}

