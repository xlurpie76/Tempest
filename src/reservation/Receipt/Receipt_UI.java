package reservation.Receipt;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Receipt_UI extends JFrame {
    private static final String UNAVAILABLE = "unavailable";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    private JPanel pnlMain;
    private JLabel HRlabel;
    private JPanel Wholepnl;
    private JPanel Hrpnl;
    private JPanel JColorBg1;

    private JLabel Namejbl;
    private JLabel contactjbl;
    private JLabel cInjlb;
    private JLabel rdjbl;
    private JLabel emjbl;
    private JLabel cOjbl;

    private JLabel nmplder;
    private JLabel contactplder;
    private JLabel Emlplder;
    private JLabel rdplder;
    private JLabel chkinplder;
    private JLabel cOplder;

    public Receipt_UI() {
        setTitle("Hotel Receipt");
        if (pnlMain != null) {
            setContentPane(pnlMain);
        }
        loadLogoSafely();
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadLogoSafely() {
        URL logoUrl = getClass().getResource("Logo.png");
        if (logoUrl == null || Hrpnl == null) {
            return;
        }
        for (Component comp : Hrpnl.getComponents()) {
            if (comp instanceof JLabel label && label != HRlabel) {
                ImageIcon raw = new ImageIcon(logoUrl);
                Image scaled = raw.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
                break;
            }
        }
    }

    public JPanel getRootPanel() {
        return pnlMain;
    }

    public void setReceiptDetails(
            String fullName,
            String contactNumber,
            String email,
            LocalDate receiptDate,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        if (nmplder != null) nmplder.setText(safeText(fullName));
        if (contactplder != null) contactplder.setText(safeText(contactNumber));
        if (Emlplder != null) Emlplder.setText(safeText(email));
        if (rdplder != null) rdplder.setText(formatDate(receiptDate != null ? receiptDate : LocalDate.now()));
        if (chkinplder != null) chkinplder.setText(formatDate(checkInDate));
        if (cOplder != null) cOplder.setText(formatDate(checkOutDate));
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : UNAVAILABLE;
    }

    private String safeText(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : UNAVAILABLE;
    }
}