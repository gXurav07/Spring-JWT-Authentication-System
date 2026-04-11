package proxies;

public class PaymentServiceImpl implements PaymentService {

    @Override
    public void makePayment() {
        System.out.println("Original method: payment processed");
    }
}