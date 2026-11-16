import javax.swing.*;

import javax.swing.border.LineBorder;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;

import java.awt.event.ItemListener;

import java.util.HashMap;

import java.util.Map;

public class ContactInfoForm extends JFrame {

    private JPanel panel1;

    private JTextField tfLastName;

    private JTextField tfFirstName;

    private JTextField tfPhoneNum;

    private JTextField tfEmailAdd;

    private JTextField tfAddress; // Changed to JComboBox in GUI or handled via code if bound

    private JComboBox<String> cbAddress; // Province / Region

    private JComboBox<String> cbCity;    // City (Dependent on Address)

    private JButton btnSubmit;

    private JButton btnBack;

    private JLabel tlPhoneNum;

    private JLabel tlEmailAdd;

    private JLabel tlAddress;

    private JLabel tlCity;

    private JLabel tlFirstName;

    private JLabel tlLastName;

    // Mapping of Provinces to their respective cities/municipalities

    private Map<String, String[]> locationMap;

    public ContactInfoForm() {

        // Configure the JFrame container

        setTitle("Tempest Hotel Reservation");

        setContentPane(panel1);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize Location Data mapping

        initLocationData();

        // Populate and setup cascading combo boxes

        setupLocationComboBoxes();

        // Initialize button actions

        initListeners();

        pack();

        setLocationRelativeTo(null); // Center the window on the screen

    }

    private void initLocationData() {

        locationMap = new HashMap<>();

        // Cebu and its prominent cities/municipalities

        String[] cebuLocations = {

                "Select City",

                "Cebu City",

                "Mandaue City",

                "Lapu-Lapu City",

                "Talisay City",

                "Danao City",

                "Toledo City",

                "Consolation",

                "Liloan",

                "Compostela"

        };

        locationMap.put("Cebu", cebuLocations);

        // You can easily add other provinces later if needed, e.g.:

        // locationMap.put("Bohol", new String[]{"Select City", "Tagbilaran City", "Panglao", "Dao"});

    }

