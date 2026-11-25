package reservation.payment_details;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;

public class Payment_Details extends JPanel {
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
    private JLabel HolderNameError1;
    private JLabel CdNumError1;
    private JLabel ExpiryDateError1;
    private JLabel CVVHolderError1;
    // Optional: if you add a JLabel bound in your .form file for the lock icon, uncomment below:
    // private JLabel lockLabel;

    private Payment selectedPaymentMethod = null;
    private double totalPrice = 0.0;
    private final Runnable onPaymentSuccess;

    private String actualCvvDigits = "";
    private boolean updatingCvvDisplay = false;

    private static final String NAME_HINT = "Name on Card";
    private static final String CARD_HINT = "Card Number";
    private static final String EXPIRY_HINT = "MM/YY";
    private static final String CVV_HINT = "CVC/CVV";

    private static final Color HINT_GRAY = new Color(150, 150, 150);
    private static final Color INPUT_BLACK = Color.BLACK;
    private static final Color ERROR_RED = new Color(255, 18, 0);
    private static final Color NORMAL_BORDER_COLOR = new Color(150, 150, 150);

    // Default constructor for standalone testing
    public Payment_Details() {
        this(null);
        JFrame frame = new JFrame("CHECKOUT");
        frame.setContentPane(pnlMAIN);
        frame.setSize(400, 495);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Constructor used by hotel.Main
    public Payment_Details(Runnable onPaymentSuccess) {
        this.onPaymentSuccess = onPaymentSuccess;

        jlPaymentDetails.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLUE),
                        BorderFactory.createEmptyBorder(0, 3, 0, 3)
                )
        );

        TitledBorder cdetailsBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0), 1),
                "CardDetails",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial Black", Font.PLAIN, 20),
                Color.BLACK
        );
        cdetailsBorder.setTitleColor(Color.BLACK);
        cdetailsPanel.setBorder(cdetailsBorder);

        TitledBorder pymentmethodBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(NORMAL_BORDER_COLOR, 1),
                "Payment Method",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial Black", Font.BOLD, 12),
                Color.BLACK
        );
        pymentmethodBorder.setTitleColor(Color.BLACK);
        pymentmethodpnl.setBorder(pymentmethodBorder);

        VISAButton.setBackground(Color.WHITE);
        mastercardButton.setBackground(Color.WHITE);
        setupPaymentIcons();

        clearAllErrors();
        setupPlaceholder(tfHolderName, NAME_HINT);
        setupPlaceholder(tfCnumber, CARD_HINT);
        setupPlaceholder(textField3, EXPIRY_HINT);
        setupExpiryInputFilter(textField3);
        setupMaskedCvvField();

        VISAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPaymentMethod = new Visa();
                VISAButton.setBackground(Color.CYAN);
                mastercardButton.setBackground(Color.WHITE);
                JOptionPane.showMessageDialog(getParentComponent(), "VISA selected.");
            }
        });

        mastercardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPaymentMethod = new Mastercard();
                mastercardButton.setBackground(Color.CYAN);
                VISAButton.setBackground(Color.WHITE);
                JOptionPane.showMessageDialog(getParentComponent(), "Mastercard selected.");
            }
        });

        processPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllErrors();

                try {
                    if (selectedPaymentMethod == null) {
                        JOptionPane.showMessageDialog(
                                getParentComponent(),
                                "Please select a payment method (VISA or Mastercard).",
                                "Payment Method Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    String holderName = getFieldInput(tfHolderName, NAME_HINT);
                    String cardNumber = getFieldInput(tfCnumber, CARD_HINT);
                    String expiryDate = getFieldInput(textField3, EXPIRY_HINT);
                    String cvvStr = getCvvInput();

                    boolean hasError = false;

                    if (holderName.isEmpty()) {
                        setHolderNameError1("Please enter the cardholder name.");
                        if (!hasError) tfHolderName.requestFocus();
                        hasError = true;
                    } else if (holderName.matches("\\d+")) {
                        setHolderNameError1("invalid name");
                        if (!hasError) {
                            tfHolderName.requestFocus();
                            tfHolderName.selectAll();
                        }
                        hasError = true;
                    }

                    if (cardNumber.isEmpty()) {
                        setCardNumberError("Card Number can't be empty.");
                        if (!hasError) tfCnumber.requestFocus();
                        hasError = true;
                    } else if (!cardNumber.matches("\\d+")) {
                        setCardNumberError("Card Number must contain numbers only.");
                        if (!hasError) {
                            tfCnumber.requestFocus();
                            tfCnumber.selectAll();
                        }
                        hasError = true;
                    }

                    if (expiryDate.isEmpty()) {
                        setExpiryDateError("Expiry Date can't be empty");
                        if (!hasError) textField3.requestFocus();
                        hasError = true;
                    } else if (!isValidExpiry(expiryDate)) {
                        setExpiryDateError("Use MM/YY (month 01-12).");
                        if (!hasError) {
                            textField3.requestFocus();
                            textField3.selectAll();
                        }
                        hasError = true;
                    }

                    if (cvvStr.isEmpty()) {
                        setCvvError("CVV/CVC can't be empty");
                        if (!hasError) textField4.requestFocus();
                        hasError = true;
                    } else if (!cvvStr.matches("\\d{3}")) {
                        setCvvError("Must be 3 numbers only.");
                        if (!hasError) {
                            textField4.requestFocus();
                            textField4.selectAll();
                        }
                        hasError = true;
                    }

                    if (hasError) {
                        return;
                    }

                    int cvv = Integer.parseInt(cvvStr);
                    CardDetails card = new CardDetails(cardNumber, holderName, expiryDate, cvv);
                    boolean success = selectedPaymentMethod.processPayment(totalPrice, card);

                    if (success) {
                        JOptionPane.showMessageDialog(
                                getParentComponent(),
                                "Payment successfully processed!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        resetForm();
                        if (onPaymentSuccess != null) {
                            onPaymentSuccess.run();
                        }
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                            getParentComponent(),
                            ex.getMessage(),
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    private void setupPaymentIcons() {
        ImageIcon visaIcon = loadPackageIcon("visa.png", 48, 28);
        if (visaIcon != null && VISAButton != null) {
            VISAButton.setIcon(visaIcon);
        }

        ImageIcon mastercardIcon = loadPackageIcon("mastercard.jpg", 48, 28);
        if (mastercardIcon != null && mastercardButton != null) {
            mastercardButton.setIcon(mastercardIcon);
        }

        // Example if you add a JLabel named lockLabel in your .form file:
        // ImageIcon lockIcon = loadPackageIcon("lock.png", 18, 18);
        // if (lockIcon != null && lockLabel != null) {
        //     lockLabel.setIcon(lockIcon);
        // }
    }

    private ImageIcon loadPackageIcon(String fileName, int width, int height) {
        URL resourceUrl = getClass().getResource(fileName);
        if (resourceUrl == null) {
            System.err.println("Could not find image in Payment_Details package: " + fileName);
            return null;
        }
        ImageIcon icon = new ImageIcon(resourceUrl);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public JPanel getRootPanel() {
        return pnlMAIN;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private Component getParentComponent() {
        return SwingUtilities.getWindowAncestor(pnlMAIN);
    }

    private void resetForm() {
        selectedPaymentMethod = null;
        VISAButton.setBackground(Color.WHITE);
        mastercardButton.setBackground(Color.WHITE);
        clearAllErrors();
        setupPlaceholder(tfHolderName, NAME_HINT);
        setupPlaceholder(tfCnumber, CARD_HINT);
        setupPlaceholder(textField3, EXPIRY_HINT);
        showCvvHint();
    }

    private void setupMaskedCvvField() {
        showCvvHint();
        setupCvvInputFilter(textField4);

        textField4.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (actualCvvDigits.isEmpty() && textField4.getForeground().equals(HINT_GRAY)) {
                    setCvvDisplay("");
                    textField4.setForeground(INPUT_BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (actualCvvDigits.isEmpty()) {
                    showCvvHint();
                }
            }
        });
    }

    private void showCvvHint() {
        actualCvvDigits = "";
        setCvvDisplay(CVV_HINT);
        textField4.setForeground(HINT_GRAY);
    }

    private void setCvvDisplay(String text) {
        updatingCvvDisplay = true;
        try {
            textField4.setText(text);
        } finally {
            updatingCvvDisplay = false;
        }
    }

    private String maskDigits(String digits) {
        return "*".repeat(digits.length());
    }

    private String getCvvInput() {
        return actualCvvDigits.trim();
    }

    private void setupCvvInputFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            private void syncFromCandidate(FilterBypass fb, String candidate) throws BadLocationException {
                if (updatingCvvDisplay) {
                    fb.replace(0, fb.getDocument().getLength(), candidate, null);
                    return;
                }

                if (candidate.equals(CVV_HINT)) {
                    actualCvvDigits = "";
                    setCvvDisplay(CVV_HINT);
                    field.setForeground(HINT_GRAY);
                    return;
                }

                StringBuilder nextDigits = new StringBuilder();
                int digitIndex = 0;

                for (int i = 0; i < candidate.length(); i++) {
                    char ch = candidate.charAt(i);
                    if (Character.isDigit(ch)) {
                        nextDigits.append(ch);
                    } else if (ch == '*') {
                        if (digitIndex < actualCvvDigits.length()) {
                            nextDigits.append(actualCvvDigits.charAt(digitIndex));
                            digitIndex++;
                        }
                    } else {
                        return;
                    }
                }

                if (nextDigits.length() > 3) {
                    return;
                }

                actualCvvDigits = nextDigits.toString();
                String masked = maskDigits(actualCvvDigits);

                setCvvDisplay(masked);
                field.setForeground(INPUT_BLACK);
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String candidate = current.substring(0, offset) + string + current.substring(offset);
                syncFromCandidate(fb, candidate);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String replacement = (text == null) ? "" : text;
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String candidate = current.substring(0, offset) + replacement + current.substring(offset + length);
                syncFromCandidate(fb, candidate);
            }
        });
    }

    private boolean isValidExpiry(String expiryDate) {
        if (!expiryDate.matches("\\d{2}/\\d{2}")) return false;
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        return month >= 1 && month <= 12;
    }

    private void setupExpiryInputFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            private String formatWithAutoSlash(String candidate) {
                if (candidate.equals(EXPIRY_HINT)) return EXPIRY_HINT;
                String digitsOnly = candidate.replace("/", "");
                if (!digitsOnly.matches("\\d*")) return null;
                if (digitsOnly.length() > 4) return null;

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digitsOnly.length(); i++) {
                    if (i == 2) formatted.append('/');
                    formatted.append(digitsOnly.charAt(i));
                }
                if (candidate.endsWith("/") && digitsOnly.length() == 2) {
                    formatted.append('/');
                }
                return formatted.toString();
            }

            private void applyFormattedText(FilterBypass fb, String formatted) throws BadLocationException {
                fb.replace(0, fb.getDocument().getLength(), formatted, null);
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String candidate = current.substring(0, offset) + string + current.substring(offset);
                String formatted = formatWithAutoSlash(candidate);
                if (formatted != null) applyFormattedText(fb, formatted);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String replacement = (text == null) ? "" : text;
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String candidate = current.substring(0, offset) + replacement + current.substring(offset + length);
                String formatted = formatWithAutoSlash(candidate);
                if (formatted != null) applyFormattedText(fb, formatted);
            }
        });
    }

    private void setupPlaceholder(JTextField field, String hint) {
        field.setText(hint);
        field.setForeground(HINT_GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint) && field.getForeground().equals(HINT_GRAY)) {
                    field.setText("");
                    field.setForeground(INPUT_BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(HINT_GRAY);
                }
            }
        });
    }

    private String getFieldInput(JTextField field, String hint) {
        String value = field.getText().trim();
        if (value.equals(hint) && field.getForeground().equals(HINT_GRAY)) {
            return "";
        }
        return value;
    }

    private void showErrorLabel(JLabel label, JTextField field, String message) {
        if (field != null) field.setBorder(BorderFactory.createLineBorder(ERROR_RED, 2));
        if (label != null) {
            label.setForeground(ERROR_RED);
            label.setText(message);
            label.setVisible(true);
        }
    }

    private void hideErrorLabel(JLabel label, JTextField field) {
        if (field != null) field.setBorder(BorderFactory.createLineBorder(NORMAL_BORDER_COLOR, 1));
        if (label != null) label.setVisible(false);
    }

    private void setHolderNameError1(String message) {
        showErrorLabel(HolderNameError1, tfHolderName, message);
    }

    private void setCardNumberError(String message) {
        showErrorLabel(CdNumError1, tfCnumber, message);
    }

    private void setExpiryDateError(String message) {
        showErrorLabel(ExpiryDateError1, textField3, message);
    }

    private void setCvvError(String message) {
        showErrorLabel(CVVHolderError1, textField4, message);
    }

    private void clearAllErrors() {
        hideErrorLabel(HolderNameError1, tfHolderName);
        hideErrorLabel(CdNumError1, tfCnumber);
        hideErrorLabel(ExpiryDateError1, textField3);
        hideErrorLabel(CVVHolderError1, textField4);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Payment_Details::new);
    }
}