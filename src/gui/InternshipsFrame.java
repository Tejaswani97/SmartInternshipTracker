package gui;

import dao.ApplicationDAO;
import dao.InternshipDAO;
import model.Application;
import model.Internship;
import model.User;
import util.SkillMatchResult;
import util.SkillMatcher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InternshipsFrame extends JFrame {

    private final User user;

    private static final Color SIDEBAR =
            new Color(31, 41, 55);

    private static final Color SIDEBAR_SELECTED =
            new Color(55, 65, 81);

    private static final Color BACKGROUND =
            new Color(245, 247, 250);

    private static final Color CARD =
            Color.WHITE;

    private static final Color TEXT =
            new Color(31, 41, 55);

    private static final Color MUTED =
            new Color(107, 114, 128);

    private static final Color BLUE =
            new Color(59, 130, 246);

    private static final Color GREEN =
            new Color(22, 163, 74);

    private static final Color ORANGE =
            new Color(234, 88, 12);

    private static final Color RED =
            new Color(220, 38, 38);

    private static final Color BORDER =
            new Color(229, 231, 235);


    private JTextField searchField;

    private JComboBox<String> categoryBox;
    private JComboBox<String> locationBox;
    private JComboBox<String> workModeBox;

    private JLabel resultCountLabel;

    private JPanel resultsPanel;

    private final InternshipDAO internshipDAO;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public InternshipsFrame(User user) {

        this.user = user;

        internshipDAO =
                new InternshipDAO();


        setTitle(
                "Smart Internship Tracker - Internship Opportunities"
        );


        setSize(
                1200,
                750
        );
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);


        // X returns to dashboard
        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );


        addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(
                            WindowEvent e
                    ) {

                        goToDashboard();
                    }
                }
        );


        buildUI();


        loadAllInternships();
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
                createContent(),
                BorderLayout.CENTER
        );


        setContentPane(root);
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {
        

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setBackground(
                SIDEBAR
        );

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        750
                )
        );


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


        JLabel logo =
                new JLabel(
                        "SmartIntern"
                );

        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        logo.setForeground(
                Color.WHITE
        );

        logo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        JLabel subtitle =
                new JLabel(
                        "Internship Tracker"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                new Color(
                        156,
                        163,
                        175
                )
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        top.add(logo);

        top.add(
                Box.createVerticalStrut(2)
        );

        top.add(subtitle);

        top.add(
                Box.createVerticalStrut(30)
        );


        top.add(
                sidebarButton(
                        "Dashboard",
                        false,
                        e -> goToDashboard()
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        top.add(
                sidebarButton(
                        "My Profile",
                        false,
                        e -> goToProfile()
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        top.add(
                sidebarButton(
                        "Internship Opportunities",
                        true,
                        e -> {
                        }
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        top.add(
                sidebarButton(
                        "My Applications",
                        false,
                        e -> goToApplications()
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        top.add(
                sidebarButton(
                        "Add Application",
                        false,
                        e -> goToAddApplication()
                )
        );


        top.add(
                Box.createVerticalStrut(8)
        );


        top.add(
                sidebarButton(
                        "Analytics",
                        false,
                        e -> goToAnalytics()
                )
        );


        sidebar.add(
                top,
                BorderLayout.NORTH
        );


        JPanel bottom =
                new JPanel();

        bottom.setOpaque(false);

        bottom.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        25,
                        20
                )
        );


        bottom.add(
                sidebarButton(
                        "Logout",
                        false,
                        e -> logout()
                )
        );


        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );


        return sidebar;
    }


    // =========================================================
    // CONTENT
    // =========================================================

    private JPanel createContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                0,
                                20
                        )
                );

        content.setBackground(
                BACKGROUND
        );

        content.setBorder(
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

        JPanel heading =
                new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Internship Opportunities"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                TEXT
        );


        JLabel subtitle =
                new JLabel(
                        "Find internships that match your interests and skills"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                MUTED
        );


        heading.add(title);

        heading.add(
                Box.createVerticalStrut(5)
        );

        heading.add(subtitle);


        content.add(
                heading,
                BorderLayout.NORTH
        );


        // -----------------------------------------------------
        // FILTER SECTION
        // -----------------------------------------------------

        JPanel filterContainer =
                new JPanel(
                        new GridBagLayout()
                );

        filterContainer.setBackground(
                CARD
        );

        filterContainer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        5,
                        5,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        // Search

        JLabel searchLabel =
                new JLabel(
                        "Search"
                );

        searchLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        searchField =
                new JTextField();


        searchField.setPreferredSize(
                new Dimension(
                        250,
                        36
                )
        );


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        filterContainer.add(
                searchLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;

        filterContainer.add(
                searchField,
                gbc
        );


        // Category

        JLabel categoryLabel =
                new JLabel(
                        "Category"
                );


        categoryLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        gbc.gridx = 2;
        gbc.weightx = 0;

        filterContainer.add(
                categoryLabel,
                gbc
        );


        categoryBox =
                new JComboBox<>(
                        new String[]{
                                "All Categories",
                                "Software Development",
                                "Data Science",
                                "AI/ML",
                                "Web Development",
                                "Cloud",
                                "Cybersecurity",
                                "Mobile Development",
                                "Networking"
                        }
                );


        gbc.gridx = 3;
        gbc.weightx = 1;

        filterContainer.add(
                categoryBox,
                gbc
        );


        // Second row

        JLabel locationLabel =
                new JLabel(
                        "Location"
                );


        locationLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;


        filterContainer.add(
                locationLabel,
                gbc
        );


        locationBox =
                new JComboBox<>(
                        new String[]{
                                "All Locations",
                                "Bengaluru",
                                "Hyderabad",
                                "Pune",
                                "Chennai",
                                "Noida",
                                "Gurugram"
                        }
                );


        gbc.gridx = 1;
        gbc.weightx = 1;


        filterContainer.add(
                locationBox,
                gbc
        );


        JLabel workModeLabel =
                new JLabel(
                        "Work Mode"
                );


        workModeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        gbc.gridx = 2;
        gbc.weightx = 0;


        filterContainer.add(
                workModeLabel,
                gbc
        );


        workModeBox =
                new JComboBox<>(
                        new String[]{
                                "All Work Modes",
                                "Remote",
                                "Hybrid",
                                "On-site"
                        }
                );


        gbc.gridx = 3;
        gbc.weightx = 1;


        filterContainer.add(
                workModeBox,
                gbc
        );


        // Buttons

        JButton searchButton =
                createButton(
                        "Apply Filters",
                        BLUE
                );


        JButton resetButton =
                createButton(
                        "Reset",
                        new Color(
                                107,
                                114,
                                128
                        )
                );


        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;


        JPanel filterButtons =
                new JPanel();


        filterButtons.setOpaque(false);

        filterButtons.setLayout(
                new BoxLayout(
                        filterButtons,
                        BoxLayout.Y_AXIS
                )
        );


        filterButtons.add(
                searchButton
        );


        filterButtons.add(
                Box.createVerticalStrut(8)
        );


        filterButtons.add(
                resetButton
        );


        filterContainer.add(
                filterButtons,
                gbc
        );


        JPanel north =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );


        north.setOpaque(false);

        north.add(
                heading,
                BorderLayout.NORTH
        );


        north.add(
                filterContainer,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // RESULTS
        // -----------------------------------------------------

        JPanel resultsHeader =
                new JPanel(
                        new BorderLayout()
                );

        resultsHeader.setOpaque(false);


        resultCountLabel =
                new JLabel(
                        "Loading internships..."
                );


        resultCountLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        resultCountLabel.setForeground(
                MUTED
        );


        resultsHeader.add(
                resultCountLabel,
                BorderLayout.WEST
        );


        resultsPanel =
                new JPanel();


        resultsPanel.setBackground(
                BACKGROUND
        );


        resultsPanel.setLayout(
                new BoxLayout(
                        resultsPanel,
                        BoxLayout.Y_AXIS
                )
        );
        resultsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JScrollPane scrollPane =
                new JScrollPane(
                        resultsPanel
                );


        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);


        JPanel center =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );


        center.setOpaque(false);


        center.add(
                resultsHeader,
                BorderLayout.NORTH
        );


        center.add(
                scrollPane,
                BorderLayout.CENTER
        );


        JPanel main =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );


        main.setOpaque(false);


        main.add(
                north,
                BorderLayout.NORTH
        );


        main.add(
                center,
                BorderLayout.CENTER
        );


        content.add(
                main,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // ACTIONS
        // -----------------------------------------------------

        searchButton.addActionListener(
                e -> applyFilters()
        );


        resetButton.addActionListener(
                e -> resetFilters()
        );


        searchField.addActionListener(
                e -> applyFilters()
        );


        return content;
    }


    // =========================================================
    // LOAD ALL
    // =========================================================

    private void loadAllInternships() {

        List<Internship> internships =
                internshipDAO.getAllInternships();


        displayInternships(
                internships
        );
    }


    // =========================================================
    // COMBINED FILTER
    // =========================================================

    private void applyFilters() {

        String keyword =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String category =
                String.valueOf(
                        categoryBox
                                .getSelectedItem()
                );


        String location =
                String.valueOf(
                        locationBox
                                .getSelectedItem()
                );


        String workMode =
                String.valueOf(
                        workModeBox
                                .getSelectedItem()
                );


        List<Internship> all =
                internshipDAO.getAllInternships();


        List<Internship> filtered =
                new ArrayList<>();


        for (
                Internship internship :
                all
        ) {

            boolean matches =
                    true;


            // -------------------------------------------------
            // SEARCH
            // -------------------------------------------------

            if (!keyword.isEmpty()) {

                String searchable =
                        (
                                internship.getCompanyName()
                                        + " "
                                        + internship.getJobRole()
                                        + " "
                                        + internship.getCategory()
                                        + " "
                                        + internship.getLocation()
                                        + " "
                                        + internship.getWorkMode()
                                        + " "
                                        + internship.getStipend()
                                        + " "
                                        + internship.getRequiredSkills()
                        )
                                .toLowerCase();


                if (
                        !searchable.contains(
                                keyword
                        )
                ) {

                    matches = false;
                }
            }


            // -------------------------------------------------
            // CATEGORY
            // -------------------------------------------------

            if (
                    !category.equals(
                            "All Categories"
                    )
                    &&
                    !internship
                            .getCategory()
                            .equalsIgnoreCase(
                                    category
                            )
            ) {

                matches = false;
            }


            // -------------------------------------------------
            // LOCATION
            // -------------------------------------------------

            if (
                    !location.equals(
                            "All Locations"
                    )
                    &&
                    !internship
                            .getLocation()
                            .equalsIgnoreCase(
                                    location
                            )
            ) {

                matches = false;
            }


            // -------------------------------------------------
            // WORK MODE
            // -------------------------------------------------

            if (
                    !workMode.equals(
                            "All Work Modes"
                    )
                    &&
                    !internship
                            .getWorkMode()
                            .equalsIgnoreCase(
                                    workMode
                            )
            ) {

                matches = false;
            }


            if (matches) {

                filtered.add(
                        internship
                );
            }
        }


        displayInternships(
                filtered
        );
    }


    // =========================================================
    // DISPLAY
    // =========================================================

    private void displayInternships(
            List<Internship> internships
    ) {

        resultsPanel.removeAll();


        resultCountLabel.setText(
                internships.size()
                        + " internship"
                        + (
                        internships.size() == 1
                                ? ""
                                : "s"
                )
                        + " found"
        );


        if (
                internships.isEmpty()
        ) {

            JPanel empty =
                    new JPanel();

            empty.setOpaque(false);

            empty.setLayout(
                    new BoxLayout(
                            empty,
                            BoxLayout.Y_AXIS
                    )
            );


            JLabel title =
                    new JLabel(
                            "No internships match your filters."
                    );

            title.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            16
                    )
            );

            title.setForeground(
                    TEXT
            );

            title.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );


            JLabel hint =
                    new JLabel(
                            "Try removing a filter or changing your search."
                    );

            hint.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            13
                    )
            );

            hint.setForeground(
                    MUTED
            );

            hint.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );


            empty.add(
                    Box.createVerticalStrut(80)
            );

            empty.add(title);

            empty.add(
                    Box.createVerticalStrut(8)
            );

            empty.add(hint);


            resultsPanel.add(
                    empty
            );

        } else {

           for (int i = 0; i < internships.size(); i++) {

                Internship internship = internships.get(i);

             resultsPanel.add(
                 createInternshipCard(
                    internship
            )
    );

           if (i < internships.size() - 1) {
             resultsPanel.add(
                Box.createVerticalStrut(12)
            );
         }
   }
        }


        resultsPanel.revalidate();

        resultsPanel.repaint();
    }


    // =========================================================
    // INTERNSHIP CARD
    // =========================================================

    private JPanel createInternshipCard(
            Internship internship
    ) {

        SkillMatchResult matchResult = SkillMatcher.calculateMatch(
        user.getSkills(),
        internship.getRequiredSkills()
        );

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                30,
                                10
                        )
                );


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
                                20,
                                18,
                                20
                        )
                )
        );


         card.setMaximumSize(
        new Dimension(
                Integer.MAX_VALUE,
                300
        )
);

        // -----------------------------------------------------
        // LEFT
        // -----------------------------------------------------

        JPanel left =
                new JPanel();


        left.setOpaque(false);


        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel company =
                new JLabel(
                        internship.getCompanyName()
                );


        company.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );


        company.setForeground(
                TEXT
        );


        JLabel role =
                new JLabel(
                        internship.getJobRole()
                );


        role.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );


        role.setForeground(
                MUTED
        );


        JLabel category =
                new JLabel(
                        internship.getCategory()
                );

        JLabel matchLabel = new JLabel(
        "🔥 " + matchResult.getMatchPercentage() + "% Match"
         );
          String missingSkillsText;

