package gui;

import dao.ApplicationDAO;
import model.Application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {

    private final int userId;

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

    private static final Color URGENT_COLOR =
            new Color(220, 38, 38);


    public DashboardFrame(int userId) {

        this.userId = userId;

        setTitle("Smart Internship Tracker");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // =========================
        // ROOT PANEL
        // =========================

        JPanel rootPanel =
                new JPanel(new BorderLayout());

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


        // Logo

        JLabel logoLabel =
                new JLabel("SMART TRACKER");

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

        sidebar.add(logoLabel);

        sidebar.add(
                Box.createVerticalStrut(40)
        );


        // Sidebar buttons

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


        sidebar.add(dashboardButton);

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(applicationsButton);

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(addButton);

        sidebar.add(
                Box.createVerticalStrut(10)
        );

        sidebar.add(deadlineButton);

        sidebar.add(
                Box.createVerticalGlue()
        );

        sidebar.add(logoutButton);


        rootPanel.add(
                sidebar,
                BorderLayout.WEST
        );


        // =========================
        // CONTENT PANEL
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

        headerPanel.setOpaque(false);


        JLabel welcomeLabel =
                new JLabel(
                        "Welcome back 👋"
                );

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        welcomeLabel.setForeground(
                TEXT_COLOR
        );


        JLabel subtitleLabel =
                new JLabel(
                        "Here's an overview of your internship applications."
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

        headerText.setOpaque(false);

        headerText.setLayout(
                new BoxLayout(
                        headerText,
                        BoxLayout.Y_AXIS
                )
        );

        headerText.add(
                welcomeLabel
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
        // CENTER PANEL
        // =========================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );

        centerPanel.setOpaque(false);


        // =========================
        // STATISTICS
        // =========================

        JPanel statsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                5,
                                15,
                                15
                        )
                );

        statsPanel.setOpaque(false);


        ApplicationDAO applicationDAO =
                new ApplicationDAO();


        Map<String, Integer> statistics =
                applicationDAO
                        .getApplicationStatistics(
                                userId
                        );


        int applied =
                statistics.getOrDefault(
                        "Applied",
                        0
                );

        int shortlisted =
                statistics.getOrDefault(
                        "Shortlisted",
                        0
                );

        int interview =
                statistics.getOrDefault(
                        "Interview",
                        0
                );

        int rejected =
                statistics.getOrDefault(
                        "Rejected",
                        0
                );


        int total =
                applied
                        + shortlisted
                        + interview
                        + rejected;


        statsPanel.add(
                createStatCard(
                        "Total",
                        total
                )
        );

        statsPanel.add(
                createStatCard(
                        "Applied",
                        applied
                )
        );

        statsPanel.add(
                createStatCard(
                        "Shortlisted",
                        shortlisted
                )
        );

        statsPanel.add(
                createStatCard(
                        "Interview",
                        interview
                )
        );

        statsPanel.add(
                createStatCard(
                        "Rejected",
                        rejected
                )
        );


        centerPanel.add(
                statsPanel,
                BorderLayout.NORTH
        );


        // =========================
        // DEADLINE CARDS
        // =========================

        JPanel deadlinePanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        deadlinePanel.setBackground(
                CARD_COLOR
        );

        deadlinePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );


        JLabel deadlineTitle =
                new JLabel(
                        "Upcoming Deadlines"
                );

        deadlineTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        deadlineTitle.setForeground(
                TEXT_COLOR
        );


        deadlinePanel.add(
                deadlineTitle,
                BorderLayout.NORTH
        );


        // Container for deadline cards

        JPanel deadlineCardsPanel =
                new JPanel();

        deadlineCardsPanel.setLayout(
                new BoxLayout(
                        deadlineCardsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        deadlineCardsPanel.setBackground(
                CARD_COLOR
        );


        List<Application> upcoming =
                applicationDAO
                        .getUpcomingDeadlines(
                                userId
                        );


        LocalDate today =
                LocalDate.now();


        if (upcoming.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "No upcoming deadlines."
                    );

            emptyLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            14
                    )
            );

            emptyLabel.setForeground(
                    MUTED_TEXT_COLOR
            );

            emptyLabel.setBorder(
                    new EmptyBorder(
                            20,
                            5,
                            20,
                            5
                    )
            );

            deadlineCardsPanel.add(
                    emptyLabel
            );

        } else {

            for (
                    Application application :
                    upcoming
            ) {

                long daysLeft =
                        ChronoUnit.DAYS.between(
                                today,
                                application
                                        .getDeadline()
                        );


                // =========================
                // SINGLE DEADLINE CARD
                // =========================

                JPanel card =
                        new JPanel(
                                new BorderLayout(
                                        15,
                                        5
                                )
                        );

                card.setBackground(
                        new Color(
                                248,
                                250,
                                252
                        )
                );


                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        BORDER_COLOR
                                ),
                                new EmptyBorder(
                                        12,
                                        15,
                                        12,
                                        15
                                )
                        )
                );


                card.setMaximumSize(
                        new Dimension(
                                Integer.MAX_VALUE,
                                95
                        )
                );


                // =========================
                // LEFT SIDE
                // =========================

                JPanel leftPanel =
                        new JPanel();

                leftPanel.setOpaque(false);

                leftPanel.setLayout(
                        new BoxLayout(
                                leftPanel,
                                BoxLayout.Y_AXIS
                        )
                );


                JLabel companyLabel =
                        new JLabel(
                                application
                                        .getCompanyName()
                        );

                companyLabel.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                );

                companyLabel.setForeground(
                        TEXT_COLOR
                );


                JLabel roleLabel =
                        new JLabel(
                                application
                                        .getJobRole()
                        );

                roleLabel.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                13
                        )
                );

                roleLabel.setForeground(
                        MUTED_TEXT_COLOR
                );


                leftPanel.add(
                        companyLabel
                );

                leftPanel.add(
                        Box.createVerticalStrut(5)
                );

                leftPanel.add(
                        roleLabel
                );


                // =========================
                // RIGHT SIDE
                // =========================

                JPanel rightPanel =
                        new JPanel();

                rightPanel.setOpaque(false);

                rightPanel.setLayout(
                        new BoxLayout(
                                rightPanel,
                                BoxLayout.Y_AXIS
                        )
                );


                JLabel deadlineLabel =
                        new JLabel(
                                "Deadline: "
                                        + application
                                        .getDeadline()
                        );

                deadlineLabel.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                12
                        )
                );

                deadlineLabel.setForeground(
                        MUTED_TEXT_COLOR
                );


                String daysText;


                if (daysLeft == 0) {

                    daysText =
                            "⚠ Due today!";

                } else if (daysLeft == 1) {

                    daysText =
                            "⚠ 1 day left";

                } else {

                    daysText =
                            "⏰ "
                                    + daysLeft
                                    + " days left";
                }


                JLabel daysLabel =
                        new JLabel(
                                daysText
                        );

                daysLabel.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );


                if (daysLeft <= 3) {

                    daysLabel.setForeground(
                            URGENT_COLOR
                    );

                } else {

                    daysLabel.setForeground(
                            ACCENT_COLOR
                    );
                }


                rightPanel.add(
                        deadlineLabel
                );

                rightPanel.add(
                        Box.createVerticalStrut(8)
                );

                rightPanel.add(
                        daysLabel
                );


                // Add sides to card

                card.add(
                        leftPanel,
                        BorderLayout.CENTER
                );

                card.add(
                        rightPanel,
                        BorderLayout.EAST
                );


                deadlineCardsPanel.add(
                        card
                );

                deadlineCardsPanel.add(
                        Box.createVerticalStrut(10)
                );
            }
        }


        JScrollPane deadlineScroll =
                new JScrollPane(
                        deadlineCardsPanel
                );

        deadlineScroll.setBorder(
                null
        );

        deadlineScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );


        deadlinePanel.add(
                deadlineScroll,
                BorderLayout.CENTER
        );


        centerPanel.add(
                deadlinePanel,
                BorderLayout.CENTER
        );


        contentPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        rootPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );


        // =========================
        // BUTTON ACTIONS
        // =========================

        applicationsButton
                .addActionListener(
                        e ->
                                new ApplicationsFrame(
                                        userId
                                )
                );


        addButton
                .addActionListener(
                        e ->
                                new AddApplicationFrame(
                                        userId
                                )
                );


        deadlineButton
                .addActionListener(
                        e ->
                                showDeadlineAlerts()
                );


        logoutButton
                .addActionListener(
                        e -> {

                            dispose();

                            new LoginFrame();
                        }
                );


        // =========================
        // FINAL SETUP
        // =========================

        add(rootPanel);

        setVisible(true);
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            int value
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
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
                                15,
                                15,
                                15,
                                15
                        )
                )
        );


        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        titleLabel.setForeground(
                MUTED_TEXT_COLOR
        );


        JLabel valueLabel =
                new JLabel(
                        String.valueOf(value)
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        valueLabel.setForeground(
                ACCENT_COLOR
        );


        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

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

        button.setFocusPainted(false);

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


    // =========================================================
    // DEADLINE ALERT POPUP
    // =========================================================

    private void showDeadlineAlerts() {

        ApplicationDAO applicationDAO =
                new ApplicationDAO();


        List<Application> applications =
                applicationDAO
                        .getUpcomingDeadlines(
                                userId
                        );


        if (applications.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No upcoming deadlines!",
                    "Deadline Alerts",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        LocalDate today =
                LocalDate.now();


        StringBuilder message =
                new StringBuilder();


        message.append(
                "UPCOMING DEADLINES\n"
        );

        message.append(
                "========================\n\n"
        );


        for (
                Application application :
                applications
        ) {

            long daysLeft =
                    ChronoUnit.DAYS.between(
                            today,
                            application
                                    .getDeadline()
                    );


            message.append(
                    application
                            .getCompanyName()
            );

            message.append(
                    " - "
            );

            message.append(
                    application
                            .getJobRole()
            );


            message.append(
                    "\nDeadline: "
            );

            message.append(
                    application
                            .getDeadline()
            );


            message.append(
                    "\n"
            );


            if (daysLeft == 0) {

                message.append(
                        "⚠ Due today!"
                );

            } else if (daysLeft == 1) {

                message.append(
                        "⚠ 1 day left"
                );

            } else {

                message.append(
                        daysLeft
                                + " days left"
                );
            }


            message.append(
                    "\n------------------------\n"
            );
        }


        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "Deadline Alerts",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}