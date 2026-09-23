package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Internship;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeverSource {

    private static final HttpClient HTTP_CLIENT =
            HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

    private static final List<CompanyBoard> COMPANY_BOARDS = Arrays.asList(
            new CompanyBoard("epifi", "Fi"),
            new CompanyBoard("drivetrain", "Drivetrain"),
            new CompanyBoard("aleph", "Aleph"),
            new CompanyBoard("safe", "Safe"),
            new CompanyBoard("dnb", "Dun & Bradstreet")
    );

    /**
     * Backward-compatible method used by the existing InternshipsFrame.
     * It now aggregates all configured Lever public boards.
     */
    public List<Internship> fetchInternships() {
        return fetchAllInternships();
    }

    /**
     * Fetches published internship postings from all configured public
     * Lever job boards.
     *
     * A network/API failure throws instead of returning an empty list so
     * the caller does not accidentally treat a source outage as "no jobs".
     */
    public List<Internship> fetchAllInternships() {

        List<Internship> internships = new ArrayList<>();

        for (CompanyBoard board : COMPANY_BOARDS) {
            internships.addAll(fetchCompanyInternships(board));
        }

        return internships;
    }

    private List<Internship> fetchCompanyInternships(
            CompanyBoard board
    ) {

        List<Internship> internships = new ArrayList<>();

        String endpoint =
                "https://api.lever.co/v0/postings/"
                        + board.slug
                        + "?mode=json";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .header("Accept", "application/json")
                        .header("User-Agent", "SmartInternshipTracker/1.0")
                        .GET()
                        .build();

        try {

            HttpResponse<String> response =
                    HTTP_CLIENT.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            int status = response.statusCode();

            if (status < 200 || status >= 300) {
                throw new RuntimeException(
                        "Lever request failed for "
                                + board.companyName
                                + " with HTTP status "
                                + status
                );
            }

            JsonElement root =
                    JsonParser.parseString(response.body());

            if (!root.isJsonArray()) {
                throw new RuntimeException(
                        "Unexpected Lever response for "
                                + board.companyName
                );
            }

            JsonArray postings = root.getAsJsonArray();

            for (JsonElement element : postings) {

                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject posting =
                        element.getAsJsonObject();

                if (!isInternship(posting)) {
                    continue;
                }

                Internship internship =
                        toInternship(
                                posting,
                                board
                        );

                if (internship != null) {
                    internships.add(internship);
                }
            }

            System.out.println(
                    "Lever: "
                            + board.companyName
                            + " -> "
                            + internships.size()
                            + " internships"
            );

            return internships;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Lever request interrupted for "
                            + board.companyName,
                    e
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to reach Lever for "
                            + board.companyName,
                    e
            );
        }
    }

    private boolean isInternship(JsonObject posting) {

        String title =
                getString(
                        posting,
                        "text"
                );

        JsonObject categories =
                getObject(
                        posting,
                        "categories"
                );

        String commitment =
                getString(
                        categories,
                        "commitment"
                );

        String level =
                getString(
                        categories,
                        "level"
                );

        String searchable =
                (safe(title) + " "
                        + safe(commitment) + " "
                        + safe(level))
                        .toLowerCase(Locale.ROOT);

        return searchable.matches(
                ".*\\bintern(ship|ships)?\\b.*"
        );
    }

    private Internship toInternship(
            JsonObject posting,
            CompanyBoard board
    ) {

        String externalId =
                getString(
                        posting,
                        "id"
                );

        String title =
                getString(
                        posting,
                        "text"
                );

        if (externalId.isBlank() || title.isBlank()) {
            return null;
        }

        JsonObject categories =
                getObject(
                        posting,
                        "categories"
                );

        String department =
                getString(
                        categories,
                        "department"
                );

        String team =
                getString(
                        categories,
                        "team"
                );

        String category =
                !department.isBlank()
                        ? department
                        : (!team.isBlank() ? team : "Internship");

        String location =
                getString(
                        categories,
                        "location"
                );

        String commitment =
                getString(
                        categories,
                        "commitment"
                );

        String workplaceType =
                getString(
                        posting,
                        "workplaceType"
                );

        String workMode =
                formatWorkMode(workplaceType);

        if (workMode.isBlank()) {
            workMode = commitment;
        }

        JsonObject content =
                getObject(
                        posting,
                        "content"
                );

        String description =
                firstNonBlank(
                        getString(content, "descriptionPlain"),
                        getString(posting, "descriptionPlain"),
                        stripHtml(
                                getString(content, "description")
                        ),
                        stripHtml(
                                getString(posting, "description")
                        )
                );

        String stipend =
                firstNonBlank(
                        getString(posting, "salaryDescription"),
                        extractStipend(description),
                        "Not specified"
                );

        String duration =
                firstNonBlank(
                        extractDuration(description),
                        "Not specified"
                );

        String requiredSkills =
                extractSkills(description);

        JsonObject urls =
                getObject(
                        posting,
                        "urls"
                );

        String jobPage =
                getString(
                        urls,
                        "show"
                );

        String applyUrl =
                getString(
                        urls,
                        "apply"
                );

        String jobLink =
                firstNonBlank(
                        applyUrl,
                        jobPage
                );

        LocalDate postedDate =
                parseCreatedDate(
                        posting,
                        "createdAt"
                );

        String sourceUrl =
                "https://jobs.lever.co/"
                        + board.slug;

        String recruiterEmail =
                extractEmail(description);

        String recruiterLinkedin =
                extractLinkedIn(description);

        String recruiterName =
                "";

        String recruiterRole =
                "";

        String contactSource =
                (!recruiterEmail.isBlank() || !recruiterLinkedin.isBlank())
                        ? jobLink
                        : "";

        return new Internship(
                0,
                board.companyName,
                title,
                category,
                location,
                workMode,
                stipend,
                duration,
                requiredSkills,
                null,
                jobLink,
                recruiterName,
                recruiterRole,
                recruiterEmail,
                recruiterLinkedin,
                contactSource,
                "Lever",
                externalId,
                sourceUrl,
                postedDate,
                LocalDateTime.now()
        );
    }

    private LocalDate parseCreatedDate(
            JsonObject object,
            String key
    ) {

        try {

            JsonElement element =
                    object.get(key);

            if (element == null || element.isJsonNull()) {
                return null;
            }

            long milliseconds =
                    element.getAsLong();

            return Instant.ofEpochMilli(milliseconds)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

        } catch (Exception ignored) {
            return null;
        }
    }

    private String extractEmail(String text) {

        Matcher matcher =
                Pattern.compile(
                        "(?i)\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b"
                ).matcher(safe(text));

        return matcher.find()
                ? matcher.group()
                : "";
    }

    private String extractLinkedIn(String text) {

        Matcher matcher =
                Pattern.compile(
                        "https?://(?:www\\.)?linkedin\\.com/[^\\s<\\\"]+"
                ).matcher(safe(text));

        return matcher.find()
                ? matcher.group()
                : "";
    }

    private String extractStipend(String text) {

        Matcher matcher =
                Pattern.compile(
                        "(?i)(?:₹|INR\\s*)[0-9][0-9,]*(?:\\s*(?:per|/)?\\s*(?:month|mo|week|year|annum))?"
                ).matcher(safe(text));

        return matcher.find()
                ? matcher.group().trim()
                : "";
    }

    private String extractDuration(String text) {

        Matcher matcher =
                Pattern.compile(
                        "(?i)\\b(?:for\\s+)?[0-9]+(?:\\.[0-9]+)?\\s*(?:months?|weeks?)\\b"
                ).matcher(safe(text));

        return matcher.find()
                ? matcher.group().trim()
                : "";
    }

    private String extractSkills(String text) {

        String[] knownSkills = {
                "Java", "Python", "C++", "C", "JavaScript", "TypeScript",
                "React", "Node.js", "Spring", "Spring Boot", "SQL", "MySQL",
                "PostgreSQL", "MongoDB", "AWS", "Azure", "GCP", "Docker",
                "Kubernetes", "Git", "GitHub", "REST", "API", "HTML", "CSS",
                "Machine Learning", "Deep Learning", "NLP", "TensorFlow",
                "PyTorch", "Spark", "Hadoop", "Selenium", "Figma", "Excel",
                "Power BI", "Tableau", "Data Analysis", "Data Science"
        };

        List<String> found = new ArrayList<>();

        String searchable =
                safe(text).toLowerCase(Locale.ROOT);

        for (String skill : knownSkills) {

            String lower =
                    skill.toLowerCase(Locale.ROOT);

            Pattern pattern =
                    Pattern.compile(
                            "(?<![a-z0-9])"
                                    + Pattern.quote(lower)
                                    + "(?![a-z0-9])"
                    );

            if (pattern.matcher(searchable).find()
                    && !found.contains(skill)) {
                found.add(skill);
            }
        }

        return String.join(
                ", ",
                found
        );
    }

    private String formatWorkMode(String workplaceType) {

        String value =
                safe(workplaceType)
                        .trim()
                        .toLowerCase(Locale.ROOT);

        return switch (value) {
            case "remote" -> "Remote";
            case "hybrid" -> "Hybrid";
            case "onsite", "on-site" -> "On-site";
            default -> "";
        };
    }

    private String stripHtml(String html) {

        if (html == null || html.isBlank()) {
            return "";
        }

        String text =
                html.replaceAll(
                        "(?i)<br\\s*/?>",
                        "\\n"
                );

        text =
                text.replaceAll(
                        "(?i)</p>|</div>|</li>|</h[1-6]>",
                        "\\n"
                );

        text =
                text.replaceAll(
                        "<[^>]+>",
                        " "
                );

        text =
                text.replace(
                        "&amp;",
                        "&"
                ).replace(
                        "&nbsp;",
                        " "
                ).replace(
                        "&lt;",
                        "<"
                ).replace(
                        "&gt;",
                        ">"
                );

        return text
                .replaceAll(
                        "[ \\t]+",
                        " "
                )
                .replaceAll(
                        "\\n{3,}",
                        "\\n\\n"
                )
                .trim();
    }

    private JsonObject getObject(
            JsonObject object,
            String key
    ) {

        if (object == null
                || !object.has(key)
                || object.get(key).isJsonNull()
                || !object.get(key).isJsonObject()) {
            return new JsonObject();
        }

        return object.getAsJsonObject(key);
    }

    private String getString(
            JsonObject object,
            String key
    ) {

        if (object == null
                || !object.has(key)
                || object.get(key).isJsonNull()) {
            return "";
        }

        try {
            return object.get(key).getAsString().trim();
        } catch (Exception ignored) {
            return "";
        }
    }

    private String firstNonBlank(String... values) {

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return "";
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private static class CompanyBoard {

        private final String slug;
        private final String companyName;

        private CompanyBoard(
                String slug,
                String companyName
        ) {
            this.slug = slug;
            this.companyName = companyName;
        }
    }
}
