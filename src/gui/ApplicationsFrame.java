package gui;

import dao.ApplicationDAO;
import model.Application;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationsFrame extends JFrame {

    private final User user;

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

    private static final Color DELETE_COLOR =
            new Color(220, 38, 38);


    public ApplicationsFrame(User user) {

        this.user = user;

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
                new JPanel(
                        new GridBagLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        0
                )
        );

        sidebar.setBackground(
                SIDEBAR_COLOR
        );

        sidebar.setBorder(
                new EmptyBorder(
                        25,
                        15,
                        25,
                        15
                )
        );


        GridBagConstraints sideGbc =
                new GridBagConstraints();

        sideGbc.gridx = 0;
        sideGbc.weightx = 1;
        sideGbc.fill =
                GridBagConstraints.HORIZONTAL;

        sideGbc.anchor =
                GridBagConstraints.NORTHWEST;

        sideGbc.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );


        // Logo

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

        sideGbc.gridy = 0;

        sidebar.add(
                logoLabel,
                sideGbc
        );


        // Space

        sideGbc.gridy = 1;

        sideGbc.weighty = 0;

        sidebar.add(
                Box.createVerticalStrut(
                        25
                ),
                sideGbc
        );


        // =========================
        // SIDEBAR BUTTONS
        // =========================

        JButton dashboardButton =
                createSidebarButton(
                        "Dashboard"
                );

        JButton profileButton =
                createSidebarButton(
                        "My Profile"
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


        addSidebarButton(
                sidebar,
                dashboardButton,
                2
        );

        addSidebarButton(
                sidebar,
                profileButton,
                3
        );

        addSidebarButton(
                sidebar,
                applicationsButton,
                4
        );

        addSidebarButton(
                sidebar,
                addButton,
                5
        );

        addSidebarButton(
                sidebar,
                deadlineButton,
                6
        );


        // Push logout to bottom

        GridBagConstraints glueGbc =
                new GridBagConstraints();

        glueGbc.gridx = 0;
        glueGbc.gridy = 7;

        glueGbc.weighty = 1;
        glueGbc.fill =
                GridBagConstraints.VERTICAL;

        sidebar.add(
                Box.createVerticalGlue(),
                glueGbc
        );


        GridBagConstraints logoutGbc =
                new GridBagConstraints();

        logoutGbc.gridx = 0;
        logoutGbc.gridy = 8;

        logoutGbc.weightx = 1;

        logoutGbc.fill =
                GridBagConstraints.HORIZONTAL;

        logoutGbc.anchor =
                GridBagConstraints.SOUTHWEST;

        sidebar.add(
                logoutButton,
                logoutGbc
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
                Box.createVerticalStrut(
                        5
                )
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
        // APPLICATION CARD
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
        // FILTER PANEL
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
                        15
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


        statusBox =
                new JComboBox<>(
                        new String[]{
                                "All",
                                "Applied",
                                "Shortlisted",
                                "Interview",
                                "Rejected"
                        }
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
                Box.createHorizontalStrut(
                        15
                )
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


        JButton detailsButton =
                createSecondaryButton(
                        "View Details"
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
                detailsButton
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
                e ->
                        searchApplications()
        );


        showAllButton.addActionListener(
                e ->
                        loadApplications()
        );


        filterButton.addActionListener(
                e ->
                        filterApplications()
        );


        refreshButton.addActionListener(
                e ->
                        loadApplications()
        );


        detailsButton.addActionListener(
                e ->
                        showApplicationDetails()
        );


        updateButton.addActionListener(
                e ->
                        updateSelectedApplication()
        );


        deleteButton.addActionListener(
                e ->
                        deleteSelectedApplication()
        );


        // Back → Dashboard

        backButton.addActionListener(
                e -> {

                    dispose();

                    new DashboardFrame(
                            user
                    );
                }
        );


        // Dashboard

        dashboardButton.addActionListener(
                e -> {

                    dispose();

                    new DashboardFrame(
                            user
                    );
                }
        );


        // Profile

        profileButton.addActionListener(
                e ->
                        new ProfileFrame(
                                user
                        )
        );


        // Applications

        applicationsButton.addActionListener(
                e ->
                        loadApplications()
        );


        // Add

        addButton.addActionListener(
                e -> {

                    dispose();

                    new AddApplicationFrame(
                            user
                    );
                }
        );


        // Deadlines

        deadlineButton.addActionListener(
                e -> {

                    JOptionPane.showMessageDialog(
                            this,
                            "Upcoming deadlines are displayed on the dashboard.",
                            "Upcoming Deadlines",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        );


        // Logout

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
    // SIDEBAR HELPER
    // =========================

    private void addSidebarButton(
            JPanel sidebar,
            JButton button,
            int row
    ) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = row;

        gbc.weightx = 1;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );

        sidebar.add(
                button,
                gbc
        );
    }


    // =========================
    // LOAD APPLICATIONS
    // =========================

    private void loadApplications() {

        tableModel.setRowCount(0);

        ApplicationDAO dao =
                new ApplicationDAO();

        List<Application> applications =
                dao.getApplicationsByUser(
                        user.getUserId()
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


        ApplicationDAO dao =
                new ApplicationDAO();


        List<Application> applications =
                dao.searchByCompany(
                        company,
                        user.getUserId()
                );


        addApplicationsToTable(
                applications
        );
    }


    // =========================
    // FILTER
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


        ApplicationDAO dao =
                new ApplicationDAO();


        List<Application> applications =
                dao.filterByStatus(
                        status,
                        user.getUserId()
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

            tableModel.addRow(
                    new Object[]{
                            application.getApplicationId(),
                            application.getCompanyName(),
                            application.getJobRole(),
                            application.getApplicationDate(),
                            application.getDeadline(),
                            application.getStatus()
                    }
            );
        }
    }


    // =========================
    // VIEW DETAILS
    // =========================

    private void showApplicationDetails() {

        int selectedRow =
                applicationsTable
                        .getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application first.",
                    "No Application Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int applicationId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );


        ApplicationDAO dao =
                new ApplicationDAO();


        Application application =
                dao.getApplicationById(
                        applicationId,
                        user.getUserId()
                );


        if (application == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load application.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        JTextArea details =
                new JTextArea();


        details.setEditable(false);

        details.setLineWrap(true);

        details.setWrapStyleWord(true);


        details.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );


        details.setText(
                "APPLICATION DETAILS\n\n"
                        + "Company: "
                        + application.getCompanyName()
                        + "\n\n"
                        + "Job Role: "
                        + application.getJobRole()
                        + "\n\n"
                        + "Application Date: "
                        + application.getApplicationDate()
                        + "\n\n"
                        + "Deadline: "
                        + application.getDeadline()
                        + "\n\n"
                        + "Status: "
                        + application.getStatus()
                        + "\n\n"
                        + "Job Link: "
                        + application.getJobLink()
                        + "\n\n"
                        + "Notes: "
                        + application.getNotes()
        );


        JScrollPane detailsScroll =
                new JScrollPane(
                        details
                );


        detailsScroll.setPreferredSize(
                new Dimension(
                        500,
                        400
                )
        );


        JOptionPane.showMessageDialog(
                this,
                detailsScroll,
                "Application Details",
                JOptionPane.INFORMATION_MESSAGE
        );
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
                    "Please select an application first."
            );

            return;
        }


        int applicationId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );


        String company =
                tableModel.getValueAt(
                        selectedRow,
                        1
                ).toString();


        String role =
                tableModel.getValueAt(
                        selectedRow,
                        2
                ).toString();


        String status =
                tableModel.getValueAt(
                        selectedRow,
                        5
                ).toString();


        new UpdateApplicationFrame(
                user.getUserId(),
                applicationId,
                company,
                role,
                status,
                "",
                ""
        );
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
                    "Please select an application first."
            );

            return;
        }


        int applicationId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );


        String company =
                tableModel.getValueAt(
                        selectedRow,
                        1
                ).toString();


        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete "
                                + company
                                + " application?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                choice != JOptionPane.YES_OPTION
        ) {

            return;
        }


        ApplicationDAO dao =
                new ApplicationDAO();


        boolean success =
                dao.deleteApplication(
                        applicationId,
                        user.getUserId()
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
                    "Failed to delete application.",
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


        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );


        button.setFocusPainted(
                false
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        15,
                        12,
                        12
                )
        );


        button.setPreferredSize(
                new Dimension(
                        190,
                        45
                )
        );


        button.setMinimumSize(
                new Dimension(
                        190,
                        45
                )
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
                DELETE_COLOR
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