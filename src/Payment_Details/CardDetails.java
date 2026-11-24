package Payment_Details;

public class CardDetails {
    public String cardNumber;
    public String cardHolderName;
    public String expiryDate;
    public int cvv;

    public CardDetails(String cardNumber, String cardHolderName, String expiryDate, int cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
}