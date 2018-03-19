package jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import orm.User;

import static jwt.SecurityConstants.EXPIRATION_TIME;
import static jwt.SecurityConstants.HEADER_STRING;
import static jwt.SecurityConstants.LOGIN_URL;
import static jwt.SecurityConstants.SECRET;
import static jwt.SecurityConstants.TOKEN_PREFIX;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JWTAuthenticationFilter(AuthenticationManager authManager) {

        super(new AntPathRequestMatcher(LOGIN_URL));

        setAuthenticationManager(authManager);

    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        
        try {

            ObjectMapper                            mapper      = new ObjectMapper();
            User                                    user        = mapper.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken     authToken   = new UsernamePasswordAuthenticationToken(
                    user.getName(),
                    user.getPassword(),
                    new ArrayList<>()
            );

            return getAuthenticationManager().authenticate( authToken );

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth
    ) throws IOException, ServletException {
        
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        if ( user != null ) {
            
            String jwtToken = Jwts.builder()
                    .setSubject( user.getUsername() )
                    .setExpiration( new Date(System.currentTimeMillis() + EXPIRATION_TIME) )
                    .signWith( SignatureAlgorithm.HS512, SECRET )
                    .compact();

            String token = TOKEN_PREFIX + jwtToken;
            ObjectMapper mapper = new ObjectMapper();
            JWTResponse jwtResponse = new JWTResponseToken(token);

            response.addHeader(HEADER_STRING, token);
            response.setContentType("application/json");
            response.getWriter().write( mapper.writeValueAsString( jwtResponse ) );
            
        } else {
            
            ObjectMapper mapper = new ObjectMapper();
            JWTResponse jwtResponse = new JWTErrorResponse("invalid_credentials");
            
            response.setContentType("application/json");
            response.getWriter().write( mapper.writeValueAsString(jwtResponse) );
            
        }

    }
    
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
        ) {
        
        try {
            
            ObjectMapper mapper = new ObjectMapper();
            JWTResponse jwtResponse = new JWTErrorResponse("invalid_credentials");
            
            response.setContentType("application/json");
            response.getWriter().write( mapper.writeValueAsString(jwtResponse) );
            
        } catch ( Exception e ) {
            
            throw new RuntimeException(e);
            
        }
        
    }

}
