import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
class Contact {
    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Name: " + name + "\nPhone: " + phone + "\nEmail: " + email + "\n";
    }
}

public class NewGUI extends JFrame {

    ArrayList<Contact> contacts = new ArrayList<>();

    JTextField nameField, phoneField, emailField, searchField;
    JTextArea displayArea;

    public NewGUI() {

        setTitle("Contact Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP PANEL
        JPanel topPanel = new JPanel(new GridLayout(4, 2));

        topPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Phone (10 digits):"));
        phoneField = new JTextField();
        topPanel.add(phoneField);

        topPanel.add(new JLabel("Email (@gmail.com):"));
        emailField = new JTextField();
        topPanel.add(emailField);

        JButton addButton = new JButton("Add Contact");
        topPanel.add(addButton);

        JButton viewButton = new JButton("View All");
        topPanel.add(viewButton);

        add(topPanel, BorderLayout.NORTH);

        // CENTER AREA
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");
        bottomPanel.add(new JLabel("Keyword:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // ACTIONS
        addButton.addActionListener(e -> addContact());
        viewButton.addActionListener(e -> viewContacts());
        searchButton.addActionListener(e -> searchContact());
        deleteButton.addActionListener(e -> deleteContact());
    }

    // VALIDATION
    boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    boolean isDuplicate(String phone) {
        for (Contact c : contacts) {
            if (c.getPhone().equals(phone)) return true;
        }
        return false;
    }

    // CRUD METHODS

    void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Phone must be exactly 10 digits!");
            return;
        }

        if (isDuplicate(phone)) {
            JOptionPane.showMessageDialog(this, "Phone already exists!");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email must end with @gmail.com");
            return;
        }

        contacts.add(new Contact(name, phone, email));
        JOptionPane.showMessageDialog(this, "Contact Added Successfully!");

        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    void viewContacts() {
        if (contacts.isEmpty()) {
            displayArea.setText("No contacts found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Contact c : contacts) {
            sb.append(c.toString()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    void searchContact() {
        String key = searchField.getText().trim().toLowerCase();

        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a keyword to search.");
            return;
        }

        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(key) || c.getPhone().contains(key)) {
                displayArea.setText("CONTACT FOUND:\n\n" + c.toString());
                return;
            }
        }

        displayArea.setText("Contact not found.");
    }

    void deleteContact() {
        String key = searchField.getText().trim();

        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter phone number to delete.");
            return;
        }

        for (Contact c : contacts) {
            if (c.getPhone().equals(key)) {
                contacts.remove(c);
                JOptionPane.showMessageDialog(this, "Contact Deleted Successfully!");
                viewContacts();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Contact not found.");
    }

    public static void main(String[] args) {
        new NewGUI().setVisible(true);
    }
}
