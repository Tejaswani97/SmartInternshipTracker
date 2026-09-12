package gui;

import dao.ApplicationDAO;
import model.Application;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class AddApplicationFrame extends JFrame {

    private final User user;

    private JTextField companyField;
    private JTextField roleField;
    private JTextField applicationDateField;
    private JTextField deadlineField;
    private JTextField linkField;

    private JComboBox<String> statusBox;

    private JTextArea notesArea;

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


    public AddApplicationFrame(User user) {

        this.user = user;

        setTitle(
                "Smart Internship Tracker - Add Application"
        );

        setSize(700, 650);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);


        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND_COLOR
        );


        // Header

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        15,
                        30
                )
        );


        JLabel title =
                new JLabel(
                        "Add Internship Application"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(
                TEXT_COLOR
        );


        JLabel subtitle =
                new JLabel(
                        "Add a new opportunity to your tracker."
                );

        subtitle.setForeground(
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

        headerText.add(title);

        headerText.add(
                Box.createVerticalStrut(5)
        );

        headerText.add(subtitle);


        header.add(
                headerText,
                BorderLayout.WEST
        );


        root.add(
                header,
                BorderLayout.NORTH
        );


        // Form

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


        // Company

        gbc.gridx = 0;
        gbc.gridy = 0;

        card.add(
                createLabel("Company"),
                gbc
        );

        companyField =
                createTextField();

        gbc.gridx = 1;

        card.add(
                companyField,
                gbc
        );


        // Role

        gbc.gridx = 0;
        gbc.gridy = 1;

        card.add(
                createLabel("Job Role"),
                gbc
        );

        roleField =
                createTextField();

        gbc.gridx = 1;

        card.add(
                roleField,
                gbc
        );


        // Application Date

        gbc.gridx = 0;
        gbc.gridy = 2;

        card.add(
                createLabel(
                        "Application Date"
                ),
                gbc
        );

        applicationDateField =
                createTextField();

        applicationDateField.setToolTipText(
                "YYYY-MM-DD"
        );

        gbc.gridx = 1;

        card.add(
                applicationDateField,
                gbc
        );


        // Deadline

        gbc.gridx = 0;
        gbc.gridy = 3;

        card.add(
                createLabel("Deadline"),
                gbc
        );

        deadlineField =
                createTextField();

        deadlineField.setToolTipText(
                "YYYY-MM-DD"
        );

        gbc.gridx = 1;

        card.add(
                deadlineField,
                gbc
        );


        // Status

        gbc.gridx = 0;
        gbc.gridy = 4;

        card.add(
                createLabel("Status"),
                gbc
        );

        statusBox =
                new JComboBox<>(
                        new String[]{
                                "Applied",
                                "Shortlisted",
                                "Interview",
                                "Rejected"
                        }
                );

        statusBox.setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );

        gbc.gridx = 1;

        card.add(
                statusBox,
                gbc
        );


        // Job Link

        gbc.gridx = 0;
        gbc.gridy = 5;

        card.add(
                createLabel("Job Link"),
                gbc
        );

        linkField =
                createTextField();

        gbc.gridx = 1;

        card.add(
                linkField,
                gbc
        );


        // Notes

        gbc.gridx = 0;
        gbc.gridy = 6;

        card.add(
                createLabel("Notes"),
                gbc
        );


        notesArea =
                new JTextArea(
                        5,
                        20
                );

        notesArea.setLineWrap(true);

        notesArea.setWrapStyleWord(true);

        JScrollPane notesScroll =
                new JScrollPane(
                        notesArea
                );

        gbc.gridx = 1;

        gbc.fill =
                GridBagConstraints.BOTH;

        card.add(
                notesScroll,
                gbc
        );


        // Buttons

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttons.setOpaque(false);


        JButton cancel =
                new JButton(
                        "Cancel"
                );

        JButton save =
                new JButton(
                        "Save Application"
                );


        styleSecondaryButton(
                cancel
        );

        stylePrimaryButton(
                save
        );


        buttons.add(cancel);

        buttons.add(save);


        gbc.gridx = 0;
        gbc.gridy = 7;

        gbc.gridwidth = 2;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        card.add(
                buttons,
                gbc
        );


        root.add(
                card,
                BorderLayout.CENTER
        );


        // Actions

        cancel.addActionListener(
                e -> {

                    dispose();

                    new DashboardFrame(
                            user
                    );
                }
        );


        save.addActionListener(
                e -> saveApplication()
        );


        add(root);

        setVisible(true);
    }


    private void saveApplication() {

        String company =
                companyField
                        .getText()
                        .trim();

        String role =
                roleField
                        .getText()
                        .trim();

        String applicationDateText =
                applicationDateField
                        .getText()
                        .trim();

        String deadlineText =
                deadlineField
                        .getText()
                        .trim();

        String status =
                statusBox
                        .getSelectedItem()
                        .toString();

        String link =
                linkField
                        .getText()
                        .trim();

        String notes =
                notesArea
                        .getText()
                        .trim();


        if (
                company.isEmpty()
                        || role.isEmpty()
                        || applicationDateText.isEmpty()
                        || deadlineText.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
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


            if (
                    deadline.isBefore(
                            applicationDate
                    )
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Deadline cannot be before application date.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            Application application =
                    new Application(
                            user.getUserId(),
                            company,
                            role,
                            applicationDate,
                            deadline,
                            status,
                            link,
                            notes
                    );


            ApplicationDAO dao =
                    new ApplicationDAO();


            boolean success =
                    dao.addApplication(
                            application
                    );


            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Application added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                new DashboardFrame(
                        user
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add application.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (
                java.time.format.DateTimeParseException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter dates in YYYY-MM-DD format.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    private JLabel createLabel(
            String text
    ) {

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


    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );

        return field;
    }


    private void stylePrimaryButton(
            JButton button
    ) {

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
    }


    private void styleSecondaryButton(
            JButton button
    ) {

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
    }
}