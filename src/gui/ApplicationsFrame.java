package gui;

import dao.ApplicationDAO;
import model.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationsFrame extends JFrame {

    private int userId;

    private JTable applicationsTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;

    public ApplicationsFrame(int userId) {

        this.userId = userId;

        setTitle("Smart Internship Tracker - My Applications");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // Title
        JLabel titleLabel =
                new JLabel("My Internship Applications");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // Search panel
        JPanel searchPanel =
                new JPanel(new FlowLayout());

        searchField =
                new JTextField(20);

        JButton searchButton =
                new JButton("Search");

        JButton showAllButton =
                new JButton("Show All");

        searchPanel.add(
                new JLabel("Company:")
        );

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // Table
        String[] columns = {
                "ID",
                "Company",
                "Role",
                "Application Date",
                "Deadline",
                "Status"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        applicationsTable =
                new JTable(tableModel);

        applicationsTable.setRowHeight(30);

        JScrollPane scrollPane =
                new JScrollPane(applicationsTable);

        // Center panel
        JPanel centerPanel =
                new JPanel(new BorderLayout());

        centerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // Bottom buttons
        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton refreshButton =
                new JButton("Refresh");

        JButton updateButton =
                new JButton("Update");

        JButton deleteButton =
                new JButton("Delete");

        JButton backButton =
                new JButton("Back");

        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // Search
        searchButton.addActionListener(
                e -> searchApplications()
        );

        // Show all
        showAllButton.addActionListener(
                e -> loadApplications()
        );

        // Refresh
        refreshButton.addActionListener(
                e -> loadApplications()
        );

        // Update
        updateButton.addActionListener(
                e -> updateSelectedApplication()
        );

        // Delete
        deleteButton.addActionListener(
                e -> deleteSelectedApplication()
        );

        // Back
        backButton.addActionListener(
                e -> dispose()
        );

        add(mainPanel);

        loadApplications();

        setVisible(true);
    }

    // Load all applications
    private void loadApplications() {

        tableModel.setRowCount(0);

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        List<Application> applications =
                applicationDAO
                        .getApplicationsByUser(userId);

        addApplicationsToTable(applications);
    }

    // Search applications by company
    private void searchApplications() {

        String company =
                searchField.getText().trim();

        if (company.isEmpty()) {

            loadApplications();

            return;
        }

        tableModel.setRowCount(0);

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        List<Application> applications =
                applicationDAO.searchByCompany(
                        company,
                        userId
                );

        addApplicationsToTable(applications);
    }

    // Add applications to table
    private void addApplicationsToTable(
            List<Application> applications) {

        for (Application application : applications) {

            Object[] row = {
                    application.getApplicationId(),
                    application.getCompanyName(),
                    application.getJobRole(),
                    application.getApplicationDate(),
                    application.getDeadline(),
                    application.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    // Update selected application
    private void updateSelectedApplication() {

    int selectedRow =
            applicationsTable.getSelectedRow();

    if (selectedRow == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Please select an application first!"
        );

        return;
    }

    int applicationId =
            (int) tableModel.getValueAt(
                    selectedRow,
                    0
            );

    String currentCompany =
            tableModel.getValueAt(
                    selectedRow,
                    1
            ).toString();

    String currentRole =
            tableModel.getValueAt(
                    selectedRow,
                    2
            ).toString();

    String currentStatus =
            tableModel.getValueAt(
                    selectedRow,
                    5
            ).toString();

    String company =
            JOptionPane.showInputDialog(
                    this,
                    "Company Name:",
                    currentCompany
            );

    if (company == null || company.trim().isEmpty()) {
        return;
    }

    String role =
            JOptionPane.showInputDialog(
                    this,
                    "Job Role:",
                    currentRole
            );

    if (role == null || role.trim().isEmpty()) {
        return;
    }

    String status =
            JOptionPane.showInputDialog(
                    this,
                    "Status (Applied / Shortlisted / Interview / Rejected):",
                    currentStatus
            );

    if (status == null || status.trim().isEmpty()) {
        return;
    }

    String jobLink =
            JOptionPane.showInputDialog(
                    this,
                    "Job Link:",
                    ""
            );

    if (jobLink == null) {
        return;
    }

    String notes =
            JOptionPane.showInputDialog(
                    this,
                    "Notes:",
                    ""
            );

    if (notes == null) {
        return;
    }

    ApplicationDAO applicationDAO =
            new ApplicationDAO();

    boolean success =
            applicationDAO.updateApplication(
                    applicationId,
                    userId,
                    company.trim(),
                    role.trim(),
                    status.trim(),
                    jobLink.trim(),
                    notes.trim()
            );

    if (success) {

        JOptionPane.showMessageDialog(
                this,
                "Application updated successfully!"
        );

        loadApplications();

    } else {

        JOptionPane.showMessageDialog(
                this,
                "Failed to update application!",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

   

          
    // Delete selected application
   // Delete selected application
private void deleteSelectedApplication() {

    int selectedRow =
            applicationsTable.getSelectedRow();

    // Step 1: Check if user selected a row
    if (selectedRow == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Please select an application first!"
        );

        return;
    }

    // Step 2: Get application ID from selected row
    int applicationId =
            (int) tableModel.getValueAt(
                    selectedRow,
                    0
            );

    // Step 3: Get company name for confirmation message
    String companyName =
            tableModel.getValueAt(
                    selectedRow,
                    1
            ).toString();

    // Step 4: Ask user for confirmation
    int choice =
            JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete "
                            + companyName
                            + " application?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

    // Step 5: If user chooses NO
    if (choice != JOptionPane.YES_OPTION) {
        return;
    }

    // Step 6: Delete from database
    ApplicationDAO applicationDAO =
            new ApplicationDAO();

    boolean success =
            applicationDAO.deleteApplication(
                    applicationId,
                    userId
            );

    // Step 7: Show result
    if (success) {

        JOptionPane.showMessageDialog(
                this,
                "Application deleted successfully!"
        );

        // Refresh table
        loadApplications();

    } else {

        JOptionPane.showMessageDialog(
                this,
                "Failed to delete application!",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
}