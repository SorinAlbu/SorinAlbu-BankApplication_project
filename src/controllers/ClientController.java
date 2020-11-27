package controllers;

import config.AppException;
import config.AppMessage;
import data.IAccountDTO;
import org.springframework.stereotype.Component;
import repos.ClientCache;
import services.ClientService;
import users.Client;

import java.util.List;

@Component
public class ClientController {
    private ClientService clientService;
    private AppMessage appMessage;

    public ClientController(ClientService clientService, AppMessage msg) {
        this.clientService = clientService;
        this.appMessage = msg;
    }

    public List<IAccountDTO> getAllAccounts(Client client) {
        return this.clientService.returnAccounts(client);
    }

    public double showAmount(Client client, Integer noAccount) throws AppException {
        return this.clientService.returnAmount(client, noAccount);
    }

    public String transferMoneyToDebitAccount(Client client, String iban, double sum) {
        return this.clientService.transferMoney(client, iban, sum) ?
                appMessage.getSUCCESS_MSG() : appMessage.getINCORRECT_IBAN();
    }

    public String transferBetweenDebitAccounts(Client client,
                                               String accountIbanFrom,
                                               String accountIbanTo,
                                               double sum) throws AppException {
        return this.clientService.operationBetweenAccounts(client, accountIbanFrom, accountIbanTo, sum) ?
                appMessage.getSUCCESS_MSG() : appMessage.getINCORRECT_IBAN();
    }

    public String topUpCreditAccount(Client client, String id, double sum) {
        return this.clientService.addMoneyToCreditAccount(client, id, sum) ?
                appMessage.getSUCCESS_MSG() : appMessage.getINCORRECT_ID();
    }

    public String withdrawMoneyFromDebitAccount(Client client, String iban, double sum) throws AppException {
        return this.clientService.withdrawFromDebitAccount(client, iban, sum);
    }

    public String generateReport(Client client) {
        return this.clientService.createReport(client.getUsername(), client.getUniqueId());
    }
}
