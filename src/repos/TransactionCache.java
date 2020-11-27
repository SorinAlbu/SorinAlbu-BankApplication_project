package repos;

import data.Transaction;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class TransactionCache {
    private List<Transaction> transactionList;

    public TransactionCache() {
        this.transactionList = new ArrayList<>();
    }
}
