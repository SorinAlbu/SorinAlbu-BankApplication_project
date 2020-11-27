package config;

import controllers.LoginController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApp {

    public static void main(String[] args) throws AppException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigClass.class);
        LoginController loginController = context.getBean("loginController", LoginController.class);
        loginController.enterAdminCredentials("were", "235");


        context.close();
    }
}
