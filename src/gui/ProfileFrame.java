package gui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfileFrame extends JFrame {

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

    public ProfileFrame(User user) {

        setTitle("Smart Internship Tracker - My Profile");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel rootPanel =
                new JPanel(new BorderLayout());

        rootPanel.setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setOpaque(false);

        headerPanel.setBorder(
                new EmptyBorder(25, 30, 15, 30)
        );

        JLabel titleLabel =
                new JLabel("My Profile");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel =
                new JLabel("Your Smart Internship Tracker account");

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        subtitleLabel.setForeground(MUTED_TEXT_COLOR);

        JPanel headerText =
                new JPanel();

        headerText.setOpaque(false);

        headerText.setLayout(
                new BoxLayout(
                        headerText,
                        BoxLayout.Y_AXIS
                )
        );

        headerText.add(titleLabel);

        headerText.add(
                Box.createVerticalStrut(5)
        );

        headerText.add(subtitleLabel);

        headerPanel.add(
                headerText,
                BorderLayout.WEST
        );

        rootPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // Profile card
        JPanel card =
                new JPanel(new GridBagLayout());

        card.setBackground(CARD_COLOR);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        new EmptyBorder(
                                30,
                                40,
                                30,
                                40
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(12, 10, 12, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Avatar
        JLabel avatarLabel =
                new JLabel(
                        getInitials(user.getName()),
                        SwingConstants.CENTER
                );

        avatarLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setBackground(ACCENT_COLOR);
        avatarLabel.setOpaque(true);

        avatarLabel.setPreferredSize(
                new Dimension(80, 80)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        card.add(avatarLabel, gbc);

        // Name
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        card.add(
                createLabel("Name"),
                gbc
        );

        gbc.gridx = 1;

        card.add(
                createValueLabel(user.getName()),
                gbc
        );

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;

        card.add(
                createLabel("Email"),
                gbc
        );

        gbc.gridx = 1;

        card.add(
                createValueLabel(user.getEmail()),
                gbc
        );

        // User ID
        gbc.gridx = 0;
        gbc.gridy = 3;

        card.add(
                createLabel("User ID"),
                gbc
        );

        gbc.gridx = 1;

        card.add(
                createValueLabel(
                        String.valueOf(
                                user.getUserId()
                        )
                ),
                gbc
        );

        // Close
        JButton closeButton =
                new JButton("Close");

        closeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        closeButton.setForeground(TEXT_COLOR);

        closeButton.setBackground(
                new Color(243, 244, 246)
        );

        closeButton.setFocusPainted(false);

        closeButton.addActionListener(
                e -> dispose()
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        gbc.insets =
                new Insets(
                        25,
                        10,
                        5,
                        10
                );

        card.add(closeButton, gbc);

        rootPanel.add(
                card,
                BorderLayout.CENTER
        );

        add(rootPanel);

        setVisible(true);
    }

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                MUTED_TEXT_COLOR
        );

        return label;
    }

    private JLabel createValueLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        label.setForeground(
                TEXT_COLOR
        );

        return label;
    }

    private String getInitials(String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "?";
        }

        String[] parts =
                name.trim().split("\\s+");

        if (parts.length == 1) {

            return parts[0]
                    .substring(0, 1)
                    .toUpperCase();
        }

        return (
                parts[0].substring(0, 1)
                        +
                parts[parts.length - 1]
                        .substring(0, 1)
        ).toUpperCase();
    }
}