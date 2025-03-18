package com.zfs.tasks;

import com.zfs.transactions.FileOperations;
import com.zfs.transactions.TransactionManager;

import java.io.File;

/**
 * Verwalterklasse für das Hinzufügen, Lesen und Kommentieren von Ideen.
 */
public class IdeaManager {

    private TransactionManager transactionManager = new TransactionManager();

    /**
     * Fügt eine neue Idee hinzu und speichert sie in einer Datei.
     *
     * @param ideaName Der Name der Idee
     * @param content  Der Inhalt der Idee
     * @throws Exception Bei Datei- oder Transaktionsfehlern
     */
    public void addIdea(String ideaName, String content) throws Exception {
        String filePath = "ideas/" + ideaName + ".txt";

        // Sicherstellen, dass das Verzeichnis "ideas" existiert
        File directory = new File("ideas");
        if (!directory.exists()) {
            System.out.println("Verzeichnis 'ideas' wird erstellt...");
            if (directory.mkdir()) {
                System.out.println("Verzeichnis 'ideas' erfolgreich erstellt.");
            } else {
                throw new Exception("Fehler beim Erstellen des Verzeichnisses 'ideas'.");
            }
        }

        // Überprüfen, ob die Datei existiert, und sie gegebenenfalls erstellen
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Datei existiert nicht. Sie wird jetzt erstellt: " + filePath);
            FileOperations.writeFile(filePath, ""); // Erstellt die Datei mit leerem Inhalt
            System.out.println("Datei erfolgreich erstellt: " + filePath);
        }

        // Transaktion starten
        transactionManager.startTransaction(ideaName, filePath);
        System.out.println("Transaktion gestartet für Datei: " + filePath);

        // Transaktion abschließen
        transactionManager.commitTransaction(ideaName, filePath, content);
        System.out.println("Transaktion erfolgreich abgeschlossen: " + filePath);
    }

    /**
     * Liest den Inhalt einer bestehenden Idee.
     *
     * @param ideaName Der Name der Idee
     * @throws Exception Bei Datei- oder Lesefehlern
     */
    public void readIdea(String ideaName) throws Exception {
        String filePath = "ideas/" + ideaName + ".txt";

        // Prüfen, ob die Datei existiert
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("Datei existiert nicht: " + filePath);
        }

        // Datei lesen
        String content = FileOperations.readFile(filePath);
        System.out.println("Inhalt von " + ideaName + ":");
        System.out.println(content);
    }

    /**
     * Fügt einen Kommentar zu einer bestehenden Idee hinzu.
     *
     * @param ideaName Der Name der Idee
     * @param comment  Der hinzuzufügende Kommentar
     * @throws Exception Bei Datei- oder Transaktionsfehlern
     */
    public void addComment(String ideaName, String comment) throws Exception {
        String filePath = "ideas/" + ideaName + ".txt";

        // Transaktion starten
        transactionManager.startTransaction(ideaName, filePath);
        System.out.println("Transaktion gestartet für Datei: " + filePath);

        // Kommentar hinzufügen
        String content = FileOperations.readFile(filePath) +  "\nKommentar: " + comment; 
        transactionManager.commitTransaction(ideaName, filePath, content);
        System.out.println("Transaktion erfolgreich abgeschlossen: " + filePath);
    }
}

