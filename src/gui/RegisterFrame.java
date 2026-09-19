package gui;

import dao.EmailVerificationDAO;
import dao.UserDAO;
import model.User;
import util.EmailService;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterFrame() {

        setTitle("Smart Internship Tracker - Register");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel("Create Your Account");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(
                titleLabel,
                gbc
        );

        gbc.gridwidth = 1;


        // =====================================================
        // NAME
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(
                new JLabel("Name:"),
                gbc
        );

        nameField =
                new JTextField(20);

        gbc.gridx = 1;

        panel.add(
                nameField,
                gbc
        );


        // =====================================================
        // EMAIL
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(
                new JLabel("Email:"),
                gbc
        );

        emailField =
                new JTextField(20);

        gbc.gridx = 1;

        panel.add(
                emailField,
                gbc
        );


        // =====================================================
        // PASSWORD
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 3;

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


        // =====================================================
        // REGISTER BUTTON
        // =====================================================

        JButton registerButton =
                new JButton("Register");

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        panel.add(
                registerButton,
                gbc
        );


        registerButton.addActionListener(
                e -> registerUser()
        );


        add(panel);

        setVisible(true);
    }


    // =========================================================
    // REGISTER USER
    // =========================================================

    private void registerUser() {

        String name =
                nameField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );


        // =====================================================
        // BASIC VALIDATION
        // =====================================================

        if (
                name.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
        ) {

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


        // =====================================================
        // GENERATE OTP
        // =====================================================

        String otp =
                generateOtp();


        // =====================================================
        // SAVE OTP
        // =====================================================

        EmailVerificationDAO verificationDAO =
                new EmailVerificationDAO();

        boolean saved =
                verificationDAO.saveVerification(
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


        // =====================================================
        // SEND OTP EMAIL
        // =====================================================

        boolean emailSent =
                EmailService.sendOtp(
                        email,
                        otp
                );


        if (!emailSent) {

            verificationDAO.deleteVerification(
                    email
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Could not send OTP email.\n"
                            + "Please try again.",
                    "Email Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =====================================================
        // ASK USER FOR OTP
        // =====================================================

        String enteredOtp =
                JOptionPane.showInputDialog(
                        this,
                        "A 6-digit OTP has been sent to:\n"
                                + email
                                + "\n\n"
                                + "Enter the OTP:",
                        "Verify Email",
                        JOptionPane.PLAIN_MESSAGE
                );


        // User closed the dialog
        if (enteredOtp == null) {

            return;
        }


        enteredOtp =
                enteredOtp.trim();


        // =====================================================
        // VERIFY OTP
        // =====================================================

        boolean verified =
                verificationDAO.verifyOtp(
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


        // =====================================================
        // CREATE ACTUAL USER
        // =====================================================

        User user =
                new User(
                        name,
                        email,
                        password
                );

        UserDAO userDAO =
                new UserDAO();

        boolean success =
                userDAO.registerUser(
                        user
                );


        if (success) {

            verificationDAO.deleteVerification(
                    email
            );

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


    // =========================================================
    // GENERATE 6-DIGIT OTP
    // =========================================================

    private String generateOtp() {

        SecureRandom random =
                new SecureRandom();

        int number =
                100000
                + random.nextInt(900000);

        return String.valueOf(number);
    }
}