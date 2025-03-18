package com.zfs.tasks;

import com.zfs.transactions.FileOperations;

public class App {
    public static void main(String[] args) {
        try {
            // Test: Schreibe, lies und lösche eine Datei
            String filePath = "testfile.txt";
            String content = "Hallo aus der ZFS-Library!";
            
            FileOperations.writeFile(filePath, content);
            System.out.println("Dateiinhalt: " + FileOperations.readFile(filePath));
            FileOperations.deleteFile(filePath);
            System.out.println("Datei gelöscht.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

