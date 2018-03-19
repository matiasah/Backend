package security;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;

public class UserService {
    
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
    
}