if (matchResult.getMissingSkills().isEmpty()) {

    missingSkillsText = "✓ All required skills matched";

} else {

    missingSkillsText =
            "⚠ Missing: "
                    + String.join(
                            ", ",
                            matchResult.getMissingSkills()
                    );
}

      JLabel missingSkillsLabel =
        new JLabel(
                "<html>"
                        + missingSkillsText
                        + "</html>"
        );

      missingSkillsLabel.setFont(
        new Font(
                "SansSerif",
                Font.PLAIN,
                12
        )
);

missingSkillsLabel.setForeground(
        matchResult.getMissingSkills().isEmpty()
                ? GREEN
                : ORANGE
);
        matchLabel.setFont(
        new Font(
                "SansSerif",
                Font.BOLD,
                13
        )
     );

matchLabel.setForeground(
        GREEN
);


        category.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        category.setForeground(
                BLUE
        );


        JLabel details =
                new JLabel(
                        "📍 "
                                + internship.getLocation()
                                + "   •   💼 "
                                + internship.getWorkMode()
                                + "   •   💰 "
                                + internship.getStipend()
                );


        details.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        details.setForeground(
                MUTED
        );


        JLabel duration =
                new JLabel(
                        "Duration: "
                                + internship.getDuration()
                );


        duration.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        duration.setForeground(
                TEXT
        );


          JLabel skills =
        new JLabel(
                "<html><b>Skills:</b> "
                        + internship.getRequiredSkills()
                        + "</html>"
        );


        skills.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        skills.setForeground(
                MUTED
        );


        left.add(company);

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(role);

        left.add(
                Box.createVerticalStrut(7)
        );

        left.add(category);

        left.add(
           Box.createVerticalStrut(8)
        );

        left.add(matchLabel);

         left.add(
        Box.createVerticalStrut(5)
        );

         left.add(missingSkillsLabel);

        left.add(
        Box.createVerticalStrut(8)
           );

         left.add(details);
        left.add(
                Box.createVerticalStrut(5)
        );

        left.add(duration);

        left.add(
                Box.createVerticalStrut(5)
        );

        left.add(skills);


        // -----------------------------------------------------
        // RIGHT
        // -----------------------------------------------------

        JPanel right =
                new JPanel();


        right.setOpaque(false);


        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );


        boolean hasDeadline =
        internship.getDeadline() != null;

