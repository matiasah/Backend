package jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static jwt.SecurityConstants.HEADER_STRING;
import static jwt.SecurityConstants.SECRET;
import static jwt.SecurityConstants.TOKEN_PREFIX;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            
            ObjectMapper mapper = new ObjectMapper();
            JWTResponse jwtResponse = new JWTErrorResponse("token_not_provided");
            
            response.setContentType("application/json");
            response.getWriter().write( mapper.writeValueAsString(jwtResponse) );
            
            return;
            
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        
        if ( authentication == null ) {
            
            ObjectMapper mapper = new ObjectMapper();
            JWTResponse jwtResponse = new JWTErrorResponse("token_not_provided");
            
            response.setContentType("application/json");
            response.getWriter().write( mapper.writeValueAsString(jwtResponse) );
            
            return;
            
        }
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);

        if (token != null) {

            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {

                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

            }

        }

        return null;

    }

}
