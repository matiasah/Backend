package jwt;

import static java.util.Collections.emptyList;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository = new UserRepository();
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        orm.User user = userRepository.findByUsername(username);
        
        if ( user == null ) {
            throw new UsernameNotFoundException(username);
        }
        
        return new User(user.getName(), user.getPassword(), emptyList());
        
    }
    
}
