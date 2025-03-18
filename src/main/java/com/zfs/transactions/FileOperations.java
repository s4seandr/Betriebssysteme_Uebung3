package com.zfs.transactions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

/**
 * Utility-Klasse für grundlegende Dateioperationen.
 */
public class FileOperations {

    /**
     * Schreibt den angegebenen Inhalt in eine Datei.
     *
     * @param filePath der Pfad der Datei
     * @param content der zu schreibende Inhalt
     * @throws IOException falls ein Fehler beim Schreiben der Datei auftritt
     */
    public static void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    /**
     * Liest den Inhalt einer Datei und gibt ihn als String zurück.
     *
     * @param filePath der Pfad der Datei
     * @return der Inhalt der Datei als String
     * @throws IOException falls ein Fehler beim Lesen der Datei auftritt
     */
    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Löscht die angegebene Datei.
     *
     * @param filePath der Pfad der zu löschenden Datei
     * @throws IOException falls ein Fehler beim Löschen der Datei auftritt
     */
    public static void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }

    /**
     * Berechnet den SHA-256-Hash einer Datei.
     *
     * @param filePath der Pfad der Datei
     * @return der berechnete Hash als Hex-String
     * @throws Exception falls ein Fehler beim Lesen der Datei oder beim Hashing auftritt
     */
    public static String calculateHash(String filePath) throws Exception {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);

        // Hash in einen Hex-String umwandeln
        StringBuilder hashString = new StringBuilder();
        for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString();
    }
}

