package jwt;

public class JWTResponseToken extends JWTResponse {
    
    private final String token;
    
    public JWTResponseToken(String token) {
        
        this.token = token;
        
    }
    
    public String getToken() {
        
        return this.token;
        
    }
    
}
