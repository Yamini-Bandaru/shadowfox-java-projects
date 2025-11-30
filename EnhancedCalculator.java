import java.awt.*;
import javax.script.*;
import javax.swing.*;

public class EnhancedCalculator extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField basicDisplay;
    private JTextField sciDisplay;

    private JTextField unitInput, unitOutput;
    private JComboBox<String> unitTypeBox, fromBox, toBox;

    public EnhancedCalculator() {

        setTitle("Enhanced Calculator");
        setSize(420, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Enhanced Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel tabPanel = new JPanel(new GridLayout(1, 3));
        tabPanel.setBackground(Color.BLACK);

        JButton basicTab = createTab("Basic");
        JButton sciTab = createTab("Scientific");
        JButton unitTab = createTab("Unit Converter");

        tabPanel.add(basicTab);
        tabPanel.add(sciTab);
        tabPanel.add(unitTab);

        add(tabPanel, BorderLayout.CENTER);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        mainPanel.add(createBasicPanel(), "Basic");
        mainPanel.add(createScientificPanel(), "Scientific");
        mainPanel.add(createUnitPanel(), "Unit Converter");

        add(mainPanel, BorderLayout.SOUTH);

        basicTab.addActionListener(e -> cardLayout.show(mainPanel, "Basic"));
        sciTab.addActionListener(e -> cardLayout.show(mainPanel, "Scientific"));
        unitTab.addActionListener(e -> cardLayout.show(mainPanel, "Unit Converter"));
    }

    private JButton createTab(String name) {
        JButton btn = new JButton(name);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setPreferredSize(new Dimension(100, 40));
        return btn;
    }

    // --------------------------------------------------
    // BUTTON STYLE (Option A Small Buttons)
    // --------------------------------------------------
    private JButton createSmallButton(String text) {

        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 16)); // smaller font
        b.setPreferredSize(new Dimension(48, 34)); // Option A size
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);

        return b;
    }

    // --------------------------------------------------
    // BASIC CALCULATOR
    // --------------------------------------------------
    private JPanel createBasicPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        basicDisplay = new JTextField();
        basicDisplay.setEditable(false);
        basicDisplay.setFont(new Font("Arial", Font.BOLD, 32));
        basicDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        basicDisplay.setPreferredSize(new Dimension(400, 90)); // 90px height
        panel.add(basicDisplay, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(5, 4, 6, 6));
        btnPanel.setBackground(Color.BLACK);

        String[] btns = {
                "C", "⌫", "/", "*",
                "7", "8", "9", "-",
                "4", "5", "6", "+",
                "1", "2", "3", "=",
                "0", ".", "", ""
        };

        for (String label : btns) {
            if (label.equals("")) {
                btnPanel.add(new JLabel());
                continue;
            }
            JButton b = createSmallButton(label);
            b.addActionListener(e -> handleBasicInput(label));
            btnPanel.add(b);
        }

        panel.add(btnPanel, BorderLayout.CENTER);
        return panel;
    }

    private void handleBasicInput(String input) {
        String cur = basicDisplay.getText();

        switch (input) {
            case "C":
                basicDisplay.setText("");
                return;
            case "⌫":
                if (!cur.isEmpty()) basicDisplay.setText(cur.substring(0, cur.length() - 1));
                return;
            case "=":
                try {
                    basicDisplay.setText("" + eval(cur));
                } catch (Exception e) {
                    basicDisplay.setText("Error");
                }
                return;
        }
        basicDisplay.setText(cur + input);
    }

    public static double eval(String exp) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() { ch = (++pos < exp.length()) ? exp.charAt(pos) : -1; }

            boolean eat(int target) {
                while (ch == ' ') nextChar();
                if (ch == target) { nextChar(); return true; }
                return false;
            }

            double parse() { nextChar(); return expression(); }

            double expression() {
                double x = term();
                while (true) {
                    if (eat('+')) x += term();
                    else if (eat('-')) x -= term();
                    else return x;
                }
            }

            double term() {
                double x = factor();
                while (true) {
                    if (eat('*')) x *= factor();
                    else if (eat('/')) x /= factor();
                    else return x;
                }
            }

            double factor() {
                if (eat('+')) return factor();
                if (eat('-')) return -factor();

                double x;
                int start = this.pos;

                if (eat('(')) {
                    x = expression();
                    eat(')');
                } else {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(exp.substring(start, this.pos));
                }
                return x;
            }
        }.parse();
    }

    // ------------------------------------------------------
    // SCIENTIFIC CALCULATOR
    // ------------------------------------------------------
    private JPanel createScientificPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        sciDisplay = new JTextField();
        sciDisplay.setEditable(false);
        sciDisplay.setFont(new Font("Arial", Font.BOLD, 32));
        sciDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        sciDisplay.setPreferredSize(new Dimension(400, 90)); // 90px
        panel.add(sciDisplay, BorderLayout.NORTH);

        JPanel sciPanel = new JPanel(new GridLayout(6, 4, 6, 6));
        sciPanel.setBackground(Color.BLACK);

        String[] sciBtns = {
                "sin", "cos", "tan", "√",
                "log", "ln", "^", "%",
                "(", ")", "π", "e",
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String s : sciBtns) {
            JButton b = createSmallButton(s);
            b.addActionListener(e -> handleScientific(s));
            sciPanel.add(b);
        }

        panel.add(sciPanel, BorderLayout.CENTER);
        return panel;
    }

    private void handleScientific(String input) {
        try {
            if (input.equals("=")) {
                sciDisplay.setText("" + evalScientific(sciDisplay.getText()));
                return;
            }
            sciDisplay.setText(sciDisplay.getText() + input);
        } catch (Exception e) {
            sciDisplay.setText("Err");
        }
    }

    private double evalScientific(String exp) throws Exception {
        exp = exp.replace("π", "" + Math.PI);
        exp = exp.replace("e", "" + Math.E);
        exp = exp.replace("√", "Math.sqrt");

        exp = exp.replace("sin", "Math.sin");
        exp = exp.replace("cos", "Math.cos");
        exp = exp.replace("tan", "Math.tan");
        exp = exp.replace("log", "Math.log10");
        exp = exp.replace("ln", "Math.log");

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        return Double.parseDouble(engine.eval(exp).toString());
    }

    // ------------------------------------------------------
    // UNIT CONVERTER
    // ------------------------------------------------------
    private JPanel createUnitPanel() {

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBackground(Color.BLACK);

        String[] types = {"Length", "Weight", "Temperature"};
        unitTypeBox = new JComboBox<>(types);

        String[] lengths = {"Meter", "Centimeter", "Kilometer"};
        fromBox = new JComboBox<>(lengths);
        toBox = new JComboBox<>(lengths);

        unitInput = new JTextField();
        unitInput.setPreferredSize(new Dimension(400, 90));
        unitInput.setFont(new Font("Arial", Font.BOLD, 26));

        unitOutput = new JTextField();
        unitOutput.setPreferredSize(new Dimension(400, 90));
        unitOutput.setEditable(false);
        unitOutput.setFont(new Font("Arial", Font.BOLD, 26));

        JButton convertBtn = createSmallButton("Convert");
        convertBtn.addActionListener(e -> convertUnits());

        panel.add(unitTypeBox);
        panel.add(unitInput);
        panel.add(fromBox);
        panel.add(toBox);
        panel.add(convertBtn);
        panel.add(unitOutput);

        return panel;
    }

    private void convertUnits() {
        try {
            double v = Double.parseDouble(unitInput.getText());
            String type = unitTypeBox.getSelectedItem().toString();
            String from = fromBox.getSelectedItem().toString();
            String to = toBox.getSelectedItem().toString();

            double result = 0;

            if (type.equals("Length"))
                result = convertLength(v, from, to);

            unitOutput.setText("" + result);

        } catch (Exception e) {
            unitOutput.setText("Invalid!");
        }
    }

    private double convertLength(double v, String f, String t) {
        if (f.equals("Meter")) {
            if (t.equals("Centimeter")) return v * 100;
            if (t.equals("Kilometer")) return v / 1000;
        }
        if (f.equals("Centimeter")) {
            if (t.equals("Meter")) return v / 100;
            if (t.equals("Kilometer")) return v / 100000;
        }
        if (f.equals("Kilometer")) {
            if (t.equals("Meter")) return v * 1000;
            if (t.equals("Centimeter")) return v * 100000;
        }
        return v;
    }

    public static void main(String[] args) {
        new EnhancedCalculator().setVisible(true);
    }
}
