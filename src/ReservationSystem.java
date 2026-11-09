import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReservationSystem extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JComboBox<String> comboBox1; // Month
    private JComboBox<String> comboBox2; // Day
    private JComboBox<String> comboBox3; // Year
    private JTextField textField2;
    private JButton submitButton; // Optional action component

    public ReservationSystem() {
        // Configure the JFrame container
        setTitle("Tempest Hotel Reservation");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the window on the screen

        // Populate the Date ComboBoxes dynamically
        populateDateFields();
    }

    private void populateDateFields() {
        // Populate Months
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        comboBox1.setModel(new DefaultComboBoxModel<>(months));

        // Populate Days (1 - 31)
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        comboBox2.setModel(new DefaultComboBoxModel<>(days));

        // Populate Years (1987 - 2027)
        int startYear = 1987;
        int endYear = 2027;
        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i <= (endYear - startYear); i++) {
            years[i] = String.valueOf(startYear + i);
        }
        comboBox3.setModel(new DefaultComboBoxModel<>(years));
    }

    // Helper method to make the form launchable from Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ReservationSystem frame = new ReservationSystem();
                frame.setVisible(true);
                frame.setSize(400, 300);
            }
        });
    }
}
