package gui;

import api.GreenhouseSource;
import api.LeverSource;
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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    private JComboBox<String> sourceBox;
    private JComboBox<String> sortBox;
    private JCheckBox skillsOnlyCheckBox;

    private JLabel resultCountLabel;
    private JButton refreshButton;

    private Timer autoRefreshTimer;

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

        // Refresh real opportunities immediately when the page opens.
        refreshAllSources();
        startAutoRefresh();
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
                                "All Categories"
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
                                "All Locations"
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
                                "All Work Modes"
                        }
                );


        gbc.gridx = 3;
        gbc.weightx = 1;


        filterContainer.add(
                workModeBox,
                gbc
        );


        // -----------------------------------------------------
        // Source
        // -----------------------------------------------------

        JLabel sourceLabel =
                new JLabel(
                        "Source"
                );

        sourceLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        filterContainer.add(
                sourceLabel,
                gbc
        );


        sourceBox =
                new JComboBox<>(
                        new String[]{
                                "All Sources"
                        }
                );

        gbc.gridx = 1;
        gbc.weightx = 1;

        filterContainer.add(
                sourceBox,
                gbc
        );


        // -----------------------------------------------------
        // Sort
        // -----------------------------------------------------

        JLabel sortLabel =
                new JLabel(
                        "Sort By"
                );

        sortLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0;

        filterContainer.add(
                sortLabel,
                gbc
        );

        sortBox =
                new JComboBox<>(
                        new String[]{
                                "Latest First",
                                "Deadline Soonest",
                                "Skill Match Highest"
                        }
                );

        gbc.gridx = 3;
        gbc.weightx = 1;

        filterContainer.add(
                sortBox,
                gbc
        );


        // -----------------------------------------------------
        // Skill Match Toggle
        // -----------------------------------------------------

        skillsOnlyCheckBox =
                new JCheckBox(
                        "Only show internships matching my skills"
                );

        skillsOnlyCheckBox.setOpaque(false);
        skillsOnlyCheckBox.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );
        skillsOnlyCheckBox.setForeground(TEXT);
        skillsOnlyCheckBox.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weightx = 1;

        filterContainer.add(
                skillsOnlyCheckBox,
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


        refreshButton =
                createButton(
                        "Refresh Opportunities",
                        GREEN
                );


        gbc.gridwidth = 1;
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 4;
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

        filterButtons.add(
                Box.createVerticalStrut(8)
        );

        filterButtons.add(
                refreshButton
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


        refreshButton.addActionListener(
                e -> refreshAllSources()
        );


        searchField.addActionListener(
                e -> applyFilters()
        );


        sortBox.addActionListener(
                e -> applyFilters()
        );


        skillsOnlyCheckBox.addActionListener(
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


        updateFilterOptions(
                internships
        );


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

        String source =
                String.valueOf(
                        sourceBox
                                .getSelectedItem()
                );

        String sort =
                String.valueOf(
                        sortBox
                                .getSelectedItem()
                );

        boolean skillsOnly =
                skillsOnlyCheckBox.isSelected();

        List<Internship> all =
                internshipDAO.getAllInternships();

        List<Internship> filtered =
                new ArrayList<>();

        for (Internship internship : all) {

            boolean matches = true;

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
                                        + " "
                                        + internship.getSourceName()
                                        + " "
                                        + internship.getRecruiterName()
                                        + " "
                                        + internship.getRecruiterRole()
                                        + " "
                                        + internship.getRecruiterEmail()
                                        + " "
                                        + internship.getRecruiterLinkedin()
                                        + " "
                                        + internship.getContactSource()
                        )
                                .toLowerCase();

                if (!searchable.contains(keyword)) {
                    matches = false;
                }
            }

            // -------------------------------------------------
            // CATEGORY
            // -------------------------------------------------

            if (
                    !category.equals("All Categories")
                    && !safeFilterValue(internship.getCategory())
                            .equalsIgnoreCase(category)
            ) {
                matches = false;
            }

            // -------------------------------------------------
            // LOCATION
            // -------------------------------------------------

            if (
                    !location.equals("All Locations")
                    && !safeFilterValue(internship.getLocation())
                            .equalsIgnoreCase(location)
            ) {
                matches = false;
            }

            // -------------------------------------------------
            // WORK MODE
            // -------------------------------------------------

            if (
                    !workMode.equals("All Work Modes")
                    && !safeFilterValue(internship.getWorkMode())
                            .equalsIgnoreCase(workMode)
            ) {
                matches = false;
            }

            // -------------------------------------------------
            // SOURCE
            // -------------------------------------------------

            if (
                    !source.equals("All Sources")
                    && !safeFilterValue(internship.getSourceName())
                            .equalsIgnoreCase(source)
            ) {
                matches = false;
            }

            // -------------------------------------------------
            // SKILL MATCH
            // -------------------------------------------------

            if (skillsOnly) {

                SkillMatchResult matchResult =
                        SkillMatcher.calculateMatch(
                                user.getSkills(),
                                internship.getRequiredSkills()
                        );

                if (matchResult.getMatchPercentage() <= 0) {
                    matches = false;
                }
            }

            if (matches) {
                filtered.add(internship);
            }
        }

        // -----------------------------------------------------
        // SORT RESULTS
        // -----------------------------------------------------

        Comparator<Internship> comparator;

        if ("Deadline Soonest".equals(sort)) {

            comparator =
                    Comparator.comparing(
                            Internship::getDeadline,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    );

        } else if ("Skill Match Highest".equals(sort)) {

            comparator = (first, second) -> {

                int firstMatch =
                        SkillMatcher.calculateMatch(
                                user.getSkills(),
                                first.getRequiredSkills()
                        ).getMatchPercentage();

                int secondMatch =
                        SkillMatcher.calculateMatch(
                                user.getSkills(),
                                second.getRequiredSkills()
                        ).getMatchPercentage();

                int matchComparison =
                        Integer.compare(
                                secondMatch,
                                firstMatch
                        );

                if (matchComparison != 0) {
                    return matchComparison;
                }

                return comparePostedDateDesc(first, second);
            };

        } else {

            comparator =
                    this::comparePostedDateDesc;
        }

        filtered.sort(comparator);

        displayInternships(filtered);
    }


    private int comparePostedDateDesc(
            Internship first,
            Internship second
    ) {

        LocalDate firstDate = first.getPostedDate();
        LocalDate secondDate = second.getPostedDate();

        if (firstDate == null && secondDate == null) {
            return Integer.compare(
                    second.getInternshipId(),
                    first.getInternshipId()
            );
        }

        if (firstDate == null) {
            return 1;
        }

        if (secondDate == null) {
            return -1;
        }

        int result = secondDate.compareTo(firstDate);

        if (result != 0) {
            return result;
        }

        return Integer.compare(
                second.getInternshipId(),
                first.getInternshipId()
        );
    }


    private String safeFilterValue(String value) {

        return value == null ? "" : value;
    }


    // =========================================================
    // UPDATE FILTER OPTIONS FROM REAL DATABASE DATA
    // =========================================================

    private void updateFilterOptions(
            List<Internship> internships
    ) {

        String selectedCategory =
                String.valueOf(
                        categoryBox
                                .getSelectedItem()
                );

        String selectedLocation =
                String.valueOf(
                        locationBox
                                .getSelectedItem()
                );

        String selectedWorkMode =
                String.valueOf(
                        workModeBox
                                .getSelectedItem()
                );

        String selectedSource =
                String.valueOf(
                        sourceBox
                                .getSelectedItem()
                );


        Set<String> categories =
                new TreeSet<>(
                        String.CASE_INSENSITIVE_ORDER
                );

        Set<String> locations =
                new TreeSet<>(
                        String.CASE_INSENSITIVE_ORDER
                );

        Set<String> workModes =
                new TreeSet<>(
                        String.CASE_INSENSITIVE_ORDER
                );

        Set<String> sources =
                new TreeSet<>(
                        String.CASE_INSENSITIVE_ORDER
                );


        for (Internship internship : internships) {

            addFilterValue(
                    categories,
                    internship.getCategory()
            );

            addFilterValue(
                    locations,
                    internship.getLocation()
            );

            addFilterValue(
                    workModes,
                    internship.getWorkMode()
            );

            addFilterValue(
                    sources,
                    internship.getSourceName()
            );
        }


        categoryBox.removeAllItems();
        categoryBox.addItem(
                "All Categories"
        );

        for (String value : categories) {
            categoryBox.addItem(value);
        }


        locationBox.removeAllItems();
        locationBox.addItem(
                "All Locations"
        );

        for (String value : locations) {
            locationBox.addItem(value);
        }


        workModeBox.removeAllItems();
        workModeBox.addItem(
                "All Work Modes"
        );

        for (String value : workModes) {
            workModeBox.addItem(value);
        }


        sourceBox.removeAllItems();
        sourceBox.addItem(
                "All Sources"
        );

        for (String value : sources) {
            sourceBox.addItem(value);
        }


        restoreSelection(
                categoryBox,
                selectedCategory
        );

        restoreSelection(
                locationBox,
                selectedLocation
        );

        restoreSelection(
                workModeBox,
                selectedWorkMode
        );

        restoreSelection(
                sourceBox,
                selectedSource
        );
    }


    private void addFilterValue(
            Set<String> values,
            String value
    ) {

        if (
                value != null
                && !value.isBlank()
                && !value.equalsIgnoreCase(
                        "Not specified"
                )
        ) {

            values.add(
                    value.trim()
            );
        }
    }


    private void restoreSelection(
            JComboBox<String> box,
            String selected
    ) {

        if (
                selected == null
                || selected.isBlank()
        ) {

            return;
        }

        for (int i = 0; i < box.getItemCount(); i++) {

            if (
                    box.getItemAt(i)
                            .equalsIgnoreCase(
                                    selected
                            )
            ) {

                box.setSelectedIndex(i);

                return;
            }
        }
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

        JLabel sourceLabel =
                new JLabel(
                        "Source: "
                                + internship.getSourceName()
                );

        sourceLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        sourceLabel.setForeground(
                BLUE
        );

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(
                sourceLabel
        );

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


        JButton applyButton;

        boolean alreadyApplied =
                hasAlreadyApplied(
                        internship
                );

        if (alreadyApplied) {

            applyButton =
                    createButton(
                            "Already Applied",
                            new Color(107, 114, 128)
                    );

            applyButton.setEnabled(false);

        } else {

            applyButton =
                    createButton(
                            "Apply",
                            GREEN
                    );
        }


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


        if (!alreadyApplied) {

            applyButton.addActionListener(
                    e -> apply(
                            internship
                    )
            );
        }


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

        addDetailRow(
                internshipGrid,
                6,
                "Source",
                internship.getSourceName()
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

    private boolean hasAlreadyApplied(
            Internship internship
    ) {

        if (
                internship.getJobLink() == null
                        || internship.getJobLink().isBlank()
        ) {

            return false;
        }

        try {

            ApplicationDAO applicationDAO =
                    new ApplicationDAO();

            List<Application> applications =
                    applicationDAO.getApplicationsByUser(
                            user.getUserId()
                    );

            for (Application application : applications) {

                if (
                        application.getJobLink() != null
                                && application.getJobLink().equalsIgnoreCase(
                                        internship.getJobLink()
                                )
                ) {

                    return true;
                }
            }

        } catch (Exception ex) {

            System.out.println(
                    "Could not check application status: "
                            + ex.getMessage()
            );
        }

        return false;
    }


    // =========================================================
    // APPLY
    // =========================================================

    private void apply(
            Internship internship
    ) {

        if (hasAlreadyApplied(internship)) {

            JOptionPane.showMessageDialog(
                    this,
                    "You have already tracked this application.",
                    "Already Applied",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

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
        // CHECK JOB LINK
        // ---------------------------------------------------------

        String jobLink =
                internship.getJobLink();

        if (
                jobLink == null
                        || jobLink.isBlank()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "This internship does not have a valid application link.",
                    "Application Link Missing",
                    JOptionPane.WARNING_MESSAGE
            );

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
                                    jobLink
                            )
                    );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open the application link.\n\n"
                            + "Please check the internship link and try again.",
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
                        "IMPORTANT:\n\n"
                                + "Did the employer's application page load successfully\n"
                                + "AND did you submit your application?\n\n"
                                + "Choose NO if:\n"
                                + "• the page showed a network/error message\n"
                                + "• the page did not load\n"
                                + "• you did not submit the application",
                        "Confirm Application Submission",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                appliedAnswer != JOptionPane.YES_OPTION
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Application was NOT added to My Applications.",
                    "Not Tracked",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // ---------------------------------------------------------
        // CREATE APPLICATION
        // ---------------------------------------------------------

        Application application =
                new Application(
                        user.getUserId(),
                        internship.getCompanyName(),
                        internship.getJobRole(),
                        LocalDate.now(),
                        internship.getDeadline(),
                        "Applied",
                        jobLink,
                        "Applied through Smart Internship Tracker"
                );

        ApplicationDAO applicationDAO =
                new ApplicationDAO();

        boolean success =
                applicationDAO.addApplication(
                        application
                );

        // ---------------------------------------------------------
        // RESULT
        // ---------------------------------------------------------

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
                    "This application may already be tracked,\n"
                            + "or we could not save it.",
                    "Not Added",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================================================
    // REFRESH REAL INTERNSHIP SOURCES
    // =========================================================

    private void refreshAllSources() {

        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::refreshAllSources);
            return;
        }

        refreshButton.setEnabled(false);

        resultCountLabel.setText(
                "Checking live internship opportunities..."
        );

        SwingWorker<String, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected String doInBackground() {

                        InternshipDAO dao =
                                new InternshipDAO();

                        int greenhouseCount = 0;
                        int leverCount = 0;

                        StringBuilder status =
                                new StringBuilder();

                        // -------------------------------------------------
                        // GREENHOUSE
                        // -------------------------------------------------

                        try {

                            GreenhouseSource greenhouseSource =
                                    new GreenhouseSource();

                            List<Internship> liveGreenhouse =
                                    greenhouseSource.fetchAllInternships();

                            greenhouseCount =
                                    dao.syncSource(
                                            "Greenhouse",
                                            liveGreenhouse
                                    );

                            status.append(
                                    "Greenhouse: "
                                            + greenhouseCount
                                            + " live listings"
                            );

                        } catch (Exception e) {

                            status.append(
                                    "Greenhouse refresh failed"
                            );

                            e.printStackTrace();
                        }

                        status.append(" | ");

                        // -------------------------------------------------
                        // LEVER
                        // -------------------------------------------------

                        try {

                            LeverSource leverSource =
                                    new LeverSource();

                            List<Internship> liveLever =
                                    leverSource.fetchInternships();

                            leverCount =
                                    dao.syncSource(
                                            "Lever",
                                            liveLever
                                    );

                            status.append(
                                    "Lever: "
                                            + leverCount
                                            + " live listings"
                            );

                        } catch (Exception e) {

                            status.append(
                                    "Lever refresh failed"
                            );

                            e.printStackTrace();
                        }

                        return status.toString();
                    }

                    @Override
                    protected void done() {

                        refreshButton.setEnabled(true);

                        loadAllInternships();

                        try {
                            resultCountLabel.setToolTipText(
                                    get()
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

        worker.execute();
    }


    // =========================================================
    // AUTO REFRESH
    // =========================================================

    private void startAutoRefresh() {

        if (autoRefreshTimer != null) {
            autoRefreshTimer.stop();
        }

        autoRefreshTimer =
                new Timer(
                        15 * 60 * 1000,
                        e -> refreshAllSources()
                );

        autoRefreshTimer.setRepeats(true);
        autoRefreshTimer.start();
    }


    private void stopAutoRefresh() {

        if (autoRefreshTimer != null) {
            autoRefreshTimer.stop();
            autoRefreshTimer = null;
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

        sourceBox.setSelectedIndex(0);

        sortBox.setSelectedIndex(0);

        skillsOnlyCheckBox.setSelected(false);

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

        stopAutoRefresh();

        setVisible(false);

        new DashboardFrame(user)
                .setVisible(true);

        dispose();
    }


    private void goToProfile() {

        stopAutoRefresh();

        setVisible(false);

        new ProfileFrame(user)
                .setVisible(true);

        dispose();
    }


    private void goToApplications() {

        stopAutoRefresh();

        setVisible(false);

        new ApplicationsFrame(user)
                .setVisible(true);

        dispose();
    }


    private void goToAddApplication() {

        stopAutoRefresh();

        setVisible(false);

        new AddApplicationFrame(user)
                .setVisible(true);

        dispose();
    }


    private void goToAnalytics() {

        stopAutoRefresh();

        setVisible(false);

        new AnalyticsFrame(user)
                .setVisible(true);

        dispose();
    }


    private void logout() {

        stopAutoRefresh();

        setVisible(false);

        new LoginFrame()
                .setVisible(true);

        dispose();
    }
}