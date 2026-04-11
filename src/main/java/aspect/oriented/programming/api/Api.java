package aspect.oriented.programming.api;

import aspect.oriented.programming.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        userService.login();
        return "login success!";
    }

}
