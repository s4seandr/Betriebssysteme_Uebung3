package com.zfs.validation;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsCollector {

    private AtomicInteger totalTransactions = new AtomicInteger(0);
    private AtomicInteger successfulTransactions = new AtomicInteger(0);
    private AtomicInteger conflictCount = new AtomicInteger(0);
    private AtomicInteger rollbackCount = new AtomicInteger(0);
    private AtomicLong totalTransactionTime = new AtomicLong(0);

    public void recordSuccess(long transactionTime) {
        totalTransactions.incrementAndGet();
        successfulTransactions.incrementAndGet();
        totalTransactionTime.addAndGet(transactionTime);
    }

    public void recordConflict() {
        totalTransactions.incrementAndGet();
        conflictCount.incrementAndGet();
    }

    public void recordRollback() {
        rollbackCount.incrementAndGet();
    }

    public void printMetrics() {
        System.out.println("\n=== Validierungsergebnisse ===");
        System.out.println("Gesamtanzahl Transaktionen: " + totalTransactions.get());
        System.out.println("Erfolgreiche Transaktionen: " + successfulTransactions.get());
        System.out.println("Konfliktraten: " + conflictCount.get());
        System.out.println("Rollback-Count: " + rollbackCount.get());
        System.out.println("Durchschnittliche Transaktionszeit: " +
                (successfulTransactions.get() > 0 ? (totalTransactionTime.get() / successfulTransactions.get()) + " ms" : "N/A"));
    }
}

