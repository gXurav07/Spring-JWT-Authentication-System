package aspect.oriented.programming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "aspect.oriented.programming")
@EnableAspectJAutoProxy
public class App {
    static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
