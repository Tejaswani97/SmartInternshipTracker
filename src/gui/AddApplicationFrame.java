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

    private final Color SIDEBAR = new Color(31, 41, 55);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BORDER = new Color(229, 231, 235);

    private JTextField companyField;
    private JTextField roleField;
    private JTextField applicationDateField;
    private JTextField deadlineField;
    private JTextField jobLinkField;
    private JTextArea notesArea;
    private JComboBox<String> statusCombo;

    public AddApplicationFrame(User user) {

        this.user = user;

        setTitle("Smart Internship Tracker - Add Application");
        setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();
    }

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMainContent(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 750));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel logo = new JLabel("SmartIntern");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 21));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tracker = new JLabel("Internship Tracker");
        tracker.setForeground(new Color(156, 163, 175));
        tracker.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tracker.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(logo);
        top.add(Box.createVerticalStrut(2));
        top.add(tracker);
        top.add(Box.createVerticalStrut(30));

        top.add(createSidebarButton("Dashboard", false, e -> {
            dispose();
            new DashboardFrame(user).setVisible(true);
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("My Profile", false, e -> {
            new ProfileFrame(user).setVisible(true);
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("My Applications", false, e -> {
            dispose();
            new ApplicationsFrame(user).setVisible(true);
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("Add Application", true, e -> {
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("Upcoming Deadlines", false, e -> {
            showUpcomingDeadlines();
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("Analytics", false, e -> {
            dispose();
            new AnalyticsFrame(user).setVisible(true);
        }));

        sidebar.add(top, BorderLayout.NORTH);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new EmptyBorder(15, 20, 25, 20));

        bottom.add(createSidebarButton("Logout", false, e -> {
            dispose();
            new LoginFrame().setVisible(true);
        }));

        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createSidebarButton(
            String text,
            boolean selected,
            java.awt.event.ActionListener action) {

        JButton button = new JButton(text);

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setPreferredSize(new Dimension(180, 42));

        button.setForeground(Color.WHITE);
        button.setBackground(selected
                ? new Color(55, 65, 81)
                : SIDEBAR);

        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 10));
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.addActionListener(action);

        return button;
    }

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(30, 35, 30, 35));

        JLabel title = new JLabel("Add New Application");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Enter the details of your internship application"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        main.add(header, BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(CARD);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        companyField = new JTextField();
        roleField = new JTextField();
        applicationDateField = new JTextField();
        deadlineField = new JTextField();
        jobLinkField = new JTextField();

        statusCombo = new JComboBox<>(
                new String[]{
                        "Applied",
                        "Shortlisted",
                        "Interview",
                        "Rejected"
                }
        );

        notesArea = new JTextArea(5, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        addFormRow(
                formCard,
                gbc,
                0,
                "Company Name",
                companyField
        );

        addFormRow(
                formCard,
                gbc,
                1,
                "Job Role",
                roleField
        );

        addFormRow(
                formCard,
                gbc,
                2,
                "Application Date",
                applicationDateField
        );

        addFormRow(
                formCard,
                gbc,
                3,
                "Deadline",
                deadlineField
        );

        addFormRow(
                formCard,
                gbc,
                4,
                "Status",
                statusCombo
        );

        addFormRow(
                formCard,
                gbc,
                5,
                "Job Link",
                jobLinkField
        );

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.weighty = 0;
        formCard.add(
                createLabel("Notes"),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JScrollPane notesScroll = new JScrollPane(notesArea);
        formCard.add(notesScroll, gbc);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        buttonPanel.setBackground(BACKGROUND);

        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Application");

        saveButton.setBackground(BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        cancelButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(user).setVisible(true);
        });

        saveButton.addActionListener(e -> saveApplication());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(BACKGROUND);
        center.add(formCard, BorderLayout.CENTER);
        center.add(buttonPanel, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent component) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(createLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        component.setPreferredSize(new Dimension(400, 38));

        panel.add(component, gbc);
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT);

        return label;
    }

    private void saveApplication() {

        String company = companyField.getText().trim();
        String role = roleField.getText().trim();
        String applicationDateText =
                applicationDateField.getText().trim();
        String deadlineText =
                deadlineField.getText().trim();
        String status =
                (String) statusCombo.getSelectedItem();
        String jobLink = jobLinkField.getText().trim();
        String notes = notesArea.getText().trim();

        if (company.isEmpty()
                || role.isEmpty()
                || applicationDateText.isEmpty()
                || deadlineText.isEmpty()) {

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
                    LocalDate.parse(applicationDateText);

            LocalDate deadline =
                    LocalDate.parse(deadlineText);

            if (deadline.isBefore(applicationDate)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Deadline cannot be before the application date.",
                        "Invalid Dates",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Application application = new Application(
                    user.getUserId(),
                    company,
                    role,
                    applicationDate,
                    deadline,
                    status,
                    jobLink,
                    notes
            );

            ApplicationDAO dao = new ApplicationDAO();

            boolean success = dao.addApplication(application);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Application added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
                new DashboardFrame(user).setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add application.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter dates in YYYY-MM-DD format.",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showUpcomingDeadlines() {

        ApplicationDAO dao = new ApplicationDAO();

        var applications =
                dao.getUpcomingDeadlines(user.getUserId());

        if (applications.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No upcoming deadlines."
            );

            return;
        }

        StringBuilder message = new StringBuilder();

        for (Application app : applications) {

            message.append(app.getCompanyName())
                    .append(" - ")
                    .append(app.getJobRole())
                    .append("\nDeadline: ")
                    .append(app.getDeadline())
                    .append("\n\n");
        }

        JTextArea area = new JTextArea(message.toString());
        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Upcoming Deadlines",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}