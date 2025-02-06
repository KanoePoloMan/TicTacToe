package s21.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import s21.web.model.CurrentGameDTO;

@Controller
@RequestMapping("/game/multiplayer")
public class MultiplayerGameController {
    @Autowired
    private DomainController controller;

    @GetMapping("newGame")
    public String getFindMultiplayerGame(@CurrentSecurityContext SecurityContext context) {
        CurrentGameDTO game = controller.findGameWithPlayer(context.getAuthentication().getName());
        if(game != null) return "redirect:/game/multiplayer/" + game.getUuid();
        return "/finding.html";
    }
    @GetMapping("{UUID}")
    public String getMultiplayerGame(Model model, @PathVariable("UUID") String uuid) throws Exception {

        model.addAttribute("pathUUID", uuid);

        return "gamemultiplayer.html";
    }
}
