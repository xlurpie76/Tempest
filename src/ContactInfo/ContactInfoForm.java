package ContactInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

public class ContactInfoForm {
    private JPanel panel1;
    private JTextField tfLastName;
    private JTextField tfFirstName;
    private JTextField tfPhoneNum;
    private JTextField tfEmailAdd;
    private JTextField tfAddress;
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

    private Map<String, String[]> locationMap;
    private Runnable onBackAction;
    private Runnable onSubmitSuccessAction;

    public ContactInfoForm() {
        this(null, null);
    }

    public ContactInfoForm(Runnable onBackAction, Runnable onSubmitSuccessAction) {
        this.onBackAction = onBackAction;
        this.onSubmitSuccessAction = onSubmitSuccessAction;

        initLocationData();
        setupLocationComboBoxes();
        initListeners();
    }

    public JPanel getRootPanel() {
        return panel1;
    }

    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }

    public void setOnSubmitSuccessAction(Runnable onSubmitSuccessAction) {
        this.onSubmitSuccessAction = onSubmitSuccessAction;
    }

    private void initLocationData() {
        locationMap = new HashMap<>();

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
    }

    private void setupLocationComboBoxes() {
        String[] provinces = {"Select Province", "Cebu"};
        cbAddress.setModel(new DefaultComboBoxModel<>(provinces));
        cbCity.setModel(new DefaultComboBoxModel<>(new String[]{"Select City First"}));
        cbCity.setEnabled(false);

        cbAddress.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedProvince = (String) cbAddress.getSelectedItem();
                if (locationMap.containsKey(selectedProvince)) {
                    String[] cities = locationMap.get(selectedProvince);
                    cbCity.setModel(new DefaultComboBoxModel<>(cities));
                    cbCity.setEnabled(true);
                } else {
                    cbCity.setModel(new DefaultComboBoxModel<>(new String[]{"Select City First"}));
                    cbCity.setEnabled(false);
                }
            }
        });
    }

    private void initListeners() {
        btnSubmit.addActionListener(e -> {
            resetFieldBorders();

            String lastName = tfLastName.getText().trim();
            String firstName = tfFirstName.getText().trim();
            String phone = tfPhoneNum.getText().trim();
            String email = tfEmailAdd.getText().trim().toLowerCase();
            String province = (String) cbAddress.getSelectedItem();
            String city = (String) cbCity.getSelectedItem();

            StringBuilder errorMessages = new StringBuilder();
            boolean hasErrors = false;

            if (lastName.isEmpty()) {
                showFieldErrorHighlight(tfLastName);
                errorMessages.append("- Last Name is required.\n");
                hasErrors = true;
            } else if (!lastName.matches("^[a-zA-Z\\s-]+$")) {
                showFieldErrorHighlight(tfLastName);
                errorMessages.append("- Last Name must contain only letters.\n");
                hasErrors = true;
            }

            if (firstName.isEmpty()) {
                showFieldErrorHighlight(tfFirstName);
                errorMessages.append("- First Name is required.\n");
                hasErrors = true;
            } else if (!firstName.matches("^[a-zA-Z\\s-]+$")) {
                showFieldErrorHighlight(tfFirstName);
                errorMessages.append("- First Name must contain only letters.\n");
                hasErrors = true;
            }

            if (phone.isEmpty()) {
                showFieldErrorHighlight(tfPhoneNum);
                errorMessages.append("- Phone Number is required.\n");
                hasErrors = true;
            } else if (!phone.matches("^\\+?[0-9]{8,11}$")) {
                showFieldErrorHighlight(tfPhoneNum);
                errorMessages.append("- Enter a valid Phone Number (8-11 digits).\n");
                hasErrors = true;
            }

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

            if (province == null || province.equals("Select Province")) {
                cbAddress.setBorder(new LineBorder(Color.RED, 2));
                errorMessages.append("- Please select a Province.\n");
                hasErrors = true;
            }

            if (city == null || city.equals("Select City") || city.equals("Select City First")) {
                cbCity.setBorder(new LineBorder(Color.RED, 2));
                errorMessages.append("- Please select a valid City.\n");
                hasErrors = true;
            }

            if (hasErrors) {
                JOptionPane.showMessageDialog(
                        panel1,
                        "Please fix the following errors:\n\n" + errorMessages,
                        "Multiple Input Errors",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String summary = "Reservation Confirmed!\n\n" +
                    "Name: " + firstName + " " + lastName + "\n" +
                    "Phone: " + phone + "\n" +
                    "Email: " + email + "\n" +
                    "Location: " + city + ", " + province;

            JOptionPane.showMessageDialog(
                    panel1,
                    summary,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (onSubmitSuccessAction != null) {
                onSubmitSuccessAction.run();
            }
        });

        btnBack.addActionListener(e -> {
            resetFormFields();
            if (onBackAction != null) {
                onBackAction.run();
            }
        });
    }

    public void resetFormFields() {
        resetFieldBorders();
        tfLastName.setText("");
        tfFirstName.setText("");
        tfPhoneNum.setText("");
        tfEmailAdd.setText("");
        if (tfAddress != null) {
            tfAddress.setText("");
        }
        cbAddress.setSelectedIndex(0);
        cbCity.setSelectedIndex(0);
        cbCity.setEnabled(false);
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
        cbAddress.setBorder(defaultField.getBorder());
        cbCity.setBorder(defaultField.getBorder());
    }
}