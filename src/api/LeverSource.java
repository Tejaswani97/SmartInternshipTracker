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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeverSource {

    public static void main(String[] args) {

        LeverSource source =
                new LeverSource();

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
    // FETCH LEVER INTERNSHIPS
    // =========================================================

    public List<Internship> fetchInternships() {

        List<Internship> internships =
                new ArrayList<>();

        String companySlug =
                "epifi";

        String companyName =
                "Epifi";

        String sourceUrl =
                "https://jobs.lever.co/"
                + companySlug;

        String apiUrl =
                "https://api.lever.co/v0/postings/"
                + companySlug
                + "?mode=json";


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
                        "Lever request failed!"
                );

                System.out.println(
                        response.body()
                );

                return internships;
            }


            // -------------------------------------------------
            // PARSE JSON
            // -------------------------------------------------

            JsonArray jobs =
                    JsonParser.parseString(
                            response.body()
                    ).getAsJsonArray();


            System.out.println(
                    "Total jobs received: "
                    + jobs.size()
            );


            // -------------------------------------------------
            // PROCESS EACH JOB
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
                                "text"
                        );


                // Only internships
                if (
                        !title
                                .toLowerCase()
                                .contains("intern")
                ) {

                    continue;
                }


                // -------------------------------------------------
                // EXTERNAL ID
                // -------------------------------------------------

                String externalJobId =
                        getString(
                                job,
                                "id"
                        );


                // -------------------------------------------------
                // LOCATION / CATEGORY
                // -------------------------------------------------

                String location =
                        "Not specified";

                String department =
                        "Internship";


                if (
                        job.has("categories")
                        && job.get(
                                "categories"
                        ).isJsonObject()
                ) {

                    JsonObject categories =
                            job.getAsJsonObject(
                                    "categories"
                            );


                    String categoryLocation =
                            getString(
                                    categories,
                                    "location"
                            );

                    String categoryDepartment =
                            getString(
                                    categories,
                                    "department"
                            );


                    if (
                            !categoryLocation
                                    .isBlank()
                    ) {

                        location =
                                categoryLocation;
                    }


                    if (
                            !categoryDepartment
                                    .isBlank()
                    ) {

                        department =
                                categoryDepartment;
                    }
                }


                // -------------------------------------------------
                // WORK MODE
                // -------------------------------------------------

                String workplaceType =
                        getString(
                                job,
                                "workplaceType"
                        );

                String workMode =
                        detectWorkMode(
                                workplaceType,
                                location
                        );


                // -------------------------------------------------
                // JOB LINKS
                // -------------------------------------------------

                String hostedUrl =
                        getString(
                                job,
                                "hostedUrl"
                        );

                String applyUrl =
                        getString(
                                job,
                                "applyUrl"
                        );

                String jobLink =
                        !hostedUrl.isBlank()
                                ? hostedUrl
                                : applyUrl;


                // -------------------------------------------------
                // DESCRIPTION
                // -------------------------------------------------

                String description =
                        getString(
                                job,
                                "descriptionPlain"
                        );


                // -------------------------------------------------
                // REAL SKILLS FROM DESCRIPTION
                // -------------------------------------------------

                String requiredSkills =
                        extractRequiredSkills(
                                description
                        );


                // -------------------------------------------------
                // PUBLIC EMAIL
                // -------------------------------------------------

                String recruiterEmail =
                        extractEmail(
                                description
                        );


                // -------------------------------------------------
                // PUBLIC LINKEDIN
                // -------------------------------------------------

                String recruiterLinkedin =
                        extractLinkedIn(
                                description
                        );


                String contactSource =
                        "";

                if (
                        !recruiterEmail.isBlank()
                        || !recruiterLinkedin.isBlank()
                ) {

                    contactSource =
                            "Lever job description";
                }


                // -------------------------------------------------
                // POSTED DATE
                // -------------------------------------------------

                LocalDate postedDate =
                        parseCreatedAt(
                                job
                        );


                // -------------------------------------------------
                // CREATE INTERNSHIP
                // -------------------------------------------------

                Internship internship =
                        new Internship(

                                0,

                                companyName,

                                title,

                                department,

                                location,

                                workMode,

                                "Not specified",

                                "Not specified",

                                requiredSkills,

                                null,

                                jobLink,

                                "",

                                "",

                                recruiterEmail,

                                recruiterLinkedin,

                                contactSource,

                                "Lever",

                                externalJobId,

                                sourceUrl,

                                postedDate,

                                LocalDateTime.now()
                        );


                internships.add(
                        internship
                );


                // -------------------------------------------------
                // SHOW WHAT WILL BE SAVED
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
                        "Work Mode: "
                        + workMode
                );

                System.out.println(
                        "Skills: "
                        + requiredSkills
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
                        "Apply URL: "
                        + jobLink
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return internships;
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
                || object.get(field)
                        .isJsonNull()
        ) {

            return "";
        }

        return object.get(
                field
        ).getAsString();
    }


    // =========================================================
    // WORK MODE
    // =========================================================

    private String detectWorkMode(
            String workplaceType,
            String location
    ) {

        String type =
                workplaceType == null
                        ? ""
                        : workplaceType.toLowerCase();

        String place =
                location == null
                        ? ""
                        : location.toLowerCase();


        if (
                type.contains("remote")
                || place.contains("remote")
        ) {

            return "Remote";
        }


        if (
                type.contains("hybrid")
                || place.contains("hybrid")
        ) {

            return "Hybrid";
        }


        if (
                type.contains("on-site")
                || type.contains("onsite")
                || place.contains("on-site")
                || place.contains("onsite")
        ) {

            return "On-site";
        }


        return "Not specified";
    }


    // =========================================================
    // EXTRACT SKILLS
    // =========================================================

    private String extractRequiredSkills(
            String description
    ) {

        if (
                description == null
                || description.isBlank()
        ) {

            return "";
        }


        String text =
                description.toLowerCase();


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
                        "llm",
                        "LLM"
                },

                {
                        "large language model",
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
            String description
    ) {

        if (
                description == null
                || description.isBlank()
        ) {

            return "";
        }


        Pattern pattern =
                Pattern.compile(
                        "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
                );


        Matcher matcher =
                pattern.matcher(
                        description
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
            String description
    ) {

        if (
                description == null
                || description.isBlank()
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
                        description
                );


        if (matcher.find()) {

            return matcher.group();
        }


        return "";
    }


    // =========================================================
    // PARSE LEVER CREATED AT
    // =========================================================

    private LocalDate parseCreatedAt(
            JsonObject job
    ) {

        if (
                !job.has("createdAt")
                || job.get(
                        "createdAt"
                ).isJsonNull()
        ) {

            return null;
        }


        try {

            long timestamp =
                    job.get(
                            "createdAt"
                    ).getAsLong();


            return Instant
                    .ofEpochMilli(
                            timestamp
                    )
                    .atZone(
                            ZoneId.systemDefault()
                    )
                    .toLocalDate();

        } catch (Exception e) {

            return null;
        }
    }
}