
package hotel;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Create main window
            JFrame frame = new JFrame("Veldorra Hotel");

            // Create room list
            RoomListPanel roomList = new RoomListPanel();

            // Display room list
            frame.setContentPane(
                    roomList.getRootPanel()
            );

            // Window settings
            frame.setSize(750, 350);

            frame.setLocationRelativeTo(null);

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setVisible(true);

        });
    }
}
