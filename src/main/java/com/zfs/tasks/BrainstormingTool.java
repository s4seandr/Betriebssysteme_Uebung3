package com.zfs.tasks;

import java.util.Scanner;

public class BrainstormingTool {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IdeaManager ideaManager = new IdeaManager();

        while (true) {
            System.out.println("\n=== Brainstorming-Tool ===");
            System.out.println("1. Neue Idee hinzufügen");
            System.out.println("2. Bestehende Idee lesen");
            System.out.println("3. Kommentar hinzufügen");
            System.out.println("4. Beenden");
            System.out.print("Option wählen: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Eingabepuffer leeren

            try {
                switch (option) {
                    case 1:
                        System.out.print("Name der Idee: ");
                        String ideaName = scanner.nextLine();
                        System.out.print("Inhalt der Idee: ");
                        String content = scanner.nextLine();
                        ideaManager.addIdea(ideaName, content);
                        break;
                    case 2:
                        System.out.print("Name der Idee: ");
                        String readIdeaName = scanner.nextLine();
                        ideaManager.readIdea(readIdeaName);
                        break;
                    case 3:
                        System.out.print("Name der Idee: ");
                        String commentIdeaName = scanner.nextLine();
                        System.out.print("Kommentar: ");
                        String comment = scanner.nextLine();
                        ideaManager.addComment(commentIdeaName, comment);
                        break;
                    case 4:
                        System.out.println("Programm beendet.");
                        return;
                    default:
                        System.out.println("Ungültige Option. Bitte erneut versuchen.");
                }
            } catch (Exception e) {
                System.err.println("Fehler: " + e.getMessage());
            }
        }
    }
}

