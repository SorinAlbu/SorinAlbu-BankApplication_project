package repos;

import lombok.Data;
import org.springframework.stereotype.Component;
import users.Administrator;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class AdminCache {
    private List<Administrator> adminList = new ArrayList<>();
}
