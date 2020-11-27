package data;

import config.TransactionType;
import lombok.Data;

import java.util.UUID;

@Data
public class Transaction {
    private String id;
    private String ownerId;
    private TransactionType transType;

    public Transaction() {
        this.id = UUID.randomUUID().toString();
    }
}
