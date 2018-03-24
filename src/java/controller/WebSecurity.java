package controller;

import jwt.JWTAuthenticationEntryPoint;
import jwt.UserDetailsServiceImpl;
import jwt.JWTAuthenticationFilter;
import jwt.JWTAuthorizationFilter;
import static jwt.SecurityConstants.LOGIN_URL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //(debug=true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsService        userDetailsService;
    private final BCryptPasswordEncoder     bCryptPasswordEncoder;
    private final AuthenticationEntryPoint  authenticationEntryPoint;

    public WebSecurity() {

        this.userDetailsService = new UserDetailsServiceImpl();
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.authenticationEntryPoint = new JWTAuthenticationEntryPoint();

    }
    
    @Bean
    public UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }
    
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return this.bCryptPasswordEncoder;
    }
    
    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return this.authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable().authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(getAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class )
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        auth
            .userDetailsService(this.userDetailsService)
            .passwordEncoder(this.bCryptPasswordEncoder);
        
    }

}
