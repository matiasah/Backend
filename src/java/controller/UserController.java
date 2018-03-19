package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @RequestMapping("/user")
    public java.util.List<String> index() {
        
        java.util.ArrayList<String> list = new java.util.ArrayList<>();
        
        list.add("test");
        list.add("test2");
        
        return list;
        
    }
    
}
