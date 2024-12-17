package s21.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import s21.domain.service.AuthorizationService;
import s21.web.model.SignUpRequest;


@Controller
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/authorization")
    public String authorizationGet() throws IOException {   
        return "auth.html";
    }
    @PostMapping("/authorization")
    public String authorizationPost(@ModelAttribute String request) throws IOException {
        authorizationService.authorization(request);
        return "auth.html";
    }

    @GetMapping("/register")
    public String registrationGet() throws IOException {
        return "register.html";
    }
    @PostMapping("/register")
    public String registrationPost(@ModelAttribute SignUpRequest request) throws IOException {
        System.out.println("register");
        if(authorizationService.registration(request)) return "redirect:/";
        return "redirect:/auth/authorization";
    }
    @PostMapping("testPost")
    public String postMethodName(@RequestBody String username, @RequestBody String password) {
        System.out.println(username + " " + password);
        
        return username;
    }
    
}
