package domain;

import java.time.LocalDateTime;

public class Transaction {
    private String accountNumber;
    private Type type;
    private String id;
    private Double amount;
    private LocalDateTime timestamp;
    private String note;

    public Transaction(String accountNumber, Type type, String id, Double amount, LocalDateTime timestamp, String note) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.note = note;
    }
}
