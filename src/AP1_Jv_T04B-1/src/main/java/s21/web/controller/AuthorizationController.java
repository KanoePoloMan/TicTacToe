package s21.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import s21.domain.service.AuthorizationService;
import s21.web.model.SignUpRequest;


@Controller
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/auth")
    public String authorizationGet() throws IOException {   
        return "auth.html";
    }
    @PostMapping("/auth")
    public String authorizationPost(@ModelAttribute String request) throws IOException {
        return "auth.html";
    }

    @GetMapping("/reg")
    public String registrationGet() throws IOException {
        return "register.html";
    }
    @PostMapping("/reg")
    public String registrationPost(@ModelAttribute SignUpRequest request, BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", bindingResult);
            return "register.html";
        }
        if(authorizationService.registration(request)) return "redirect:/anon";
        return "redirect:/auth";
    }

    @PostMapping("testPost")
    public String postMethodName(@RequestBody String username, @RequestBody String password) {
        System.out.println(username + " " + password);
        
        return username;
    }
    
}
