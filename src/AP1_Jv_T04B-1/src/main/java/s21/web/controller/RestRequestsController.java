package s21.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import s21.web.model.CurrentGameDTO;
import s21.web.model.GameFieldDTO;



@RestController
public class RestRequestsController {
    @Autowired
    private DomainController controller;

    @PostMapping("/game/{UUID}")
    public CurrentGameDTO updateGameField(@PathVariable("UUID") String uuid, @RequestBody GameFieldDTO field) throws Exception {
        return controller.updateFieldAndGetNextStep(new CurrentGameDTO(UUID.fromString(uuid), field, null));
    }
    @GetMapping("/game/newGame")
    public CurrentGameDTO getNewGame() {
        return controller.createNewGame();
    }
    @DeleteMapping("/game/{UUID}")
    public void getNewGame(@PathVariable("UUID") String uuid) {
        controller.deleteGame(UUID.fromString(uuid));
    }
    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello world!";
    }
    @GetMapping("/game/gamesList")
    public List<String> getMethodName() {
        return controller.getGamesList();
    }
    @GetMapping("/anon")
    public String getMethodName(@CurrentSecurityContext SecurityContext context) {
        return context.getAuthentication().getName();
    }
    
}
