package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private static final Color BACKGROUND_COLOR =
            new Color(245, 247, 250);

    private static final Color CARD_COLOR =
            Color.WHITE;

    private static final Color TEXT_COLOR =
            new Color(31, 41, 55);

    private static final Color MUTED_TEXT_COLOR =
            new Color(107, 114, 128);

    private static final Color ACCENT_COLOR =
            new Color(59, 130, 246);

    private static final Color BORDER_COLOR =
            new Color(229, 231, 235);

    public LoginFrame() {

        setTitle("Smart Internship Tracker");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root =
                new JPanel(new GridBagLayout());

        root.setBackground(BACKGROUND_COLOR);

        JPanel card =
                new JPanel(new GridBagLayout());

        card.setBackground(CARD_COLOR);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        BorderFactory.createEmptyBorder(
                                35,
                                45,
                                35,
                                45
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // Title
        JLabel titleLabel =
                new JLabel(
                        "SMART INTERNSHIP TRACKER",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(
                TEXT_COLOR
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        card.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel =
                new JLabel(
                        "Login to manage your internships",
                        SwingConstants.CENTER
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(
                MUTED_TEXT_COLOR
        );

        gbc.gridy = 1;

        card.add(subtitleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;

        card.add(
                createLabel("Email"),
                gbc
        );

        emailField =
                createTextField();

        gbc.gridx = 1;

        card.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;

        card.add(
                createLabel("Password"),
                gbc
        );

        passwordField =
                new JPasswordField();

        passwordField.setPreferredSize(
                new Dimension(0, 38)
        );

        gbc.gridx = 1;

        card.add(passwordField, gbc);

        // Login
        loginButton =
                new JButton("Login");

        stylePrimaryButton(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        card.add(loginButton, gbc);

        // Register
        registerButton =
                new JButton("Create New Account");

        styleSecondaryButton(registerButton);

        gbc.gridy = 5;

        card.add(registerButton, gbc);

        root.add(card);

        loginButton.addActionListener(
                e -> loginUser()
        );

        registerButton.addActionListener(
                e -> new RegisterFrame()
        );

        add(root);

        setVisible(true);
    }

    private void loginUser() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (
                email.isEmpty()
                        || password.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        UserDAO userDAO =
                new UserDAO();

        User user =
                userDAO.loginUser(
                        email,
                        password
                );

        if (user != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Welcome "
                            + user.getName()
            );

            new DashboardFrame(user);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(TEXT_COLOR);

        return label;
    }

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setPreferredSize(
                new Dimension(
                        0,
                        38
                )
        );

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        return field;
    }

    private void stylePrimaryButton(
            JButton button) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );
    }

    private void styleSecondaryButton(
            JButton button) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(TEXT_COLOR);
        button.setBackground(
                new Color(243, 244, 246)
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                LoginFrame::new
        );
    }
}