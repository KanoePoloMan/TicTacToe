package s21.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import s21.domain.service.AuthorizationService;



@Controller
public class GameController {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private DomainController controller;
    
    @GetMapping("/test")
    public String getTest() {
        return "gameai.html";
    }
    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("nickname", authorizationService.getCurrentUser());
        return "menu.html";
    }
}
