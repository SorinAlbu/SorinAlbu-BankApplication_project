package data;

import lombok.Data;

import java.util.UUID;
@Data
public class DebitBankAccountDTO implements IAccountDTO {
    private String id;
    private String iban;
    private double amount;
    private String ownerId;
    private static final String PREFIX_IBAN = "RODEV";

    public DebitBankAccountDTO() {
        this.id = UUID.randomUUID().toString();
        this.iban = PREFIX_IBAN + (int)(Math.random() * 10000);
    }
}
