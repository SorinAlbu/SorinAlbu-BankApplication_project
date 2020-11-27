package services;

import config.TransactionType;
import data.CreditBankAccountDTO;
import data.DebitBankAccountDTO;
import data.Transaction;
import org.springframework.stereotype.Component;
import repos.TransactionCache;

import java.util.List;
import java.util.Optional;

import lombok.Getter;

@Component
public class AccountService {
    @Getter
    private TransactionCache transactionCache;
    private TransactionType transType;

    public AccountService(TransactionCache transactionCache) {
        this.transactionCache = transactionCache;
    }

    public boolean processDebitAccountTransfer(List<DebitBankAccountDTO> accountList, String iban, double sum) {
        DebitBankAccountDTO account = this.returnDebitAccount(accountList, iban);

        if(account == null) {
            return false;
        }

        account.setAmount(account.getAmount() + sum);
        this.recordTransaction(account.getOwnerId(), this.transType.TRANSFER_DEBIT_ACCOUNT_IBAN);
        return true;
    }

    public void processAccountsTransfer(DebitBankAccountDTO accountFrom, DebitBankAccountDTO accountTo, double sum) {
        accountFrom.setAmount(accountFrom.getAmount() - sum);
        accountTo.setAmount(accountTo.getAmount() + sum);
        this.recordTransaction(accountFrom.getOwnerId(), this.transType.TRANSFER_BETWEEN_ACCOUNTS);
    }

    public boolean processCreditAccountTransfer(List<CreditBankAccountDTO>accountList, String id, double sum) {
        CreditBankAccountDTO account = this.returnCreditAccount(accountList, id);

        if(account == null) {
            return false;
        }

        account.setAmount(account.getAmount() + sum);
        this.recordTransaction(account.getOwnerId(), this.transType.TOP_UP_CREDIT_ACCOUNT_ID);
        return true;
    }

    public void processWithDraw(DebitBankAccountDTO account, double sum) {
        account.setAmount(account.getAmount() - sum);
        this.recordTransaction(account.getOwnerId(), this.transType.WITHDRAW_FROM_DEBIT_ACCOUNT);
    }

    private void recordTransaction(String clientId, TransactionType transType) {
        Transaction transaction = new Transaction();
        transaction.setTransType(transType);
        transaction.setOwnerId(clientId);
        this.transactionCache.getTransactionList().add(transaction);
    }

    protected DebitBankAccountDTO returnDebitAccount(List<DebitBankAccountDTO> accountList, String iban) {
        Optional<DebitBankAccountDTO> result = accountList.stream().findAny().filter(a -> a.getIban().equals(iban));
        return result.orElse(null);
    }

    private CreditBankAccountDTO returnCreditAccount(List<CreditBankAccountDTO> accountList, String id) {
        Optional<CreditBankAccountDTO> result = accountList.stream().findAny().filter(a -> a.getId().equals(id));
        return result.orElse(null);
    }
}
