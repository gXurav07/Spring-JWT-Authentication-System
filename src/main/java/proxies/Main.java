package proxies;

import java.lang.reflect.Proxy;

public class Main {
    static void main() {
        PaymentService target = new PaymentServiceImpl();
        PaymentService proxy = (PaymentService) Proxy.newProxyInstance(
                PaymentService.class.getClassLoader(),
                new Class[]{PaymentService.class},
                new LoggingInvocationHandler(target)
        );

        proxy.makePayment();
    }
}
