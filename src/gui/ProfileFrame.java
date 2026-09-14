package gui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

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
                1100,
                700
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
                        220,
                        700
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

        JPanel header =
                new JPanel();


        header.setOpaque(false);


        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "My Profile"
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
                        "Manage your career profile and resume"
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


        header.add(title);


        header.add(
                Box.createVerticalStrut(5)
        );


        header.add(subtitle);


        content.add(
                header,
                BorderLayout.NORTH
        );


        // -----------------------------------------------------
        // PROFILE CARD
        // -----------------------------------------------------

        JPanel profileCard =
                new JPanel(
                        new BorderLayout(
                                30,
                                0
                        )
                );


        profileCard.setBackground(
                CARD
        );


        profileCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                30,
                                30,
                                30,
                                30
                        )
                )
        );


        // -----------------------------------------------------
        // LEFT PROFILE
        // -----------------------------------------------------

        JPanel left =
                new JPanel();


        left.setOpaque(false);


        left.setPreferredSize(
                new Dimension(
                        280,
                        0
                )
        );


        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );


        avatarLabel =
                createAvatar();


        avatarLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        left.add(
                avatarLabel
        );


        left.add(
                Box.createVerticalStrut(15)
        );


        nameLabel =
                new JLabel(
                        safe(
                                user.getName()
                        )
                );


        nameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );


        nameLabel.setForeground(
                TEXT
        );


        nameLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        emailLabel =
                new JLabel(
                        safe(
                                user.getEmail()
                        )
                );


        emailLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        emailLabel.setForeground(
                MUTED
        );


        emailLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        locationLabel =
                new JLabel(
                        displayLocation()
                );


        locationLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        locationLabel.setForeground(
                MUTED
        );


        locationLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        left.add(
                nameLabel
        );


        left.add(
                Box.createVerticalStrut(5)
        );


        left.add(
                emailLabel
        );


        left.add(
                Box.createVerticalStrut(5)
        );


        left.add(
                locationLabel
        );


        left.add(
                Box.createVerticalGlue()
        );


        JButton editButton =
                createButton(
                        "Edit Profile",
                        BLUE
                );


        editButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        editButton.addActionListener(
                e -> editProfile()
        );


        left.add(
                editButton
        );


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


        // BIO

        right.add(
                createSectionTitle(
                        "About Me"
                )
        );


        bioArea =
                new JTextArea(
                        safe(
                                user.getBio()
                        ),
                        4,
                        30
                );


        bioArea.setEditable(
                false
        );


        bioArea.setLineWrap(
                true
        );


        bioArea.setWrapStyleWord(
                true
        );


        bioArea.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        bioArea.setForeground(
                TEXT
        );


        bioArea.setBackground(
                CARD
        );


        bioArea.setBorder(
                new EmptyBorder(
                        5,
                        0,
                        5,
                        0
                )
        );


        right.add(
                bioArea
        );


        right.add(
                Box.createVerticalStrut(20)
        );


        // SKILLS

        right.add(
                createSectionTitle(
                        "Skills"
                )
        );


        skillsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                5
                        )
                );


        skillsPanel.setOpaque(
                false
        );


        updateSkillsPanel();


        right.add(
                skillsPanel
        );


        right.add(
                Box.createVerticalStrut(20)
        );


        // RESUME

        right.add(
                createSectionTitle(
                        "Resume"
                )
        );


        JPanel resumeCard =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );


        resumeCard.setBackground(
                new Color(
                        249,
                        250,
                        251
                )
        );


        resumeCard.setBorder(
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


        JLabel fileIcon =
                new JLabel(
                        "📄"
                );


        fileIcon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        28
                )
        );


        resumeCard.add(
                fileIcon,
                BorderLayout.WEST
        );


        resumeLabel =
                new JLabel(
                        resumeText()
                );


        resumeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        resumeLabel.setForeground(
                TEXT
        );


        resumeCard.add(
                resumeLabel,
                BorderLayout.CENTER
        );


        JButton uploadButton =
                createButton(
                        "Upload / Replace",
                        GREEN
                );


        uploadButton.addActionListener(
                e -> uploadResume()
        );


        resumeCard.add(
                uploadButton,
                BorderLayout.EAST
        );


        right.add(
                resumeCard
        );


        right.add(
                Box.createVerticalStrut(12)
        );


        JButton viewResume =
                new JButton(
                        "Open Resume"
                );


        viewResume.setFocusPainted(
                false
        );


        viewResume.addActionListener(
                e -> openResume()
        );


        right.add(
                viewResume
        );


        profileCard.add(
                left,
                BorderLayout.WEST
        );


        profileCard.add(
                right,
                BorderLayout.CENTER
        );


        content.add(
                profileCard,
                BorderLayout.CENTER
        );


        return content;
    }


    // =========================================================
    // AVATAR
    // =========================================================

    private JLabel createAvatar() {

        String initials =
                getInitials(
                        user.getName()
                );


        JLabel label =
                new JLabel(
                        initials,
                        SwingConstants.CENTER
                );


        label.setPreferredSize(
                new Dimension(
                        130,
                        130
                )
        );


        label.setMinimumSize(
                new Dimension(
                        130,
                        130
                )
        );


        label.setMaximumSize(
                new Dimension(
                        130,
                        130
                )
        );


        label.setOpaque(
                true
        );


        label.setBackground(
                BLUE
        );


        label.setForeground(
                Color.WHITE
        );


        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        38
                )
        );


        return label;
    }


    // =========================================================
    // EDIT PROFILE
    // =========================================================

    private void editProfile() {

        JTextField nameField =
                new JTextField(
                        user.getName()
                );


        JTextField locationField =
                new JTextField(
                        user.getLocation()
                );


        JTextArea bioField =
                new JTextArea(
                        user.getBio(),
                        4,
                        20
                );


        bioField.setLineWrap(
                true
        );


        bioField.setWrapStyleWord(
                true
        );


        JTextField skillsField =
                new JTextField(
                        user.getSkills()
                );


        JPanel panel =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );


        panel.add(
                new JLabel(
                        "Name:"
                )
        );


        panel.add(
                nameField
        );


        panel.add(
                new JLabel(
                        "Location:"
                )
        );


        panel.add(
                locationField
        );


        panel.add(
                new JLabel(
                        "Bio:"
                )
        );


        panel.add(
                new JScrollPane(
                        bioField
                )
        );


        panel.add(
                new JLabel(
                        "Skills:"
                )
        );


        panel.add(
                skillsField
        );


        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Edit Profile",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


        if (
                answer ==
                        JOptionPane.OK_OPTION
        ) {

            String name =
                    nameField
                            .getText()
                            .trim();


            if (
                    name.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Name cannot be empty.",
                        "Invalid Profile",
                        JOptionPane.WARNING_MESSAGE
                );


                return;
            }


            user.setName(
                    name
            );


            user.setLocation(
                    locationField
                            .getText()
                            .trim()
            );


            user.setBio(
                    bioField
                            .getText()
                            .trim()
            );


            user.setSkills(
                    skillsField
                            .getText()
                            .trim()
            );


            refreshProfile();
        }
    }


    // =========================================================
    // REFRESH PROFILE
    // =========================================================

    private void refreshProfile() {

        nameLabel.setText(
                safe(
                        user.getName()
                )
        );


        locationLabel.setText(
                displayLocation()
        );


        bioArea.setText(
                safe(
                        user.getBio()
                )
        );


        avatarLabel.setText(
                getInitials(
                        user.getName()
                )
        );


        updateSkillsPanel();


        resumeLabel.setText(
                resumeText()
        );
    }


    // =========================================================
    // SKILLS
    // =========================================================

    private void updateSkillsPanel() {

        skillsPanel.removeAll();


        String skills =
                user.getSkills();


        if (
                skills == null
                        || skills.isBlank()
        ) {

            JLabel empty =
                    new JLabel(
                            "Add your skills from Edit Profile"
                    );


            empty.setForeground(
                    MUTED
            );


            skillsPanel.add(
                    empty
            );

        } else {

            String[] skillList =
                    skills.split(",");


            for (
                    String skill :
                    skillList
            ) {

                String clean =
                        skill.trim();


                if (
                        clean.isEmpty()
                ) {
                    continue;
                }


                JLabel pill =
                        new JLabel(
                                clean
                        );


                pill.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                12
                        )
                );


                pill.setForeground(
                        BLUE
                );


                pill.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        new Color(
                                                191,
                                                219,
                                                254
                                        )
                                ),
                                new EmptyBorder(
                                        6,
                                        10,
                                        6,
                                        10
                                )
                        )
                );


                skillsPanel.add(
                        pill
                );
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

        JButton button =
                new JButton(
                        text
                );


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
                        9,
                        14,
                        9,
                        14
                )
        );


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