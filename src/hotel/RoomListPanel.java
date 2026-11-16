package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class RoomListPanel {

    // GUI Designer components
    private JPanel rootPanel;
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JScrollPane roomScrollPane;
    private JPanel roomContainer;
    private JPanel calendar;

    // Stores all hotel rooms
    private ArrayList<Room> rooms = new ArrayList<>();

    // Constructor
    public RoomListPanel() {

        // Arrange room cards vertically
        roomContainer.setLayout(
                new BoxLayout(roomContainer, BoxLayout.Y_AXIS)
        );

        roomContainer.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        // Scroll settings
        roomScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        roomScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // Load and display rooms
        loadRooms();
        displayRooms();
    }

    // Add hotel rooms
    private void loadRooms() {

        rooms.add(new Room(
                "Veldorra King",
                "1 King Size Bed",
                "2 pax",
                "35 sqm",
                "Room with one king-sized bed and views of Tempest.",
                true
        ));

        rooms.add(new Room(
                "Rigurd Deluxe",
                "2 Queen Size Beds",
                "4 pax",
                "45 sqm",
                "Spacious room with two queen-sized beds.",
                true
        ));

        rooms.add(new Room(
                "Diablo Twin",
                "1 Twin bed",
                "2 pax",
                "50 sqm",
                "Spacious room with two queen-sized beds.",
                true
        ));

        rooms.add(new Room(
                "Rimuru Suite",
                "1 King Size Bed",
                "2 pax",
                "60 sqm",
                "Luxury suite with a spacious living area.",
                false
        ));

        rooms.add(new Room(
                "KNF Presidential",
                "1 Kurt Sized Bed",
                "50 pax",
                "900 sqm",
                "Premium accommodation with 1 discord server per square meter.",
                false
        ));
    }

    // Generate all room cards
    private void displayRooms() {

        roomContainer.removeAll();

        for (Room room : rooms) {

            JPanel card = createRoomCard(room);

            roomContainer.add(card);

            roomContainer.add(
                    Box.createVerticalStrut(12)
            );
        }

        roomContainer.revalidate();
        roomContainer.repaint();
    }

    // Generate an individual room card
    private JPanel createRoomCard(Room room) {

        // Main card
        JPanel card = new JPanel(new BorderLayout(10, 10));

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                )
        );

        // Room name
        JLabel nameLabel = new JLabel(room.getName());

        nameLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        card.add(nameLabel, BorderLayout.NORTH);

        // Room information
        JPanel infoPanel = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        infoPanel.add(new JLabel(room.getBedType()));
        infoPanel.add(new JLabel(room.getCapacity()));
        infoPanel.add(new JLabel(room.getRoomSize()));

        // Description
        JPanel centerPanel = new JPanel(
                new BorderLayout(5, 10)
        );

        centerPanel.add(infoPanel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel(
                "<html>" + room.getDescription() + "</html>"
        );

        centerPanel.add(
                descriptionLabel,
                BorderLayout.CENTER
        );

        card.add(centerPanel, BorderLayout.CENTER);

        // Availability and button
        JPanel bottomPanel = new JPanel(
                new BorderLayout()
        );

        JLabel availabilityLabel = new JLabel(
                room.isAvailable() ? "Available" : "Unavailable"
        );

        JButton selectButton = new JButton("Select");

        selectButton.setEnabled(room.isAvailable());

        if (room.isAvailable()) {

            availabilityLabel.setForeground(
                    new Color(30, 150, 80)
            );

        } else {

            availabilityLabel.setForeground(Color.RED);

        }

        // Select button action
        selectButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    rootPanel,
                    "You selected: " + room.getName()
            );

        });

        bottomPanel.add(
                availabilityLabel,
                BorderLayout.WEST
        );

        bottomPanel.add(
                selectButton,
                BorderLayout.EAST
        );

        card.add(bottomPanel, BorderLayout.SOUTH);

        // Card dimensions
        card.setPreferredSize(
                new Dimension(550, 150)
        );

        card.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 150)
        );

        card.setMinimumSize(
                new Dimension(0, 150)
        );

        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        return card;
    }

    // Return the main panel
    public JPanel getRootPanel() {
        return rootPanel;
    }

    // Add a new room dynamically
    public void addRoom(Room room) {

        rooms.add(room);

        displayRooms();
    }

    // Remove a room dynamically
    public void removeRoom(Room room) {

        rooms.remove(room);

        displayRooms();
    }

}
