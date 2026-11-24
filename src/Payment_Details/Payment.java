package Payment_Details;

public interface Payment {
    boolean processPayment(double totalPrice, CardDetails card);
}