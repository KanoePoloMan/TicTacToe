package s21.web.controller.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import s21.domain.service.UserService;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("getallusers")
    public List<String> getAllUsers() throws IOException {
        return userService.getAll().stream().map(param -> param.toString()).toList();
    }
}
