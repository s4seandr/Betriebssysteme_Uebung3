package com.zfs.test;

import com.zfs.transactions.ZfsManager;

public class ZfsManagerTest {

    public static void main(String[] args) {
        String snapshotName = "TestSnapshot";

        try {
            // 1. Snapshot erstellen
            System.out.println("Teste: Snapshot erstellen...");
            ZfsManager.createSnapshot(snapshotName);
            System.out.println("Snapshot erstellt: " + snapshotName);

            // 2. Rollback zu einem Snapshot
            System.out.println("Teste: Rollback zu Snapshot...");
            ZfsManager.rollbackSnapshot(snapshotName);
            System.out.println("Rollback erfolgreich: " + snapshotName);

            // 3. Snapshot löschen
            System.out.println("Teste: Snapshot löschen...");
            ZfsManager.deleteSnapshot(snapshotName);
            System.out.println("Snapshot gelöscht: " + snapshotName);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ein Fehler ist während des Tests aufgetreten: " + e.getMessage());
        }
    }
}

