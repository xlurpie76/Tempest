import javax.swing.*;

public class Room extends JFrame{
    private JPanel pnlMain;

    public Room() {
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Room frame = new Room();
                frame.setVisible(true);
                frame.setSize(400,300);
            }
        });
    }
}

