 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InventorySystem extends JFrame {

    class Product {
        String name;
        int qty;
        double price;

        Product(String n, int q, double p) {
            name = n;
            qty = q;
            price = p;
        }
    }

    ArrayList<Product> inventory = new ArrayList<>();
    ArrayList<String> recent = new ArrayList<>();

    JPanel mainPanel = new JPanel(new CardLayout());

    Color bg = new Color(10, 10, 10);
    Color neonBlue = new Color(0, 234, 255);
    Color neonPink = new Color(255, 0, 255);
    Color neonGreen = new Color(57, 255, 20);

    public InventorySystem() {
        setTitle("Inventory Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel sidebar = makeSidebar();
        add(sidebar, BorderLayout.WEST);

        mainPanel.add(dashboardUI(), "DASH");
        mainPanel.add(addProductUI(), "ADD");
        mainPanel.add(productListUI(), "LIST");
        mainPanel.add(lowStockUI(), "LOW");
        mainPanel.add(recentUI(), "REC");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel makeSidebar() {
        JPanel side = new JPanel(new GridLayout(10, 1, 0, 20));
        side.setPreferredSize(new Dimension(200, 600));
        side.setBackground(bg);

        JLabel lbl = neonLabel("INVENTORY", neonGreen);
        side.add(lbl);

        JButton b1 = neonButton("Dashboard");
        JButton b2 = neonButton("Add Product");
        JButton b3 = neonButton("Product List");
        JButton b4 = neonButton("Low Stock");
        JButton b5 = neonButton("Recent Activity");

        b1.addActionListener(e -> switchScreen("DASH"));
        b2.addActionListener(e -> switchScreen("ADD"));
        b3.addActionListener(e -> switchScreen("LIST"));
        b4.addActionListener(e -> switchScreen("LOW"));
        b5.addActionListener(e -> switchScreen("REC"));

        side.add(b1);
        side.add(b2);
        side.add(b3);
        side.add(b4);
        side.add(b5);

        return side;
    }

    private void switchScreen(String n) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, n);
    }

    private JPanel dashboardUI() {
        JPanel p = base();
        p.add(neonLabel("Inventory Dashboard", neonBlue));
        return p;
    }

    private JPanel addProductUI() {
        JPanel p = base();
        p.add(neonLabel("Add Product", neonGreen));

        JTextField name = input("Product Name");
        JTextField qty = input("Quantity");
        JTextField price = input("Price");

        JButton add = neonButton("Add");

        add.addActionListener(e -> {
            inventory.add(new Product(name.getText(),
                    Integer.parseInt(qty.getText()),
                    Double.parseDouble(price.getText())));
            recent.add("Added product: " + name.getText());
            JOptionPane.showMessageDialog(null, "Product Added");
        });

        p.add(name);
        p.add(qty);
        p.add(price);
        p.add(add);

        return p;
    }

    private JPanel productListUI() {
        JPanel p = base();
        JTextArea area = textArea();

        JButton show = neonButton("Refresh");
        show.addActionListener(e -> {
            area.setText("");
            for (Product pr : inventory) {
                area.append(pr.name + " | Qty: " + pr.qty + " | Price: " + pr.price + "\n");
            }
        });

        p.add(neonLabel("Product List", neonBlue));
        p.add(show);
        p.add(new JScrollPane(area));

        return p;
    }

    private JPanel lowStockUI() {
        JPanel p = base();
        JTextArea area = textArea();
        JButton show = neonButton("Check");

        show.addActionListener(e -> {
            area.setText("");
            for (Product pr : inventory) {
                if (pr.qty < 5)
                    area.append("LOW STOCK → " + pr.name + " : " + pr.qty + "\n");
            }
        });

        p.add(neonLabel("Low Stock Alerts", neonPink));
        p.add(show);
        p.add(new JScrollPane(area));

        return p;
    }

    private JPanel recentUI() {
        JPanel p = base();
        JTextArea area = textArea();
        JButton show = neonButton("Show");

        show.addActionListener(e -> {
            area.setText("");
            for (String s : recent) area.append(s + "\n");
        });

        p.add(neonLabel("Recent Activity", neonBlue));
        p.add(show);
        p.add(new JScrollPane(area));

        return p;
    }

    private JPanel base() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        p.setBackground(bg);
        return p;
    }

    private JLabel neonLabel(String text, Color c) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(c);
        lbl.setFont(new Font("Arial", Font.BOLD, 26));
        return lbl;
    }

    private JTextField input(String hint) {
        JTextField f = new JTextField(hint, 15);
        f.setForeground(Color.WHITE);
        f.setBackground(bg);
        f.setBorder(BorderFactory.createLineBorder(neonGreen));
        f.setFont(new Font("Arial", Font.PLAIN, 16));
        f.setCaretColor(neonGreen);
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
        new InventorySystem();
    }
}