package config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:project.properties")
public class AppMessage {
    @Value("${app.SUCCESS_MSG}")
    @Getter
    private  String SUCCESS_MSG;
    @Value("${app.USER_FAILED_MSG}")
    @Getter
    private  String USER_FAILED_MSG;
    @Value("${app.DUPLICATE_USER_MSG}")
    @Getter
    private  String DUPLICATE_USER_MSG;
    @Value("${app.INCORRECT_TYPE_MSG}")
    @Getter
    private  String INCORRECT_TYPE_MSG;
    @Value("${app.INCORRECT_IBAN}")
    @Getter
    private  String INCORRECT_IBAN;
    @Value("${app.INCORRECT_ID}")
    @Getter
    private  String INCORRECT_ID;
    @Value("${app.INSUFFICIENT_FUNDS}")
    @Getter
    private  String INSUFFICIENT_FUNDS;
    @Value("${app.NO_ACCOUNT_FOUND}")
    @Getter
    private  String NO_ACCOUNT_FOUND;
    @Value("${app.REPORT_MSG}")
    @Getter
    private  String REPORT_MSG;
}
