package controllers;

import config.AppException;
import config.AppMessage;
import org.springframework.stereotype.Component;
import services.AdminService;

@Component
public class AdminController {
    private AdminService adminService;
    private AppMessage appMessage;

    public AdminController(AdminService adminService, AppMessage appMessage) {
        this.adminService = adminService;
        this.appMessage = appMessage;
    }

    public String createDebitAccount(String username) throws AppException {
        return this.adminService.addDebitAccount(username);
    }

    public String createCreditAccount(String username) {
        return this.adminService.addCreditAccount(username) ?
                appMessage.getSUCCESS_MSG() : appMessage.getUSER_FAILED_MSG();
    }

    public String addClient(String userName, String password, String type) throws AppException {
        if (!type.equals("person") && !type.equals("legal person")) {
            throw new AppException(appMessage.getINCORRECT_TYPE_MSG());
        }

        return this.adminService.addClient(userName, password, type) ?
                appMessage.getSUCCESS_MSG() : appMessage.getDUPLICATE_USER_MSG();
    }
}
