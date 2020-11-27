package test;

import config.AppException;
import config.AppMessage;
import config.ConfigClass;
import controllers.AdminController;
import controllers.ClientController;
import controllers.LoginController;
import data.CreditBankAccountDTO;
import data.DebitBankAccountDTO;
import data.IAccountDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import repos.AdminCache;
import repos.ClientCache;
import users.Administrator;
import users.Client;
import users.LegalPerson;
import users.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConfigClass.class)
//@RunWith(MockitoJUnitRunner.class)
public class MyAppTest {
    @Autowired
    //@Mock
    private LoginController loginController;
    @Autowired
    private AdminController adminController;
    @Autowired
    private ClientController clientController;
    @Autowired
    private AdminCache adminCache;
    @Autowired
    private ClientCache clientCache;
    @Autowired
    private AppMessage appMessage;

    // -----Tests for LoginController------
    @Test
    public void testLoginControllerAdmin() throws AppException {
        adminCache.setAdminList(provideAdminUser());
        String username = "sorin";
        String psw = "123";

        Administrator admin = loginController.enterAdminCredentials(username, psw);

        Assert.assertEquals(Administrator.class, admin.getClass());
        Assert.assertEquals(username, admin.getUsername());
        Assert.assertEquals(psw, admin.getPassword());
    }

    @Test
    public void testLoginControllerNonexistentAdmin() {
        try {
            loginController.enterAdminCredentials("dragos", "1234");
        } catch (Exception e) {
            Assert.assertEquals(AppException.class, e.getClass());
            Assert.assertEquals(appMessage.getUSER_FAILED_MSG(), e.getMessage());
        }
    }

    @Test
    public void testLoginControllerClient() throws AppException {
        clientCache.setClientMap(provideClientUsers());

        Client client1 = loginController.enterClientCredentials("sorin", "123");
        Client client2 = loginController.enterClientCredentials("SC ABC SRL", "1234");

        Assert.assertEquals(Person.class, client1.getClass());
        Assert.assertEquals(LegalPerson.class, client2.getClass());
    }

    @Test
    public void testLoginControllerNonexistentPerson() {
        try {
            loginController.enterClientCredentials("alex", "1234");
        } catch (Exception e) {
            Assert.assertEquals(AppException.class, e.getClass());
            Assert.assertEquals(appMessage.getUSER_FAILED_MSG(), e.getMessage());
        }
    }

    @Test
    public void testLoginControllerNonexistentLegalPerson() {
        try {
            loginController.enterClientCredentials("SC QWE SRL", "123456");
        } catch (Exception e) {
            Assert.assertEquals(AppException.class, e.getClass());
            Assert.assertEquals(appMessage.getUSER_FAILED_MSG(), e.getMessage());
        }
    }
    // -----Tests for LoginController------

    @Test
    public  void testAdminControllerCreateDebitAccount() throws AppException {
        clientCache.setClientMap(provideClientUsers());
        String successMsg = adminController.createDebitAccount("sorin");

        Assert.assertEquals(appMessage.getSUCCESS_MSG(), successMsg);
    }

    @Test
    public void testAdminControllerCreateCreditAccount() {
        clientCache.setClientMap(provideClientUsers());
        String successMsg = adminController.createCreditAccount("sorin");

        Assert.assertEquals(appMessage.getSUCCESS_MSG(), successMsg);
    }

    @Test
    public void testAdminCreateDebitAccountException() {
        try {
            adminController.createDebitAccount("ion");
        } catch (Exception e) {
            Assert.assertEquals(AppException.class, e.getClass());
            Assert.assertEquals(appMessage.getUSER_FAILED_MSG(), e.getMessage());
        }
    }

    @Test
    public void testAdminCreateCreditAccountException() {
        try {
            adminController.createCreditAccount("ion");
        } catch (Exception e) {
            Assert.assertEquals(AppException.class, e.getClass());
            Assert.assertEquals(appMessage.getUSER_FAILED_MSG(), e.getMessage());
        }
    }

    @Test
    public void testAddClient() throws AppException {
        String msg1 = adminController.addClient("george", "12345", "person");
        String msg2 = adminController.addClient("SC POP SRL", "54321", "legal person");

        Assert.assertEquals(appMessage.getSUCCESS_MSG(), msg1);
        Assert.assertEquals(appMessage.getSUCCESS_MSG(), msg2);
    }

    @Test
    public void testAddClientException() {
        try {
            adminController.addClient("george", "12345", "personl");
        } catch (Exception e) {
            Assert.assertEquals(appMessage.getINCORRECT_TYPE_MSG(), e.getMessage());
        }

        try {
            adminController.addClient("SC POP SRL", "54321", "legal persona");
        } catch (Exception e) {
            Assert.assertEquals(appMessage.getINCORRECT_TYPE_MSG(), e.getMessage());
        }
    }

