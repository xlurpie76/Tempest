package Payment_Details;
class Mastercard implements Payment{

    @Override
    public boolean processPayment(double totalPrice, CardDetails card) {

        if (card == null) {
            throw new IllegalArgumentException("Card details cannot be null");
        }

        if (card.cardNumber == null || !card.cardNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Card number must contain numbers only");
        }

        if (card.cardNumber.length() < 12) {
            throw new IllegalArgumentException("Card number must be at least 12 digits");
        }

        if (card.cvv <= 0) {
            throw new IllegalArgumentException("Invalid CVV");
        }

        return true;
    }
}