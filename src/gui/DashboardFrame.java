package gui;

import dao.ApplicationDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import model.Application;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

          // Dashboard center area
JPanel centerPanel =
        new JPanel(new BorderLayout(10, 10));

centerPanel.add(
        statsPanel,
        BorderLayout.NORTH
);

// Deadline preview
JTextArea deadlineArea =
        new JTextArea();

deadlineArea.setEditable(false);
deadlineArea.setFont(
        new Font("Arial", Font.PLAIN, 15)
);

deadlineArea.setBorder(
        BorderFactory.createTitledBorder(
                "Upcoming Deadlines"
        )
);

List<Application> upcoming =
        applicationDAO.getUpcomingDeadlines(userId);

LocalDate today = LocalDate.now();

if (upcoming.isEmpty()) {

    deadlineArea.setText(
            "No upcoming deadlines."
    );

} else {

    StringBuilder text =
            new StringBuilder();

    for (Application application : upcoming) {

        long daysLeft =
                ChronoUnit.DAYS.between(
                        today,
                        application.getDeadline()
                );

        text.append(
                application.getCompanyName()
        );

        text.append(" - ");

        text.append(
                application.getJobRole()
        );

        text.append("\nDeadline: ");

        text.append(
                application.getDeadline()
        );

        text.append("\n");

        if (daysLeft == 0) {

            text.append("⚠ Due today!");

        } else if (daysLeft == 1) {

            text.append("⚠ 1 day left");

        } else {

            text.append(
                    "Days left: " + daysLeft
            );
        }

        text.append(
                "\n------------------------------\n"
        );
    }

    deadlineArea.setText(
            text.toString()
    );
}

centerPanel.add(
        new JScrollPane(deadlineArea),
        BorderLayout.CENTER
);

mainPanel.add(
        centerPanel,
        BorderLayout.CENTER
);

        // Buttons
        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton applicationsButton =
                new JButton("My Applications");

        JButton addButton =
                new JButton("Add Application");

        JButton logoutButton =
                new JButton("Logout");
        JButton deadlineButton =
        new JButton("Upcoming Deadlines");

        buttonPanel.add(applicationsButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deadlineButton);
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
        deadlineButton.addActionListener(e ->
        showDeadlineAlerts()
        );

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
    private void showDeadlineAlerts() {

    ApplicationDAO applicationDAO =
            new ApplicationDAO();

    List<Application> applications =
            applicationDAO.getUpcomingDeadlines(userId);

    if (applications.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "No upcoming deadlines found!",
                "Deadline Alerts",
                JOptionPane.INFORMATION_MESSAGE
        );

        return;
    }

    LocalDate today = LocalDate.now();

    StringBuilder message =
            new StringBuilder();

    message.append("UPCOMING DEADLINES\n");
    message.append("========================\n\n");

    for (Application application : applications) {

        long daysLeft =
                ChronoUnit.DAYS.between(
                        today,
                        application.getDeadline()
                );

        message.append(
                application.getCompanyName()
        );

        message.append(" - ");

        message.append(
                application.getJobRole()
        );

        message.append("\n");

        message.append(
                "Deadline: "
        );

        message.append(
                application.getDeadline()
        );

        message.append("\n");

        if (daysLeft == 0) {

            message.append("⚠ Due today!");

        } else if (daysLeft == 1) {

            message.append("⚠ 1 day left");

        } else {

            message.append(
                    "Days left: " + daysLeft
            );
        }

        message.append("\n");
        message.append("------------------------\n");
    }

    JOptionPane.showMessageDialog(
            this,
            message.toString(),
            "Deadline Alerts",
            JOptionPane.INFORMATION_MESSAGE
    );
}
}