import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.ArrayList;

public class Student_informationsystem extends JFrame {

    private JTextField idField, nameField, emailField, courseField, gradeField, phoneField;
    private JTable table;
    private DefaultTableModel model;

    // dashboard value labels
    private JLabel totalValueLabel, avgValueLabel, aValueLabel;

    private ArrayList<Student> students = new ArrayList<Student>();

    // colors
    private final Color BG = new Color(10, 10, 10);
    private final Color NEON = new Color(0, 200, 255);
    private final Color CARD_BG = new Color(20, 20, 20);

    public Student_informationsystem() {
        setTitle("Student Information System - Dark Neon Blue");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        // ---------- DASHBOARD (TOP) ----------
        JPanel dashboard = new JPanel(new GridLayout(1, 3, 20, 0));
        dashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        dashboard.setBackground(BG);

        dashboard.add(createCard("Total Students"));
        dashboard.add(createCard("Average Grade"));
        dashboard.add(createCard("A Grade Students"));

        add(dashboard, BorderLayout.NORTH);

        // ---------- FORM (CENTER) ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(NEON, 2),
                "Student Information",
                0, 0,
                new Font("Arial", Font.PLAIN, 16),
                NEON
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        idField = neonField();
        nameField = neonField();
        emailField = neonField();
        courseField = neonField();
        gradeField = neonField();
        phoneField = neonField();

        // Row 1: ID, Name, Email
        c.gridy = 0;

        c.gridx = 0;
        formPanel.add(neonLabel("Student ID"), c);
        c.gridx = 1;
        formPanel.add(idField, c);

        c.gridx = 2;
        formPanel.add(neonLabel("Full Name"), c);
        c.gridx = 3;
        formPanel.add(nameField, c);

        c.gridx = 4;
        formPanel.add(neonLabel("Email"), c);
        c.gridx = 5;
        formPanel.add(emailField, c);

        // Row 2: Course, Grade, Phone
        c.gridy = 1;

        c.gridx = 0;
        formPanel.add(neonLabel("Course/Program"), c);
        c.gridx = 1;
        formPanel.add(courseField, c);

        c.gridx = 2;
        formPanel.add(neonLabel("Grade (0-100)"), c);
        c.gridx = 3;
        formPanel.add(gradeField, c);

        c.gridx = 4;
        formPanel.add(neonLabel("Phone Number"), c);
        c.gridx = 5;
        formPanel.add(phoneField, c);

        // form buttons
        JPanel formButtons = new JPanel();
        formButtons.setBackground(BG);

        JButton addBtn = neonButton("Add Student");
        JButton clearFormBtn = neonButton("Clear Form");
        JButton calcBtn = neonButton("Grade Calculator");

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        clearFormBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        calcBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showGradeCalc();
            }
        });

        formButtons.add(addBtn);
        formButtons.add(clearFormBtn);
        formButtons.add(calcBtn);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(BG);
        centerContainer.add(formPanel, BorderLayout.CENTER);
        centerContainer.add(formButtons, BorderLayout.SOUTH);

        add(centerContainer, BorderLayout.CENTER);

        // ---------- TABLE (BOTTOM) ----------
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(NEON, 2),
                "Student Records",
                0, 0,
                new Font("Arial", Font.PLAIN, 16),
                NEON
        ));

        String[] columns = {"ID", "Name", "Email", "Course", "Grade", "Letter", "Phone"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setBackground(new Color(15, 15, 15));
        table.setForeground(NEON);
        table.setGridColor(NEON);
        table.setSelectionBackground(NEON);
        table.setSelectionForeground(Color.BLACK);
        table.setRowHeight(26);
        table.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(table);
        tablePanel.add(scroll, BorderLayout.CENTER);

        // bottom buttons (export + clear all)
        JPanel bottomButtons = new JPanel();
        bottomButtons.setBackground(BG);

        JButton exportCsvBtn = neonButton("Export CSV");
        JButton exportTxtBtn = neonButton("Export TXT");
        JButton clearAllBtn = neonButton("Clear All");

        exportCsvBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportCSV();
            }
        });

        exportTxtBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportTXT();
            }
        });

        clearAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAllData();
            }
        });

        bottomButtons.add(exportCsvBtn);
        bottomButtons.add(exportTxtBtn);
        bottomButtons.add(clearAllBtn);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setBackground(BG);
        southContainer.add(tablePanel, BorderLayout.CENTER);
        southContainer.add(bottomButtons, BorderLayout.SOUTH);

        add(southContainer, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------- CARD CREATOR ----------
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(NEON, 2));

        JLabel valueLabel = new JLabel("0", SwingConstants.CENTER);
        valueLabel.setForeground(NEON);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        // store references so we can update later
        if (title.equals("Total Students")) {
            totalValueLabel = valueLabel;
        } else if (title.equals("Average Grade")) {
            avgValueLabel = valueLabel;
        } else if (title.equals("A Grade Students")) {
            aValueLabel = valueLabel;
        }

        return card;
    }

    // ---------- UI HELPERS ----------
    private JTextField neonField() {
        JTextField f = new JTextField();
        f.setBackground(new Color(25, 25, 25));
        f.setForeground(Color.WHITE);
        f.setCaretColor(NEON);
        f.setBorder(BorderFactory.createLineBorder(NEON, 2));
        return f;
    }

    private JLabel neonLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(NEON);
        return l;
    }

    private JButton neonButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(Color.BLACK);
        b.setForeground(NEON);
        b.setBorder(BorderFactory.createLineBorder(NEON, 2));
        b.setFocusPainted(false);
        return b;
    }

    // ---------- LOGIC ----------
    private void addStudent() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().trim();
            String gradeText = gradeField.getText().trim();
            String phone = phoneField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() ||
                    course.isEmpty() || gradeText.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int grade = Integer.parseInt(gradeText);
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.");
                return;
            }

            String letter = gradeToLetter(grade);

            Student s = new Student(id, name, email, course, grade, letter, phone);
            students.add(s);

            model.addRow(new Object[]{id, name, email, course, grade, letter, phone});

            updateDashboard();
            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Grade must be a number.");
        }
    }

    private String gradeToLetter(int g) {
        if (g >= 90) return "A";
        if (g >= 80) return "B";
        if (g >= 70) return "C";
        if (g >= 60) return "D";
        return "F";
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");
        gradeField.setText("");
        phoneField.setText("");
    }

    private void showGradeCalc() {
        String input = JOptionPane.showInputDialog(this, "Enter marks (0-100):");
        if (input == null) return;
        try {
            int g = Integer.parseInt(input);
            if (g < 0 || g > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Letter Grade: " + gradeToLetter(g));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number.");
        }
    }

    private void updateDashboard() {
        // total students
        int total = students.size();
        totalValueLabel.setText(String.valueOf(total));

        // average grade
        double sum = 0.0;
        for (Student s : students) {
            sum += s.grade;
        }
        double avg = 0.0;
        if (total > 0) {
            avg = sum / total;
        }
        avgValueLabel.setText(String.format("%.1f", avg));

        // count of A grade
        int aCount = 0;
        for (Student s : students) {
            if ("A".equals(s.letter)) {
                aCount++;
            }
        }
        aValueLabel.setText(String.valueOf(aCount));
    }

    private void exportCSV() {
        try {
            FileWriter fw = new FileWriter("students_dark.csv");
            fw.write("ID,Name,Email,Course,Grade,Letter,Phone\n");
            for (Student s : students) {
                fw.write(s.id + "," + s.name + "," + s.email + "," +
                        s.course + "," + s.grade + "," + s.letter + "," + s.phone + "\n");
            }
            fw.close();
            JOptionPane.showMessageDialog(this, "Exported to students_dark.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while exporting CSV.");
        }
    }

    private void exportTXT() {
        try {
            FileWriter fw = new FileWriter("students_dark.txt");
            for (Student s : students) {
                fw.write(s.id + " | " + s.name + " | " + s.email + " | " +
                        s.course + " | " + s.grade + " | " + s.letter + " | " + s.phone + "\n");
            }
            fw.close();
            JOptionPane.showMessageDialog(this, "Exported to students_dark.txt");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while exporting TXT.");
        }
    }

    private void clearAllData() {
        students.clear();
        model.setRowCount(0);
        updateDashboard();
    }

    public static void main(String[] args) {
        // optional: use system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        new Student_informationsystem();
    }
}

// ---------------- Student CLASS ----------------
class Student {
    String id;
    String name;
    String email;
    String course;
    int grade;
    String letter;
    String phone;

    public Student(String id, String name, String email,
                   String course, int grade, String letter, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.grade = grade;
        this.letter = letter;
        this.phone = phone;
    }
}