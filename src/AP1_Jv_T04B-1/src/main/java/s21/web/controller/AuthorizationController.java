package s21.web.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {
    @GetMapping("/authorization")
    public String authorization() throws IOException {   
        return "auth.html";
    }

    @GetMapping("/register")
    public String registration() throws IOException {
        return "register.html";
    }
}
