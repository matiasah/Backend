package jwt;

import orm.User;
import orm.UserDAO;

public class UserRepository {
    
    public User findByUsername(String username) {
        
        return UserDAO.getUserByName(username);
        
    }
    
}