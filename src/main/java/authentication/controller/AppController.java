package authentication.controller;


import authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final UserRepository userRepository;

    @Autowired
    public AppController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/square")
    public Integer save(@RequestParam Integer num) {
        return num*num;
    }

    @GetMapping("user/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String profile() {
        return "User profile";
    }

    @GetMapping("admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Only admin can see this";
    }
}
