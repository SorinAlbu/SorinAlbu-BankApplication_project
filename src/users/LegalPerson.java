package users;

import lombok.Data;

@Data
public class LegalPerson extends Client {
    private String companyName;
    private String cui;
    private double transactionCost;
    private double capital;

    public LegalPerson(String userName, String password) {
        super(userName, password);
    }
}
