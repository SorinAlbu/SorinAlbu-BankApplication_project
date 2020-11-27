package users;

import data.CreditBankAccountDTO;
import data.DebitBankAccountDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public abstract class Client {
    private String username;
    private String password;
    private String uniqueId;
    private List<CreditBankAccountDTO> creditAccountList = new ArrayList<>();
    private List<DebitBankAccountDTO> debitAccountList = new ArrayList<>();

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
        this.uniqueId = UUID.randomUUID().toString();
        this.setOwnerIdToDebitAccounts(1);
    }

    private void setOwnerIdToDebitAccounts(Integer noDebitAccounts) {
        for (Integer i = 0; i < noDebitAccounts; i++) {
            DebitBankAccountDTO debitAccount = new DebitBankAccountDTO();
            debitAccount.setOwnerId(this.uniqueId);
            this.debitAccountList.add(debitAccount);
        }
    }
}
