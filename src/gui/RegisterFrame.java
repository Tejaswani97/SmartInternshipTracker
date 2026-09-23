package gui;

import dao.EmailVerificationDAO;
import dao.UserDAO;
import model.User;
import util.EmailService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.SecureRandom;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    private static final Color PAGE_BG = new Color(243, 246, 250);
    private static final Color NAVY = new Color(31, 41, 55);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BLUE_HOVER = new Color(29, 78, 216);
    private static final Color BORDER = new Color(224, 229, 236);
    private static final Color INPUT_BG = new Color(249, 250, 251);

    public RegisterFrame() {

        setTitle("Smart Internship Tracker - Register");
        setSize(980, 600);
        setMinimumSize(new Dimension(1000, 740));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        root.add(createBrandPanel(), BorderLayout.WEST);
        root.add(createRegisterPanel(), BorderLayout.CENTER);

        add(root);
    }

    private JPanel createBrandPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(NAVY);
        panel.setPreferredSize(new Dimension(350, 650));
        panel.setBorder(new EmptyBorder(45, 42, 45, 42));
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

        panel.add(Box.createVerticalStrut(14));

        JLabel secure = new JLabel("Email verification protects your account.");
        secure.setForeground(new Color(191, 219, 254));
        secure.setFont(new Font("SansSerif", Font.BOLD, 12));
        secure.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(secure);

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


    private JPanel createRegisterPanel() {

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(PAGE_BG);
        outer.setBorder(new EmptyBorder(35, 45, 35, 45));

        JPanel card = new RoundedPanel(24, WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(34, 38, 34, 38));
        card.setPreferredSize(new Dimension(465, 555));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create your account");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Start organizing your internship search.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(6));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(26));

        content.add(createFieldLabel("Full name"));
        nameField = createTextField();
        content.add(nameField);
        content.add(Box.createVerticalStrut(16));

        content.add(createFieldLabel("Email address"));
        emailField = createTextField();
        content.add(emailField);
        content.add(Box.createVerticalStrut(16));

        content.add(createFieldLabel("Password"));
        passwordField = createPasswordField();
        content.add(passwordField);
        content.add(Box.createVerticalStrut(9));

        JLabel otpNote = new JLabel("A 6-digit OTP will be sent to your email.");
        otpNote.setFont(new Font("SansSerif", Font.PLAIN, 11));
        otpNote.setForeground(MUTED);
        otpNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(otpNote);
        content.add(Box.createVerticalStrut(22));

        JButton registerButton = new JButton("Create account");
        stylePrimaryButton(registerButton);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(registerButton);
        content.add(Box.createVerticalStrut(16));

        JLabel footer = new JLabel("Already have an account? Close this window to return to login.");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(footer);

        card.add(content, BorderLayout.CENTER);
        outer.add(card);

        registerButton.addActionListener(e -> registerUser());

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

    private JPasswordField createPasswordField() {

        JPasswordField field = new JPasswordField();
        styleInput(field);
        return field;
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

    private void registerUser() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!email.contains("@")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address!",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String otp = generateOtp();

        EmailVerificationDAO verificationDAO = new EmailVerificationDAO();

        boolean saved = verificationDAO.saveVerification(
                name,
                email,
                password,
                otp
        );

        if (!saved) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not create verification request.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        boolean emailSent = EmailService.sendOtp(email, otp);

        if (!emailSent) {

            verificationDAO.deleteVerification(email);

            JOptionPane.showMessageDialog(
                    this,
                    "Could not send OTP email.\nPlease try again.",
                    "Email Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String enteredOtp = JOptionPane.showInputDialog(
                this,
                "A 6-digit OTP has been sent to:\n"
                        + email
                        + "\n\nEnter the OTP:",
                "Verify Email",
                JOptionPane.PLAIN_MESSAGE
        );

        if (enteredOtp == null) {
            return;
        }

        enteredOtp = enteredOtp.trim();

        boolean verified = verificationDAO.verifyOtp(
                email,
                enteredOtp
        );

        if (!verified) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid or expired OTP!",
                    "Verification Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        User user = new User(name, email, password);
        UserDAO userDAO = new UserDAO();

        boolean success = userDAO.registerUser(user);

        if (success) {

            verificationDAO.deleteVerification(email);

            JOptionPane.showMessageDialog(
                    this,
                    "Email verified successfully!\n"
                            + "Registration successful!\n\n"
                            + "You can now login."
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed!\n"
                            + "The email may already be registered.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String generateOtp() {

        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
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
}
