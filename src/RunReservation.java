import reservation.calendar.Calendar;
import reservation.contact.ContactInfoForm;
import reservation.payment_details.Payment_Details;
import reservation.room.RoomListPanel;

import javax.swing.*;
import java.awt.*;

public class RunReservation {
    private static JFrame mainFrame;

    public static void showPanel(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Tempest Hotel");
            mainFrame.setSize(1100, 550);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            RoomListPanel roomList = new RoomListPanel();
            Calendar calendar = new Calendar();

            Runnable updateTotal = () -> roomList.setTotalText(
                    String.format(
                    "%d days | Total: %.2f",
                    calendar.getNumberOfDays(), calendar.getTotalPrice())
            );

            calendar.setOnSelectionChanged(updateTotal);
            updateTotal.run(); // show the initial 0 state

            JPanel calendarPnl = roomList.getCalendarPnl();
            calendarPnl.setLayout(new BorderLayout());
            calendarPnl.add(calendar.getRootPanel(), BorderLayout.CENTER);

            JPanel roomAndCalendarPanel = new JPanel(new BorderLayout(12, 0));
            roomAndCalendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            roomAndCalendarPanel.add(roomList.getRootPanel(), BorderLayout.CENTER);
            calendarPnl.add(calendar.getRootPanel(), BorderLayout.CENTER);

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

            roomList.setOnRoomSelected(room -> calendar.setBasePrice(room.getPricePerNight()));
        });
    }
}