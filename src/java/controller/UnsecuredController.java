package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsecuredController {
    
    @GetMapping("test")
    public String getTable() {
        
        return "Hello world!";
        
    }
    
}
