package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Internship;

public class GreenhouseSource {

    public static void main(String[] args) {

        String boardToken = "nirmata";

        String url =
                "https://boards-api.greenhouse.io/v1/boards/"
                + boardToken
                + "/jobs?content=true";

        try {

            // -------------------------------------------------
            // 1. SEND REQUEST
            // -------------------------------------------------

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Status code: " + response.statusCode()
            );


            // -------------------------------------------------
            // 2. CHECK RESPONSE
            // -------------------------------------------------

            if (response.statusCode() != 200) {

                System.out.println(
                        "Greenhouse request failed!"
                );

                return;
            }


            // -------------------------------------------------
            // 3. PARSE JSON
            // -------------------------------------------------

            JsonObject data =
                    JsonParser.parseString(
                            response.body()
                    ).getAsJsonObject();

            JsonArray jobs =
                    data.getAsJsonArray("jobs");

            System.out.println(
                    "Total jobs received: " + jobs.size()
            );


            // -------------------------------------------------
            // 4. PROCESS EACH JOB
            // -------------------------------------------------

            for (JsonElement element : jobs) {

                JsonObject job =
                        element.getAsJsonObject();

                String title =
                        getString(job, "title");


                // -------------------------------------------------
                // ONLY KEEP INTERNSHIPS
                // -------------------------------------------------

                if (!title.toLowerCase().contains("intern")) {
                    continue;
                }


                // -------------------------------------------------
                // BASIC JOB DATA
                // -------------------------------------------------

                String externalJobId =
                        String.valueOf(
                                job.get("id").getAsLong()
                        );

                String jobLink =
                        getString(
                                job,
                                "absolute_url"
                        );


                // -------------------------------------------------
                // LOCATION
                // -------------------------------------------------

                String location =
                        "Not specified";

                if (
                        job.has("location")
                        && !job.get("location").isJsonNull()
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
                        detectWorkMode(location);


                // -------------------------------------------------
                // CATEGORY
                // -------------------------------------------------

                String category =
                        "Internship";

                if (
                        job.has("departments")
                        && job.get("departments").isJsonArray()
                ) {

                    JsonArray departments =
                            job.getAsJsonArray(
                                    "departments"
                            );

                    if (departments.size() > 0) {

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
                                departmentName != null
                                && !departmentName.isBlank()
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
                        null;

                if (
                        job.has("first_published")
                        && !job.get(
                                "first_published"
                        ).isJsonNull()
                ) {

                    String published =
                            job.get(
                                    "first_published"
                            ).getAsString();

                    if (
                            published != null
                            && published.length() >= 10
                    ) {

                        postedDate =
                                LocalDate.parse(
                                        published.substring(
                                                0,
                                                10
                                        )
                                );
                    }
                }


                // -------------------------------------------------
                // CREATE INTERNSHIP OBJECT
                // -------------------------------------------------

                Internship internship =
                        new Internship(

                                0,

                                "Nirmata",

                                title,

                                category,

                                location,

                                workMode,

                                "Not specified",

                                "Not specified",

                                "",

                                null,

                                jobLink,

                                "",

                                "",

                                "",

                                "",

                                "",

                                "Greenhouse",

                                externalJobId,

                                url,

                                postedDate,

                                LocalDateTime.now()
                        );


                // -------------------------------------------------
                // DISPLAY RESULT
                // -------------------------------------------------

                System.out.println(
                        "----------------------------"
                );

                System.out.println(
                        "Company: "
                        + internship.getCompanyName()
                );

                System.out.println(
                        "Role: "
                        + internship.getJobRole()
                );

                System.out.println(
                        "Category: "
                        + internship.getCategory()
                );

                System.out.println(
                        "Location: "
                        + internship.getLocation()
                );

                System.out.println(
                        "Work Mode: "
                        + internship.getWorkMode()
                );

                System.out.println(
                        "External ID: "
                        + internship.getExternalJobId()
                );

                System.out.println(
                        "Posted Date: "
                        + internship.getPostedDate()
                );

                System.out.println(
                        "Job Link: "
                        + internship.getJobLink()
                );

                System.out.println(
                        "Source: "
                        + internship.getSourceName()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================================================
    // SAFE STRING READER
    // =========================================================

    private static String getString(
            JsonObject object,
            String field
    ) {

        if (
                object == null
                || !object.has(field)
                || object.get(field).isJsonNull()
        ) {
            return "";
        }

        return object.get(field).getAsString();
    }


    // =========================================================
    // DETECT WORK MODE
    // =========================================================

    private static String detectWorkMode(
            String location
    ) {

        if (location == null) {
            return "Not specified";
        }

        String lower =
                location.toLowerCase();

        if (lower.contains("remote")) {
            return "Remote";
        }

        if (lower.contains("hybrid")) {
            return "Hybrid";
        }

        if (
                lower.contains("on-site")
                || lower.contains("onsite")
        ) {
            return "On-site";
        }

        return "Not specified";
    }
}