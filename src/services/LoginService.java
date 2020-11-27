package services;

import config.AppException;
import config.AppMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repos.AdminCache;
import repos.ClientCache;
import users.Administrator;
import users.Client;

@Component
public class LoginService {
    private ClientCache clientDB;
    private AdminCache adminDB;
    private AppMessage appMessage;

    @Autowired
    public LoginService(ClientCache clientDB, AdminCache adminDB, AppMessage appMessage) {
        this.clientDB = clientDB;
        this.adminDB = adminDB;
        this.appMessage = appMessage;
    }

    public Client returnClient(String userName, String password) throws AppException {
        for (Client client : clientDB.getClientList()) {
            if (client.getUsername().equals(userName) && client.getPassword().equals(password)) {
                return client;
            }
        }
        throw new AppException(appMessage.getUSER_FAILED_MSG());
    }

    public Administrator returnAdmin(String userName, String password) throws AppException {
        for (Administrator admin : adminDB.getAdminList()) {
            if (admin.getUsername().equals(userName) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        throw new AppException(appMessage.getUSER_FAILED_MSG());
    }
}
