package controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured("IS_AUTHENTICATED_FULLY")
public class UserController {
    
    @RequestMapping("/user")
    public java.util.List<String> index(HttpServletRequest request) {
        
        //System.out.println( request.getUserPrincipal() );
        
        java.util.ArrayList<String> list = new java.util.ArrayList<>();
        
        list.add("test");
        list.add("test2");
        
        return list;
        
    }
    
}