    private void setupLocationComboBoxes() {

        // Populate Address (Province) Combo Box

        // For now, starting with "Cebu" (and a default prompt option)

        String[] provinces = {"Select Province", "Cebu"};

        cbAddress.setModel(new DefaultComboBoxModel<>(provinces));

        // Initially disable city combo or set a blank model until province is selected

        cbCity.setModel(new DefaultComboBoxModel<>(new String[]{"Select City First"}));

        // Add listener to change City options dynamically when Province changes

        cbAddress.addItemListener(new ItemListener() {

            @Override

            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    String selectedProvince = (String) cbAddress.getSelectedItem();

                    if (locationMap.containsKey(selectedProvince)) {

                        // Load corresponding cities

                        String[] cities = locationMap.get(selectedProvince);

                        cbCity.setModel(new DefaultComboBoxModel<>(cities));

                        cbCity.setEnabled(true);

                    } else {

                        // Reset if "Select Province" is chosen

                        cbCity.setModel(new DefaultComboBoxModel<>(new String[]{"Select City First"}));

                        cbCity.setEnabled(false);

                    }

                }

            }

        });

    }

    private void initListeners() {

        btnSubmit.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                resetFieldBorders();

                String lastName = tfLastName.getText().trim();

                String firstName = tfFirstName.getText().trim();

                String phone = tfPhoneNum.getText().trim();

                String email = tfEmailAdd.getText().trim().toLowerCase();

                String province = (String) cbAddress.getSelectedItem();

                String city = (String) cbCity.getSelectedItem();

                StringBuilder errorMessages = new StringBuilder();

                boolean hasErrors = false;

                // 1. Validate Last Name

                if (lastName.isEmpty()) {

                    showFieldErrorHighlight(tfLastName);

                    errorMessages.append("- Last Name is required.\n");

                    hasErrors = true;

                } else if (!lastName.matches("^[a-zA-Z\\s-]+$")) {

                    showFieldErrorHighlight(tfLastName);

                    errorMessages.append("- Last Name must contain only letters.\n");

                    hasErrors = true;

                }

                // 2. Validate First Name

                if (firstName.isEmpty()) {

                    showFieldErrorHighlight(tfFirstName);

                    errorMessages.append("- First Name is required.\n");

                    hasErrors = true;

                } else if (!firstName.matches("^[a-zA-Z\\s-]+$")) {

                    showFieldErrorHighlight(tfFirstName);

                    errorMessages.append("- First Name must contain only letters.\n");

                    hasErrors = true;

                }

                // 3. Validate Phone Number

                if (phone.isEmpty()) {

                    showFieldErrorHighlight(tfPhoneNum);

                    errorMessages.append("- Phone Number is required.\n");

                    hasErrors = true;

                } else if (!phone.matches("^\\+?[0-9]{8,11}$")) {

                    showFieldErrorHighlight(tfPhoneNum);

                    errorMessages.append("- Enter a valid Phone Number (8-11 digits).\n");

                    hasErrors = true;

                }

                // 4. Validate Email Address

                String emailRegex = "^[\\w.-]+@(" +

                        "gmail\\.com|googlemail\\.com|" +

                        "outlook\\.com|hotmail\\.com|live\\.com|" +

                        "yahoo\\.com(?:\\.ph)?|" +

                        "icloud\\.com|me\\.com|mac\\.com|" +

                        "aol\\.com" +

                        ")$";

                if (email.isEmpty()) {

                    showFieldErrorHighlight(tfEmailAdd);

                    errorMessages.append("- Email Address is required.\n");

                    hasErrors = true;

                } else if (!email.matches(emailRegex)) {

                    showFieldErrorHighlight(tfEmailAdd);

                    errorMessages.append("- Please enter a valid email from a trusted provider\n  (e.g., Gmail, Outlook, Yahoo, iCloud, AOL).\n");

                    hasErrors = true;

                }

                // 5. Validate Province Selection

                if (province == null || province.equals("Select Province")) {

                    cbAddress.setBorder(new LineBorder(Color.RED, 2));

                    errorMessages.append("- Please select a Province.\n");

                    hasErrors = true;

                }

                // 6. Validate City Selection

                if (city == null || city.equals("Select City") || city.equals("Select City First")) {

                    cbCity.setBorder(new LineBorder(Color.RED, 2));

                    errorMessages.append("- Please select a valid City.\n");

                    hasErrors = true;

                }

                // If there are any errors, show them all together

                if (hasErrors) {

                    JOptionPane.showMessageDialog(ContactInfoForm.this,

                            "Please fix the following errors:\n\n" + errorMessages.toString(),

                            "Multiple Input Errors",

                            JOptionPane.ERROR_MESSAGE);

                    return;

                }

                // All validations passed successfully!

                String summary = "Reservation Confirmed!\n\n" +

                        "Name: " + firstName + " " + lastName + "\n" +

                        "Phone: " + phone + "\n" +

                        "Email: " + email + "\n" +

                        "Location: " + city + ", " + province;

                JOptionPane.showMessageDialog(ContactInfoForm.this,

                        summary,

                        "Success",

                        JOptionPane.INFORMATION_MESSAGE);

            }

        });

        btnBack.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                resetFieldBorders();

                tfLastName.setText("");

                tfFirstName.setText("");

                tfPhoneNum.setText("");

                tfEmailAdd.setText("");

                cbAddress.setSelectedIndex(0);

                cbCity.setSelectedIndex(0);

                cbCity.setEnabled(false);

            }

        });

    }

    private void showFieldErrorHighlight(JTextField field) {

        field.setBorder(new LineBorder(Color.RED, 2));

    }

    private void resetFieldBorders() {

        JTextField defaultField = new JTextField();

        tfLastName.setBorder(defaultField.getBorder());

        tfFirstName.setBorder(defaultField.getBorder());

        tfPhoneNum.setBorder(defaultField.getBorder());

        tfEmailAdd.setBorder(defaultField.getBorder());

        // Reset ComboBox borders

        cbAddress.setBorder(defaultField.getBorder());

        cbCity.setBorder(defaultField.getBorder());

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override

            public void run() {

                ContactInfoForm frame = new ContactInfoForm();

                frame.setVisible(true);
            }

        });

    }

}


