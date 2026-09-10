package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterFrame() {

        setTitle("Smart Internship Tracker - Register");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel =
                new JLabel("Create Your Account");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(new JLabel("Name:"), gbc);

        nameField = new JTextField(20);

        gbc.gridx = 1;

        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);

        gbc.gridx = 1;

        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);

        gbc.gridx = 1;

        panel.add(passwordField, gbc);

        // Register button
        JButton registerButton =
                new JButton("Register");

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        panel.add(registerButton, gbc);

        registerButton.addActionListener(
                e -> registerUser()
        );

        add(panel);

        setVisible(true);
    }

    private void registerUser() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (name.isEmpty()
                || email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        User user =
                new User(
                        name,
                        email,
                        password
                );

        UserDAO userDAO =
                new UserDAO();

        boolean success =
                userDAO.registerUser(user);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful!\n"
                            + "You can now login."
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
