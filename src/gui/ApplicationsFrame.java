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

    public ApplicationsFrame(int userId) {

        this.userId = userId;

        setTitle("Smart Internship Tracker - My Applications");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
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

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {
                "ID",
                "Company",
                "Role",
                "Application Date",
                "Deadline",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {

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

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Bottom buttons
        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton refreshButton =
                new JButton("Refresh");

        JButton backButton =
                new JButton("Back");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // Button actions
        refreshButton.addActionListener(
                e -> loadApplications()
        );

        backButton.addActionListener(
                e -> dispose()
        );

        add(mainPanel);

        // Load data when window opens
        loadApplications();

        setVisible(true);
    }

    private void loadApplications() {

        tableModel.setRowCount(0);

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        List<Application> applications =
                applicationDAO.getApplicationsByUser(userId);

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
}