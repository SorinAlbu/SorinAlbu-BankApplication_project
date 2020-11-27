package users;

import lombok.Data;

@Data
public class Person extends Client {
    private String cnp;
    private double salary;

    public Person(String userName, String password) {
        super(userName, password);
    }
}
