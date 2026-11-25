package reservation.room;

import javax.swing.*;

public class RoomPanel extends JFrame{
    private JPanel pnlMain;

    public RoomPanel() {
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RoomPanel frame = new RoomPanel();
                frame.setVisible(true);
                frame.setSize(400,300);
            }
        });
    }
}

