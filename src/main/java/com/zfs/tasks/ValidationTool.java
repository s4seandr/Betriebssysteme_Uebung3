package com.zfs.tasks;

import com.zfs.tasks.IdeaManager;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tool zur Validierung des BrainstormingTools durch Simulation
 * gleichzeitiger Dateioperationen.
 */
public class ValidationTool {

    private static final int THREAD_COUNT = 10; // Anzahl gleichzeitiger Threads
    private static final int SIMULATION_DURATION_SECONDS = 30; // Dauer der Simulation in Sekunden
    private static int conflictCount = 0; // Zählt Konflikte
    private static int successfulOperations = 0; // Zählt erfolgreiche Operationen
    private static long totalRollbackTime = 0; // Gesamtdauer aller Rollbacks

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        IdeaManager ideaManager = new IdeaManager();
        String ideaName = "TestValidationIdea";

        // Initiale Idee erstellen
        try {
            System.out.println("Starte Validierung...");
            ideaManager.addIdea(ideaName, "Initialer Inhalt.");
        } catch (Exception e) {
            System.err.println("Fehler beim Erstellen der initialen Idee: " + e.getMessage());
            return;
        }

        Random random = new Random();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < 100; j++) { // Mehrere Aktionen pro Thread
                        int operationType = random.nextInt(3); // Zufällige Aktion auswählen
                        switch (operationType) {
                            case 0: // Schreiboperation (Kommentar hinzufügen)
                                try {
                                    ideaManager.addComment(ideaName, "Kommentar: " + random.nextInt(1000));
                                    incrementSuccessfulOperations();
                                } catch (Exception e) {
                                    handleConflict();
                                }
                                break;
                            case 1: // Leseoperation
                                try {
                                    ideaManager.readIdea(ideaName);
                                    incrementSuccessfulOperations();
                                } catch (Exception e) {
                                    System.err.println("Fehler beim Lesen: " + e.getMessage());
                                }
                                break;
                            case 2: // Simulierter Konflikt durch mehrere Schreiboperationen
                                try {
                                    ideaManager.addComment(ideaName, "Noch ein Kommentar: " + random.nextInt(1000));
                                    incrementSuccessfulOperations();
                                } catch (Exception e) {
                                    handleConflict();
                                }
                                break;
                        }
                        // Kurze Pause, um gleichzeitige Zugriffe realistischer zu machen
                        Thread.sleep(random.nextInt(200));
                    }
                } catch (Exception e) {
                    System.err.println("Fehler in Thread: " + e.getMessage());
                }
            });
        }

        // Executor-Dienst herunterfahren
        executorService.shutdown();
        try {
            executorService.awaitTermination(SIMULATION_DURATION_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Simulation unterbrochen: " + e.getMessage());
        }

        // Ergebnisse anzeigen
        printResults();
    }

    /**
     * Erhöht die Zählung für erfolgreiche Operationen.
     */
    private synchronized static void incrementSuccessfulOperations() {
        successfulOperations++;
    }

    /**
     * Behandelt Konflikte und misst die Rollback-Dauer.
     */
    private synchronized static void handleConflict() {
        conflictCount++;
        // Rollback-Dauer simulieren (falls realer Rollback in der Zukunft integriert wird)
        long startTime = System.nanoTime();
        try {
            Thread.sleep(50); // Simulierter Rollback-Zeitaufwand
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long endTime = System.nanoTime();
        totalRollbackTime += (endTime - startTime);
    }

    /**
     * Druckt die Metriken der Simulation.
     */
    private static void printResults() {
        System.out.println("\n=== Validierung abgeschlossen ===");
        System.out.println("Konfliktrate: " + conflictCount + " Konflikte.");
        System.out.println("Systemdurchsatz: " + successfulOperations + " erfolgreiche Operationen.");
        if (conflictCount > 0) {
            System.out.println("Durchschnittliche Rollback-Dauer: " 
                + (totalRollbackTime / conflictCount) + " ns.");
        } else {
            System.out.println("Keine Konflikte aufgetreten.");
        }
        System.out.println("=================================");
    }
}

