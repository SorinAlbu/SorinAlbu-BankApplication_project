package controllers;

import config.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.LoginService;
import users.Administrator;
import users.Client;

@Component
public class LoginController {
    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public Client enterClientCredentials(String userName, String password) throws AppException {
        return this.loginService.returnClient(userName, password);
    }

    public Administrator enterAdminCredentials(String userName, String password) throws AppException {
        return this.loginService.returnAdmin(userName, password);
    }
}
