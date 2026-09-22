package gui;

import dao.ApplicationDAO;
import model.Application;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {

    private final User user;

    private static final Color SIDEBAR = new Color(31, 41, 55);
    private static final Color SIDEBAR_SELECTED = new Color(55, 65, 81);
    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color BLUE_LIGHT = new Color(239, 246, 255);
    private static final Color BORDER = new Color(229, 231, 235);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color GREEN_LIGHT = new Color(240, 253, 244);
    private static final Color PURPLE = new Color(124, 58, 237);
    private static final Color PURPLE_LIGHT = new Color(245, 243, 255);
    private static final Color RED = new Color(220, 38, 38);
    private static final Color RED_LIGHT = new Color(254, 242, 242);
    private static final Color ORANGE = new Color(234, 88, 12);
    private static final Color ORANGE_LIGHT = new Color(255, 247, 237);
    private static final Color GRAY = new Color(75, 85, 99);
    private static final Color GRAY_LIGHT = new Color(249, 250, 251);

    public DashboardFrame(User user) {

        this.user = user;

        setTitle("Smart Internship Tracker - Dashboard");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 700));
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
        sidebar.setPreferredSize(new Dimension(235, 820));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel logo = new JLabel("SmartIntern");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tracker = new JLabel("Internship Tracker");
        tracker.setForeground(new Color(156, 163, 175));
        tracker.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tracker.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(logo);
        top.add(Box.createVerticalStrut(2));
        top.add(tracker);
        top.add(Box.createVerticalStrut(32));

        top.add(createSidebarButton("Dashboard", true, e -> { }));
        top.add(Box.createVerticalStrut(8));
        top.add(createSidebarButton("My Profile", false, e -> openFrame(() -> new ProfileFrame(user))));
        top.add(Box.createVerticalStrut(8));
        top.add(createSidebarButton("Internship Opportunities", false, e -> openFrame(() -> new InternshipsFrame(user))));
        top.add(Box.createVerticalStrut(8));
        top.add(createSidebarButton("My Applications", false, e -> openFrame(() -> new ApplicationsFrame(user))));
        top.add(Box.createVerticalStrut(8));
        top.add(createSidebarButton("Add Application", false, e -> openFrame(() -> new AddApplicationFrame(user))));
        top.add(Box.createVerticalStrut(8));
        top.add(createSidebarButton("Upcoming Deadlines", false, e -> showUpcomingDeadlines()));
        top.add(Box.createVerticalStrut(8));
        top.add(createSidebarButton("Analytics", false, e -> openFrame(() -> new AnalyticsFrame(user))));

        sidebar.add(top, BorderLayout.NORTH);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new EmptyBorder(15, 20, 25, 20));

        bottom.add(createSidebarButton("Logout", false, e -> logout()));
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private void openFrame(java.util.function.Supplier<JFrame> supplier) {

        dispose();
        JFrame frame = supplier.get();
        frame.setVisible(true);
    }

    private void logout() {

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private JButton createSidebarButton(
            String text,
            boolean selected,
            java.awt.event.ActionListener action
    ) {

        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(195, 44));
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(selected ? SIDEBAR_SELECTED : SIDEBAR);
        button.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 10));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.addActionListener(action);
        return button;
    }

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(28, 30, 25, 30));

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(createStatisticsPanel());
        body.add(Box.createVerticalStrut(18));
        body.add(createMiddleSection());
        body.add(Box.createVerticalStrut(18));
        body.add(createQuickActionsPanel());

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(BACKGROUND);

        main.add(scrollPane, BorderLayout.CENTER);

        return main;
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(BACKGROUND);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel(
                "Welcome back, " + safe(user.getName(), "Student") + " 👋"
        );
        welcome.setFont(new Font("SansSerif", Font.BOLD, 30));
        welcome.setForeground(TEXT);

        JLabel email = new JLabel(
                safe(user.getEmail(), "") + "  •  Here's your internship progress at a glance."
        );
        email.setFont(new Font("SansSerif", Font.PLAIN, 14));
        email.setForeground(MUTED);

        text.add(welcome);
        text.add(Box.createVerticalStrut(5));
        text.add(email);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);

        JButton refresh = createToolbarButton("Refresh Dashboard", BLUE);
        refresh.addActionListener(e -> refreshDashboard());
        right.add(refresh);

        header.add(text, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private void refreshDashboard() {

        Container root = getContentPane();
        root.removeAll();
        root.setLayout(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMainContent(), BorderLayout.CENTER);
        root.revalidate();
        root.repaint();
    }

    private JPanel createStatisticsPanel() {

        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);

        JLabel title = new JLabel("Application Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 6, 12, 0));
        statsPanel.setOpaque(false);

        Map<String, Integer> stats =
                new ApplicationDAO().getApplicationStatistics(user.getUserId());

        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        int applied = stats.getOrDefault("Applied", 0);
        int shortlisted = stats.getOrDefault("Shortlisted", 0);
        int interview = stats.getOrDefault("Interview", 0);
        int selected = stats.getOrDefault("Selected", 0);
        int rejected = stats.getOrDefault("Rejected", 0);

        statsPanel.add(createStatCard("Total", String.valueOf(total), BLUE, BLUE_LIGHT));
        statsPanel.add(createStatCard("Applied", String.valueOf(applied), BLUE, BLUE_LIGHT));
        statsPanel.add(createStatCard("Shortlisted", String.valueOf(shortlisted), PURPLE, PURPLE_LIGHT));
        statsPanel.add(createStatCard("Interview", String.valueOf(interview), GREEN, GREEN_LIGHT));
        statsPanel.add(createStatCard("Selected", String.valueOf(selected), GREEN, GREEN_LIGHT));
        statsPanel.add(createStatCard("Rejected", String.valueOf(rejected), RED, RED_LIGHT));

        wrapper.add(statsPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createStatCard(
            String title,
            String value,
            Color accent,
            Color lightBackground
    ) {

        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(5, 55));

        JPanel icon = new JPanel(new GridBagLayout());
        icon.setBackground(lightBackground);
        icon.setPreferredSize(new Dimension(40, 40));
        icon.add(new JLabel("●"));
        ((JLabel) icon.getComponent(0)).setForeground(accent);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setForeground(TEXT);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(2));
        text.add(valueLabel);

        card.add(accentBar, BorderLayout.WEST);
        card.add(icon, BorderLayout.CENTER);
        card.add(text, BorderLayout.EAST);

        return card;
    }

    private JPanel createMiddleSection() {

        JPanel middle = new JPanel(new GridLayout(1, 2, 18, 0));
        middle.setOpaque(false);

        middle.add(createDeadlinePanel());
        middle.add(createRecentApplicationsPanel());

        return middle;
    }

    private JPanel createDeadlinePanel() {

        JPanel panel = createCardPanel();

        List<Application> applications =
                new ApplicationDAO().getUpcomingDeadlines(user.getUserId());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = createSectionTitle("Upcoming Deadlines");
        header.add(title, BorderLayout.WEST);

        int urgentCount = countUrgentDeadlines(applications);
        JLabel reminder = new JLabel();
        reminder.setFont(new Font("SansSerif", Font.BOLD, 11));

        if (urgentCount > 0) {
            reminder.setText("⚠ " + urgentCount + " deadline"
                    + (urgentCount == 1 ? "" : "s")
                    + " within 3 days");
            reminder.setForeground(ORANGE);
        } else {
            reminder.setText("No urgent deadlines");
            reminder.setForeground(GREEN);
        }

        header.add(reminder, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        int count = Math.min(applications.size(), 5);

        if (count == 0) {

            JLabel empty = new JLabel("No upcoming deadlines.");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
            empty.setForeground(MUTED);
            list.add(empty);

        } else {

            for (int i = 0; i < count; i++) {
                Application app = applications.get(i);
                list.add(createDeadlineRow(app));
                if (i < count - 1) {
                    list.add(Box.createVerticalStrut(6));
                }
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setPreferredSize(new Dimension(0, 250));

        panel.add(scroll, BorderLayout.CENTER);

        JButton viewAll = createTextButton("View all deadlines →", BLUE);
        viewAll.addActionListener(e -> showUpcomingDeadlines());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        footer.add(viewAll);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDeadlineRow(Application app) {

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(CARD);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 10, 10, 10)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel company = new JLabel(safe(app.getCompanyName(), "Unknown company"));
        company.setFont(new Font("SansSerif", Font.BOLD, 13));
        company.setForeground(TEXT);

        JLabel role = new JLabel(safe(app.getJobRole(), "Internship"));
        role.setFont(new Font("SansSerif", Font.PLAIN, 11));
        role.setForeground(MUTED);

        left.add(company);
        left.add(role);

        LocalDate deadline = app.getDeadline();
        long days = deadline == null
                ? Long.MAX_VALUE
                : ChronoUnit.DAYS.between(LocalDate.now(), deadline);

        String urgency = formatDaysLeft(days);
        JLabel deadlineLabel = new JLabel(
                deadline == null ? "No deadline" : deadline + "  " + urgency
        );
        deadlineLabel.setFont(new Font("SansSerif", Font.BOLD, 11));

        if (days == 0) {
            deadlineLabel.setForeground(RED);
        } else if (days <= 3) {
            deadlineLabel.setForeground(ORANGE);
        } else {
            deadlineLabel.setForeground(BLUE);
        }

        row.add(left, BorderLayout.CENTER);
        row.add(deadlineLabel, BorderLayout.EAST);

        return row;
    }

    private JPanel createRecentApplicationsPanel() {

        JPanel panel = createCardPanel();

        JLabel title = createSectionTitle("Recent Applications");
        panel.add(title, BorderLayout.NORTH);

        List<Application> applications =
                new ApplicationDAO().getApplicationsByUser(user.getUserId());

        List<Application> recent = new ArrayList<>(applications);
        recent.sort(Comparator.comparing(
                Application::getApplicationDate,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        int count = Math.min(recent.size(), 5);

        if (count == 0) {

            JLabel empty = new JLabel("You haven't tracked any applications yet.");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
            empty.setForeground(MUTED);
            list.add(empty);

        } else {

            for (int i = 0; i < count; i++) {
                list.add(createRecentApplicationRow(recent.get(i)));
                if (i < count - 1) {
                    list.add(Box.createVerticalStrut(6));
                }
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setPreferredSize(new Dimension(0, 250));

        panel.add(scroll, BorderLayout.CENTER);

        JButton viewAll = createTextButton("Open My Applications →", BLUE);
        viewAll.addActionListener(e -> openFrame(() -> new ApplicationsFrame(user)));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        footer.add(viewAll);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRecentApplicationRow(Application app) {

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(CARD);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 10, 10, 10)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel company = new JLabel(safe(app.getCompanyName(), "Unknown company"));
        company.setFont(new Font("SansSerif", Font.BOLD, 13));
        company.setForeground(TEXT);

        JLabel role = new JLabel(safe(app.getJobRole(), "Internship"));
        role.setFont(new Font("SansSerif", Font.PLAIN, 11));
        role.setForeground(MUTED);

        left.add(company);
        left.add(role);

        JLabel status = new JLabel(safe(app.getStatus(), "Applied"));
        status.setFont(new Font("SansSerif", Font.BOLD, 11));
        status.setOpaque(true);
        status.setBorder(new EmptyBorder(5, 8, 5, 8));
        status.setForeground(statusForeground(app.getStatus()));
        status.setBackground(statusBackground(app.getStatus()));

        row.add(left, BorderLayout.CENTER);
        row.add(status, BorderLayout.EAST);

        return row;
    }

    private JPanel createQuickActionsPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JLabel title = createSectionTitle("Quick Actions");
        panel.add(title, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(1, 4, 12, 0));
        actions.setOpaque(false);

        JButton browse = createQuickActionButton("Browse Internships", BLUE);
        JButton applications = createQuickActionButton("My Applications", PURPLE);
        JButton profile = createQuickActionButton("My Profile", GREEN);
        JButton analytics = createQuickActionButton("View Analytics", GRAY);

        browse.addActionListener(e -> openFrame(() -> new InternshipsFrame(user)));
        applications.addActionListener(e -> openFrame(() -> new ApplicationsFrame(user)));
        profile.addActionListener(e -> openFrame(() -> new ProfileFrame(user)));
        analytics.addActionListener(e -> openFrame(() -> new AnalyticsFrame(user)));

        actions.add(browse);
        actions.add(applications);
        actions.add(profile);
        actions.add(analytics);

        panel.add(actions, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCardPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 12, 16)
        ));
        return panel;
    }

    private JLabel createSectionTitle(String text) {

        JLabel title = new JLabel(text);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);
        return title;
    }

    private JButton createQuickActionButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));
        return button;
    }

    private JButton createToolbarButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 13, 9, 13));
        return button;
    }

    private JButton createTextButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setForeground(color);
        button.setBackground(CARD);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setFocusPainted(false);
        return button;
    }

    private Color statusForeground(String status) {

        if (status == null) {
            return BLUE;
        }

        switch (status.toLowerCase()) {
            case "selected":
                return GREEN;
            case "rejected":
                return RED;
            case "interview":
                return PURPLE;
            case "shortlisted":
                return ORANGE;
            default:
                return BLUE;
        }
    }

    private Color statusBackground(String status) {

        if (status == null) {
            return BLUE_LIGHT;
        }

        switch (status.toLowerCase()) {
            case "selected":
                return GREEN_LIGHT;
            case "rejected":
                return RED_LIGHT;
            case "interview":
                return PURPLE_LIGHT;
            case "shortlisted":
                return ORANGE_LIGHT;
            default:
                return BLUE_LIGHT;
        }
    }

    private int countUrgentDeadlines(List<Application> applications) {

        int count = 0;
        LocalDate today = LocalDate.now();

        for (Application app : applications) {
            LocalDate deadline = app.getDeadline();

            if (deadline == null) {
                continue;
            }

            long days = ChronoUnit.DAYS.between(today, deadline);

            if (days >= 0 && days <= 3) {
                count++;
            }
        }

        return count;
    }

    private String formatDaysLeft(long daysLeft) {

        if (daysLeft == Long.MAX_VALUE) {
            return "";
        }

        if (daysLeft < 0) {
            return "(Expired)";
        }

        if (daysLeft == 0) {
            return "(Today)";
        }

        if (daysLeft == 1) {
            return "(Tomorrow)";
        }

        return "(" + daysLeft + " days left)";
    }

    private void showUpcomingDeadlines() {

        ApplicationDAO dao = new ApplicationDAO();
        List<Application> applications =
                dao.getUpcomingDeadlines(user.getUserId());

        if (applications.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "You have no upcoming deadlines.",
                    "Upcoming Deadlines",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder message = new StringBuilder("UPCOMING DEADLINES\n\n");

        for (Application app : applications) {

            LocalDate deadline = app.getDeadline();
            long days = deadline == null
                    ? Long.MAX_VALUE
                    : ChronoUnit.DAYS.between(LocalDate.now(), deadline);

            message.append(safe(app.getCompanyName(), "Unknown company"))
                    .append(" - ")
                    .append(safe(app.getJobRole(), "Internship"))
                    .append("\nDeadline: ")
                    .append(deadline == null ? "Not provided" : deadline);

            if (days == 0) {
                message.append("  ⚠ TODAY");
            } else if (days == 1) {
                message.append("  ⚠ TOMORROW");
            } else if (days > 1) {
                message.append("  (").append(days).append(" days left)");
            }

            message.append("\n\n");
        }

        JTextArea area = new JTextArea(message.toString());
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(560, 420));

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "Upcoming Deadlines",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
