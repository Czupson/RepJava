package org.example.bank;

public interface AuditLogger {
    void logTransfer(TransferLog transferLog);
}