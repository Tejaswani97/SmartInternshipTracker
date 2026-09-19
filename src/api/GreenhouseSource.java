package api;

import dao.InternshipDAO;
import model.Internship;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GreenhouseSource {

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        GreenhouseSource source =
                new GreenhouseSource();

        List<Internship> internships =
                source.fetchAllInternships();

        InternshipDAO dao =
                new InternshipDAO();

        System.out.println();
        System.out.println("==============================");
        System.out.println("SAVING GREENHOUSE INTERNSHIPS");
        System.out.println("==============================");

        for (Internship internship : internships) {

            String result =
                    dao.saveOrUpdate(
                            internship
                    );

            System.out.println(
                    result
                            + ": "
                            + internship.getCompanyName()
                            + " - "
                            + internship.getJobRole()
            );
        }

        System.out.println();
        System.out.println(
                "Total internships collected: "
                        + internships.size()
        );
    }


    // =========================================================
    // FETCH FROM ALL GREENHOUSE COMPANIES
    // =========================================================

    public List<Internship> fetchAllInternships() {

        List<Internship> allInternships =
                new ArrayList<>();


        // -----------------------------------------------------
        // NIRMATA
        // -----------------------------------------------------

        fetchCompanyInternships(
                "nirmata",
                "Nirmata",
                allInternships
        );


        // -----------------------------------------------------
        // INSTAWORK
        // -----------------------------------------------------

        fetchCompanyInternships(
                "instawork",
                "Instawork",
                allInternships
        );


        // -----------------------------------------------------
        // GRAPHCORE
        // -----------------------------------------------------

        fetchCompanyInternships(
                "graphcore",
                "Graphcore",
                allInternships
        );


        return allInternships;
    }


    // =========================================================
    // FETCH ONE COMPANY
    // =========================================================

    private void fetchCompanyInternships(
            String boardToken,
            String companyName,
            List<Internship> allInternships
    ) {

        String sourceUrl =
                "https://job-boards.greenhouse.io/"
                        + boardToken;

        String apiUrl =
                "https://boards-api.greenhouse.io/v1/boards/"
                        + boardToken
                        + "/jobs?content=true";


        System.out.println();
        System.out.println(
                "================================"
        );

        System.out.println(
                "Fetching: "
                        + companyName
        );

        System.out.println(
                "================================"
        );


        try {

            // -------------------------------------------------
            // HTTP CLIENT
            // -------------------------------------------------

            HttpClient client =
                    HttpClient.newHttpClient();


            // -------------------------------------------------
            // REQUEST
            // -------------------------------------------------

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            apiUrl
                                    )
                            )
                            .GET()
                            .build();


            // -------------------------------------------------
            // SEND REQUEST
            // -------------------------------------------------

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );


            System.out.println(
                    "Status code: "
                            + response.statusCode()
            );


            // -------------------------------------------------
            // CHECK RESPONSE
            // -------------------------------------------------

            if (
                    response.statusCode()
                            != 200
            ) {

                System.out.println(
                        "Could not fetch "
                                + companyName
                );

                return;
            }


            // -------------------------------------------------
            // PARSE JSON
            // -------------------------------------------------

            JsonObject data =
                    JsonParser.parseString(
                            response.body()
                    ).getAsJsonObject();


            JsonArray jobs =
                    data.getAsJsonArray(
                            "jobs"
                    );


            System.out.println(
                    "Total jobs received: "
                            + jobs.size()
            );


            int internshipCount = 0;


            // -------------------------------------------------
            // PROCESS JOBS
            // -------------------------------------------------

            for (
                    JsonElement element :
                    jobs
            ) {

                JsonObject job =
                        element.getAsJsonObject();


                // -------------------------------------------------
                // JOB TITLE
                // -------------------------------------------------

                String title =
                        getString(
                                job,
                                "title"
                        );


                // -------------------------------------------------
                // ONLY REAL INTERNSHIPS
                // -------------------------------------------------

                if (
                        !isInternshipTitle(
                                title
                        )
                ) {

                    continue;
                }


                internshipCount++;


                // -------------------------------------------------
                // EXTERNAL JOB ID
                // -------------------------------------------------

                String externalJobId =
                        "";

                if (
                        job.has("id")
                        && !job.get(
                                "id"
                        ).isJsonNull()
                ) {

                    externalJobId =
                            String.valueOf(
                                    job.get(
                                            "id"
                                    ).getAsLong()
                            );
                }


                // -------------------------------------------------
                // JOB LINK
                // -------------------------------------------------

                String jobLink =
                        getString(
                                job,
                                "absolute_url"
                        );


                // -------------------------------------------------
                // DESCRIPTION
                // -------------------------------------------------

                String content =
                        getString(
                                job,
                                "content"
                        );


                // -------------------------------------------------
                // REQUIRED SKILLS
                // -------------------------------------------------

                String requiredSkills =
                        extractRequiredSkills(
                                content
                        );


                // -------------------------------------------------
                // PUBLIC EMAIL
                // -------------------------------------------------

                String recruiterEmail =
                        extractEmail(
                                content
                        );


                // -------------------------------------------------
                // PUBLIC LINKEDIN
                // -------------------------------------------------

                String recruiterLinkedin =
                        extractLinkedIn(
                                content
                        );


                String contactSource =
                        "";

                if (
                        !recruiterEmail.isBlank()
                        ||
                        !recruiterLinkedin.isBlank()
                ) {

                    contactSource =
                            "Greenhouse job description";
                }


                // -------------------------------------------------
                // LOCATION
                // -------------------------------------------------

                String location =
                        "Not specified";


                if (
                        job.has("location")
                        && job.get(
                                "location"
                        ).isJsonObject()
                ) {

                    JsonObject locationObject =
                            job.getAsJsonObject(
                                    "location"
                            );

                    String locationName =
                            getString(
                                    locationObject,
                                    "name"
                            );


                    if (
                            !locationName.isBlank()
                    ) {

                        location =
                                locationName;
                    }
                }


                // -------------------------------------------------
                // WORK MODE
                // -------------------------------------------------

                String workMode =
                        detectWorkMode(
                                location
                        );


                // -------------------------------------------------
                // CATEGORY
                // -------------------------------------------------

                String category =
                        "Internship";


                if (
                        job.has("departments")
                        && job.get(
                                "departments"
                        ).isJsonArray()
                ) {

                    JsonArray departments =
                            job.getAsJsonArray(
                                    "departments"
                            );


                    if (
                            departments.size()
                                    > 0
                    ) {

                        JsonObject department =
                                departments
                                        .get(0)
                                        .getAsJsonObject();


                        String departmentName =
                                getString(
                                        department,
                                        "name"
                                );


                        if (
                                !departmentName
                                        .isBlank()
                        ) {

                            category =
                                    departmentName;
                        }
                    }
                }


                // -------------------------------------------------
                // POSTED DATE
                // -------------------------------------------------

                LocalDate postedDate =
                        parseDate(
                                job,
                                "first_published"
                        );


                // -------------------------------------------------
                // DEADLINE
                // -------------------------------------------------

                LocalDate deadline =
                        parseDate(
                                job,
                                "application_deadline"
                        );


                // -------------------------------------------------
                // CREATE INTERNSHIP OBJECT
                // -------------------------------------------------

                Internship internship =
                        new Internship(

                                0,

                                companyName,

                                title,

                                category,

                                location,

                                workMode,

                                "Not specified",

                                "Not specified",

                                requiredSkills,

                                deadline,

                                jobLink,

                                "",

                                "",

                                recruiterEmail,

                                recruiterLinkedin,

                                contactSource,

                                "Greenhouse",

                                externalJobId,

                                sourceUrl,

                                postedDate,

                                LocalDateTime.now()
                        );


                allInternships.add(
                        internship
                );


                // -------------------------------------------------
                // DISPLAY
                // -------------------------------------------------

                System.out.println(
                        "----------------------------"
                );

                System.out.println(
                        "Company: "
                                + companyName
                );

                System.out.println(
                        "Role: "
                                + title
                );

                System.out.println(
                        "Location: "
                                + location
                );

                System.out.println(
                        "Work Mode: "
                                + workMode
                );

                System.out.println(
                        "Skills: "
                                + (
                                        requiredSkills.isBlank()
                                                ? "Not detected"
                                                : requiredSkills
                                )
                );

                System.out.println(
                        "Email: "
                                + (
                                        recruiterEmail.isBlank()
                                                ? "Not published"
                                                : recruiterEmail
                                )
                );

                System.out.println(
                        "LinkedIn: "
                                + (
                                        recruiterLinkedin.isBlank()
                                                ? "Not published"
                                                : recruiterLinkedin
                                )
                );

                System.out.println(
                        "Job ID: "
                                + externalJobId
                );
            }


            System.out.println(
                    "Internships found from "
                            + companyName
                            + ": "
                            + internshipCount
            );


        } catch (Exception e) {

            System.out.println(
                    "Error while fetching "
                            + companyName
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // CHECK WHETHER TITLE IS REALLY AN INTERNSHIP
    // =========================================================

    private boolean isInternshipTitle(
            String title
    ) {

        if (
                title == null
                || title.isBlank()
        ) {

            return false;
        }


        Pattern pattern =
                Pattern.compile(
                        "\\b(intern|interns|internship)\\b",
                        Pattern.CASE_INSENSITIVE
                );


        return pattern
                .matcher(title)
                .find();
    }


    // =========================================================
    // EXTRACT REQUIRED SKILLS
    // =========================================================

    private String extractRequiredSkills(
            String content
    ) {

        if (
                content == null
                || content.isBlank()
        ) {

            return "";
        }


        String text =
                content
                        .replaceAll(
                                "<[^>]*>",
                                " "
                        )
                        .replace(
                                "&amp;",
                                "&"
                        )
                        .replace(
                                "&nbsp;",
                                " "
                        )
                        .toLowerCase();


        String[][] skillNames = {

                {
                        "java",
                        "Java"
                },

                {
                        "python",
                        "Python"
                },

                {
                        "sql",
                        "SQL"
                },

                {
                        "machine learning",
                        "Machine Learning"
                },

                {
                        "data science",
                        "Data Science"
                },

                {
                        "artificial intelligence",
                        "Artificial Intelligence"
                },

                {
                        "generative ai",
                        "Generative AI"
                },

                {
                        "ai agents",
                        "AI Agents"
                },

                {
                        "llm",
                        "LLM"
                },

                {
                        "large language models",
                        "Large Language Models"
                },

                {
                        "deep learning",
                        "Deep Learning"
                },

                {
                        "tensorflow",
                        "TensorFlow"
                },

                {
                        "pytorch",
                        "PyTorch"
                },

                {
                        "kubernetes",
                        "Kubernetes"
                },

                {
                        "aws",
                        "AWS"
                },

                {
                        "gcp",
                        "GCP"
                },

                {
                        "docker",
                        "Docker"
                },

                {
                        "react",
                        "React"
                },

                {
                        "javascript",
                        "JavaScript"
                },

                {
                        "typescript",
                        "TypeScript"
                },

                {
                        "c++",
                        "C++"
                },

                {
                        "c#",
                        "C#"
                },

                {
                        "git",
                        "Git"
                },

                {
                        "linux",
                        "Linux"
                },

                {
                        "kotlin",
                        "Kotlin"
                },

                {
                        "swift",
                        "Swift"
                },

                {
                        "ruby",
                        "Ruby"
                },

                {
                        "go",
                        "Go"
                }
        };


        Set<String> foundSkills =
                new LinkedHashSet<>();


        for (
                String[] skill :
                skillNames
        ) {

            if (
                    containsSkill(
                            text,
                            skill[0]
                    )
            ) {

                foundSkills.add(
                        skill[1]
                );
            }
        }


        return String.join(
                ", ",
                foundSkills
        );
    }


    // =========================================================
    // SAFE SKILL MATCH
    // =========================================================

    private boolean containsSkill(
            String text,
            String skill
    ) {

        String regex =
                "(?i)(?<![A-Za-z0-9])"
                        + Pattern.quote(skill)
                        + "(?![A-Za-z0-9])";


        return Pattern
                .compile(
                        regex
                )
                .matcher(text)
                .find();
    }


    // =========================================================
    // EXTRACT EMAIL
    // =========================================================

    private String extractEmail(
            String content
    ) {

        if (
                content == null
                || content.isBlank()
        ) {

            return "";
        }


        Pattern pattern =
                Pattern.compile(
                        "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
                );


        Matcher matcher =
                pattern.matcher(
                        content
                );


        if (matcher.find()) {

            return matcher.group();
        }


        return "";
    }


    // =========================================================
    // EXTRACT LINKEDIN
    // =========================================================

    private String extractLinkedIn(
            String content
    ) {

        if (
                content == null
                || content.isBlank()
        ) {

            return "";
        }


        Pattern pattern =
                Pattern.compile(
                        "https?://(?:www\\.)?linkedin\\.com/[^\\s\"'<>]+",
                        Pattern.CASE_INSENSITIVE
                );


        Matcher matcher =
                pattern.matcher(
                        content
                );


        if (matcher.find()) {

            return matcher.group();
        }


        return "";
    }


    // =========================================================
    // PARSE DATE
    // =========================================================

    private LocalDate parseDate(
            JsonObject job,
            String field
    ) {

        if (
                !job.has(field)
                || job.get(
                        field
                ).isJsonNull()
        ) {

            return null;
        }


        String value =
                job.get(
                        field
                ).getAsString();


        if (
                value == null
                || value.length() < 10
        ) {

            return null;
        }


        try {

            return LocalDate.parse(
                    value.substring(
                            0,
                            10
                    )
            );

        } catch (Exception e) {

            return null;
        }
    }


    // =========================================================
    // SAFE STRING READER
    // =========================================================

    private String getString(
            JsonObject object,
            String field
    ) {

        if (
                object == null
                || !object.has(field)
                || object.get(
                        field
                ).isJsonNull()
        ) {

            return "";
        }


        return object.get(
                field
        ).getAsString();
    }


    // =========================================================
    // DETECT WORK MODE
    // =========================================================

    private String detectWorkMode(
            String location
    ) {

        if (
                location == null
                || location.isBlank()
        ) {

            return "Not specified";
        }


        String lower =
                location.toLowerCase();


        if (
                lower.contains(
                        "remote"
                )
        ) {

            return "Remote";
        }


        if (
                lower.contains(
                        "hybrid"
                )
        ) {

            return "Hybrid";
        }


        if (
                lower.contains(
                        "on-site"
                )
                ||
                lower.contains(
                        "onsite"
                )
        ) {

            return "On-site";
        }


        return "Not specified";
    }
}