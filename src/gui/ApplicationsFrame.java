package gui;

import dao.ApplicationDAO;
import model.Application;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ApplicationsFrame extends JFrame {

    private final User user;

    private final Color SIDEBAR = new Color(31, 41, 55);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BORDER = new Color(229, 231, 235);

    private JTextField searchField;
    private JComboBox<String> statusCombo;
    private JTable table;
    private DefaultTableModel tableModel;

    public ApplicationsFrame(User user) {

        this.user = user;

        setTitle("Smart Internship Tracker - My Applications");
        setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();
        loadApplications();
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

        top.add(createSidebarButton("My Applications", true, e -> {
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("Add Application", false, e -> {
            dispose();
            new AddApplicationFrame(user).setVisible(true);
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
        main.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);

        JLabel title = new JLabel("My Applications");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Track and manage your internship applications");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel titleBox = new JPanel();
        titleBox.setBackground(BACKGROUND);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        header.add(titleBox, BorderLayout.WEST);

        main.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(BACKGROUND);

        center.add(createToolbar(), BorderLayout.NORTH);
        center.add(createTableCard(), BorderLayout.CENTER);
        center.add(createBottomButtons(), BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel createToolbar() {

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setBackground(BACKGROUND);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(230, 38));

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        statusCombo = new JComboBox<>(
                new String[]{
                        "All",
                        "Applied",
                        "Shortlisted",
                        "Interview",
                        "Rejected"
                }
        );

        statusCombo.setPreferredSize(new Dimension(140, 38));

        JButton filterButton = new JButton("Filter");

        searchButton.addActionListener(e -> searchApplications());
        showAllButton.addActionListener(e -> loadApplications());
        filterButton.addActionListener(e -> filterApplications());

        toolbar.add(searchField);
        toolbar.add(searchButton);
        toolbar.add(showAllButton);
        toolbar.add(statusCombo);
        toolbar.add(filterButton);

        return toolbar;
    }

    private JPanel createTableCard() {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 10, 10, 10)
        ));

        tableModel = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Company",
                        "Job Role",
                        "Application Date",
                        "Deadline",
                        "Status"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        table.getTableHeader().setBackground(
                new Color(249, 250, 251)
        );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(table);

        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBottomButtons() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(BACKGROUND);

        JButton refreshButton = new JButton("Refresh");
        JButton exportButton = new JButton("Export CSV");
        JButton detailsButton = new JButton("View Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        refreshButton.addActionListener(e -> loadApplications());
        exportButton.addActionListener(e -> exportApplicationsToCsv());
        detailsButton.addActionListener(e -> viewDetails());
        updateButton.addActionListener(e -> updateApplication());
        deleteButton.addActionListener(e -> deleteApplication());

        panel.add(refreshButton);
        panel.add(exportButton);
        panel.add(detailsButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        return panel;
    }

    private void loadApplications() {

        ApplicationDAO dao = new ApplicationDAO();

        List<Application> applications =
                dao.getApplicationsByUser(user.getUserId());

        populateTable(applications);
    }

    private void searchApplications() {

        String company = searchField.getText().trim();

        if (company.isEmpty()) {
            loadApplications();
            return;
        }

        ApplicationDAO dao = new ApplicationDAO();

        List<Application> applications =
                dao.searchByCompany(company, user.getUserId());

        populateTable(applications);
    }

    private void filterApplications() {

        String status = (String) statusCombo.getSelectedItem();

        if ("All".equals(status)) {
            loadApplications();
            return;
        }

        ApplicationDAO dao = new ApplicationDAO();

        List<Application> applications =
                dao.filterByStatus(status, user.getUserId());

        populateTable(applications);
    }

    private void populateTable(List<Application> applications) {

        tableModel.setRowCount(0);

        for (Application app : applications) {

            tableModel.addRow(new Object[]{
                    app.getApplicationId(),
                    app.getCompanyName(),
                    app.getJobRole(),
                    app.getApplicationDate(),
                    app.getDeadline(),
                    app.getStatus()
            });
        }
    }

    private int getSelectedApplicationId() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return -1;
        }

        return (int) tableModel.getValueAt(row, 0);
    }

    private void viewDetails() {

        int applicationId = getSelectedApplicationId();

        if (applicationId == -1) {
            return;
        }

        ApplicationDAO dao = new ApplicationDAO();

        Application app = dao.getApplicationById(
                applicationId,
                user.getUserId()
        );

        if (app == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JTextArea area = new JTextArea();

        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        area.setText(
                "Company: " + app.getCompanyName() + "\n\n" +
                "Job Role: " + app.getJobRole() + "\n\n" +
                "Application Date: " + app.getApplicationDate() + "\n\n" +
                "Deadline: " + app.getDeadline() + "\n\n" +
                "Status: " + app.getStatus() + "\n\n" +
                "Job Link: " + app.getJobLink() + "\n\n" +
                "Notes: " + app.getNotes()
        );

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Application Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateApplication() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int applicationId =
                (int) tableModel.getValueAt(row, 0);

        String company =
                tableModel.getValueAt(row, 1).toString();

        String role =
                tableModel.getValueAt(row, 2).toString();

        String status =
                tableModel.getValueAt(row, 5).toString();

        dispose();

        new UpdateApplicationFrame(
                user.getUserId(),
                applicationId,
                company,
                role,
                status,
                "",
                ""
        ).setVisible(true);
    }

    private void exportApplicationsToCsv() {

        ApplicationDAO dao = new ApplicationDAO();

        List<Application> applications =
                dao.getApplicationsByUser(user.getUserId());

        if (applications.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "There are no applications to export.",
                    "Nothing to Export",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Applications CSV");
        fileChooser.setSelectedFile(
                new File("my_internship_applications.csv")
        );

        int choice = fileChooser.showSaveDialog(this);

        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(
                    "application_id,company_name,job_role,application_date,deadline,status,job_link,notes"
            );
            writer.newLine();

            for (Application app : applications) {

                writer.write(
                        csv(app.getApplicationId()) + "," +
                        csv(app.getCompanyName()) + "," +
                        csv(app.getJobRole()) + "," +
                        csv(app.getApplicationDate()) + "," +
                        csv(app.getDeadline()) + "," +
                        csv(app.getStatus()) + "," +
                        csv(app.getJobLink()) + "," +
                        csv(app.getNotes())
                );
                writer.newLine();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Applications exported successfully.\n\nSaved to:\n" + file.getAbsolutePath(),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to export applications.\n\n" + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String csv(Object value) {

        if (value == null) {
            return "";
        }

        String text = String.valueOf(value);

        text = text.replace("\"", "\"\"");

        return "\"" + text + "\"";
    }

    private void deleteApplication() {

        int applicationId = getSelectedApplicationId();

        if (applicationId == -1) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this application?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        ApplicationDAO dao = new ApplicationDAO();

        boolean success = dao.deleteApplication(
                applicationId,
                user.getUserId()
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application deleted successfully."
            );

            loadApplications();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete application.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showUpcomingDeadlines() {

        ApplicationDAO dao = new ApplicationDAO();

        List<Application> applications =
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
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));

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