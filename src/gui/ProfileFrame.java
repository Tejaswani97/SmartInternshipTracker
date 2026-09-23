package gui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ProfileFrame extends JFrame {

    private final User user;

    private static final Color SIDEBAR =
            new Color(31, 41, 55);

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

    private static final Color BORDER =
            new Color(229, 231, 235);

    private static final Color BLUE_LIGHT =
            new Color(239, 246, 255);

    private static final Color GREEN_LIGHT =
            new Color(240, 253, 244);

    private static final Color CHIP_TEXT =
            new Color(30, 64, 175);

    private static final Color SHADOW =
            new Color(15, 23, 42, 18);


    private JLabel avatarLabel;

    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel locationLabel;

    private JLabel resumeLabel;

    private JTextArea bioArea;

    private JPanel skillsPanel;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProfileFrame(User user) {

        this.user = user;

        setTitle(
                "Smart Internship Tracker - My Profile"
        );


        setSize(
                1280,
                820
        );

        setMinimumSize(
                new Dimension(
                        1100,
                        700
                )
        );

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        setLocationRelativeTo(null);


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
    }


    // =========================================================
    // BUILD
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
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setBackground(
                SIDEBAR
        );


        sidebar.setPreferredSize(
                new Dimension(
                        235,
                        820
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
                        "Smart Internship"
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


        top.add(
                logo
        );


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
                        "Internship Opportunities",
                        false,
                        e -> goToInternships()
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
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout(0, 18)
                );

        content.setBackground(BACKGROUND);

        content.setBorder(
                new EmptyBorder(
                        28,
                        30,
                        28,
                        30
                )
        );

        // =========================================================
        // HEADER
        // =========================================================

        JPanel header =
                new JPanel(
                        new BorderLayout(20, 0)
                );

        header.setOpaque(false);

        JPanel titleBlock =
                new JPanel();

        titleBlock.setOpaque(false);
        titleBlock.setLayout(
                new BoxLayout(
                        titleBlock,
                        BoxLayout.Y_AXIS
                )
        );

        titleBlock.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title =
                new JLabel(
                        "My Profile"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle =
                new JLabel(
                        "Build a recruiter-ready profile that represents your skills and experience."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(5));
        titleBlock.add(subtitle);

        header.add(
                titleBlock,
                BorderLayout.CENTER
        );

        JPanel strengthWrap =
                new JPanel(
                        new BorderLayout()
                );

        strengthWrap.setOpaque(false);
        strengthWrap.setPreferredSize(
                new Dimension(
                        280,
                        52
                )
        );
        strengthWrap.setMinimumSize(
                new Dimension(
                        280,
                        52
                )
        );
        strengthWrap.setMaximumSize(
                new Dimension(
                        280,
                        52
                )
        );

        strengthWrap.add(
                createProfileStrengthCard(),
                BorderLayout.CENTER
        );

        header.add(
                strengthWrap,
                BorderLayout.EAST
        );

        content.add(
                header,
                BorderLayout.NORTH
        );

        // =========================================================
        // SCROLLABLE PROFILE BODY
        // =========================================================

        JPanel contentStack =
                new JPanel();

        contentStack.setOpaque(false);
        contentStack.setLayout(
                new BoxLayout(
                        contentStack,
                        BoxLayout.Y_AXIS
                )
        );

        // =========================================================
        // PROFILE HERO
        // =========================================================

        JPanel heroCard =
                createCard();

        heroCard.setLayout(
                new BorderLayout(
                        24,
                        0
                )
        );

        heroCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(
                                22,
                                24,
                                22,
                                24
                        )
                )
        );

        heroCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        220
                )
        );

        // ---------------------------------------------------------
        // AVATAR COLUMN
        // ---------------------------------------------------------

        JPanel avatarWrap =
                new JPanel(
                        new BorderLayout(0, 8)
                );

        avatarWrap.setOpaque(false);
        avatarWrap.setPreferredSize(
                new Dimension(
                        170,
                        174
                )
        );
        avatarWrap.setMinimumSize(
                new Dimension(
                        170,
                        174
                )
        );
        avatarWrap.setMaximumSize(
                new Dimension(
                        170,
                        174
                )
        );

        avatarLabel = createAvatar();

        JPanel avatarCenter =
                new JPanel(
                        new GridBagLayout()
                );

        avatarCenter.setOpaque(false);

        avatarCenter.add(
                avatarLabel
        );

        avatarWrap.add(
                avatarCenter,
                BorderLayout.CENTER
        );

        JButton changePhotoButton =
                createOutlineButton(
                        "Change photo",
                        BLUE
                );

        changePhotoButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        changePhotoButton.setPreferredSize(
                new Dimension(
                        126,
                        31
                )
        );

        changePhotoButton.addActionListener(
                e -> showPhotoMenu()
        );

        JPanel photoButtonWrap =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                0,
                                0
                        )
                );

        photoButtonWrap.setOpaque(false);
        photoButtonWrap.add(changePhotoButton);

        avatarWrap.add(
                photoButtonWrap,
                BorderLayout.SOUTH
        );

        heroCard.add(
                avatarWrap,
                BorderLayout.WEST
        );

        // ---------------------------------------------------------
        // IDENTITY COLUMN
        // ---------------------------------------------------------

        JPanel identityText =
                new JPanel();

        identityText.setOpaque(false);
        identityText.setLayout(
                new BoxLayout(
                        identityText,
                        BoxLayout.Y_AXIS
                )
        );

        identityText.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameLabel =
                new JLabel(
                        safe(user.getName())
                );

        nameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        nameLabel.setForeground(TEXT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailLabel =
                new JLabel(
                        safe(user.getEmail())
                );

        emailLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        emailLabel.setForeground(MUTED);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleHint =
                new JLabel(
                        "STUDENT / JOB SEEKER"
                );

        roleHint.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        roleHint.setForeground(BLUE);
        roleHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        locationLabel =
                new JLabel(
                        displayLocation()
                );

        locationLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        locationLabel.setForeground(MUTED);

        JPanel locationRow =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        locationRow.setOpaque(false);
        locationRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel locationDot =
                new JLabel(
                        "●"
                );

        locationDot.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        8
                )
        );

        locationDot.setForeground(BLUE);

        locationRow.add(locationDot);
        locationRow.add(
                Box.createHorizontalStrut(7)
        );
        locationRow.add(locationLabel);

        JLabel profileHint =
                new JLabel(
                        "<html><div style='width:360px;'>"
                                + "Keep your profile specific, concise, and focused on skills recruiters search for."
                                + "</div></html>"
                );

        profileHint.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        profileHint.setForeground(MUTED);
        profileHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        identityText.add(nameLabel);
        identityText.add(Box.createVerticalStrut(5));
        identityText.add(emailLabel);
        identityText.add(Box.createVerticalStrut(13));
        identityText.add(roleHint);
        identityText.add(Box.createVerticalStrut(8));
        identityText.add(locationRow);
        identityText.add(Box.createVerticalStrut(12));
        identityText.add(profileHint);

        heroCard.add(
                identityText,
                BorderLayout.CENTER
        );

        // ---------------------------------------------------------
        // SNAPSHOT COLUMN
        // ---------------------------------------------------------

        JPanel snapshotSide =
                new JPanel();

        snapshotSide.setOpaque(false);
        snapshotSide.setPreferredSize(
                new Dimension(
                        230,
                        174
                )
        );
        snapshotSide.setMinimumSize(
                new Dimension(
                        230,
                        174
                )
        );
        snapshotSide.setMaximumSize(
                new Dimension(
                        230,
                        174
                )
        );

        snapshotSide.setLayout(
                new BoxLayout(
                        snapshotSide,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel snapshotTitle =
                createSectionTitle(
                        "Career Snapshot"
                );

        snapshotTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        snapshotSide.add(snapshotTitle);
        snapshotSide.add(Box.createVerticalStrut(12));

        JPanel locationSnapshot =
                createSnapshotRow(
                        "Location",
                        displayLocation()
                );

        locationSnapshot.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        snapshotSide.add(locationSnapshot);
        snapshotSide.add(Box.createVerticalStrut(9));

        JPanel resumeSnapshot =
                createSnapshotRow(
                        "Resume",
                        resumeStatusText()
                );

        resumeSnapshot.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        snapshotSide.add(resumeSnapshot);
        snapshotSide.add(Box.createVerticalGlue());

        JButton editButton =
                createButton(
                        "Edit Profile",
                        BLUE
                );

        editButton.setPreferredSize(
                new Dimension(
                        170,
                        40
                )
        );

        editButton.setMinimumSize(
                new Dimension(
                        170,
                        40
                )
        );

        editButton.setMaximumSize(
                new Dimension(
                        170,
                        40
                )
        );

        editButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        editButton.addActionListener(
                e -> editProfile()
        );

        snapshotSide.add(editButton);

        heroCard.add(
                snapshotSide,
                BorderLayout.EAST
        );

        contentStack.add(heroCard);
        contentStack.add(Box.createVerticalStrut(18));

        // =========================================================
        // LOWER CONTENT
        // =========================================================

        JPanel lower =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                18,
                                0
                        )
                );

        lower.setOpaque(false);
        lower.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftColumn =
                new JPanel();

        leftColumn.setOpaque(false);
        leftColumn.setLayout(
                new BoxLayout(
                        leftColumn,
                        BoxLayout.Y_AXIS
                )
        );

        JPanel aboutCard = createAboutCard();
        aboutCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        aboutCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        180
                )
        );

        JPanel skillsCard = createSkillsCard();
        skillsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        skillsCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        160
                )
        );

        leftColumn.add(aboutCard);
        leftColumn.add(Box.createVerticalStrut(16));
        leftColumn.add(skillsCard);

        JPanel rightColumn =
                new JPanel(
                        new BorderLayout()
                );

        rightColumn.setOpaque(false);

        JPanel resumeCard = createResumeCard();
        resumeCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightColumn.add(
                resumeCard,
                BorderLayout.NORTH
        );

        lower.add(leftColumn);
        lower.add(rightColumn);

        contentStack.add(lower);

        JScrollPane scrollPane =
                new JScrollPane(
                        contentStack
                );

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        content.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return content;
    }

    private JPanel createProfileStrengthCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        card.setBackground(BLUE_LIGHT);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(191, 219, 254)
                        ),
                        new EmptyBorder(10, 14, 10, 14)
                )
        );

        JLabel label =
                new JLabel(
                        "Profile strength"
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(CHIP_TEXT);

        JProgressBar progress =
                new JProgressBar(0, 100);

        int value = calculateProfileStrength();
        progress.setValue(value);
        progress.setPreferredSize(
                new Dimension(110, 8)
        );
        progress.setForeground(BLUE);
        progress.setBackground(Color.WHITE);
        progress.setBorderPainted(false);

        JLabel valueLabel =
                new JLabel(value + "%");

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        valueLabel.setForeground(TEXT);

        JPanel meter =
                new JPanel(
                        new BorderLayout(8, 0)
                );

        meter.setOpaque(false);
        meter.add(label, BorderLayout.WEST);
        meter.add(progress, BorderLayout.CENTER);
        meter.add(valueLabel, BorderLayout.EAST);

        card.add(
                meter,
                BorderLayout.CENTER
        );

        return card;
    }


    private int calculateProfileStrength() {

        int score = 0;

        if (user.getName() != null && !user.getName().isBlank()) score += 15;
        if (user.getEmail() != null && !user.getEmail().isBlank()) score += 15;
        if (user.getLocation() != null && !user.getLocation().isBlank()) score += 10;
        if (user.getBio() != null && !user.getBio().isBlank()) score += 20;
        if (user.getSkills() != null && !user.getSkills().isBlank()) score += 20;
        if (user.getResumePath() != null && !user.getResumePath().isBlank()) score += 20;

        return score;
    }


    private JPanel createAboutCard() {

        JPanel card = createCard();

        card.setLayout(
                new BorderLayout(0, 12)
        );

        card.add(
                createSectionTitle("About Me"),
                BorderLayout.NORTH
        );

        bioArea =
                new JTextArea(
                        safe(user.getBio())
                );

        bioArea.setEditable(false);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );
        bioArea.setForeground(TEXT);
        bioArea.setBackground(CARD);
        bioArea.setBorder(new EmptyBorder(0, 0, 0, 0));

        JScrollPane bioScroll =
                new JScrollPane(bioArea);

        bioScroll.setBorder(null);
        bioScroll.setOpaque(false);
        bioScroll.getViewport().setOpaque(false);
        bioScroll.setPreferredSize(
                new Dimension(0, 130)
        );

        card.add(
                bioScroll,
                BorderLayout.CENTER
        );

        return card;
    }


    private JPanel createSkillsCard() {

        JPanel card = createCard();

        card.setLayout(
                new BorderLayout(0, 12)
        );

        card.add(
                createSectionTitle("Skills & Technologies"),
                BorderLayout.NORTH
        );

        skillsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                6
                        )
                );

        skillsPanel.setOpaque(false);
        updateSkillsPanel();

        JScrollPane skillsScroll =
                new JScrollPane(skillsPanel);

        skillsScroll.setBorder(null);
        skillsScroll.setOpaque(false);
        skillsScroll.getViewport().setOpaque(false);
        skillsScroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
        );
        skillsScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        skillsScroll.setPreferredSize(
                new Dimension(0, 118)
        );

        card.add(
                skillsScroll,
                BorderLayout.CENTER
        );

        return card;
    }


    private JPanel createResumeCard() {

        JPanel card = createCard();

        card.setLayout(
                new BorderLayout(18, 0)
        );

        JPanel resumeInfo =
                new JPanel(
                        new BorderLayout(12, 0)
                );

        resumeInfo.setOpaque(false);

        JLabel icon =
                new JLabel("PDF", SwingConstants.CENTER);

        icon.setPreferredSize(
                new Dimension(52, 52)
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        icon.setForeground(GREEN);
        icon.setOpaque(true);
        icon.setBackground(GREEN_LIGHT);
        icon.setBorder(
                BorderFactory.createLineBorder(
                        new Color(187, 247, 208)
                )
        );

        resumeInfo.add(
                icon,
                BorderLayout.WEST
        );

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("Resume");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        title.setForeground(TEXT);

        resumeLabel =
                new JLabel(resumeText());

        resumeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        resumeLabel.setForeground(MUTED);

        text.add(title);
        text.add(Box.createVerticalStrut(4));
        text.add(resumeLabel);

        resumeInfo.add(
                text,
                BorderLayout.CENTER
        );

        card.add(
                resumeInfo,
                BorderLayout.CENTER
        );

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        actions.setOpaque(false);

        JButton uploadButton =
                createOutlineButton(
                        "Upload / Replace",
                        GREEN
                );

        uploadButton.addActionListener(
                e -> uploadResume()
        );

        JButton viewResume =
                createOutlineButton(
                        "Open Resume",
                        BLUE
                );

        viewResume.addActionListener(
                e -> openResume()
        );

        actions.add(uploadButton);
        actions.add(viewResume);

        card.add(
                actions,
                BorderLayout.EAST
        );

        return card;
    }


    private JPanel createSnapshotRow(
            String title,
            String value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(8, 0)
                );

        row.setOpaque(false);

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        titleLabel.setForeground(MUTED);

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        valueLabel.setForeground(TEXT);

        row.add(titleLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);

        return row;
    }


    private JPanel createCard() {

        return new RoundedPanel(
                CARD,
                BORDER,
                18
        );
    }


    private static class RoundedPanel extends JPanel {

        private final Color backgroundColor;
        private final Color borderColor;
        private final int radius;

        private RoundedPanel(
                Color backgroundColor,
                Color borderColor,
                int radius
        ) {

            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {

            Graphics2D g2 =
                    (Graphics2D) graphics.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(SHADOW);
            g2.fillRoundRect(
                    3,
                    3,
                    getWidth() - 4,
                    getHeight() - 4,
                    radius,
                    radius
            );

            g2.setColor(backgroundColor);
            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 4,
                    getHeight() - 4,
                    radius,
                    radius
            );

            g2.setColor(borderColor);
            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 4,
                    getHeight() - 4,
                    radius,
                    radius
            );

            g2.dispose();
            super.paintComponent(graphics);
        }
    }


    // =========================================================
    // AVATAR
    // =========================================================

    private JLabel createAvatar() {

        AvatarLabel label =
                new AvatarLabel();

        label.setPreferredSize(new Dimension(148, 148));
        label.setMinimumSize(new Dimension(148, 148));
        label.setMaximumSize(new Dimension(148, 148));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setToolTipText(
                "Click to change your profile photo"
        );

        label.setInitials(getInitials(user.getName()));

        label.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {
                        showPhotoMenu();
                    }
                }
        );

        loadSavedPhoto(label);
        return label;
    }


    private void showPhotoMenu() {

        String[] options = {
                "Choose from Gallery",
                "Take Photo",
                "Remove Photo",
                "Cancel"
        };

        int choice =
                JOptionPane.showOptionDialog(
                        this,
                        "Update your profile photo",
                        "Profile Photo",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (choice == 0) {
            chooseProfilePhoto();
        } else if (choice == 1) {
            openCameraAndChooseSavedPhoto();
        } else if (choice == 2) {
            removeProfilePhoto();
        }
    }


    private void chooseProfilePhoto() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle(
                "Choose Profile Photo"
        );

        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Image files",
                        "jpg", "jpeg", "png", "gif", "bmp"
                )
        );

        if (chooser.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        saveProfilePhoto(
                chooser.getSelectedFile()
        );
    }


    private void openCameraAndChooseSavedPhoto() {

        try {

            if (System.getProperty("os.name")
                    .toLowerCase()
                    .contains("win")) {

                new ProcessBuilder(
                        "cmd",
                        "/c",
                        "start",
                        "",
                        "microsoft.windows.camera:"
                ).start();

            } else {

                Desktop.getDesktop()
                        .browse(
                                new URI("ms-camera:")
                        );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Camera opened.\n\n"
                            + "Take your photo and save it.\n"
                            + "Then select that saved image.",
                    "Take Photo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            chooseProfilePhoto();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not open the camera app.\n\n"
                            + "You can still choose a photo from Gallery.",
                    "Camera Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    private void saveProfilePhoto(File source) {

        try {

            BufferedImage image = ImageIO.read(source);

            if (image == null) {
                throw new IllegalArgumentException(
                        "Unsupported image format"
                );
            }

            Path directory =
                    Path.of(
                            System.getProperty("user.home"),
                            "SmartInternshipTracker",
                            "profile_photos"
                    );

            Files.createDirectories(directory);

            Path destination =
                    directory.resolve(
                            "avatar_"
                                    + profilePhotoKey()
                                    + ".png"
                    );

            BufferedImage normalized =
                    normalizeAvatarImage(image);

            ImageIO.write(
                    normalized,
                    "png",
                    destination.toFile()
            );

            ((AvatarLabel) avatarLabel)
                    .setAvatarImage(normalized);

            avatarLabel.repaint();

            JOptionPane.showMessageDialog(
                    this,
                    "Profile photo updated successfully!",
                    "Photo Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to use that image. Please choose another photo.",
                    "Photo Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private BufferedImage normalizeAvatarImage(
            BufferedImage source
    ) {

        int size = Math.min(
                source.getWidth(),
                source.getHeight()
        );

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        BufferedImage square =
                source.getSubimage(x, y, size, size);

        BufferedImage output =
                new BufferedImage(
                        500,
                        500,
                        BufferedImage.TYPE_INT_ARGB
                );

        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        g2.drawImage(
                square,
                0, 0, 500, 500, null
        );

        g2.dispose();
        return output;
    }


    private void loadSavedPhoto(JLabel label) {

        Path photo = savedPhotoPath();

        if (!Files.exists(photo)) {
            label.setText(
                    getInitials(user.getName())
            );
            return;
        }

        try {

            BufferedImage image =
                    ImageIO.read(photo.toFile());

            ((AvatarLabel) label)
                    .setAvatarImage(image);

        } catch (Exception ignored) {

            label.setText(
                    getInitials(user.getName())
            );
        }
    }


    private void removeProfilePhoto() {

        try {

            Files.deleteIfExists(savedPhotoPath());

            AvatarLabel avatar =
                    (AvatarLabel) avatarLabel;

            avatar.setAvatarImage(null);
            avatar.setInitials(
                    getInitials(user.getName())
            );
            avatar.repaint();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to remove the profile photo.",
                    "Photo Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private Path savedPhotoPath() {

        return Path.of(
                System.getProperty("user.home"),
                "SmartInternshipTracker",
                "profile_photos",
                "avatar_"
                        + profilePhotoKey()
                        + ".png"
        );
    }


    private String profilePhotoKey() {

        String email = user.getEmail();

        if (email == null || email.isBlank()) {
            email = safe(user.getName());
        }

        return Integer.toUnsignedString(
                email.hashCode()
        );
    }


    private static class AvatarLabel extends JLabel {

        private BufferedImage avatarImage;
        private String initials = "U";

        private AvatarLabel() {
            super();
            setOpaque(false);
            setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            46
                    )
            );
            setForeground(Color.WHITE);
        }

        private void setAvatarImage(BufferedImage image) {
            this.avatarImage = image;
            repaint();
        }

        private void setInitials(String initials) {
            this.initials =
                    (initials == null || initials.isBlank())
                            ? "U"
                            : initials;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {

            Graphics2D g2 =
                    (Graphics2D) graphics.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height) - 4;
            int x = (width - size) / 2;
            int y = (height - size) / 2;

            g2.setColor(
                    new Color(59, 130, 246)
            );

            g2.fillOval(x, y, size, size);

            if (avatarImage != null) {

                Shape clip =
                        new Ellipse2D.Double(
                                x, y, size, size
                        );

                g2.clip(clip);

                double scale = Math.max(
                        (double) size / avatarImage.getWidth(),
                        (double) size / avatarImage.getHeight()
                );

                int drawWidth =
                        (int) (avatarImage.getWidth() * scale);

                int drawHeight =
                        (int) (avatarImage.getHeight() * scale);

                int drawX =
                        x + (size - drawWidth) / 2;

                int drawY =
                        y + (size - drawHeight) / 2;

                g2.setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC
                );

                g2.drawImage(
                        avatarImage,
                        drawX, drawY,
                        drawWidth, drawHeight,
                        null
                );

            } else {

                g2.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                Math.max(
                                        30,
                                        size / 3
                                )
                        )
                );

                g2.setColor(Color.WHITE);

                String text = initials;

                if (text == null || text.isBlank()) {
                    text = "U";
                }

                FontMetrics fm = g2.getFontMetrics();

                int tx =
                        width / 2 - fm.stringWidth(text) / 2;

                int ty =
                        height / 2
                                - (fm.getAscent()
                                + fm.getDescent()) / 2;

                g2.drawString(text, tx, ty);
            }

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4f));
            g2.drawOval(
                    x + 2,
                    y + 2,
                    size - 4,
                    size - 4
            );

            g2.dispose();
        }
    }


    // =========================================================
    // EDIT PROFILE
    // =========================================================

    private void editProfile() {

        JTextField nameField =
                createFormField(user.getName());

        JTextField locationField =
                createFormField(user.getLocation());

        JTextArea bioField =
                new JTextArea(
                        user.getBio(),
                        6,
                        30
                );

        styleTextArea(bioField);

        JTextField skillsField =
                createFormField(user.getSkills());

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(CARD);
        form.setBorder(
                new EmptyBorder(
                        12, 12, 4, 12
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        addFormLabel(form, gbc, 0, "Full name");
        addFormComponent(form, gbc, 0, nameField);

        addFormLabel(form, gbc, 1, "Location");
        addFormComponent(form, gbc, 1, locationField);

        addFormLabel(form, gbc, 2, "Bio");

        JScrollPane bioScroll =
                new JScrollPane(bioField);

        bioScroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        form.add(bioScroll, gbc);

        addFormLabel(form, gbc, 3, "Skills");
        addFormComponent(form, gbc, 3, skillsField);

        JLabel skillHint =
                new JLabel(
                        "Use commas, for example: Java, SQL, Spring Boot, Git"
                );

        skillHint.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        skillHint.setForeground(MUTED);

        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets =
                new Insets(0, 8, 10, 8);

        form.add(skillHint, gbc);

        JLabel dialogHint =
                new JLabel(
                        "Keep your profile specific, concise, and focused on skills recruiters search for."
                );

        dialogHint.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        dialogHint.setForeground(MUTED);

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(0, 8)
                );

        wrapper.setBackground(CARD);
        wrapper.add(
                dialogHint,
                BorderLayout.NORTH
        );
        wrapper.add(
                form,
                BorderLayout.CENTER
        );

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        wrapper,
                        "Edit Profile",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (answer != JOptionPane.OK_OPTION) {
            return;
        }

        String name =
                nameField.getText().trim();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Name cannot be empty.",
                    "Invalid Profile",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        user.setName(name);
        user.setLocation(
                locationField.getText().trim()
        );
        user.setBio(
                bioField.getText().trim()
        );
        user.setSkills(
                skillsField.getText().trim()
        );

        refreshProfile();
    }


    private JTextField createFormField(String value) {

        JTextField field =
                new JTextField(
                        value == null ? "" : value
                );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(8, 10, 8, 10)
                )
        );

        return field;
    }


    private void styleTextArea(JTextArea area) {

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        area.setBorder(
                new EmptyBorder(
                        8, 10, 8, 10
                )
        );
    }


    private void addFormLabel(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String text
    ) {

        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(MUTED);
        panel.add(label, gbc);
    }


    private void addFormComponent(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            Component component
    ) {

        gbc.gridy = row;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;

        panel.add(component, gbc);
    }


    // =========================================================
    // REFRESH PROFILE
    // =========================================================

    private void refreshProfile() {

        nameLabel.setText(
                safe(user.getName())
        );

        emailLabel.setText(
                safe(user.getEmail())
        );

        locationLabel.setText(
                displayLocation()
        );

        bioArea.setText(
                safe(user.getBio())
        );

        Path path = savedPhotoPath();

        if (Files.exists(path)) {
            loadSavedPhoto(avatarLabel);
        } else {
            AvatarLabel avatar =
                    (AvatarLabel) avatarLabel;
            avatar.setAvatarImage(null);
            avatar.setInitials(
                    getInitials(user.getName())
            );
        }

        updateSkillsPanel();
        resumeLabel.setText(resumeText());
    }


    // =========================================================
    // SKILLS
    // =========================================================

    private void updateSkillsPanel() {

        skillsPanel.removeAll();

        String skills = user.getSkills();

        if (skills == null || skills.isBlank()) {

            JLabel empty =
                    new JLabel(
                            "Add skills such as Java, SQL, Git, Spring Boot..."
                    );

            empty.setForeground(MUTED);
            empty.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            13
                    )
            );

            skillsPanel.add(empty);

        } else {

            String[] skillList =
                    skills.split(",");

            for (String skill : skillList) {

                String clean = skill.trim();

                if (clean.isEmpty()) {
                    continue;
                }

                JLabel pill = new JLabel(clean);

                pill.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                12
                        )
                );

                pill.setForeground(CHIP_TEXT);
                pill.setOpaque(true);
                pill.setBackground(BLUE_LIGHT);
                pill.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        new Color(191, 219, 254)
                                ),
                                new EmptyBorder(
                                        7, 11, 7, 11
                                )
                        )
                );

                skillsPanel.add(pill);
            }
        }

        skillsPanel.revalidate();
        skillsPanel.repaint();
    }


    // =========================================================
    // RESUME UPLOAD
    // =========================================================

    private void uploadResume() {

        JFileChooser chooser =
                new JFileChooser();


        chooser.setDialogTitle(
                "Select Your Resume"
        );


        int result =
                chooser.showOpenDialog(
                        this
                );


        if (
                result ==
                        JFileChooser.APPROVE_OPTION
        ) {

            File file =
                    chooser.getSelectedFile();


            user.setResumePath(
                    file.getAbsolutePath()
            );


            resumeLabel.setText(
                    file.getName()
            );


            JOptionPane.showMessageDialog(
                    this,
                    "Resume selected successfully!\n\n"
                            + file.getName(),
                    "Resume Ready",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    // =========================================================
    // OPEN RESUME
    // =========================================================

    private void openResume() {

        String path =
                user.getResumePath();


        if (
                path == null
                        || path.isBlank()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please upload a resume first.",
                    "No Resume",
                    JOptionPane.INFORMATION_MESSAGE
            );


            return;
        }


        try {

            Desktop.getDesktop()
                    .open(
                            new File(
                                    path
                            )
                    );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open the resume.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // HELPERS
    // =========================================================

    private String resumeStatusText() {

        if (
                user.getResumePath() == null
                        || user.getResumePath().isBlank()
        ) {

            return "Not uploaded";
        }

        return "Ready";
    }


    private String resumeText() {

        if (
                user.getResumePath() == null
                        || user.getResumePath().isBlank()
        ) {

            return "No resume uploaded";
        }


        return new File(
                user.getResumePath()
        ).getName();
    }


    private String displayLocation() {

        if (
                user.getLocation() == null
                        || user.getLocation().isBlank()
        ) {

            return "Location not added";
        }


        return user.getLocation();
    }


    private void addButtonHover(JButton button, Color normal, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }

    private String safe(
            String text
    ) {

        if (
                text == null
                        || text.isBlank()
        ) {

            return "Not added";
        }


        return text;
    }


    private String getInitials(
            String name
    ) {

        if (
                name == null
                        || name.isBlank()
        ) {

            return "U";
        }


        String[] parts =
                name.trim()
                        .split("\\s+");


        if (
                parts.length == 1
        ) {

            return parts[0]
                    .substring(
                            0,
                            1
                    )
                    .toUpperCase();
        }


        return (
                parts[0].substring(
                        0,
                        1
                )
                        +
                        parts[parts.length - 1]
                                .substring(
                                        0,
                                        1
                                )
        ).toUpperCase();
    }


    private JLabel createSectionTitle(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );


        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );


        label.setForeground(
                TEXT
        );


        return label;
    }


    private JButton createButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16
                )
        );
        addButtonHover(button, color, color.darker());

        return button;
    }


    private JButton createOutlineButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(color);
        button.setBackground(CARD);
        button.setFocusPainted(false);
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color),
                        BorderFactory.createEmptyBorder(
                                9, 13, 9, 13
                        )
                )
        );
        addButtonHover(button, CARD, new Color(248, 250, 252));

        return button;
    }


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
                        205,
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
                        ? new Color(
                        55,
                        65,
                        81
                )
                        : SIDEBAR
        );


        button.setFocusPainted(
                false
        );
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButtonHover(
                button,
                selected ? new Color(55, 65, 81) : SIDEBAR,
                selected ? new Color(67, 78, 95) : new Color(43, 54, 70)
        );


        button.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        14,
                        0,
                        10
                )
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


    private void goToInternships() {

        dispose();

        new InternshipsFrame(user)
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