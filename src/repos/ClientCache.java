package repos;

import data.CreditBankAccountDTO;
import data.DebitBankAccountDTO;
import lombok.Data;
import org.springframework.stereotype.Component;
import users.Client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class ClientCache {
    private Map<String, Client> clientMap = new HashMap<>();

    public Collection<Client> getClientList() {
        return this.clientMap.values();
    }

    public void addDebitAccountOnClient(Client client, DebitBankAccountDTO debitAccount) {
        client.getDebitAccountList().add(debitAccount);
    }

    public void addCreditAccountOnClient(Client client, CreditBankAccountDTO creditAccount) {
        client.getCreditAccountList().add(creditAccount);
    }

    public void addClient(Client client) {
        this.clientMap.put(client.getUniqueId(), client);
    }

    // On save
    public void updateAccount(Client client, String id) {
        this.clientMap.put(id, client);
    }
}
