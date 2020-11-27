package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"config", "controllers", "repos", "services", "users"})
public class ConfigClass {

}