    @Test
    public void testDuplicateClient() throws AppException {
        clientCache.setClientMap(provideClientUsers());

        String msg = adminController.addClient("sorin", "123", "person");
        Assert.assertEquals(appMessage.getDUPLICATE_USER_MSG(), msg);
    }

    @Test
    public void testGetAllAccounts() throws AppException {
        clientCache.setClientMap(provideClientUsers());
        Client client = loginController.enterClientCredentials("sorin", "123");
        List<IAccountDTO> accountList = clientController.getAllAccounts(client);

        Assert.assertEquals(1, accountList.size());
    }

    @Test
    public void testAddToDebitAccountOnIbanAndShowAmount() throws AppException {
        clientCache.setClientMap(provideClientUsers());
        Client client = loginController.enterClientCredentials("sorin", "123");
        List<IAccountDTO> accountList = clientController.getAllAccounts(client);
        DebitBankAccountDTO debitAccount = (DebitBankAccountDTO) accountList.get(0);
        double sumAdded = 500;
        String msg = clientController.transferMoneyToDebitAccount(client, debitAccount.getIban(), sumAdded);

        Assert.assertEquals(appMessage.getSUCCESS_MSG(), msg);
        double sumFromAccount = clientController.showAmount(client, 0);
        Assert.assertEquals(String.valueOf(sumAdded), String.valueOf(sumFromAccount));
    }

    @Test
    public void testTransferBetweenAccounts() throws AppException {
        clientCache.setClientMap(provideClientUsers());
        Client client = loginController.enterClientCredentials("sorin", "123");
        adminController.createDebitAccount(client.getUsername());
        List<IAccountDTO> accountList = clientController.getAllAccounts(client);

        Assert.assertEquals(2, accountList.size());

        DebitBankAccountDTO debitAccountFrom = (DebitBankAccountDTO) accountList.get(0);
        DebitBankAccountDTO debitAccountTo = (DebitBankAccountDTO) accountList.get(1);
        double sumAdded = 500;
        clientController.transferMoneyToDebitAccount(client, debitAccountFrom.getIban(), sumAdded);

        Assert.assertEquals(String.valueOf(sumAdded), String.valueOf(debitAccountFrom.getAmount()));

        double transferSum = 300;

        String msg = clientController.transferBetweenDebitAccounts(client,
                                                                   debitAccountFrom.getIban(),
                                                                   debitAccountTo.getIban(),
                                                                   transferSum);

        //Assert.assertEquals(appMessage.getSUCCESS_MSG(), msg);
        //Assert.assertEquals(String.valueOf(transferSum), debitAccountTo.getAmount());

        try {
            transferSum = 600;
            clientController.transferBetweenDebitAccounts(client, debitAccountFrom.getIban(), debitAccountTo.getIban(), transferSum);
        } catch (Exception e) {
            Assert.assertEquals(AppException.class, e.getClass());
            Assert.assertEquals(appMessage.getINSUFFICIENT_FUNDS(), e.getMessage());
        }
    }

    @Test
    public void testTopUpCreditAccount() throws AppException {
        clientCache.setClientMap(provideClientUsers());
        Client client = loginController.enterClientCredentials("sorin", "123");
        adminController.createCreditAccount("sorin");

        CreditBankAccountDTO account = client.getCreditAccountList().get(0);

        double sum = 200;
        clientController.topUpCreditAccount(client, account.getId(), sum);

        Assert.assertEquals(String.valueOf(sum), String.valueOf(account.getAmount()));
    }

    //Methods for creating test data
    private ArrayList<Administrator> provideAdminUser() {
        Administrator admin1 = new Administrator("sorin", "123");
        Administrator admin2 = new Administrator("andreea", "1234");

        ArrayList<users.Administrator> adminList = new ArrayList<>();
        adminList.add(admin1);
        adminList.add(admin2);

        return adminList;
    }

    private Map<String, Client> provideClientUsers() {
        Client person = new Person("sorin", "123");
        Client legalPerson = new LegalPerson("SC ABC SRL", "1234");

        Map<String, Client> clientMap = new HashMap<>();
        clientMap.put(person.getUniqueId(), person);
        clientMap.put(legalPerson.getUniqueId(), legalPerson);

        return clientMap;
    }


    /*
    @Test
    public void returnAdminTest() throws AppException {
        //AdminCache adminCache = new AdminCache();
        //ClientCache clientCache = new ClientCache();
        //AppMessage appMessage = new AppMessage();
        //loginService = new LoginService(clientCache, adminCache, appMessage);
        String username = "sorin";
        String psw = "123";
        Administrator admin = new Administrator(username, psw);

        Mockito.when(loginController.enterAdminCredentials(Mockito.anyString(), Mockito.anyString())).thenReturn(admin);
        Assert.assertEquals(admin.getUsername(), loginController.enterAdminCredentials("sdff", "asdasd").getUsername());
    }

     */
}
