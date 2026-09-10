package gui;

import dao.ApplicationDAO;
import model.Application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UpdateApplicationFrame extends JFrame {

    private final int userId;
    private final int applicationId;

    private JTextField companyField;
    private JTextField roleField;
    private JTextField linkField;
    private JTextArea notesArea;

    private JComboBox<String> statusBox;

    // =========================
    // THEME
    // =========================

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

    public UpdateApplicationFrame(
            int userId,
            int applicationId,
            String company,
            String role,
            String status,
            String jobLink,
            String notes) {

        this.userId = userId;
        this.applicationId = applicationId;

        setTitle(
                "Smart Internship Tracker - Update Application"
        );

        setSize(700, 600);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // =========================
        // ROOT
        // =========================

        JPanel rootPanel =
                new JPanel(new BorderLayout());

        rootPanel.setBackground(
                BACKGROUND_COLOR
        );

        // =========================
        // HEADER
        // =========================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setOpaque(false);

        headerPanel.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        15,
                        30
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "Update Internship Application"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        titleLabel.setForeground(
                TEXT_COLOR
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Modify the details of your application."
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

        // =========================
        // FORM CARD
        // =========================

        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBackground(
                CARD_COLOR
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // =========================
        // COMPANY
        // =========================

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        card.add(
                createLabel("Company"),
                gbc
        );

        companyField =
                createTextField();

        companyField.setText(company);

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        card.add(
                companyField,
                gbc
        );

        // =========================
        // ROLE
        // =========================

        gbc.gridx = 0;
        gbc.gridy = 1;

        card.add(
                createLabel("Job Role"),
                gbc
        );

        roleField =
                createTextField();

        roleField.setText(role);

        gbc.gridx = 1;

        card.add(
                roleField,
                gbc
        );

        // =========================
        // STATUS
        // =========================

        gbc.gridx = 0;
        gbc.gridy = 2;

        card.add(
                createLabel("Status"),
                gbc
        );

        String[] statuses = {
                "Applied",
                "Shortlisted",
                "Interview",
                "Rejected"
        };

        statusBox =
                new JComboBox<>(
                        statuses
                );

        statusBox.setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );

        statusBox.setSelectedItem(status);

        gbc.gridx = 1;

        card.add(
                statusBox,
                gbc
        );

        // =========================
        // JOB LINK
        // =========================

        gbc.gridx = 0;
        gbc.gridy = 3;

        card.add(
                createLabel("Job Link"),
                gbc
        );

        linkField =
                createTextField();

        linkField.setText(jobLink);

        gbc.gridx = 1;

        card.add(
                linkField,
                gbc
        );

        // =========================
        // NOTES
        // =========================

        gbc.gridx = 0;
        gbc.gridy = 4;

        card.add(
                createLabel("Notes"),
                gbc
        );

        notesArea =
                new JTextArea(
                        5,
                        20
                );

        notesArea.setText(notes);

        notesArea.setLineWrap(true);

        notesArea.setWrapStyleWord(true);

        notesArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        notesArea.setForeground(
                TEXT_COLOR
        );

        notesArea.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );

        JScrollPane notesScrollPane =
                new JScrollPane(
                        notesArea
                );

        gbc.gridx = 1;

        gbc.fill =
                GridBagConstraints.BOTH;

        card.add(
                notesScrollPane,
                gbc
        );

        // =========================
        // BUTTONS
        // =========================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        buttonPanel.setOpaque(false);

        JButton cancelButton =
                createSecondaryButton(
                        "Cancel"
                );

        JButton updateButton =
                createUpdateButton(
                        "Save Changes"
                );

        buttonPanel.add(
                cancelButton
        );

        buttonPanel.add(
                updateButton
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        20,
                        8,
                        5,
                        8
                );

        card.add(
                buttonPanel,
                gbc
        );

        rootPanel.add(
                card,
                BorderLayout.CENTER
        );

        // =========================
        // ACTIONS
        // =========================

        updateButton.addActionListener(
                e -> updateApplication()
        );

        cancelButton.addActionListener(
                e -> dispose()
        );

        add(rootPanel);

        setVisible(true);
    }

    // =========================
    // UPDATE APPLICATION
    // =========================

    private void updateApplication() {

        String company =
                companyField.getText().trim();

        String role =
                roleField.getText().trim();

        String status =
                statusBox
                        .getSelectedItem()
                        .toString();

        String jobLink =
                linkField.getText().trim();

        String notes =
                notesArea.getText().trim();

        if (company.isEmpty()
                || role.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Company and Job Role are required.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        boolean success =
                applicationDAO.updateApplication(
                        applicationId,
                        userId,
                        company,
                        role,
                        status,
                        jobLink,
                        notes
                );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update application.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // LABEL
    // =========================

    private JLabel createLabel(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                TEXT_COLOR
        );

        return label;
    }

    // =========================
    // TEXT FIELD
    // =========================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(
                TEXT_COLOR
        );

        field.setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );

        return field;
    }

    // =========================
    // UPDATE BUTTON
    // =========================

    private JButton createUpdateButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                ACCENT_COLOR
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        9,
                        18,
                        9,
                        18
                )
        );

        return button;
    }

    // =========================
    // SECONDARY BUTTON
    // =========================

    private JButton createSecondaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                TEXT_COLOR
        );

        button.setBackground(
                new Color(
                        243,
                        244,
                        246
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        9,
                        18,
                        9,
                        18
                )
        );

        return button;
    }
}