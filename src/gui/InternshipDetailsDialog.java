package gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Internship;
import model.User;
import util.SkillMatchResult;
import util.SkillMatcher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InternshipDetailsDialog extends JDialog {

    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);
    private static final Color BLUE = new Color(59, 130, 246);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color ORANGE = new Color(234, 88, 12);
    private static final Color BORDER = new Color(229, 231, 235);

    private final Internship internship;
    private final User user;
    private final Runnable applyAction;
    private final JEditorPane descriptionPane;

    public InternshipDetailsDialog(
            Window owner,
            Internship internship,
            User user,
            Runnable applyAction
    ) {
        super(owner, "Internship Details", ModalityType.APPLICATION_MODAL);

        this.internship = internship;
        this.user = user;
        this.applyAction = applyAction;

        setSize(1120, 760);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        descriptionPane = createDescriptionPane();

        buildUI();
        loadDescriptionAsync();
    }

    private void buildUI() {

        JPanel header = createHeader();
        JPanel content = createContent();
        JPanel footer = createFooter();

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(20, 10));
        header.setBackground(CARD);
        header.setBorder(new EmptyBorder(22, 28, 20, 28));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel company = new JLabel(safe(internship.getCompanyName()));
        company.setFont(new Font("SansSerif", Font.BOLD, 28));
        company.setForeground(TEXT);

        JLabel role = new JLabel(safe(internship.getJobRole()));
        role.setFont(new Font("SansSerif", Font.PLAIN, 17));
        role.setForeground(MUTED);

        JLabel source = new JLabel(
                "Source: " + safe(internship.getSourceName())
        );
        source.setFont(new Font("SansSerif", Font.BOLD, 12));
        source.setForeground(BLUE);

        titlePanel.add(company);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(role);
        titlePanel.add(Box.createVerticalStrut(9));
        titlePanel.add(source);

        JPanel matchPanel = createMatchPanel();

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(matchPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createMatchPanel() {

        SkillMatchResult result = SkillMatcher.calculateMatch(
                safe(user.getSkills()),
                safe(internship.getRequiredSkills())
        );

        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 253, 244));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(187, 247, 208)),
                new EmptyBorder(11, 14, 11, 14)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel match = new JLabel(
                result.getMatchPercentage() + "% skill match"
        );
        match.setFont(new Font("SansSerif", Font.BOLD, 15));
        match.setForeground(GREEN);
        match.setAlignmentX(Component.CENTER_ALIGNMENT);

        String missingText = result.getMissingSkills().isEmpty()
                ? "All listed skills matched"
                : "Missing: " + String.join(", ", result.getMissingSkills());

        JLabel missing = new JLabel(
                "<html><div style='width:210px; text-align:center;'>"
                        + escapeHtml(missingText)
                        + "</div></html>"
        );
        missing.setFont(new Font("SansSerif", Font.PLAIN, 11));
        missing.setForeground(
                result.getMissingSkills().isEmpty() ? GREEN : ORANGE
        );
        missing.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(match);
        panel.add(Box.createVerticalStrut(5));
        panel.add(missing);

        return panel;
    }

    private JPanel createContent() {

        JPanel main = new JPanel(new BorderLayout(16, 16));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(18, 22, 10, 22));

        JPanel leftColumn = new JPanel();
        leftColumn.setOpaque(false);
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));

        leftColumn.add(createOverviewCard());
        leftColumn.add(Box.createVerticalStrut(14));
        leftColumn.add(createSkillsCard());
        leftColumn.add(Box.createVerticalStrut(14));
        leftColumn.add(createContactCard());

        JScrollPane leftScroll = new JScrollPane(leftColumn);
        leftScroll.setBorder(null);
        leftScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        leftScroll.getVerticalScrollBar().setUnitIncrement(14);
        leftScroll.setBackground(BACKGROUND);

        JPanel descriptionCard = createSectionCard("Job Description");

        JScrollPane descriptionScroll = new JScrollPane(descriptionPane);
        descriptionScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        descriptionScroll.getVerticalScrollBar().setUnitIncrement(16);

        descriptionCard.add(descriptionScroll, BorderLayout.CENTER);

        main.add(leftScroll, BorderLayout.WEST);
        main.add(descriptionCard, BorderLayout.CENTER);

        leftScroll.setPreferredSize(new Dimension(405, 0));

        return main;
    }

    private JPanel createOverviewCard() {

        JPanel card = createSectionCard("Internship Overview");

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        addRow(grid, 0, "Category", internship.getCategory());
        addRow(grid, 1, "Location", internship.getLocation());
        addRow(grid, 2, "Work Mode", internship.getWorkMode());
        addRow(grid, 3, "Stipend", internship.getStipend());
        addRow(grid, 4, "Duration", internship.getDuration());
        addRow(
                grid,
                5,
                "Deadline",
                internship.getDeadline() == null
                        ? "Not specified"
                        : internship.getDeadline().toString()
        );
        addRow(
                grid,
                6,
                "Posted",
                internship.getPostedDate() == null
                        ? "Not provided"
                        : internship.getPostedDate().toString()
        );
        addRow(grid, 7, "Source", internship.getSourceName());

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSkillsCard() {

        JPanel card = createSectionCard("Required Skills");

        JLabel skills = new JLabel(
                "<html><div style='width:330px; line-height:1.45;'>"
                        + escapeHtml(safe(internship.getRequiredSkills()))
                        + "</div></html>"
        );
        skills.setFont(new Font("SansSerif", Font.PLAIN, 14));
        skills.setForeground(TEXT);

        card.add(skills, BorderLayout.CENTER);
        return card;
    }

    private JPanel createContactCard() {

        JPanel card = createSectionCard("Recruiter & Contact");

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String recruiterName = internship.getRecruiterName();
        String recruiterRole = internship.getRecruiterRole();
        String email = internship.getRecruiterEmail();
        String linkedin = internship.getRecruiterLinkedin();

        addContactLine(panel, "Recruiter", recruiterName);
        addContactLine(panel, "Role", recruiterRole);

        if (email != null && !email.isBlank()) {
            JLabel emailLabel = clickableLabel(email);
            emailLabel.setToolTipText("Click to compose an email");
            emailLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openColdEmail(email);
                }
            });
            addContactComponent(panel, "Email", emailLabel);
        } else {
            addContactLine(panel, "Email", "Not publicly provided");
        }

        if (linkedin != null && !linkedin.isBlank()) {
            JButton button = smallButton("Open LinkedIn", BLUE);
            button.addActionListener(e -> openUrl(linkedin));
            addContactComponent(panel, "LinkedIn", button);
        } else {
            addContactLine(panel, "LinkedIn", "Not publicly provided");
        }

        if (internship.getContactSource() != null
                && !internship.getContactSource().isBlank()) {
            addContactLine(
                    panel,
                    "Contact Source",
                    internship.getContactSource()
            );
        }

        card.add(panel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSectionCard(String title) {

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 18, 18, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(TEXT);

        card.add(titleLabel, BorderLayout.NORTH);

        return card;
    }

    private void addRow(
            JPanel grid,
            int row,
            String title,
            String value
    ) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 0, 5, 10);

        gbc.gridx = 0;
        gbc.weightx = 0.40;

        JLabel key = new JLabel(title);
        key.setFont(new Font("SansSerif", Font.BOLD, 11));
        key.setForeground(MUTED);
        grid.add(key, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel result = new JLabel(
                "<html><div style='width:205px;'>"
                        + escapeHtml(safe(value))
                        + "</div></html>"
        );
        result.setFont(new Font("SansSerif", Font.PLAIN, 13));
        result.setForeground(TEXT);
        grid.add(result, gbc);
    }

    private void addContactLine(
            JPanel panel,
            String title,
            String value
    ) {
        JLabel label = new JLabel(safe(value));
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(TEXT);
        addContactComponent(panel, title, label);
    }

    private void addContactComponent(
            JPanel panel,
            String title,
            JComponent component
    ) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 0, 4, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(MUTED);
        titleLabel.setPreferredSize(new Dimension(90, 24));

        row.add(titleLabel, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);

        panel.add(row);
    }

    private JLabel clickableLabel(String text) {
        JLabel label = new JLabel("<html><u>" + escapeHtml(text) + "</u></html>");
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(BLUE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private JPanel createFooter() {

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(BACKGROUND);
        footer.setBorder(new EmptyBorder(0, 18, 8, 18));

        JButton sourceButton = null;

        if (internship.getSourceUrl() != null
                && !internship.getSourceUrl().isBlank()) {
            sourceButton = smallButton("Open Source", BLUE);
            sourceButton.addActionListener(
                    e -> openUrl(internship.getSourceUrl())
            );
        }

        JButton copyButton = smallButton("Copy Job Link", new Color(75, 85, 99));
        copyButton.addActionListener(e -> copyJobLink());

        JButton employerButton = smallButton("Employer Page", BLUE);
        employerButton.addActionListener(
                e -> openUrl(internship.getJobLink())
        );

        JButton closeButton = smallButton("Close", new Color(107, 114, 128));
        closeButton.addActionListener(e -> dispose());

        JButton applyButton = smallButton("Apply Now", GREEN);
        applyButton.addActionListener(e -> {
            dispose();
            if (applyAction != null) {
                applyAction.run();
            }
        });

        if (sourceButton != null) {
            footer.add(sourceButton);
        }

        footer.add(copyButton);
        footer.add(employerButton);
        footer.add(closeButton);
        footer.add(applyButton);

        return footer;
    }

    private JButton smallButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 13, 8, 13));
        return button;
    }

    private JEditorPane createDescriptionPane() {
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        pane.setBackground(CARD);
        pane.setBorder(new EmptyBorder(14, 16, 14, 16));

        pane.setEditorKit(new HTMLEditorKit());
        pane.setText(
                htmlPage(
                        "Loading the full job description..."
                                + "<br><br>Please wait while Smart Internship Tracker "
                                + "loads the real posting from the source."
                )
        );
        pane.setCaretPosition(0);

        return pane;
    }

    private void loadDescriptionAsync() {

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() {
                try {
                    return fetchJobDescription(internship);
                } catch (Exception e) {
                    return "__ERROR__";
                }
            }

            @Override
            protected void done() {
                try {
                    String result = get();

                    if ("__ERROR__".equals(result)
                            || result == null
                            || result.isBlank()) {
                        descriptionPane.setText(
                                htmlPage(
                                        "The full job description could not be loaded right now."
                                                + "<br><br>"
                                                + "The listing is still real and available through "
                                                + "the employer page below."
                                )
                        );
                    } else {
                        descriptionPane.setText(htmlPage(result));
                    }

                    descriptionPane.setCaretPosition(0);
                    descriptionPane.revalidate();
                    descriptionPane.repaint();

                } catch (Exception e) {
                    descriptionPane.setText(
                            htmlPage(
                                    "Unable to load the job description. "
                                            + "Please open the employer page for the latest posting details."
                            )
                    );
                }
            }
        };

        worker.execute();
    }

    private String fetchJobDescription(Internship internship) throws Exception {

        String source = safe(internship.getSourceName()).toLowerCase();

        if (source.contains("greenhouse")) {
            return fetchGreenhouseDescription(internship);
        }

        if (source.contains("lever")) {
            return fetchLeverDescription(internship);
        }

        return "The source does not expose a public description endpoint. "
                + "Please use the employer page for the full posting.";
    }

    private String fetchGreenhouseDescription(Internship internship)
            throws Exception {

        String link = safe(internship.getJobLink());

        Matcher matcher = Pattern.compile(
                "https?://(?:job-boards|boards)\\.greenhouse\\.io/([^/]+)/jobs/(\\d+).*",
                Pattern.CASE_INSENSITIVE
        ).matcher(link);

        String boardToken = null;
        String jobId = safe(internship.getExternalJobId());

        if (matcher.matches()) {
            boardToken = matcher.group(1);

            if (jobId.isBlank()) {
                jobId = matcher.group(2);
            }
        }

        if (boardToken == null || boardToken.isBlank() || jobId.isBlank()) {
            return "The Greenhouse job details could not be identified from this listing."
                    + "<br><br>Please open the employer page for the full description.";
        }

        String apiUrl =
                "https://boards-api.greenhouse.io/v1/boards/"
                        + URLEncoder.encode(boardToken, StandardCharsets.UTF_8)
                        + "/jobs/"
                        + URLEncoder.encode(jobId, StandardCharsets.UTF_8)
                        + "?content=true";

        String json = get(apiUrl);
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        String content = getString(root, "content");

        if (content.isBlank()) {
            return "The employer did not provide description content through the public Greenhouse job-board API."
                    + "<br><br>Please open the employer page for the complete posting.";
        }

        return decodeHtmlEntities(content);
    }

    private String fetchLeverDescription(Internship internship)
            throws Exception {

        String link = safe(internship.getJobLink());
        Matcher matcher = Pattern.compile(
                "https?://jobs\\.lever\\.co/([^/]+)/([^/?#]+).*",
                Pattern.CASE_INSENSITIVE
        ).matcher(link);

        String slug = null;
        String postingId = safe(internship.getExternalJobId());

        if (matcher.matches()) {
            slug = matcher.group(1);

            if (postingId.isBlank()) {
                postingId = matcher.group(2);
            }
        }

        if (slug == null || slug.isBlank()) {
            slug = safeSlug(internship.getCompanyName());
        }

        if (slug.isBlank()) {
            return "The Lever company board could not be identified from this listing."
                    + "<br><br>Please open the employer page for the full description.";
        }

        String apiUrl =
                "https://api.lever.co/v0/postings/"
                        + URLEncoder.encode(slug, StandardCharsets.UTF_8)
                        + "?mode=json";

        String json = get(apiUrl);
        JsonArray postings = JsonParser.parseString(json).getAsJsonArray();

        for (JsonElement element : postings) {

            JsonObject posting = element.getAsJsonObject();

            if (!postingId.isBlank()
                    && posting.has("id")
                    && posting.get("id").isJsonPrimitive()
                    && postingId.equals(
                            posting.get("id").getAsString()
                    )) {

                String plain = getString(posting, "descriptionPlain");

                if (!plain.isBlank()) {
                    return escapeHtml(plain).replace("\n", "<br>");
                }

                String html = getString(posting, "description");

                if (!html.isBlank()) {
                    return decodeHtmlEntities(html);
                }

                return buildLeverDescription(posting);
            }
        }

        return "The Lever posting could not be found in the current public board response."
                + "<br><br>Please open the employer page for the latest posting.";
    }

    private String buildLeverDescription(JsonObject posting) {

        StringBuilder html = new StringBuilder();

        String description = getString(posting, "descriptionPlain");
        if (!description.isBlank()) {
            html.append(escapeHtml(description).replace("\n", "<br>"));
        }

        if (posting.has("lists") && posting.get("lists").isJsonArray()) {
            for (JsonElement element : posting.getAsJsonArray("lists")) {
                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject list = element.getAsJsonObject();
                String heading = getString(list, "text");
                String content = getString(list, "content");

                if (!heading.isBlank()) {
                    html.append("<h3>")
                            .append(escapeHtml(heading))
                            .append("</h3>");
                }

                if (!content.isBlank()) {
                    html.append(decodeHtmlEntities(content));
                }
            }
        }

        return html.toString();
    }

    private String get(String url) throws Exception {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("User-Agent", "SmartInternshipTracker/1.0")
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "HTTP " + response.statusCode() + " from " + url
            );
        }

        return response.body();
    }

    private String getString(JsonObject object, String key) {
        if (object == null
                || !object.has(key)
                || object.get(key).isJsonNull()) {
            return "";
        }

        try {
            return object.get(key).getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    private String safeSlug(String companyName) {
        String value = safe(companyName).toLowerCase();
        return value.replaceAll("[^a-z0-9]+", "").trim();
    }

    private String decodeHtmlEntities(String value) {
        String result = value;

        for (int i = 0; i < 3; i++) {
            result = result
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&quot;", "\"")
                    .replace("&#39;", "'")
                    .replace("&#x27;", "'")
                    .replace("&nbsp;", " ");
        }

        return result;
    }

    private String htmlPage(String body) {
        return "<html><head><style>"
                + "body{font-family:SansSerif;color:#1f2937;font-size:14px;}"
                + "h1,h2,h3{color:#1f2937;margin-top:16px;}"
                + "p{line-height:1.55;margin:8px 0;}"
                + "li{margin:4px 0;line-height:1.45;}"
                + "a{color:#2563eb;}"
                + ".loading{color:#6b7280;}"
                + "</style></head><body>"
                + body
                + "</body></html>";
    }

    private void openColdEmail(String recruiterEmail) {

        String studentName = safe(user.getName());
        if (studentName.isBlank()) {
            studentName = "Student";
        }

        String company = safe(internship.getCompanyName());
        String role = safe(internship.getJobRole());
        String recruiterName = safe(internship.getRecruiterName());
        String skills = safe(user.getSkills());

        String subject = "Application for " + role + " at " + company;

        String body =
                "Hi " + recruiterName + ",\n\n"
                        + "My name is " + studentName
                        + ", and I am interested in the " + role
                        + " opportunity at " + company + ".\n\n"
                        + "My relevant skills include " + skills + ".\n\n"
                        + "I would be grateful for the opportunity to be considered. "
                        + "I have attached my resume for your review.\n\n"
                        + "Thank you for your time and consideration.\n\n"
                        + "Regards,\n" + studentName;

        try {
            String encodedSubject = URLEncoder.encode(
                    subject,
                    StandardCharsets.UTF_8
            ).replace("+", "%20");

            String encodedBody = URLEncoder.encode(
                    body,
                    StandardCharsets.UTF_8
            ).replace("+", "%20");

            openUrl(
                    "mailto:" + recruiterEmail
                            + "?subject=" + encodedSubject
                            + "&body=" + encodedBody
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

    private void openUrl(String url) {
        if (url == null || url.isBlank()) {
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open the link.",
                    "Link Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void copyJobLink() {
        String link = safe(internship.getJobLink());

        if (link.isBlank()) {
            return;
        }

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(link), null);

        JOptionPane.showMessageDialog(
                this,
                "Job link copied to clipboard.",
                "Copied",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
