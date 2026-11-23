package Payment_Details;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Payment_Details extends JFrame{
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

    public Payment_Details() {
        JFrame frame = new JFrame("Payment Details");
        frame.setContentPane(pnlMAIN);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

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
                VISAButton.setBackground(new Color(173, 216, 230)); // Light blue
                mastercardButton.setBackground(null); // Reset to default

                JOptionPane.showMessageDialog(frame, "VISA selected.");
            }
        });
        mastercardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPaymentMethod = new Mastercard();

                // Highlight Mastercard, reset VISA
                mastercardButton.setBackground(new Color(173, 216, 230)); // Light blue
                VISAButton.setBackground(null); // Reset to default

                JOptionPane.showMessageDialog(frame, "Mastercard selected.");
            }
        });

        processPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 1. Payment Method check
                    if (selectedPaymentMethod == null) {
                        JOptionPane.showMessageDialog(frame, "Please select a payment method (VISA or Mastercard).", "Payment Method Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String cardNumber = tfCnumber.getText().trim();
                    String holderName = tfHolderName.getText().trim();
                    String expiryDate = textField3.getText().trim();
                    String cvvStr = textField4.getText().trim();

                    // 2. Individual empty checks
                    if (cardNumber.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Card Number cannot be empty.", "Card Number Error", JOptionPane.ERROR_MESSAGE);
                        tfCnumber.requestFocus();
                        return;
                    }

                    if (holderName.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Card Holder Name cannot be empty.", "Name Error", JOptionPane.ERROR_MESSAGE);
                        tfHolderName.requestFocus();
                        return;
                    }

                    if (expiryDate.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Expiry Date cannot be empty.", "Expiry Date Error", JOptionPane.ERROR_MESSAGE);
                        textField3.requestFocus();
                        return;
                    }

                    if (cvvStr.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "CVV cannot be empty.", "CVV Error", JOptionPane.ERROR_MESSAGE);
                        textField4.requestFocus();
                        return;
                    }

                    // 3. Validate that Card Number contains numbers only (before parsing CVV)
                    if (!cardNumber.matches("\\d+")) {
                        JOptionPane.showMessageDialog(frame, "Card Number must contain numbers only.", "Card Number Error", JOptionPane.ERROR_MESSAGE);
                        tfCnumber.requestFocus();
                        tfCnumber.selectAll();
                        return;
                    }

                    // 4. Parse CVV safely
                    int cvv;
                    try {
                        cvv = Integer.parseInt(cvvStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "CVV must contain numbers only.", "CVV Error", JOptionPane.ERROR_MESSAGE);
                        textField4.requestFocus();
                        textField4.selectAll();
                        return;
                    }

                    // 5. Run full validation in Visa / Mastercard class
                    CardDetails card = new CardDetails(cardNumber, holderName, expiryDate, cvv);
                    boolean success = selectedPaymentMethod.processPayment(totalPrice, card);

                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Payment successfully processed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }



    public static void main(String[] args) {
        new Payment_Details();
    }
}
