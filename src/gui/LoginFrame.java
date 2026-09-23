package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private static final Color PAGE_BG = new Color(243, 246, 250);
    private static final Color NAVY = new Color(31, 41, 55);
    private static final Color NAVY_SOFT = new Color(55, 65, 81);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BLUE_HOVER = new Color(29, 78, 216);
    private static final Color BORDER = new Color(224, 229, 236);
    private static final Color INPUT_BG = new Color(249, 250, 251);

    public LoginFrame() {

        setTitle("Smart Internship Tracker");
        setSize(980, 600);
        setMinimumSize(new Dimension(1000, 740));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        root.add(createBrandPanel(), BorderLayout.WEST);
        root.add(createLoginPanel(), BorderLayout.CENTER);

        add(root);
    }

    private JPanel createBrandPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(NAVY);
        panel.setPreferredSize(new Dimension(390, 600));
        panel.setBorder(new EmptyBorder(45, 38, 45, 38));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Smart Internship Tracker");
        logo.setForeground(WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Everything in one place.");
        subtitle.setForeground(new Color(156, 163, 175));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(logo);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitle);
        panel.add(Box.createVerticalGlue());

        

        addFeature(panel, "Find real internships");
        addFeature(panel, "Save opportunities");
        addFeature(panel, "Track applications");
        addFeature(panel, "Manage deadlines");
        return panel;
    }

    private void addFeature(JPanel parent, String text) {

        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dot = new JLabel("•");
        dot.setForeground(new Color(96, 165, 250));
        dot.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel label = new JLabel(text);
        label.setForeground(new Color(229, 231, 235));
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));

        row.add(dot, BorderLayout.WEST);
        row.add(label, BorderLayout.CENTER);
        parent.add(row);
        parent.add(Box.createVerticalStrut(9));
    }

    private JPanel createLoginPanel() {

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(PAGE_BG);
        outer.setBorder(new EmptyBorder(35, 45, 35, 45));

        JPanel card = new RoundedPanel(24, WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(34, 38, 34, 38));
        card.setPreferredSize(new Dimension(430, 470));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome back");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to continue managing your internships.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(6));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(28));

        content.add(createFieldLabel("Email address"));
        emailField = createTextField();
        content.add(emailField);
        content.add(Box.createVerticalStrut(18));

        content.add(createFieldLabel("Password"));
        content.add(createPasswordFieldRow());
        content.add(Box.createVerticalStrut(24));

        loginButton = new JButton("Login");
        stylePrimaryButton(loginButton);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(loginButton);
        content.add(Box.createVerticalStrut(12));

        registerButton = new JButton("Create new account");
        styleSecondaryButton(registerButton);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(registerButton);
        content.add(Box.createVerticalStrut(18));

        JLabel note = new JLabel("Your account helps keep your internship search organized.");
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setForeground(MUTED);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(note);

        card.add(content, BorderLayout.CENTER);
        outer.add(card);

        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> new RegisterFrame());

        return outer;
    }

    private JLabel createFieldLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {

        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    private JPanel createPasswordFieldRow() {

        passwordField = new JPasswordField();
        styleInput(passwordField);

        char hiddenEcho = passwordField.getEchoChar();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        wrapper.setPreferredSize(new Dimension(0, 44));

        JButton eyeButton = new EyeButton();
        eyeButton.setToolTipText("Show password");

        eyeButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == 0) {
                passwordField.setEchoChar(hiddenEcho);
                eyeButton.setToolTipText("Show password");
                ((EyeButton) eyeButton).setPasswordVisible(false);
                eyeButton.repaint();
            } else {
                passwordField.setEchoChar((char) 0);
                eyeButton.setToolTipText("Hide password");
                ((EyeButton) eyeButton).setPasswordVisible(true);
                eyeButton.repaint();
            }
        });

        wrapper.add(passwordField, BorderLayout.CENTER);
        wrapper.add(eyeButton, BorderLayout.EAST);

        return wrapper;
    }

    private static class EyeButton extends JButton {

        private boolean passwordVisible = false;

        EyeButton() {
            setPreferredSize(new Dimension(46, 44));
            setMinimumSize(new Dimension(46, 44));
            setMaximumSize(new Dimension(46, 44));
            setBackground(INPUT_BG);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setRolloverEnabled(false);
        }

        void setPasswordVisible(boolean visible) {
            passwordVisible = visible;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(MUTED);
            g2.setStroke(new BasicStroke(1.7f));

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            g2.drawOval(cx - 10, cy - 6, 20, 12);
            g2.fillOval(cx - 3, cy - 3, 6, 6);

            if (!passwordVisible) {
                g2.drawLine(cx - 10, cy - 10, cx + 10, cy + 10);
            }

            g2.dispose();
        }
    }

    private void styleInput(JTextField field) {

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(INPUT_BG);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(9, 12, 9, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setPreferredSize(new Dimension(0, 44));
    }

    private void stylePrimaryButton(JButton button) {

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(BLUE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 18, 12, 18));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setPreferredSize(new Dimension(0, 46));

        addHover(button, BLUE, BLUE_HOVER);
    }

    private void styleSecondaryButton(JButton button) {

        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(TEXT);
        button.setBackground(new Color(243, 244, 246));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(11, 18, 11, 18));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(0, 44));

        addHover(button, new Color(243, 244, 246), new Color(229, 231, 235));
    }

    private void addHover(JButton button, Color normal, Color hover) {

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }

    private void loginUser() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.loginUser(email, password);

        if (user != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Welcome " + user.getName()
            );

            dispose();
            new DashboardFrame(user).setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class RoundedPanel extends JPanel {

        private final int radius;
        private final Color fill;

        RoundedPanel(int radius, Color fill) {
            this.radius = radius;
            this.fill = fill;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
