package gui;

import dao.ApplicationDAO;
import model.Application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationsFrame extends JFrame {

    private final int userId;

    private JTable applicationsTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> statusBox;

    // =========================
    // THEME
    // =========================

    private static final Color SIDEBAR_COLOR =
            new Color(31, 41, 55);

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


    public ApplicationsFrame(int userId) {

        this.userId = userId;

        setTitle(
                "Smart Internship Tracker - My Applications"
        );

        setSize(1100, 700);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);


        // =========================
        // ROOT
        // =========================

        JPanel rootPanel =
                new JPanel(
                        new BorderLayout()
                );

        rootPanel.setBackground(
                BACKGROUND_COLOR
        );


        // =========================
        // SIDEBAR
        // =========================

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(220, 0)
        );

        sidebar.setBackground(
                SIDEBAR_COLOR
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.setBorder(
                new EmptyBorder(
                        25,
                        15,
                        25,
                        15
                )
        );


        JLabel logoLabel =
                new JLabel(
                        "SMART TRACKER"
                );

        logoLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        logoLabel.setForeground(
                Color.WHITE
        );

        logoLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sidebar.add(
                logoLabel
        );

        sidebar.add(
                Box.createVerticalStrut(40)
        );


        JButton dashboardButton =
                createSidebarButton(
                        "Dashboard"
                );

        JButton applicationsButton =
                createSidebarButton(
                        "My Applications"
                );

        JButton addButton =
                createSidebarButton(
                        "Add Application"
                );

        JButton deadlineButton =
                createSidebarButton(
                        "Upcoming Deadlines"
                );

        JButton logoutButton =
                createSidebarButton(
                        "Logout"
                );


        sidebar.add(
                dashboardButton
        );

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(
                applicationsButton
        );

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(
                addButton
        );

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(
                deadlineButton
        );

        sidebar.add(
                Box.createVerticalGlue()
        );

        sidebar.add(
                logoutButton
        );


        rootPanel.add(
                sidebar,
                BorderLayout.WEST
        );


        // =========================
        // CONTENT
        // =========================

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );

        contentPanel.setBackground(
                BACKGROUND_COLOR
        );

        contentPanel.setBorder(
                new EmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );


        // =========================
        // HEADER
        // =========================

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setOpaque(
                false
        );


        JLabel titleLabel =
                new JLabel(
                        "My Applications"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(
                TEXT_COLOR
        );


        JLabel subtitleLabel =
                new JLabel(
                        "Search, filter and manage your internship applications."
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

        headerText.setOpaque(
                false
        );

        headerText.setLayout(
                new BoxLayout(
                        headerText,
                        BoxLayout.Y_AXIS
                )
        );

        headerText.add(
                titleLabel
        );

        headerText.add(
                Box.createVerticalStrut(5)
        );

        headerText.add(
                subtitleLabel
        );


        headerPanel.add(
                headerText,
                BorderLayout.WEST
        );


        contentPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );


        // =========================
        // MAIN CARD
        // =========================

        JPanel applicationsCard =
                new JPanel(
                        new BorderLayout(
                                10,
                                15
                        )
                );

        applicationsCard.setBackground(
                CARD_COLOR
        );

        applicationsCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        // =========================
        // SEARCH / FILTER
        // =========================

        JPanel filterPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                5
                        )
                );

        filterPanel.setOpaque(
                false
        );


        JLabel companyLabel =
                new JLabel(
                        "Company"
                );

        companyLabel.setForeground(
                TEXT_COLOR
        );


        searchField =
                new JTextField(
                        16
                );


        JButton searchButton =
                createActionButton(
                        "Search"
                );


        JButton showAllButton =
                createSecondaryButton(
                        "Show All"
                );


        JLabel statusLabel =
                new JLabel(
                        "Status"
                );

        statusLabel.setForeground(
                TEXT_COLOR
        );


        String[] statuses = {
                "All",
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
                        130,
                        32
                )
        );


        JButton filterButton =
                createActionButton(
                        "Filter"
                );


        filterPanel.add(
                companyLabel
        );

        filterPanel.add(
                searchField
        );

        filterPanel.add(
                searchButton
        );

        filterPanel.add(
                showAllButton
        );

        filterPanel.add(
                Box.createHorizontalStrut(15)
        );

        filterPanel.add(
                statusLabel
        );

        filterPanel.add(
                statusBox
        );

        filterPanel.add(
                filterButton
        );


        applicationsCard.add(
                filterPanel,
                BorderLayout.NORTH
        );


        // =========================
        // TABLE
        // =========================

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
                            int column
                    ) {

                        return false;
                    }
                };


        applicationsTable =
                new JTable(
                        tableModel
                );


        applicationsTable.setRowHeight(
                36
        );

        applicationsTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        applicationsTable.setForeground(
                TEXT_COLOR
        );

        applicationsTable.setBackground(
                Color.WHITE
        );

        applicationsTable.setGridColor(
                BORDER_COLOR
        );

        applicationsTable.setSelectionBackground(
                new Color(
                        219,
                        234,
                        254
                )
        );

        applicationsTable.setSelectionForeground(
                TEXT_COLOR
        );

        applicationsTable.setShowVerticalLines(
                false
        );


        // Table header

        applicationsTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        applicationsTable
                .getTableHeader()
                .setBackground(
                        new Color(
                                243,
                                244,
                                246
                        )
                );

        applicationsTable
                .getTableHeader()
                .setForeground(
                        TEXT_COLOR
                );

        applicationsTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                38
                        )
                );


        // Center align ID

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        applicationsTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        centerRenderer
                );


        JScrollPane scrollPane =
                new JScrollPane(
                        applicationsTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );


        applicationsCard.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =========================
        // BOTTOM BUTTONS
        // =========================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        buttonPanel.setOpaque(
                false
        );


        JButton refreshButton =
                createSecondaryButton(
                        "Refresh"
                );


        JButton updateButton =
                createActionButton(
                        "Update"
                );


        JButton deleteButton =
                createDeleteButton(
                        "Delete"
                );


        JButton backButton =
                createSecondaryButton(
                        "Back"
                );


        buttonPanel.add(
                refreshButton
        );

        buttonPanel.add(
                updateButton
        );

        buttonPanel.add(
                deleteButton
        );

        buttonPanel.add(
                backButton
        );


        applicationsCard.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        contentPanel.add(
                applicationsCard,
                BorderLayout.CENTER
        );


        rootPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );


        // =========================
        // ACTIONS
        // =========================

        searchButton.addActionListener(
                e -> searchApplications()
        );


        showAllButton.addActionListener(
                e -> loadApplications()
        );


        filterButton.addActionListener(
                e -> filterApplications()
        );


        refreshButton.addActionListener(
                e -> loadApplications()
        );


        updateButton.addActionListener(
                e -> updateSelectedApplication()
        );


        deleteButton.addActionListener(
                e -> deleteSelectedApplication()
        );


        backButton.addActionListener(
                e -> dispose()
        );


        dashboardButton.addActionListener(
                e -> dispose()
        );


        applicationsButton.addActionListener(
                e -> loadApplications()
        );


        addButton.addActionListener(
                e -> new AddApplicationFrame(
                        userId
                )
        );


        deadlineButton.addActionListener(
                e -> {

                    JOptionPane.showMessageDialog(
                            this,
                            "Use the Upcoming Deadlines section on the dashboard.",
                            "Upcoming Deadlines",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        );


        logoutButton.addActionListener(
                e -> {

                    dispose();

                    new LoginFrame();
                }
        );


        add(rootPanel);

        loadApplications();

        setVisible(true);
    }


    // =========================
    // LOAD APPLICATIONS
    // =========================

    private void loadApplications() {

        tableModel.setRowCount(0);

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        List<Application> applications =
                applicationDAO
                        .getApplicationsByUser(
                                userId
                        );

        addApplicationsToTable(
                applications
        );
    }


    // =========================
    // SEARCH
    // =========================

    private void searchApplications() {

        String company =
                searchField
                        .getText()
                        .trim();


        if (company.isEmpty()) {

            loadApplications();

            return;
        }


        tableModel.setRowCount(0);

        ApplicationDAO applicationDAO =
                new ApplicationDAO();


        List<Application> applications =
                applicationDAO
                        .searchByCompany(
                                company,
                                userId
                        );


        addApplicationsToTable(
                applications
        );
    }


    // =========================
    // FILTER BY STATUS
    // =========================

    private void filterApplications() {

        String status =
                statusBox
                        .getSelectedItem()
                        .toString();


        if (status.equals("All")) {

            loadApplications();

            return;
        }


        tableModel.setRowCount(0);


        ApplicationDAO applicationDAO =
                new ApplicationDAO();


        List<Application> applications =
                applicationDAO
                        .filterByStatus(
                                status,
                                userId
                        );


        addApplicationsToTable(
                applications
        );
    }


    // =========================
    // ADD TO TABLE
    // =========================

    private void addApplicationsToTable(
            List<Application> applications
    ) {

        for (
                Application application :
                applications
        ) {

            Object[] row = {

                    application
                            .getApplicationId(),

                    application
                            .getCompanyName(),

                    application
                            .getJobRole(),

                    application
                            .getApplicationDate(),

                    application
                            .getDeadline(),

                    application
                            .getStatus()
            };


            tableModel.addRow(
                    row
            );
        }
    }


    // =========================
    // UPDATE
    // =========================

    private void updateSelectedApplication() {

        int selectedRow =
                applicationsTable
                        .getSelectedRow();


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


        if (
                company == null
                        || company.trim().isEmpty()
        ) {

            return;
        }


        String role =
                JOptionPane.showInputDialog(
                        this,
                        "Job Role:",
                        currentRole
                );


        if (
                role == null
                        || role.trim().isEmpty()
        ) {

            return;
        }


        String status =
                JOptionPane.showInputDialog(
                        this,
                        "Status (Applied / Shortlisted / Interview / Rejected):",
                        currentStatus
                );


        if (
                status == null
                        || status.trim().isEmpty()
        ) {

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


    // =========================
    // DELETE
    // =========================

    private void deleteSelectedApplication() {

        int selectedRow =
                applicationsTable
                        .getSelectedRow();


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


        String companyName =
                tableModel.getValueAt(
                        selectedRow,
                        1
                ).toString();


        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete "
                                + companyName
                                + " application?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                choice
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }


        ApplicationDAO applicationDAO =
                new ApplicationDAO();


        boolean success =
                applicationDAO.deleteApplication(
                        applicationId,
                        userId
                );


        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application deleted successfully!"
            );

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


    // =========================
    // SIDEBAR BUTTON
    // =========================

    private JButton createSidebarButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );


        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        button.setForeground(
                Color.WHITE
        );


        button.setBackground(
                SIDEBAR_COLOR
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        15,
                        12,
                        15
                )
        );


        button.setFocusPainted(
                false
        );


        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );


        return button;
    }


    // =========================
    // ACTION BUTTON
    // =========================

    private JButton createActionButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );


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


        button.setFocusPainted(
                false
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        16,
                        8,
                        16
                )
        );


        return button;
    }


    // =========================
    // SECONDARY BUTTON
    // =========================

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );


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


        button.setFocusPainted(
                false
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        16,
                        8,
                        16
                )
        );


        return button;
    }


    // =========================
    // DELETE BUTTON
    // =========================

    private JButton createDeleteButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );


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
                new Color(
                        220,
                        38,
                        38
                )
        );


        button.setFocusPainted(
                false
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        16,
                        8,
                        16
                )
        );


        return button;
    }
}