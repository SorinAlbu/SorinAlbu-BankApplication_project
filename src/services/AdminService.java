package services;

import config.AppException;
import config.AppMessage;
import data.CreditBankAccountDTO;
import data.DebitBankAccountDTO;
import org.springframework.stereotype.Component;
import repos.ClientCache;
import users.Client;
import users.LegalPerson;
import users.Person;

@Component
public class AdminService {
    private ClientCache clientCache;
    private AppMessage appMessage;

    public AdminService(ClientCache clientDB, AppMessage appMessage) {
        this.clientCache = clientDB;
        this.appMessage = appMessage;
    }

    public String addDebitAccount(String username) throws AppException {
        Client client = this.searchClient(username);

        if (client == null) {
            throw new AppException(this.appMessage.getUSER_FAILED_MSG());
        }

        DebitBankAccountDTO debitAccount = new DebitBankAccountDTO();
        debitAccount.setOwnerId(client.getUniqueId());
        this.clientCache.addDebitAccountOnClient(client, debitAccount);

        return appMessage.getSUCCESS_MSG();
    }

    public boolean addCreditAccount(String username) {
        Client client = this.searchClient(username);

        if (client == null) {
            return false;
        }

        CreditBankAccountDTO creditAccount = new CreditBankAccountDTO();
        creditAccount.setOwnerId(client.getUniqueId());
        this.setAccountLimit(client, creditAccount);
        this.clientCache.addCreditAccountOnClient(client, creditAccount);
        return true;
    }

    public boolean addClient(String userName, String password, String type) {
        Client existingClient = this.searchClient(userName);

        if (existingClient != null) {
            return false;
        }

        Client client = type.equals("person") ? new Person(userName, password) : new LegalPerson(userName, password);
        this.clientCache.addClient(client);
        return true;
    }

    private Client searchClient(String userName) {
        for(Client client : this.clientCache.getClientList()) {
            if (client.getUsername().equals(userName)) {
                return client;
            }
        }
        return null;
    }

    private void setAccountLimit(Client client, CreditBankAccountDTO creditAccount) {
        if (client instanceof Person) {
            Person person = (Person) client;
            creditAccount.setLimitAmount(person.getSalary());
        }

        if (client instanceof LegalPerson) {
            LegalPerson lPerson = (LegalPerson) client;
            creditAccount.setLimitAmount(lPerson.getCapital() * 0.001);
        }
    }
}
