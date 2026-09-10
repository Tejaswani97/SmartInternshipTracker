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

    public LoginFrame() {

        setTitle("Smart Internship Tracker");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel =
                new JLabel("Smart Internship Tracker");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(
                new JLabel("Email:"),
                gbc
        );

        emailField = new JTextField(20);

        gbc.gridx = 1;

        panel.add(
                emailField,
                gbc
        );

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(
                new JLabel("Password:"),
                gbc
        );

        passwordField =
                new JPasswordField(20);

        gbc.gridx = 1;

        panel.add(
                passwordField,
                gbc
        );

        // Login button
        loginButton =
                new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        panel.add(
                loginButton,
                gbc
        );

        // Register button
        registerButton =
                new JButton("Create New Account");

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        panel.add(
                registerButton,
                gbc
        );

        add(panel);

        // Login button action
        loginButton.addActionListener(
                e -> loginUser()
        );

        // Register button action
        registerButton.addActionListener(
                e -> new RegisterFrame()
        );

        setVisible(true);
    }

    private void loginUser() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password!",
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

            new DashboardFrame(
                    user.getUserId()
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new LoginFrame()
        );
    }
}