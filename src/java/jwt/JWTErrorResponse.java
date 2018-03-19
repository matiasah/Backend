package jwt;

public class JWTErrorResponse extends JWTResponse {
    
    private final String error;
    
    public JWTErrorResponse(String error) {
        
        this.error = error;
        
    }
    
    public String getError() {
        
        return this.error;
        
    }
}
