package s21.web.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import s21.domain.service.UserService;
import s21.web.mapper.UserDomainWebMapper;
import s21.web.model.UserDTO;



@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    private final UserDomainWebMapper toWebMapper = UserDomainWebMapper.INSTANCE;

    @GetMapping("getallusers")
    public List<String> getAllUsers() throws IOException {
        return userService.getAll().stream().map(param -> param.toString()).toList();
    }
    @GetMapping("getUser/{UUID}")
    public UserDTO getUser(@PathVariable("UUID") String uuid) {
        return toWebMapper.domainToWeb(userService.getUserByUUID(UUID.fromString(uuid.replaceAll("\"", ""))));
    }
    
}
