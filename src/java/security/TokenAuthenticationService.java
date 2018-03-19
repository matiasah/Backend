package security;

public class TokenAuthenticationService {
    
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    
    private final TokenHandler tokenHandler;
    
    public TokenAuthenticationService(String secret, UserService userService) {
        this.tokenHandler = new TokenHandler(secret, userService);
    }
    
}
