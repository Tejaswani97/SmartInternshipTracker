package gui;

import model.User;
import dao.UserDAO;
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
            setExtendedState(JFrame.MAXIMIZED_BOTH);

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

    JPanel content = new JPanel(new BorderLayout(0, 20));

    content.setBackground(BACKGROUND);

    content.setBorder(
            new EmptyBorder(
                    30,
                    30,
                    30,
                    30
            )
    );

    // =========================================================
    // HEADER
    // =========================================================

    JPanel header = new JPanel(new BorderLayout());

    header.setOpaque(false);

    JPanel heading = new JPanel();
    heading.setOpaque(false);
    heading.setLayout(
            new BoxLayout(
                    heading,
                    BoxLayout.Y_AXIS
            )
    );

    JLabel title = new JLabel("My Profile");

    title.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    28
            )
    );

    title.setForeground(TEXT);

    JLabel subtitle = new JLabel(
            "Manage your personal information, skills and resume"
    );

    subtitle.setFont(
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    14
            )
    );

    subtitle.setForeground(MUTED);

    heading.add(title);

    heading.add(
            Box.createVerticalStrut(5)
    );

    heading.add(subtitle);

    header.add(
            heading,
            BorderLayout.WEST
    );

    content.add(
            header,
            BorderLayout.NORTH
    );

    // =========================================================
    // MAIN SCROLLABLE AREA
    // =========================================================

    JPanel mainPanel = new JPanel();
    mainPanel.setOpaque(false);

    mainPanel.setLayout(
            new BoxLayout(
                    mainPanel,
                    BoxLayout.Y_AXIS
            )
    );

    // =========================================================
    // TOP PROFILE CARD
    // =========================================================

    JPanel profileCard = new JPanel(
            new BorderLayout(
                    35,
                    0
            )
    );

    profileCard.setBackground(CARD);

    profileCard.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(
                            25,
                            25,
                            25,
                            25
                    )
            )
    );

    profileCard.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    240
            )
    );

    // ---------------------------------------------------------
    // LEFT - AVATAR
    // ---------------------------------------------------------

    JPanel left = new JPanel();

    left.setOpaque(false);

    left.setPreferredSize(
            new Dimension(
                    220,
                    180
            )
    );

    left.setLayout(
            new BoxLayout(
                    left,
                    BoxLayout.Y_AXIS
            )
    );

    avatarLabel = createAvatar();

    avatarLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
    );

    left.add(avatarLabel);

    left.add(
            Box.createVerticalStrut(12)
    );

    JLabel profileText = new JLabel(
            "Your Profile"
    );

    profileText.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    13
            )
    );

    profileText.setForeground(MUTED);

    profileText.setAlignmentX(
            Component.CENTER_ALIGNMENT
    );

    left.add(profileText);

    // ---------------------------------------------------------
    // RIGHT - PERSONAL INFORMATION
    // ---------------------------------------------------------

    JPanel right = new JPanel(
            new GridBagLayout()
    );

    right.setOpaque(false);

    GridBagConstraints gbc =
            new GridBagConstraints();

    gbc.insets =
            new Insets(
                    7,
                    10,
                    7,
                    10
            );

    gbc.fill =
            GridBagConstraints.HORIZONTAL;

    gbc.anchor =
            GridBagConstraints.WEST;

    // Section title

    JLabel personalTitle =
            createSectionTitle(
                    "Personal Information"
            );

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;

    right.add(
            personalTitle,
            gbc
    );

    // ---------------------------------------------------------
    // NAME
    // ---------------------------------------------------------

    JLabel nameTitle =
            new JLabel("Name");

    nameTitle.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    12
            )
    );

    nameTitle.setForeground(MUTED);

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
                    14
            )
    );

    nameLabel.setForeground(TEXT);

    gbc.gridy = 1;
    gbc.gridx = 0;
    gbc.gridwidth = 1;
    gbc.weightx = 0.25;

    right.add(
            nameTitle,
            gbc
    );

    gbc.gridx = 1;
    gbc.weightx = 0.75;

    right.add(
            nameLabel,
            gbc
    );

    // ---------------------------------------------------------
    // EMAIL
    // ---------------------------------------------------------

    JLabel emailTitle =
            new JLabel("Email");

    emailTitle.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    12
            )
    );

    emailTitle.setForeground(MUTED);

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
                    14
            )
    );

    emailLabel.setForeground(TEXT);

    gbc.gridy = 2;
    gbc.gridx = 0;
    gbc.weightx = 0.25;

    right.add(
            emailTitle,
            gbc
    );

    gbc.gridx = 1;
    gbc.weightx = 0.75;

    right.add(
            emailLabel,
            gbc
    );

    // ---------------------------------------------------------
    // LOCATION
    // ---------------------------------------------------------

    JLabel locationTitle =
            new JLabel("Location");

    locationTitle.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    12
            )
    );

    locationTitle.setForeground(MUTED);

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

    locationLabel.setForeground(TEXT);

    gbc.gridy = 3;
    gbc.gridx = 0;
    gbc.weightx = 0.25;

    right.add(
            locationTitle,
            gbc
    );

    gbc.gridx = 1;
    gbc.weightx = 0.75;

    right.add(
            locationLabel,
            gbc
    );

    // ---------------------------------------------------------
    // EDIT BUTTON
    // ---------------------------------------------------------

    JButton editButton =
            createButton(
                    "Edit Profile",
                    BLUE
            );

    editButton.addActionListener(
            e -> editProfile()
    );

    gbc.gridy = 4;
    gbc.gridx = 1;
    gbc.weightx = 0;

    gbc.anchor =
            GridBagConstraints.EAST;

    right.add(
            editButton,
            gbc
    );

    profileCard.add(
            left,
            BorderLayout.WEST
    );

    profileCard.add(
            right,
            BorderLayout.CENTER
    );

    mainPanel.add(profileCard);

    mainPanel.add(
            Box.createVerticalStrut(20)
    );

    // =========================================================
    // ABOUT ME CARD
    // =========================================================

    JPanel aboutCard =
            new JPanel(
                    new BorderLayout(
                            0,
                            10
                    )
            );

    aboutCard.setBackground(CARD);

    aboutCard.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            )
    );

    aboutCard.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    170
            )
    );

    aboutCard.add(
            createSectionTitle(
                    "About Me"
            ),
            BorderLayout.NORTH
    );

    bioArea =
            new JTextArea(
                    safe(
                            user.getBio()
                    )
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

    bioArea.setBorder(
            new EmptyBorder(
                    5,
                    0,
                    5,
                    0
            )
    );

    aboutCard.add(
            bioArea,
            BorderLayout.CENTER
    );

    mainPanel.add(aboutCard);

    mainPanel.add(
            Box.createVerticalStrut(20)
    );

    // =========================================================
    // SKILLS CARD
    // =========================================================

    JPanel skillsCard =
            new JPanel(
                    new BorderLayout(
                            0,
                            12
                    )
            );

    skillsCard.setBackground(CARD);

    skillsCard.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            )
    );

    skillsCard.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    140
            )
    );

    skillsCard.add(
            createSectionTitle(
                    "Skills"
            ),
            BorderLayout.NORTH
    );

    skillsPanel =
            new JPanel(
                    new FlowLayout(
                            FlowLayout.LEFT,
                            8,
                            5
                    )
            );

    skillsPanel.setOpaque(false);

    updateSkillsPanel();

    skillsCard.add(
            skillsPanel,
            BorderLayout.CENTER
    );

    mainPanel.add(skillsCard);

    mainPanel.add(
            Box.createVerticalStrut(20)
    );

    // =========================================================
    // RESUME CARD
    // =========================================================

    JPanel resumeCard =
            new JPanel(
                    new BorderLayout(
                            15,
                            0
                    )
            );

    resumeCard.setBackground(CARD);

    resumeCard.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            )
    );

    resumeCard.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    100
            )
    );

    // Resume icon

    JLabel fileIcon =
            new JLabel("📄");

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

    // Resume text

    JPanel resumeInfo =
            new JPanel();

    resumeInfo.setOpaque(false);

    resumeInfo.setLayout(
            new BoxLayout(
                    resumeInfo,
                    BoxLayout.Y_AXIS
            )
    );

    JLabel resumeTitle =
            new JLabel(
                    "Resume"
            );

    resumeTitle.setFont(
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    15
            )
    );

    resumeTitle.setForeground(TEXT);

    resumeLabel =
            new JLabel(
                    resumeText()
            );

    resumeLabel.setFont(
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    13
            )
    );

    resumeLabel.setForeground(MUTED);

    resumeInfo.add(resumeTitle);

    resumeInfo.add(
            Box.createVerticalStrut(5)
    );

    resumeInfo.add(resumeLabel);

    resumeCard.add(
            resumeInfo,
            BorderLayout.CENTER
    );

    // Resume buttons

    JPanel resumeButtons =
            new JPanel(
                    new FlowLayout(
                            FlowLayout.RIGHT,
                            8,
                            0
                    )
            );

    resumeButtons.setOpaque(false);

    JButton viewResume =
            createButton(
                    "Open Resume",
                    BLUE
            );

    viewResume.addActionListener(
            e -> openResume()
    );

    JButton uploadButton =
            createButton(
                    "Upload / Replace",
                    GREEN
            );

    uploadButton.addActionListener(
            e -> uploadResume()
    );

    resumeButtons.add(viewResume);

    resumeButtons.add(uploadButton);

    resumeCard.add(
            resumeButtons,
            BorderLayout.EAST
    );

    mainPanel.add(resumeCard);

    // =========================================================
    // SCROLL PANE
    // =========================================================

    JScrollPane scrollPane =
            new JScrollPane(
                    mainPanel
            );

    scrollPane.setBorder(null);

    scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    scrollPane.getVerticalScrollBar()
            .setUnitIncrement(16);

    scrollPane.getViewport()
            .setBackground(BACKGROUND);

    content.add(
            scrollPane,
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


            user.setName(name);

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

UserDAO userDAO = new UserDAO();

boolean updated =
        userDAO.updateProfile(user);

if (updated) {

    refreshProfile();

    JOptionPane.showMessageDialog(
            this,
            "Profile updated successfully!",
            "Profile Updated",
            JOptionPane.INFORMATION_MESSAGE
    );

} else {

    JOptionPane.showMessageDialog(
            this,
            "Unable to save your profile.",
            "Update Failed",
            JOptionPane.ERROR_MESSAGE
    );
}
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