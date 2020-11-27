package users;

import lombok.Data;

import java.util.UUID;

@Data
public class Administrator {
    private String username;
    private String password;
    private String uniqueId;

    public Administrator(String username, String password) {
        this.username = username;
        this.password = password;
        this.uniqueId = UUID.randomUUID().toString();
    }
}
