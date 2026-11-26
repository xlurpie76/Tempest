import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class Receipt_UI extends JFrame {
    private static final String UNAVAILABLE = "unavailable";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    private JPanel pnlMain;
    private JLabel HRlabel;
    private JPanel Wholepnl;
    private JPanel Hrpnl;
    private JPanel JColorBg1;
    private JPanel JcolorBg2;

    // Static section/heading labels
    private JLabel Namejbl;
    private JLabel contactjbl;
    private JLabel cInjlb;
    private JLabel rdjbl;
    private JLabel emjbl;
    private JLabel cOjbl;

    // Dynamic placeholder labels bound from Receipt_UI.form
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
        setSize(320, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        if (nmplder != null) {
            nmplder.setText(safeText(fullName));
        }
        if (contactplder != null) {
            contactplder.setText(safeText(contactNumber));
        }
        if (Emlplder != null) {
            Emlplder.setText(safeText(email));
        }
        if (rdplder != null) {
            rdplder.setText(formatDate(receiptDate != null ? receiptDate : LocalDate.now()));
        }
        if (chkinplder != null) {
            chkinplder.setText(formatDate(checkInDate));
        }
        if (cOplder != null) {
            cOplder.setText(formatDate(checkOutDate));
        }
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : UNAVAILABLE;
    }

    private String safeText(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : UNAVAILABLE;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Receipt_UI receipt = new Receipt_UI();
            receipt.setReceiptDetails(
                    null,
                    "",
                    "   ",
                    LocalDate.now(),
                    null,
                    null
            );
            receipt.setVisible(true);
        });
    }
}