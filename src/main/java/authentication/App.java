package authentication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "authentication")
public class App {
    static void main(String[] args) {
        SpringApplication.run(authentication.App.class, args);
    }
}
