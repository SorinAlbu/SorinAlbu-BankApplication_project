package data;

import lombok.Data;

import java.util.UUID;

@Data
public class CreditBankAccountDTO implements IAccountDTO {
    private String id;
    private double amount;
    private String ownerId;
    private double limitAmount;

    public CreditBankAccountDTO() {
        this.id = UUID.randomUUID().toString();
    }

}
