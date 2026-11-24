package hotel;

import ContactInfo.ContactInfoForm;
import Payment_Details.Payment_Details;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame mainFrame;

    public static void showPanel(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Veldorra Hotel");
            mainFrame.setSize(1100, 550);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            RoomListPanel roomList = new RoomListPanel();
            Calendar.Calendar calendar = new Calendar.Calendar();

            JPanel roomAndCalendarPanel = new JPanel(new BorderLayout(12, 0));
            roomAndCalendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            roomAndCalendarPanel.add(roomList.getRootPanel(), BorderLayout.CENTER);
            roomAndCalendarPanel.add(calendar.getRootPanel(), BorderLayout.EAST);

            Payment_Details paymentDetails = new Payment_Details(
                    () -> showPanel(roomAndCalendarPanel) // Optional: return to start after payment succeeds
            );

            ContactInfoForm contactInfoForm = new ContactInfoForm(
                    () -> showPanel(roomAndCalendarPanel),          // Back button -> Room + Calendar
                    () -> showPanel(paymentDetails.getRootPanel())  // Submit button -> Payment screen
            );

            roomList.setOnRoomSelected(selectedRoom -> showPanel(contactInfoForm.getRootPanel()));

            mainFrame.setContentPane(roomAndCalendarPanel);
            mainFrame.setVisible(true);
        });
    }
}