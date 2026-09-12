package gui;

import dao.ApplicationDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class AnalyticsFrame extends JFrame {

    private final User user;

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

    private static final Color GREEN_COLOR =
            new Color(22, 163, 74);

    private static final Color ORANGE_COLOR =
            new Color(234, 88, 12);

    private static final Color PURPLE_COLOR =
            new Color(124, 58, 237);

    private static final Color RED_COLOR =
            new Color(220, 38, 38);


    public AnalyticsFrame(User user) {

        this.user = user;

        int userId = user.getUserId();

        setTitle(
                "Smart Internship Tracker - Analytics"
        );

        setSize(
                1000,
                650
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);


        // =========================
        // ROOT
        // =========================

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND_COLOR
        );


        // =========================
        // SIDEBAR
        // =========================

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        0
                )
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


        JLabel logo =
                new JLabel(
                        "SMART TRACKER"
                );

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        logo.setForeground(
                Color.WHITE
        );

        sidebar.add(logo);

        sidebar.add(
                Box.createVerticalStrut(
                        35
                )
        );


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

        JButton analyticsButton =
                createSidebarButton(
                        "Analytics"
                );

        JButton logoutButton =
                createSidebarButton(
                        "Logout"
                );


        sidebar.add(
                dashboardButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        8
                )
        );

        sidebar.add(
                profileButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        8
                )
        );

        sidebar.add(
                applicationsButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        8
                )
        );

        sidebar.add(
                addButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        8
                )
        );

        sidebar.add(
                analyticsButton
        );

        sidebar.add(
                Box.createVerticalGlue()
        );

        sidebar.add(
                logoutButton
        );


        root.add(
                sidebar,
                BorderLayout.WEST
        );


        // =========================
        // CONTENT
        // =========================

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );

        content.setBackground(
                BACKGROUND_COLOR
        );

        content.setBorder(
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

        JLabel title =
                new JLabel(
                        "Application Analytics"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                TEXT_COLOR
        );


        JLabel subtitle =
                new JLabel(
                        "Track the progress of your internship applications."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                MUTED_TEXT_COLOR
        );


        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.add(title);

        header.add(
                Box.createVerticalStrut(
                        5
                )
        );

        header.add(subtitle);


        content.add(
                header,
                BorderLayout.NORTH
        );


        // =========================
        // DATA
        // =========================

        ApplicationDAO dao =
                new ApplicationDAO();

        Map<String, Integer> statistics =
                dao.getApplicationStatistics(
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


        // =========================
        // SUMMARY CARDS
        // =========================

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                15
                        )
                );

        summaryPanel.setOpaque(false);


        summaryPanel.add(
                createMetricCard(
                        "Applied",
                        applied,
                        ACCENT_COLOR
                )
        );

        summaryPanel.add(
                createMetricCard(
                        "Shortlisted",
                        shortlisted,
                        GREEN_COLOR
                )
        );

        summaryPanel.add(
                createMetricCard(
                        "Interview",
                        interview,
                        PURPLE_COLOR
                )
        );

        summaryPanel.add(
                createMetricCard(
                        "Rejected",
                        rejected,
                        RED_COLOR
                )
        );


        // =========================
        // ANALYTICS CARD
        // =========================

        JPanel analyticsCard =
                new JPanel(
                        new BorderLayout(
                                20,
                                15
                        )
                );

        analyticsCard.setBackground(
                CARD_COLOR
        );

        analyticsCard.setBorder(
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


        JLabel progressTitle =
                new JLabel(
                        "Application Progress"
                );

        progressTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        progressTitle.setForeground(
                TEXT_COLOR
        );


        analyticsCard.add(
                progressTitle,
                BorderLayout.NORTH
        );


        JPanel progressPanel =
                new JPanel();

        progressPanel.setOpaque(false);

        progressPanel.setLayout(
                new BoxLayout(
                        progressPanel,
                        BoxLayout.Y_AXIS
                )
        );


        progressPanel.add(
                createProgressRow(
                        "Applied",
                        applied,
                        total,
                        ACCENT_COLOR
                )
        );

        progressPanel.add(
                Box.createVerticalStrut(
                        18
                )
        );

        progressPanel.add(
                createProgressRow(
                        "Shortlisted",
                        shortlisted,
                        total,
                        GREEN_COLOR
                )
        );

        progressPanel.add(
                Box.createVerticalStrut(
                        18
                )
        );

        progressPanel.add(
                createProgressRow(
                        "Interview",
                        interview,
                        total,
                        PURPLE_COLOR
                )
        );

        progressPanel.add(
                Box.createVerticalStrut(
                        18
                )
        );

        progressPanel.add(
                createProgressRow(
                        "Rejected",
                        rejected,
                        total,
                        RED_COLOR
                )
        );


        analyticsCard.add(
                progressPanel,
                BorderLayout.CENTER
        );


        // =========================
        // TOTAL CARD
        // =========================

        JPanel totalCard =
                new JPanel(
                        new BorderLayout()
                );

        totalCard.setBackground(
                new Color(
                        248,
                        250,
                        252
                )
        );

        totalCard.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );


        JLabel totalTitle =
                new JLabel(
                        "Total Applications"
                );

        totalTitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        totalTitle.setForeground(
                MUTED_TEXT_COLOR
        );


        JLabel totalValue =
                new JLabel(
                        String.valueOf(
                                total
                        )
                );

        totalValue.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        34
                )
        );

        totalValue.setForeground(
                TEXT_COLOR
        );


        JPanel totalText =
                new JPanel();

        totalText.setOpaque(false);

        totalText.setLayout(
                new BoxLayout(
                        totalText,
                        BoxLayout.Y_AXIS
                )
        );

        totalText.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        totalText.add(
                totalTitle
        );

        totalText.add(
                Box.createVerticalStrut(
                        5
                )
        );

        totalText.add(
                totalValue
        );


        totalCard.add(
                totalText,
                BorderLayout.CENTER
        );


        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setOpaque(false);

        bottomPanel.add(
                totalCard,
                BorderLayout.CENTER
        );


        content.add(
                summaryPanel,
                BorderLayout.CENTER
        );


        // Put analytics + total together

        JPanel centerWrapper =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        centerWrapper.setOpaque(false);

        centerWrapper.add(
                summaryPanel,
                BorderLayout.NORTH
        );

        centerWrapper.add(
                analyticsCard,
                BorderLayout.CENTER
        );

        centerWrapper.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        content.add(
                centerWrapper,
                BorderLayout.CENTER
        );


        // =========================
        // NAVIGATION
        // =========================

        dashboardButton.addActionListener(
                e -> {

                    dispose();

                    new DashboardFrame(
                            user
                    );
                }
        );


        profileButton.addActionListener(
                e ->
                        new ProfileFrame(
                                user
                        )
        );


        applicationsButton.addActionListener(
                e -> {

                    dispose();

                    new ApplicationsFrame(
                            user
                    );
                }
        );


        addButton.addActionListener(
                e -> {

                    dispose();

                    new AddApplicationFrame(
                            user
                    );
                }
        );


        analyticsButton.addActionListener(
                e -> {
                    // Already on analytics
                }
        );


        logoutButton.addActionListener(
                e -> {

                    dispose();

                    new LoginFrame();
                }
        );


        add(root);

        setVisible(true);
    }


    // =========================
    // METRIC CARD
    // =========================

    private JPanel createMetricCard(
            String title,
            int value,
            Color accent
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
                        String.valueOf(
                                value
                        )
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        valueLabel.setForeground(
                accent
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


    // =========================
    // PROGRESS ROW
    // =========================

    private JPanel createProgressRow(
            String title,
            int value,
            int total,
            Color accent
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                10,
                                5
                        )
                );

        row.setOpaque(false);


        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        titleLabel.setForeground(
                TEXT_COLOR
        );


        JLabel countLabel =
                new JLabel(
                        String.valueOf(
                                value
                        )
                );

        countLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        countLabel.setForeground(
                accent
        );


        JProgressBar progressBar =
                new JProgressBar(
                        0,
                        Math.max(
                                total,
                                1
                        )
                );

        progressBar.setValue(
                value
        );

        progressBar.setStringPainted(false);

        progressBar.setForeground(
                accent
        );


        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        top.add(
                titleLabel,
                BorderLayout.WEST
        );

        top.add(
                countLabel,
                BorderLayout.EAST
        );


        JPanel container =
                new JPanel();

        container.setOpaque(false);

        container.setLayout(
                new BorderLayout(
                        0,
                        7
                )
        );

        container.add(
                top,
                BorderLayout.NORTH
        );

        container.add(
                progressBar,
                BorderLayout.CENTER
        );


        return container;
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

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        15,
                        12,
                        12
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
}
