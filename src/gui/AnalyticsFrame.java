package gui;

import dao.ApplicationDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class AnalyticsFrame extends JFrame {

    private final User user;

    private final Color SIDEBAR = new Color(31, 41, 55);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color BLUE = new Color(59, 130, 246);
    private final Color BORDER = new Color(229, 231, 235);

    public AnalyticsFrame(User user) {

        this.user = user;

        setTitle("Smart Internship Tracker - Analytics");
        setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();
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
            dispose();
            new ProfileFrame(user).setVisible(true);
        }));

        top.add(Box.createVerticalStrut(8));

        top.add(createSidebarButton("My Applications", false, e -> {
            dispose();
            new ApplicationsFrame(user).setVisible(true);
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

        top.add(createSidebarButton("Analytics", true, e -> {
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

        JPanel header = new JPanel();
        header.setBackground(BACKGROUND);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Application Analytics");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Understand your internship application progress"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        main.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(BACKGROUND);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        ApplicationDAO dao = new ApplicationDAO();

        Map<String, Integer> stats =
                dao.getApplicationStatistics(user.getUserId());

        int applied = stats.getOrDefault("Applied", 0);
        int shortlisted = stats.getOrDefault("Shortlisted", 0);
        int interview = stats.getOrDefault("Interview", 0);
        int rejected = stats.getOrDefault("Rejected", 0);

        int total = applied + shortlisted + interview + rejected;

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setBackground(BACKGROUND);
        cards.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                130
        ));

        cards.add(createStatCard(
                "Applied",
                applied,
                BLUE
        ));

        cards.add(createStatCard(
                "Shortlisted",
                shortlisted,
                new Color(124, 58, 237)
        ));

        cards.add(createStatCard(
                "Interview",
                interview,
                new Color(5, 150, 105)
        ));

        cards.add(createStatCard(
                "Rejected",
                rejected,
                new Color(220, 38, 38)
        ));

        center.add(cards);
        center.add(Box.createVerticalStrut(20));

        JPanel insights = createInsightsPanel(
                total,
                shortlisted,
                interview
        );

        center.add(insights);

        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel createStatCard(
            String title,
            int value,
            Color accent) {

        JPanel card = new JPanel();
        card.setBackground(CARD);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 18, 18, 18)
        ));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel bar = new JPanel();
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(45, 4));
        bar.setMaximumSize(new Dimension(45, 4));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        titleLabel.setForeground(MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(TEXT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(bar);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(valueLabel);

        return card;
    }

    private JPanel createInsightsPanel(
            int total,
            int shortlisted,
            int interview) {

        JPanel panel = new JPanel();
        panel.setBackground(CARD);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(25, 25, 25, 25)
        ));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Insights");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        double shortlistRate =
                total == 0 ? 0 :
                        ((double) shortlisted / total) * 100;

        double interviewRate =
                total == 0 ? 0 :
                        ((double) interview / total) * 100;

        addInsight(
                panel,
                "Total Applications",
                String.valueOf(total)
        );

        addInsight(
                panel,
                "Shortlist Rate",
                String.format("%.1f%%", shortlistRate)
        );

        addInsight(
                panel,
                "Interview Rate",
                String.format("%.1f%%", interviewRate)
        );

        return panel;
    }

    private void addInsight(
            JPanel panel,
            String label,
            String value) {

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD);
        row.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        JLabel left = new JLabel(label);
        left.setFont(new Font(
                "SansSerif",
                Font.PLAIN,
                14
        ));
        left.setForeground(MUTED);

        JLabel right = new JLabel(value);
        right.setFont(new Font(
                "SansSerif",
                Font.BOLD,
                15
        ));
        right.setForeground(TEXT);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        panel.add(row);
        panel.add(Box.createVerticalStrut(8));
    }

    private void showUpcomingDeadlines() {

        ApplicationDAO dao = new ApplicationDAO();

        var applications =
                dao.getUpcomingDeadlines(user.getUserId());

        if (applications.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No upcoming deadlines."
            );

            return;
        }

        StringBuilder message = new StringBuilder();

        for (var app : applications) {

            message.append(app.getCompanyName())
                    .append(" - ")
                    .append(app.getJobRole())
                    .append("\nDeadline: ")
                    .append(app.getDeadline())
                    .append("\n\n");
        }

        JTextArea area = new JTextArea(
                message.toString()
        );

        area.setEditable(false);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(
                new Dimension(450, 350)
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Upcoming Deadlines",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}