long days = 0;

if (hasDeadline) {

    days =
            ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    internship.getDeadline()
            );
}


JLabel deadline =
        new JLabel(
                hasDeadline
                        ? "Deadline: "
                                + internship.getDeadline()
                        : "Deadline: Not specified"
        );


        deadline.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );


        deadline.setForeground(
                MUTED
        );


        JLabel daysLabel =
                new JLabel();


        daysLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        if (!hasDeadline) {

    daysLabel.setText(
            "No deadline listed"
    );

    daysLabel.setForeground(
            MUTED
    );

} else if (days < 0) {

    daysLabel.setText(
            "Deadline passed"
    );

    daysLabel.setForeground(
            RED
    );

} else if (days == 0) {

    daysLabel.setText(
            "⚠ Due today"
    );

    daysLabel.setForeground(
            RED
    );

} else if (days <= 3) {

    daysLabel.setText(
            "⚠ "
                    + days
                    + " days left"
    );

    daysLabel.setForeground(
            ORANGE
    );

} else {

    daysLabel.setText(
            "⏰ "
                    + days
                    + " days left"
    );

    daysLabel.setForeground(
            BLUE
    );
}

        JButton detailsButton =
                createButton(
                        "View Details",
                        BLUE
                );


        JButton applyButton =
                createButton(
                        "Apply",
                        GREEN
                );


        right.add(
                deadline
        );


        right.add(
                Box.createVerticalStrut(7)
        );


        right.add(
                daysLabel
        );


        right.add(
                Box.createVerticalGlue()
        );


        right.add(
                detailsButton
        );


        right.add(
                Box.createVerticalStrut(7)
        );


        right.add(
                applyButton
        );


        card.add(
                left,
                BorderLayout.CENTER
        );


        card.add(
                right,
                BorderLayout.EAST
        );


        detailsButton.addActionListener(
                e -> showDetails(
                        internship
                )
        );


        applyButton.addActionListener(
                e -> apply(
                        internship
                )
        );


        return card;
    }


    // =========================================================
    // DETAILS
    // =========================================================

    private void showDetails(
            Internship internship
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Internship Details",
                        true
                );

        dialog.setSize(
                820,
                700
        );

        dialog.setMinimumSize(
                new Dimension(
                        700,
                        600
                )
        );

        dialog.setLocationRelativeTo(this);

        dialog.setLayout(
                new BorderLayout()
        );


        // =========================================================
        // HEADER
        // =========================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                CARD
        );

        header.setBorder(
                new EmptyBorder(
                        20,
                        24,
                        18,
                        24
                )
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


        JLabel companyLabel =
                new JLabel(
                        safeValue(
                                internship.getCompanyName()
                        )
                );

        companyLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25
                )
        );

        companyLabel.setForeground(
                TEXT
        );


        JLabel roleLabel =
                new JLabel(
                        safeValue(
                                internship.getJobRole()
                        )
                );

        roleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        16
                )
        );

        roleLabel.setForeground(
                MUTED
        );


        headerText.add(
                companyLabel
        );

        headerText.add(
                Box.createVerticalStrut(4)
        );

        headerText.add(
                roleLabel
        );


        header.add(
                headerText,
                BorderLayout.CENTER
        );


        // =========================================================
        // SCROLLABLE CONTENT
        // =========================================================

        JPanel body =
                new JPanel(
                        new GridBagLayout()
                );

        body.setBackground(
                BACKGROUND
        );

        body.setBorder(
                new EmptyBorder(
                        20,
                        24,
                        20,
                        24
                )
        );


        GridBagConstraints bodyGbc =
                new GridBagConstraints();

        bodyGbc.gridx = 0;
        bodyGbc.gridy = 0;
        bodyGbc.weightx = 1;
        bodyGbc.fill =
                GridBagConstraints.HORIZONTAL;
        bodyGbc.anchor =
                GridBagConstraints.NORTHWEST;
        bodyGbc.insets =
                new Insets(
                        0,
                        0,
                        14,
                        0
                );


        // =========================================================
        // INTERNSHIP INFORMATION
        // =========================================================

        JPanel internshipSection =
                createDetailsSection(
                        "Internship Information"
                );

        JPanel internshipGrid =
                new JPanel(
                        new GridBagLayout()
                );

        internshipGrid.setOpaque(false);

        addDetailRow(
                internshipGrid,
                0,
                "Category",
                internship.getCategory()
        );

        addDetailRow(
                internshipGrid,
                1,
                "Location",
                internship.getLocation()
        );

        addDetailRow(
                internshipGrid,
                2,
                "Work Mode",
                internship.getWorkMode()
        );

        addDetailRow(
                internshipGrid,
                3,
                "Stipend",
                internship.getStipend()
        );

        addDetailRow(
                internshipGrid,
                4,
                "Duration",
                internship.getDuration()
        );

        addDetailRow(
                internshipGrid,
                5,
                "Deadline",
                internship.getDeadline() != null
                        ? internship.getDeadline().toString()
                        : "Not specified"
        );

        internshipSection.add(
                internshipGrid,
                BorderLayout.CENTER
        );

        body.add(
                internshipSection,
                bodyGbc
        );


        // =========================================================
        // SKILLS
        // =========================================================

        bodyGbc.gridy++;

        JPanel skillsSection =
                createDetailsSection(
                        "Required Skills"
                );

        JLabel skillsLabel =
                new JLabel(
                        "<html><div style='width:680px;'>"
                                + safeValue(
                                        internship.getRequiredSkills()
                                )
                                + "</div></html>"
                );

        skillsLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        skillsLabel.setForeground(
                TEXT
        );

        skillsSection.add(
                skillsLabel,
                BorderLayout.CENTER
        );

        body.add(
                skillsSection,
                bodyGbc
        );


        // =========================================================
        // RECRUITER & OUTREACH
        // =========================================================

        bodyGbc.gridy++;

        JPanel recruiterSection =
                createDetailsSection(
                        "Recruiter & Outreach"
                );

        JPanel recruiterGrid =
                new JPanel(
                        new GridBagLayout()
                );

        recruiterGrid.setOpaque(false);

        addDetailRow(
                recruiterGrid,
                0,
                "Recruiter",
                internship.getRecruiterName()
        );

        addDetailRow(
                recruiterGrid,
                1,
                "Role",
                internship.getRecruiterRole()
        );


        GridBagConstraints emailGbc =
                new GridBagConstraints();

        emailGbc.gridx = 0;
        emailGbc.gridy = 2;
        emailGbc.weightx = 0.5;
        emailGbc.fill =
                GridBagConstraints.HORIZONTAL;
        emailGbc.anchor =
                GridBagConstraints.NORTHWEST;
        emailGbc.insets =
                new Insets(
                        7,
                        0,
                        7,
                        20
                );

        JLabel emailTitle =
                new JLabel(
                        "Email"
                );

        emailTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        emailTitle.setForeground(
                MUTED
        );

        recruiterGrid.add(
                emailTitle,
                emailGbc
        );


        emailGbc.gridx = 1;
        emailGbc.weightx = 1;
        emailGbc.insets =
                new Insets(
                        7,
                        0,
                        7,
                        0
                );

        recruiterGrid.add(
                createEmailValuePanel(
                        internship
                ),
                emailGbc
        );


        addDetailRow(
                recruiterGrid,
                3,
                "Contact Source",
                internship.getContactSource()
        );

        recruiterSection.add(
                recruiterGrid,
                BorderLayout.CENTER
        );


        String linkedin =
                internship.getRecruiterLinkedin();

        if (
                linkedin != null
                        && !linkedin.isBlank()
        ) {

            final String linkedinUrl =
                    linkedin;

            JButton linkedinButton =
                    createButton(
                            "Open LinkedIn",
                            BLUE
                    );

            linkedinButton.addActionListener(
                    e -> {

                        try {

                            Desktop.getDesktop()
                                    .browse(
                                            new URI(
                                                    linkedinUrl
                                            )
                                    );

                        } catch (Exception ex) {

                            JOptionPane.showMessageDialog(
                                    dialog,
                                    "Unable to open the LinkedIn profile.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
            );

            JPanel linkedinPanel =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.LEFT,
                                    0,
                                    8
                            )
                    );

            linkedinPanel.setOpaque(false);

            linkedinPanel.add(
                    linkedinButton
            );

            recruiterSection.add(
                    linkedinPanel,
                    BorderLayout.SOUTH
            );
        }

        body.add(
                recruiterSection,
                bodyGbc
        );


        // =========================================================
        // BOTTOM SPACER
        // =========================================================

        bodyGbc.gridy++;
        bodyGbc.weighty = 1;
        bodyGbc.fill = GridBagConstraints.BOTH;
        bodyGbc.insets = new Insets(0, 0, 0, 0);

        body.add(
                Box.createGlue(),
                bodyGbc
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        body
                );

        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);


        // =========================================================
        // BOTTOM ACTIONS
        // =========================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                12
                        )
                );

        bottom.setBackground(
                BACKGROUND
        );

        bottom.setBorder(
                new EmptyBorder(
                        0,
                        20,
                        5,
                        20
                )
        );

        JButton closeButton =
                createButton(
                        "Close",
                        new Color(
                                107,
                                114,
                                128
                        )
                );

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        bottom.add(
                closeButton
        );


        dialog.add(
                header,
                BorderLayout.NORTH
        );

        dialog.add(
                scrollPane,
                BorderLayout.CENTER
        );

        dialog.add(
                bottom,
                BorderLayout.SOUTH
        );

        dialog.setVisible(true);
    }


    // =========================================================
    // DETAILS SECTION
    // =========================================================

    private JPanel createDetailsSection(
            String title
    ) {

        JPanel section =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        section.setBackground(
                CARD
        );

        section.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        titleLabel.setForeground(
                TEXT
        );

        section.add(
                titleLabel,
                BorderLayout.NORTH
        );

        return section;
    }


    // =========================================================
    // DETAIL ROW
    // =========================================================

    private void addDetailRow(
            JPanel grid,
            int row,
            String title,
            String value
    ) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridy = row;
        gbc.fill =
                GridBagConstraints.HORIZONTAL;
        gbc.anchor =
                GridBagConstraints.NORTHWEST;
        gbc.insets =
                new Insets(
                        7,
                        0,
                        7,
                        20
                );


        gbc.gridx = 0;
        gbc.weightx = 0.35;

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        titleLabel.setForeground(
                MUTED
        );

        grid.add(
                titleLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets =
                new Insets(
                        7,
                        0,
                        7,
                        0
                );

        JLabel valueLabel =
                new JLabel(
                        "<html><div style='width:360px;'>"
                                + safeValue(value)
                                + "</div></html>"
                );

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        valueLabel.setForeground(
                TEXT
        );

        grid.add(
                valueLabel,
                gbc
        );
    }


    // =========================================================
    // CLICKABLE RECRUITER EMAIL VALUE
    // =========================================================

    private JPanel createEmailValuePanel(
            Internship internship
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        String email =
                internship.getRecruiterEmail();

        if (
                email == null
                        || email.isBlank()
        ) {

            JLabel label =
                    new JLabel(
                            "Not provided"
                    );

            label.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            14
                    )
            );

            label.setForeground(
                    MUTED
            );

            panel.add(
                    label,
                    BorderLayout.WEST
            );

            return panel;
        }


        JLabel emailLabel =
                new JLabel(
                        "<html><u>"
                                + email
                                + "</u></html>"
                );

        emailLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        emailLabel.setForeground(
                BLUE
        );

        emailLabel.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        emailLabel.setToolTipText(
                "Click to compose a cold email"
        );

        emailLabel.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        openColdEmail(
                                internship,
                                email
                        );
                    }

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        emailLabel.setForeground(
                                GREEN
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        emailLabel.setForeground(
                                BLUE
                        );
                    }
                }
        );

        panel.add(
                emailLabel,
                BorderLayout.WEST
        );

        return panel;
    }


    // =========================================================
    // OPEN COLD EMAIL
    // =========================================================

    private void openColdEmail(
            Internship internship,
            String recruiterEmail
    ) {

        String studentName =
                user.getName() == null
                        || user.getName().isBlank()
                        ? "Student"
                        : user.getName();

        String company =
                safeValue(
                        internship.getCompanyName()
                );

        String role =
                safeValue(
                        internship.getJobRole()
                );

        String recruiterName =
                safeValue(
                        internship.getRecruiterName()
                );

        String studentSkills =
                safeValue(
                        user.getSkills()
                );

        String subject =
                "Application for "
                        + role
                        + " at "
                        + company;

        String body =
                "Hi "
                        + recruiterName
                        + ",\n\n"
                        + "My name is "
                        + studentName
                        + ", and I am interested in the "
                        + role
                        + " opportunity at "
                        + company
                        + ".\n\n"
                        + "My relevant skills include "
                        + studentSkills
                        + ".\n\n"
                        + "I would be grateful for the opportunity "
                        + "to be considered for this internship. "
                        + "I have attached my resume for your review.\n\n"
                        + "Thank you for your time and consideration.\n\n"
                        + "Regards,\n"
                        + studentName;

        try {

            String encodedSubject =
                    URLEncoder.encode(
                            subject,
                            StandardCharsets.UTF_8
                    ).replace(
                            "+",
                            "%20"
                    );

            String encodedBody =
                    URLEncoder.encode(
                            body,
                            StandardCharsets.UTF_8
                    ).replace(
                            "+",
                            "%20"
                    );

            URI mailto =
                    new URI(
                            "mailto:"
                                    + recruiterEmail
                                    + "?subject="
                                    + encodedSubject
                                    + "&body="
                                    + encodedBody
                    );

            Desktop.getDesktop()
                    .browse(
                            mailto
                    );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open your email application.",
                    "Email Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // SAFE VALUE
    // =========================================================

    private String safeValue(
            String value
    ) {

        if (
                value == null
                        || value.isBlank()
        ) {

            return "Not provided";
        }

        return value;
    }

    // =========================================================
    // APPLY
    // =========================================================

    private void apply(
        Internship internship
) {

    String resume =
            user.getResumePath();

    // ---------------------------------------------------------
    // CHECK RESUME
    // ---------------------------------------------------------

    if (
            resume == null
                    || resume.isBlank()
    ) {

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "You haven't uploaded a resume yet.\n"
                                + "Open your profile and upload one first?",
                        "Resume Required",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                answer ==
                        JOptionPane.YES_OPTION
        ) {

            goToProfile();
        }

        return;
    }

    // ---------------------------------------------------------
    // CONFIRM OPENING APPLICATION PAGE
    // ---------------------------------------------------------

    int answer =
            JOptionPane.showConfirmDialog(
                    this,
                    "Your resume is ready.\n\n"
                            + "Open the employer's application page?",
                    "Apply",
                    JOptionPane.YES_NO_OPTION
            );

    if (
            answer != JOptionPane.YES_OPTION
    ) {

        return;
    }

    // ---------------------------------------------------------
    // OPEN REAL EMPLOYER PAGE
    // ---------------------------------------------------------

    try {

        Desktop.getDesktop()
                .browse(
                        new URI(
                                internship.getJobLink()
                        )
                );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Unable to open the application link.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    // ---------------------------------------------------------
    // ASK WHETHER USER ACTUALLY APPLIED
    // ---------------------------------------------------------

    int appliedAnswer =
            JOptionPane.showConfirmDialog(
                    this,
                    "Did you submit your application on the employer's website?",
                    "Track Application",
                    JOptionPane.YES_NO_OPTION
            );

    if (
            appliedAnswer != JOptionPane.YES_OPTION
    ) {

        return;
    }

    // ---------------------------------------------------------
    // ADD APPLICATION TO DATABASE
    // ---------------------------------------------------------

    Application application =
            new Application(
                    user.getUserId(),
                    internship.getCompanyName(),
                    internship.getJobRole(),
                    LocalDate.now(),
                    internship.getDeadline(),
                    "Applied",
                    internship.getJobLink(),
                    "Applied through Smart Internship Tracker"
            );

    ApplicationDAO applicationDAO =
            new ApplicationDAO();

    boolean success =
            applicationDAO.addApplication(
                    application
            );

    if (success) {

        JOptionPane.showMessageDialog(
                this,
                "Application added to My Applications!",
                "Application Tracked",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        JOptionPane.showMessageDialog(
                this,
                "You may have applied successfully, "
                        + "but we could not save it to My Applications.",
                "Tracking Failed",
                JOptionPane.WARNING_MESSAGE
        );
    }
}

    // =========================================================
    // RESET
    // =========================================================

    private void resetFilters() {

        searchField.setText("");

        categoryBox.setSelectedIndex(0);

        locationBox.setSelectedIndex(0);

        workModeBox.setSelectedIndex(0);

        loadAllInternships();
    }


    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(text);


        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
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
                        8,
                        14,
                        8,
                        14
                )
        );


        return button;
    }


    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private JButton sidebarButton(
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
                        13
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


        button.addActionListener(
                action
        );


        return button;
    }


    // =========================================================
    // NAVIGATION
    // =========================================================

    private void goToDashboard() {

        dispose();

        new DashboardFrame(user)
                .setVisible(true);
    }


    private void goToProfile() {

        dispose();

        new ProfileFrame(user)
                .setVisible(true);
    }


    private void goToApplications() {

        dispose();

        new ApplicationsFrame(user)
                .setVisible(true);
    }


    private void goToAddApplication() {

        dispose();

        new AddApplicationFrame(user)
                .setVisible(true);
    }


    private void goToAnalytics() {

        dispose();

        new AnalyticsFrame(user)
                .setVisible(true);
    }


    private void logout() {

        dispose();

        new LoginFrame()
                .setVisible(true);
    }
}