package gui;

import dao.ApplicationDAO;
import model.Application;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddApplicationFrame extends JFrame {

    private int userId;

    private JTextField companyField;
    private JTextField roleField;
    private JTextField applicationDateField;
    private JTextField deadlineField;
    private JComboBox<String> statusBox;
    private JTextField linkField;
    private JTextArea notesArea;

    public AddApplicationFrame(int userId) {

        this.userId = userId;

        setTitle("Smart Internship Tracker - Add Application");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel =
                new JLabel("Add Internship Application");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Company
        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(new JLabel("Company:"), gbc);

        companyField = new JTextField();

        gbc.gridx = 1;

        panel.add(companyField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(new JLabel("Job Role:"), gbc);

        roleField = new JTextField();

        gbc.gridx = 1;

        panel.add(roleField, gbc);

        // Application Date
        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(
                new JLabel("Application Date:"),
                gbc
        );

        applicationDateField = new JTextField();

        applicationDateField.setToolTipText(
                "Format: YYYY-MM-DD"
        );

        gbc.gridx = 1;

        panel.add(
                applicationDateField,
                gbc
        );

        // Deadline
        gbc.gridx = 0;
        gbc.gridy = 4;

        panel.add(new JLabel("Deadline:"), gbc);

        deadlineField = new JTextField();

        deadlineField.setToolTipText(
                "Format: YYYY-MM-DD"
        );

        gbc.gridx = 1;

        panel.add(deadlineField, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;

        panel.add(new JLabel("Status:"), gbc);

        String[] statuses = {
                "Applied",
                "Shortlisted",
                "Interview",
                "Rejected"
        };

        statusBox =
                new JComboBox<>(statuses);

        gbc.gridx = 1;

        panel.add(statusBox, gbc);

        // Job Link
        gbc.gridx = 0;
        gbc.gridy = 6;

        panel.add(new JLabel("Job Link:"), gbc);

        linkField = new JTextField();

        gbc.gridx = 1;

        panel.add(linkField, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 7;

        panel.add(new JLabel("Notes:"), gbc);

        notesArea = new JTextArea(4, 20);

        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JScrollPane notesScrollPane =
                new JScrollPane(notesArea);

        gbc.gridx = 1;

        panel.add(
                notesScrollPane,
                gbc
        );

        // Save button
        JButton saveButton =
                new JButton("Save Application");

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;

        panel.add(saveButton, gbc);

        saveButton.addActionListener(
                e -> saveApplication()
        );

        add(panel);

        setVisible(true);
    }

    private void saveApplication() {

        String company =
                companyField.getText().trim();

        String role =
                roleField.getText().trim();

        String applicationDateText =
                applicationDateField.getText().trim();

        String deadlineText =
                deadlineField.getText().trim();

        String status =
                (String) statusBox.getSelectedItem();

        String link =
                linkField.getText().trim();

        String notes =
                notesArea.getText().trim();

        // Basic validation
        if (company.isEmpty()
                || role.isEmpty()
                || applicationDateText.isEmpty()
                || deadlineText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            LocalDate applicationDate =
                    LocalDate.parse(
                            applicationDateText
                    );

            LocalDate deadline =
                    LocalDate.parse(
                            deadlineText
                    );

            Application application =
                    new Application(
                            userId,
                            company,
                            role,
                            applicationDate,
                            deadline,
                            status,
                            link,
                            notes
                    );

            ApplicationDAO applicationDAO =
                    new ApplicationDAO();

            boolean success =
                    applicationDAO.addApplication(
                            application
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Application added successfully!"
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add application!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter dates in YYYY-MM-DD format.\n"
                            + "Example: 2026-09-25",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}