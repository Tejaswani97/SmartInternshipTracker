package gui;

import dao.ApplicationDAO;
import model.Application;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {

    private final User user;

    // =========================================================
    // THEME
    // =========================================================

    private final Color SIDEBAR = new Color(31, 41, 55);
    private final Color SIDEBAR_SELECTED = new Color(55, 65, 81);

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;

    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);

    private final Color BLUE = new Color(59, 130, 246);
    private final Color BORDER = new Color(229, 231, 235);

    private final Color GREEN = new Color(22, 163, 74);
    private final Color PURPLE = new Color(124, 58, 237);
    private final Color RED = new Color(220, 38, 38);
    private final Color ORANGE = new Color(234, 88, 12);


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DashboardFrame(User user) {

        this.user = user;

        setTitle(
                "Smart Internship Tracker - Dashboard"
        );

        setSize(
                1200,
                750
        );
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        buildUI();
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

        root.add(
                createSidebar(),
                BorderLayout.WEST
        );

        root.add(
                createMainContent(),
                BorderLayout.CENTER
        );

        setContentPane(root);
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setBackground(
                SIDEBAR
        );

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        750
                )
        );

        sidebar.setLayout(
                new BorderLayout()
        );


        // -----------------------------------------------------
        // TOP
        // -----------------------------------------------------

        JPanel top =
                new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        top.setBorder(
                new EmptyBorder(
                        30,
                        20,
                        20,
                        20
                )
        );


        // Logo

        JLabel logo =
                new JLabel(
                        "SmartIntern"
                );

        logo.setForeground(
                Color.WHITE
        );

        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        logo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        // Subtitle

        JLabel tracker =
                new JLabel(
                        "Internship Tracker"
                );

        tracker.setForeground(
                new Color(
                        156,
                        163,
                        175
                )
        );

        tracker.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        tracker.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        top.add(logo);

        top.add(
                Box.createVerticalStrut(2)
        );

        top.add(tracker);

        top.add(
                Box.createVerticalStrut(30)
        );


        // -----------------------------------------------------
        // DASHBOARD
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "Dashboard",
                        true,
                        e -> {
                            // Already on dashboard
                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // PROFILE
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "My Profile",
                        false,
                        e -> {
                              dispose();
                            new ProfileFrame(user)
                                    .setVisible(true);

                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // INTERNSHIP OPPORTUNITIES
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "Internship Opportunities",
                        false,
                        e -> {
                               dispose();
                            new InternshipsFrame(user)
                                    .setVisible(true);

                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // MY APPLICATIONS
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "My Applications",
                        false,
                        e -> {
                             dispose();
                            new ApplicationsFrame(user)
                                    .setVisible(true);

                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // ADD APPLICATION
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "Add Application",
                        false,
                        e -> {
                                  dispose();
                            new AddApplicationFrame(user)
                                    .setVisible(true);

                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // UPCOMING DEADLINES
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "Upcoming Deadlines",
                        false,
                        e -> {

                            showUpcomingDeadlines();

                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // ANALYTICS
        // -----------------------------------------------------

        top.add(
                createSidebarButton(
                        "Analytics",
                        false,
                        e -> {
                              dispose();
                            new AnalyticsFrame(user)
                                    .setVisible(true);

                        }
                )
        );


        sidebar.add(
                top,
                BorderLayout.NORTH
        );


        // =====================================================
        // BOTTOM
        // =====================================================

        JPanel bottom =
                new JPanel();

        bottom.setOpaque(false);

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        25,
                        20
                )
        );


        JButton logout =
                createSidebarButton(
                        "Logout",
                        false,
                        e -> {

                            int answer =
                                    JOptionPane.showConfirmDialog(
                                            this,
                                            "Are you sure you want to logout?",
                                            "Logout",
                                            JOptionPane.YES_NO_OPTION
                                    );

                            if (answer ==
                                    JOptionPane.YES_OPTION) {

                                dispose();

                                new LoginFrame()
                                        .setVisible(true);
                            }
                        }
                );


        bottom.add(logout);


        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );


        return sidebar;
    }


    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private JButton createSidebarButton(
            String text,
            boolean selected,
            java.awt.event.ActionListener action
    ) {

        JButton button =
                new JButton(
                        text
                );


        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );


        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );


        button.setPreferredSize(
                new Dimension(
                        180,
                        42
                )
        );


        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        button.setForeground(
                Color.WHITE
        );


        button.setBackground(
                selected
                        ? SIDEBAR_SELECTED
                        : SIDEBAR
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        14,
                        0,
                        10
                )
        );


        button.setFocusPainted(
                false
        );


        button.setOpaque(
                true
        );


        button.addActionListener(
                action
        );


        return button;
    }


    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {

        JPanel main =
                new JPanel(
                        new BorderLayout(
                                0,
                                20
                        )
                );


        main.setBackground(
                BACKGROUND
        );


        main.setBorder(
                new EmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );


        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        JPanel header =
                new JPanel();


        header.setBackground(
                BACKGROUND
        );


        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel welcome =
                new JLabel(
                        "Welcome back, "
                                + user.getName()
                                + " 👋"
                );


        welcome.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );


        welcome.setForeground(
                TEXT
        );


        JLabel email =
                new JLabel(
                        user.getEmail()
                );


        email.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        email.setForeground(
                MUTED
        );


        header.add(
                welcome
        );


        header.add(
                Box.createVerticalStrut(5)
        );


        header.add(
                email
        );


        main.add(
                header,
                BorderLayout.NORTH
        );


        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        JPanel center =
                new JPanel();


        center.setBackground(
                BACKGROUND
        );


        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );


        center.add(
                createStatisticsPanel()
        );


        center.add(
                Box.createVerticalStrut(20)
        );


        center.add(
                createDeadlinePanel()
        );


        center.add(
                Box.createVerticalStrut(20)
        );


        center.add(
                createQuickActionsPanel()
        );


        main.add(
                center,
                BorderLayout.CENTER
        );


        return main;
    }


    // =========================================================
    // STATISTICS
    // =========================================================

    private JPanel createStatisticsPanel() {

        JPanel statsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                5,
                                15,
                                0
                        )
                );


        statsPanel.setBackground(
                BACKGROUND
        );


        ApplicationDAO dao =
                new ApplicationDAO();


        Map<String, Integer> stats =
                dao.getApplicationStatistics(
                        user.getUserId()
                );


        int total = 0;


        int applied =
                stats.getOrDefault(
                        "Applied",
                        0
                );


        int shortlisted =
                stats.getOrDefault(
                        "Shortlisted",
                        0
                );


        int interview =
                stats.getOrDefault(
                        "Interview",
                        0
                );


        int rejected =
                stats.getOrDefault(
                        "Rejected",
                        0
                );


        for (int value :
                stats.values()) {

            total += value;
        }


        statsPanel.add(
                createStatCard(
                        "Total",
                        String.valueOf(total),
                        BLUE
                )
        );


        statsPanel.add(
                createStatCard(
                        "Applied",
                        String.valueOf(applied),
                        BLUE
                )
        );


        statsPanel.add(
                createStatCard(
                        "Shortlisted",
                        String.valueOf(shortlisted),
                        PURPLE
                )
        );


        statsPanel.add(
                createStatCard(
                        "Interview",
                        String.valueOf(interview),
                        GREEN
                )
        );


        statsPanel.add(
                createStatCard(
                        "Rejected",
                        String.valueOf(rejected),
                        RED
                )
        );


        return statsPanel;
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            String value,
            Color accent
    ) {

        JPanel card =
                new JPanel();


        card.setBackground(
                CARD
        );


        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );


        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        JPanel accentBar =
                new JPanel();


        accentBar.setBackground(
                accent
        );


        accentBar.setMaximumSize(
                new Dimension(
                        45,
                        4
                )
        );


        accentBar.setPreferredSize(
                new Dimension(
                        45,
                        4
                )
        );


        accentBar.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JLabel titleLabel =
                new JLabel(
                        title
                );


        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        titleLabel.setForeground(
                MUTED
        );


        titleLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JLabel valueLabel =
                new JLabel(
                        value
                );


        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );


        valueLabel.setForeground(
                TEXT
        );


        valueLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        card.add(
                accentBar
        );


        card.add(
                Box.createVerticalStrut(12)
        );


        card.add(
                titleLabel
        );


        card.add(
                Box.createVerticalStrut(4)
        );


        card.add(
                valueLabel
        );


        return card;
    }


    // =========================================================
    // UPCOMING DEADLINES
    // =========================================================

    private JPanel createDeadlinePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );


        panel.setBackground(
                CARD
        );


        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "Upcoming Deadlines"
                );


        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );


        title.setForeground(
                TEXT
        );


        panel.add(
                title,
                BorderLayout.NORTH
        );


        JPanel listPanel =
                new JPanel();


        listPanel.setBackground(
                CARD
        );


        listPanel.setLayout(
                new BoxLayout(
                        listPanel,
                        BoxLayout.Y_AXIS
                )
        );


        listPanel.setBorder(
                new EmptyBorder(
                        15,
                        0,
                        0,
                        0
                )
        );


        ApplicationDAO dao =
                new ApplicationDAO();


        List<Application> applications =
                dao.getUpcomingDeadlines(
                        user.getUserId()
                );


        if (applications.isEmpty()) {

            JLabel empty =
                    new JLabel(
                            "No upcoming deadlines."
                    );


            empty.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            14
                    )
            );


            empty.setForeground(
                    MUTED
            );


            listPanel.add(
                    empty
            );

        } else {

            int count =
                    Math.min(
                            applications.size(),
                            5
                    );


            for (
                    int i = 0;
                    i < count;
                    i++
            ) {

                Application app =
                        applications.get(i);


                JPanel row =
                        new JPanel(
                                new BorderLayout()
                        );


                row.setBackground(
                        CARD
                );


                row.setBorder(
                        new EmptyBorder(
                                8,
                                0,
                                8,
                                0
                        )
                );


                row.setMaximumSize(
                        new Dimension(
                                Integer.MAX_VALUE,
                                65
                        )
                );


                JPanel left =
                        new JPanel();


                left.setOpaque(
                        false
                );


                left.setLayout(
                        new BoxLayout(
                                left,
                                BoxLayout.Y_AXIS
                        )
                );


                JLabel company =
                        new JLabel(
                                app.getCompanyName()
                        );


                company.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                14
                        )
                );


                company.setForeground(
                        TEXT
                );


                JLabel role =
                        new JLabel(
                                app.getJobRole()
                        );


                role.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                12
                        )
                );


                role.setForeground(
                        MUTED
                );


                left.add(
                        company
                );


                left.add(
                        role
                );


                long daysLeft =
                        ChronoUnit.DAYS.between(
                                LocalDate.now(),
                                app.getDeadline()
                        );


                JLabel date =
                        new JLabel(
                                app.getDeadline()
                                        .toString()
                                        + "  "
                                        + formatDaysLeft(
                                                daysLeft
                                        )
                        );


                date.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                12
                        )
                );


                if (daysLeft <= 3) {

                    date.setForeground(
                            ORANGE
                    );

                } else {

                    date.setForeground(
                            BLUE
                    );
                }


                row.add(
                        left,
                        BorderLayout.WEST
                );


                row.add(
                        date,
                        BorderLayout.EAST
                );


                listPanel.add(
                        row
                );
            }
        }


        panel.add(
                listPanel,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================================================
    // QUICK ACTIONS
    // =========================================================

    private JPanel createQuickActionsPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );


        panel.setBackground(
                BACKGROUND
        );


        JLabel title =
                new JLabel(
                        "Quick Actions"
                );


        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );


        title.setForeground(
                TEXT
        );


        panel.add(
                title,
                BorderLayout.NORTH
        );


        JPanel actions =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                15,
                                0
                        )
                );


        actions.setBackground(
                BACKGROUND
        );


        JButton browseButton =
                createQuickActionButton(
                        "Browse Internships",
                        BLUE
                );


        JButton addButton =
                createQuickActionButton(
                        "Add Application",
                        GREEN
                );


        JButton analyticsButton =
                createQuickActionButton(
                        "View Analytics",
                        PURPLE
                );


        browseButton.addActionListener(
                e -> {
                     dispose();
                    new InternshipsFrame(user)
                            .setVisible(true);

                }
        );


        addButton.addActionListener(
                e -> {
                      dispose();
                    new AddApplicationFrame(user)
                            .setVisible(true);

                }
        );


        analyticsButton.addActionListener(
                e -> {
                          dispose();
                    new AnalyticsFrame(user)
                            .setVisible(true);

                }
        );


        actions.add(
                browseButton
        );


        actions.add(
                addButton
        );


        actions.add(
                analyticsButton
        );


        panel.add(
                actions,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================================================
    // QUICK ACTION BUTTON
    // =========================================================

    private JButton createQuickActionButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(
                        text
                );


        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );


        button.setForeground(
                Color.WHITE
        );


        button.setBackground(
                color
        );


        button.setFocusPainted(
                false
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        return button;
    }


    // =========================================================
    // FORMAT DAYS LEFT
    // =========================================================

    private String formatDaysLeft(
            long daysLeft
    ) {

        if (daysLeft < 0) {

            return "(Expired)";

        }

        if (daysLeft == 0) {

            return "(Today)";

        }

        if (daysLeft == 1) {

            return "(Tomorrow)";

        }

        return "("
                + daysLeft
                + " days left)";
    }


    // =========================================================
    // FULL DEADLINE POPUP
    // =========================================================

    private void showUpcomingDeadlines() {

        ApplicationDAO dao =
                new ApplicationDAO();


        List<Application> applications =
                dao.getUpcomingDeadlines(
                        user.getUserId()
                );


        if (applications.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "You have no upcoming deadlines.",
                    "Upcoming Deadlines",
                    JOptionPane.INFORMATION_MESSAGE
            );


            return;
        }


        StringBuilder message =
                new StringBuilder();


        message.append(
                "UPCOMING DEADLINES\n"
        );


        message.append(
                "============================\n\n"
        );


        for (
                Application app :
                applications
        ) {

            LocalDate deadline =
                    app.getDeadline();


            long days =
                    ChronoUnit.DAYS.between(
                            LocalDate.now(),
                            deadline
                    );


            message.append(
                    app.getCompanyName()
            );


            message.append(
                    " - "
            );


            message.append(
                    app.getJobRole()
            );


            message.append(
                    "\nDeadline: "
            );


            message.append(
                    deadline
            );


            if (days == 0) {

                message.append(
                        "  ⚠ TODAY"
                );

            } else if (days == 1) {

                message.append(
                        "  ⚠ TOMORROW"
                );

            } else if (days > 1) {

                message.append(
                        "  ("
                );


                message.append(
                        days
                );


                message.append(
                        " days left)"
                );
            }


            message.append(
                    "\n\n"
            );
        }


        JTextArea area =
                new JTextArea(
                        message.toString()
                );


        area.setEditable(
                false
        );


        area.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        area.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        area
                );


        scrollPane.setPreferredSize(
                new Dimension(
                        550,
                        400
                )
        );


        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Upcoming Deadlines",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}