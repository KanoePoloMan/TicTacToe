package s21.web.controller.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import s21.web.controller.DomainController;
import s21.web.model.CurrentGameDTO;

@RestController
@RequestMapping("/game/multiplayer")
public class MultiplayerGameRestController {
    @Autowired
    private DomainController controller;

    @GetMapping("{UUID}/get")
    public CurrentGameDTO updateMultiplayerGameField(@PathVariable("UUID") String uuid) {
        return controller.getMultiplayerGameByUUID(uuid);
    }
    @PostMapping("{UUID}")
    public CurrentGameDTO updateMultiplayerGameField(
                                @PathVariable("UUID") String uuid, 
                                @RequestBody CurrentGameDTO field) throws Exception {
        return controller.updateFieldAndGetNextStep(field);
    }
    
    @GetMapping("list")
    public List<String> getMultiplayerGames() {
        return controller.getMultiplayerGames();
    }
    @GetMapping("checkInFoundedList")
    public String getSearchGame(@CurrentSecurityContext SecurityContext context) {
        UUID uuid = controller.checkInFoundedList(context.getAuthentication().getName());
        if(uuid == null) return null;
        return uuid.toString();
    }
}
