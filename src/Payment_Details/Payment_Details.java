package Payment_Details;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Payment_Details {
    private JPanel pnlMAIN;
    private JPanel pnl1;
    private JLabel jlPaymentDetails;
    private JPanel cdetailsPanel;
    private JTextField tfCnumber;
    private JPanel cnumPanel;
    private JTextField tfHolderName;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel CdHolderNamePnl;
    private JPanel expdatePanel;
    private JPanel cvvpanel;
    private JPanel pymentmethodpnl;
    private JButton VISAButton;
    private JButton mastercardButton;
    private JButton processPaymentButton;
    private JPanel prcspaymentpnl;
    private Payment selectedPaymentMethod = null;
    private double totalPrice = 0.0;
    private Runnable onPaymentSuccessAction;

    public Payment_Details() {
        this(null);
    }

    public Payment_Details(Runnable onPaymentSuccessAction) {
        this.onPaymentSuccessAction = onPaymentSuccessAction;

        jlPaymentDetails.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLUE),
                        BorderFactory.createEmptyBorder(0, 3, 0, 3)
                )
        );

        cdetailsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150), 1), "CardDetails",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.PLAIN, 20),
                        Color.DARK_GRAY
                )
        );
        pymentmethodpnl.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                        "Payment Method",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.PLAIN, 12),
                        Color.DARK_GRAY
                )
        );

        VISAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPaymentMethod = new Visa();

                // Highlight VISA, reset Mastercard
                VISAButton.setBackground(new Color(173, 216, 230));
                mastercardButton.setBackground(null);

                JOptionPane.showMessageDialog(pnlMAIN, "VISA selected.");
            }
        });

        mastercardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPaymentMethod = new Mastercard();

                // Highlight Mastercard, reset VISA
                mastercardButton.setBackground(new Color(173, 216, 230));
                VISAButton.setBackground(null);

                JOptionPane.showMessageDialog(pnlMAIN, "Mastercard selected.");
            }
        });

        processPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 1. Payment Method check
                    if (selectedPaymentMethod == null) {
                        JOptionPane.showMessageDialog(pnlMAIN, "Please select a payment method (VISA or Mastercard).", "Payment Method Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String cardNumber = tfCnumber.getText().trim();
                    String holderName = tfHolderName.getText().trim();
                    String expiryDate = textField3.getText().trim();
                    String cvvStr = textField4.getText().trim();

                    // 2. Individual empty checks
                    if (cardNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(pnlMAIN, "Card Number cannot be empty.", "Card Number Error", JOptionPane.ERROR_MESSAGE);
                        tfCnumber.requestFocus();
                        return;
                    }

                    if (holderName.isEmpty()) {
                        JOptionPane.showMessageDialog(pnlMAIN, "Card Holder Name cannot be empty.", "Name Error", JOptionPane.ERROR_MESSAGE);
                        tfHolderName.requestFocus();
                        return;
                    }

                    if (expiryDate.isEmpty()) {
                        JOptionPane.showMessageDialog(pnlMAIN, "Expiry Date cannot be empty.", "Expiry Date Error", JOptionPane.ERROR_MESSAGE);
                        textField3.requestFocus();
                        return;
                    }

                    if (cvvStr.isEmpty()) {
                        JOptionPane.showMessageDialog(pnlMAIN, "CVV cannot be empty.", "CVV Error", JOptionPane.ERROR_MESSAGE);
                        textField4.requestFocus();
                        return;
                    }

                    // 3. Validate that Card Number contains numbers only (before parsing CVV)
                    if (!cardNumber.matches("\\d+")) {
                        JOptionPane.showMessageDialog(pnlMAIN, "Card Number must contain numbers only.", "Card Number Error", JOptionPane.ERROR_MESSAGE);
                        tfCnumber.requestFocus();
                        tfCnumber.selectAll();
                        return;
                    }

                    // 4. Parse CVV safely
                    int cvv;
                    try {
                        cvv = Integer.parseInt(cvvStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(pnlMAIN, "CVV must contain numbers only.", "CVV Error", JOptionPane.ERROR_MESSAGE);
                        textField4.requestFocus();
                        textField4.selectAll();
                        return;
                    }

                    // 5. Run full validation in Visa / Mastercard class
                    CardDetails card = new CardDetails(cardNumber, holderName, expiryDate, cvv);
                    boolean success = selectedPaymentMethod.processPayment(totalPrice, card);

                    if (success) {
                        JOptionPane.showMessageDialog(pnlMAIN, "Payment successfully processed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if (onPaymentSuccessAction != null) {
                            onPaymentSuccessAction.run();
                        }
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(pnlMAIN, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getRootPanel() {
        return pnlMAIN;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}