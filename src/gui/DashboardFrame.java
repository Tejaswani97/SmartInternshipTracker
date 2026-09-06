package gui;

import dao.ApplicationDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardFrame extends JFrame {

    private int userId;

    public DashboardFrame(int userId) {

        this.userId = userId;

        setTitle("Smart Internship Tracker - Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Title
        JLabel titleLabel =
                new JLabel("Smart Internship Tracker");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Statistics panel
        JPanel statsPanel =
                new JPanel(new GridLayout(2, 4, 15, 15));

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        Map<String, Integer> statistics =
                applicationDAO.getApplicationStatistics(userId);

        int applied =
                statistics.getOrDefault("Applied", 0);

        int shortlisted =
                statistics.getOrDefault("Shortlisted", 0);

        int interview =
                statistics.getOrDefault("Interview", 0);

        int rejected =
                statistics.getOrDefault("Rejected", 0);

        int total =
                applied + shortlisted + interview + rejected;

        statsPanel.add(createStatLabel("Total", total));
        statsPanel.add(createStatLabel("Applied", applied));
        statsPanel.add(createStatLabel("Shortlisted", shortlisted));
        statsPanel.add(createStatLabel("Interview", interview));

        statsPanel.add(createStatLabel("Rejected", rejected));

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton applicationsButton =
                new JButton("My Applications");

        JButton addButton =
                new JButton("Add Application");

        JButton logoutButton =
                new JButton("Logout");

        buttonPanel.add(applicationsButton);
        buttonPanel.add(addButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        applicationsButton.addActionListener(e -> {

               new ApplicationsFrame(userId);

         });

        addButton.addActionListener(e ->
        new AddApplicationFrame(userId)
         );

        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame();
        });

        add(mainPanel);

        setVisible(true);
    }

    private JLabel createStatLabel(
            String title,
            int value) {

        JLabel label =
                new JLabel(
                        "<html><center>"
                                + title
                                + "<br><br>"
                                + "<b>"
                                + value
                                + "</b>"
                                + "</center></html>"
                );

        label.setFont(
                new Font("Arial", Font.PLAIN, 18)
        );

        label.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        label.setBorder(
                BorderFactory.createLineBorder(
                        Color.GRAY
                )
        );

        return label;
    }
}