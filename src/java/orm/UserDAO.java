package orm;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDAO {
    
    public static User getUserByName(String name) {
        
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        
        return new User("testname", enc.encode("testpass"));
        
    }
    
}
