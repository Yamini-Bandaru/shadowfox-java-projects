import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class BankSystem extends JFrame {

    class Account {
        String name;
        double balance;
        ArrayList<String> history = new ArrayList<>();

        Account(String n, double b) {
            name = n;
            balance = b;
            history.add("Account created with: " + b);
        }
    }

    HashMap<String, Account> accounts = new HashMap<>();

    JPanel mainPanel = new JPanel(new CardLayout());

    Color bg = new Color(10, 10, 10);
    Color neonBlue = new Color(0, 234, 255);
    Color neonPink = new Color(255, 0, 255);
    Color neonGreen = new Color(57, 255, 20);

    public BankSystem() {
        setTitle("Bank Account Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel sidebar = makeSidebar();
        add(sidebar, BorderLayout.WEST);

        mainPanel.add(createAccountUI(), "CREATE");
        mainPanel.add(accountOpsUI(), "OPS");
        mainPanel.add(historyUI(), "HIST");
        mainPanel.add(accountsUI(), "LIST");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel makeSidebar() {
        JPanel side = new JPanel(new GridLayout(10, 1, 0, 20));
        side.setPreferredSize(new Dimension(200, 600));
        side.setBackground(bg);

        JLabel lbl = neonLabel("BANK MENU", neonGreen);
        side.add(lbl);

        JButton b1 = neonButton("Create Account");
        JButton b2 = neonButton("Deposit / Withdraw");
        JButton b3 = neonButton("Transaction History");
        JButton b4 = neonButton("All Accounts");

        b1.addActionListener(e -> switchScreen("CREATE"));
        b2.addActionListener(e -> switchScreen("OPS"));
        b3.addActionListener(e -> switchScreen("HIST"));
        b4.addActionListener(e -> switchScreen("LIST"));

        side.add(b1);
        side.add(b2);
        side.add(b3);
        side.add(b4);
        return side;
    }

    private void switchScreen(String name) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, name);
    }

    private JPanel createAccountUI() {
        JPanel p = base();
        p.add(neonLabel("Create Account", neonBlue));

        JTextField name = input("Account Name");
        JTextField bal = input("Initial Balance");

        JButton create = neonButton("Create");

        create.addActionListener(e -> {
            String n = name.getText();
            double b = Double.parseDouble(bal.getText());
            accounts.put(n, new Account(n, b));
            JOptionPane.showMessageDialog(null, "Account Created!");
        });

        p.add(name);
        p.add(bal);
        p.add(create);

        return p;
    }

    private JPanel accountOpsUI() {
        JPanel p = base();
        p.add(neonLabel("Deposit / Withdraw", neonPink));

        JTextField name = input("Account Name");
        JTextField amt = input("Amount");

        JButton dep = neonButton("Deposit");
        JButton wit = neonButton("Withdraw");

        dep.addActionListener(e -> {
            Account a = accounts.get(name.getText());
            if (a != null) {
                double v = Double.parseDouble(amt.getText());
                a.balance += v;
                a.history.add("Deposited: " + v);
                JOptionPane.showMessageDialog(null, "Deposit Success");
            }
        });

        wit.addActionListener(e -> {
            Account a = accounts.get(name.getText());
            if (a != null) {
                double v = Double.parseDouble(amt.getText());
                a.balance -= v;
                a.history.add("Withdrawn: " + v);
                JOptionPane.showMessageDialog(null, "Withdraw Success");
            }
        });

        p.add(name);
        p.add(amt);
        p.add(dep);
        p.add(wit);

        return p;
    }

    private JPanel historyUI() {
        JPanel p = base();
        p.add(neonLabel("Transaction History", neonGreen));

        JTextField name = input("Account Name");
        JTextArea area = textArea();

        JButton show = neonButton("Load");

        show.addActionListener(e -> {
            Account a = accounts.get(name.getText());
            if (a != null) {
                area.setText("");
                for (String s : a.history) area.append(s + "\n");
            }
        });

        p.add(name);
        p.add(show);
        p.add(new JScrollPane(area));

        return p;
    }

    private JPanel accountsUI() {
        JPanel p = base();
        p.add(neonLabel("All Accounts", neonBlue));

        JTextArea area = textArea();

        JButton ref = neonButton("Refresh");
        ref.addActionListener(e -> {
            area.setText("");
            for (Account a : accounts.values()) {
                area.append(a.name + " | Balance: " + a.balance + "\n");
            }
        });

        p.add(ref);
        p.add(new JScrollPane(area));

        return p;
    }

    private JPanel base() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
        p.setBackground(bg);
        return p;
    }

    private JLabel neonLabel(String text, Color c) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 26));
        lbl.setForeground(c);
        return lbl;
    }

    private JTextField input(String hint) {
        JTextField f = new JTextField(hint, 15);
        f.setBackground(bg);
        f.setForeground(Color.WHITE);
        f.setCaretColor(neonGreen);
        f.setFont(new Font("Arial", Font.PLAIN, 16));
        f.setBorder(BorderFactory.createLineBorder(neonGreen));
        return f;
    }

    private JButton neonButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(neonBlue);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBorder(BorderFactory.createLineBorder(neonBlue, 2));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(neonPink);
                btn.setBorder(BorderFactory.createLineBorder(neonPink, 2));
            }

            public void mouseExited(MouseEvent e) {
                btn.setForeground(neonBlue);
                btn.setBorder(BorderFactory.createLineBorder(neonBlue, 2));
            }
        });
        return btn;
    }

    private JTextArea textArea() {
        JTextArea a = new JTextArea(12, 20);
        a.setBackground(bg);
        a.setForeground(Color.WHITE);
        a.setFont(new Font("Arial", Font.PLAIN, 16));
        return a;
    }

    public static void main(String[] args) {
        new BankSystem();
    }
}