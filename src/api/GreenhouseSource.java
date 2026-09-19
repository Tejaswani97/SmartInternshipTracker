package api;

import dao.InternshipDAO;
import model.Internship;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GreenhouseSource {

    public static void main(String[] args) {

        GreenhouseSource source =
                new GreenhouseSource();

        List<Internship> internships =
                source.fetchInternships();

        InternshipDAO dao =
                new InternshipDAO();

        for (Internship internship : internships) {

            String result =
                    dao.saveOrUpdate(
                            internship
                    );

            System.out.println(
                    result
                    + ": "
                    + internship.getJobRole()
            );
        }
    }


    // =========================================================
    // FETCH INTERNSHIPS
    // =========================================================

    public List<Internship> fetchInternships() {

        List<Internship> internships =
                new ArrayList<>();

        String boardToken =
                "nirmata";

        String companyName =
                "Nirmata";

        String boardUrl =
                "https://job-boards.greenhouse.io/"
                + boardToken;

        String apiUrl =
                "https://boards-api.greenhouse.io/v1/boards/"
                + boardToken
                + "/jobs?content=true";

        try {

            // -------------------------------------------------
            // SEND REQUEST
            // -------------------------------------------------

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(apiUrl)
                            )
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );


            System.out.println(
                    "Status code: "
                    + response.statusCode()
            );


            if (response.statusCode() != 200) {

                System.out.println(
                        "Greenhouse request failed!"
                );

                return internships;
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


            // -------------------------------------------------
            // PROCESS JOBS
            // -------------------------------------------------

            for (
                    JsonElement element :
                    jobs
            ) {

                JsonObject job =
                        element.getAsJsonObject();

                String title =
                        getString(
                                job,
                                "title"
                        );


                // -------------------------------------------------
                // ONLY INTERNSHIPS
                // -------------------------------------------------

                if (
                        !title
                                .toLowerCase()
                                .contains("intern")
                ) {

                    continue;
                }


                // -------------------------------------------------
                // JOB ID
                // -------------------------------------------------

                String externalJobId =
                        String.valueOf(
                                job.get("id")
                                        .getAsLong()
                        );


                // -------------------------------------------------
                // JOB LINK
                // -------------------------------------------------

                String jobLink =
                        getString(
                                job,
                                "absolute_url"
                        );


                // -------------------------------------------------
                // FULL DESCRIPTION
                // -------------------------------------------------

                String content =
                        getString(
                                job,
                                "content"
                        );


                // -------------------------------------------------
                // EXTRACT REAL REQUIRED SKILLS
                // -------------------------------------------------

                String requiredSkills =
                        extractRequiredSkills(
                                content
                        );


                // -------------------------------------------------
                // EXTRACT PUBLIC EMAIL
                // -------------------------------------------------

                String recruiterEmail =
                        extractEmail(
                                content
                        );


                // -------------------------------------------------
                // EXTRACT PUBLIC LINKEDIN URL
                // -------------------------------------------------

                String recruiterLinkedin =
                        extractLinkedIn(
                                content
                        );


                String contactSource = "";

                if (
                        !recruiterEmail.isBlank()
                        || !recruiterLinkedin.isBlank()
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
                        && !job.get(
                                "location"
                        ).isJsonNull()
                ) {

                    JsonObject locationObject =
                            job.getAsJsonObject(
                                    "location"
                            );

                    location =
                            getString(
                                    locationObject,
                                    "name"
                            );
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
                                departmentName
                                        != null
                                && !departmentName
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
                // CREATE INTERNSHIP
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

                                boardUrl,

                                postedDate,

                                LocalDateTime.now()
                        );


                internships.add(
                        internship
                );


                // -------------------------------------------------
                // DISPLAY WHAT WE IMPORTED
                // -------------------------------------------------

                System.out.println(
                        "----------------------------"
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
                        "Skills: "
                        + requiredSkills
                );

                System.out.println(
                        "Recruiter email: "
                        + (
                            recruiterEmail.isBlank()
                            ? "Not published"
                            : recruiterEmail
                        )
                );

                System.out.println(
                        "Recruiter LinkedIn: "
                        + (
                            recruiterLinkedin.isBlank()
                            ? "Not published"
                            : recruiterLinkedin
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return internships;
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
                        "machine learning",
                        "Machine Learning"
                },

                {
                        "data science",
                        "Data Science"
                },

                {
                        "ai agents",
                        "AI Agents"
                },

                {
                        "mcp servers",
                        "MCP Servers"
                },

                {
                        "large language models",
                        "Large Language Models"
                },

                {
                        "generative ai",
                        "Generative AI"
                },

                {
                        "retrieval-augmented generation",
                        "Retrieval-Augmented Generation"
                },

                {
                        "model tuning",
                        "Model Tuning"
                },

                {
                        "slms",
                        "SLMs"
                },

                {
                        "fine-tuning",
                        "Fine-Tuning"
                },

                {
                        "kubernetes",
                        "Kubernetes"
                },

                {
                        "cloud native",
                        "Cloud Native"
                }
        };


        Set<String> foundSkills =
                new LinkedHashSet<>();


        for (
                String[] skill :
                skillNames
        ) {

            if (
                    text.contains(
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
    // EXTRACT LINKEDIN URL
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
                || lower.contains(
                        "onsite"
                )
        ) {

            return "On-site";
        }


        return "Not specified";
    }
}