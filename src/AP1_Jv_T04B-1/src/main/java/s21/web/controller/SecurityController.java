package s21.web.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @GetMapping("/getallusers")
    public String getAllUsers() throws IOException {
        return "getall";
    }
}
