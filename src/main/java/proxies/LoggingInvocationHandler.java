package proxies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println("Calling method via proxy: " + method.getName());
        Object result = method.invoke(target, objects);
        System.out.println("Returning via proxy: " + result);
        return result;
    }
}
