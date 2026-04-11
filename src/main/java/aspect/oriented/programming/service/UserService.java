package aspect.oriented.programming.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private User user;

    public UserService() {
        this.user = new User("Gaurav", 24, "Dharmanagar, Tripura, India");
    }
    public void login()  {
        System.out.println("Logging User In!");
    }

    public void logout() {
        System.out.println("Logging User Out!");
    }
}
