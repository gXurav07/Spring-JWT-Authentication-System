package aspect.oriented.programming.logging;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logging {

    @Before("execution(public void aspect.oriented.programming.service.UserService.login())")
    public void loginAdvice1() {
        System.out.println("Before login");
    }

    @After("execution(public void aspect.oriented.programming.service.UserService.login())")
    public void loginAdvice2() {
        System.out.println("After login");
    }
}
