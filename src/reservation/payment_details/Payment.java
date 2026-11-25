package reservation.payment_details;

public interface Payment {
    boolean processPayment(double totalPrice, CardDetails card);
}