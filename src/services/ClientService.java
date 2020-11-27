package services;

import config.AppException;
import config.AppMessage;
import data.CreditBankAccountDTO;
import data.DebitBankAccountDTO;
import data.IAccountDTO;
import data.Transaction;
import org.springframework.stereotype.Component;
import users.Client;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientService {
    private AccountService accountService;
    private AppMessage appMessage;

    public ClientService(AccountService accountService, AppMessage appMessage) {
        this.accountService = accountService;
        this.appMessage = appMessage;
    }

    public List<IAccountDTO> returnAccounts(Client client) {
        List<IAccountDTO> accountsList = new ArrayList<>();

        accountsList.addAll(client.getCreditAccountList());
        accountsList.addAll(client.getDebitAccountList());

        return accountsList;
    }

    public double returnAmount(Client client, Integer noAccount) throws AppException {
        List<DebitBankAccountDTO> accountList = client.getDebitAccountList();

        if (noAccount > accountList.size()) {
            throw new AppException(appMessage.getNO_ACCOUNT_FOUND());
        }
        return accountList.get(noAccount).getAmount();
    }

    public boolean transferMoney(Client client, String iban, double sum) {
        List<DebitBankAccountDTO> accountList = client.getDebitAccountList();

        return this.accountService.processDebitAccountTransfer(accountList, iban, sum);
    }

    public boolean operationBetweenAccounts(Client client,
                                            String accountIbanFrom,
                                            String accountIbanTo,
                                            double sum) throws AppException {

        List<DebitBankAccountDTO> accountList = client.getDebitAccountList();
        DebitBankAccountDTO accountFrom = this.accountService.returnDebitAccount(accountList, accountIbanFrom);
        DebitBankAccountDTO accountTo = this.accountService.returnDebitAccount(accountList, accountIbanTo);


        if (accountFrom == null || accountTo == null) {
            return false;
        }

        if (accountFrom.getAmount() < sum) {
            throw new AppException(this.appMessage.getINSUFFICIENT_FUNDS());
        }

        this.accountService.processAccountsTransfer(accountFrom, accountTo, sum);
        return true;
    }

    public boolean addMoneyToCreditAccount(Client client, String id, double sum) {
        List<CreditBankAccountDTO> accountList = client.getCreditAccountList();

        return (this.accountService.processCreditAccountTransfer(accountList, id, sum));
    }

    public String withdrawFromDebitAccount(Client client, String iban, double sum) throws AppException {
        List<DebitBankAccountDTO> debitList = client.getDebitAccountList();

        for (DebitBankAccountDTO account : debitList) {
            if (account.getIban().equals(iban)) {
                if (account.getAmount() < sum) {
                    throw new AppException(appMessage.getINSUFFICIENT_FUNDS());
                }
                this.accountService.processWithDraw(account, sum);
                return this.appMessage.getSUCCESS_MSG();
            }
        }
        throw new AppException(appMessage.getINCORRECT_IBAN());
    }

    public String createReport(String userName, String ownerId) {
        List<Transaction> transactionList = this.accountService.getTransactionCache().getTransactionList();
        List<Transaction> filteredList = this.filterTransactions(transactionList, ownerId);

        String finalMsg = appMessage.getREPORT_MSG().replace("%CLIENT_USERNAME%", userName);
        String tempMsg = "";

        for(Transaction trans : filteredList) {
            tempMsg += "Id: " + trans.getId() +
                       " Type: " + trans.getTransType().toString() +"\n";
        }

        finalMsg += tempMsg;
        return finalMsg;
    }

    private List<Transaction> filterTransactions(List<Transaction> transList, String ownerId) {
        List<Transaction> filteredList = new ArrayList<>();

        for (Transaction trans : transList) {
            if (trans.getOwnerId().equals(ownerId)) {
                filteredList.add(trans);
            }
        }

        return filteredList;
    }
}
