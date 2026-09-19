package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailService {

    // =========================================================
    // SEND OTP EMAIL
    // =========================================================

    public static boolean sendOtp(
            String recipientEmail,
            String otp
    ) {

        try {

            // -------------------------------------------------
            // LOAD CONFIGURATION
            // -------------------------------------------------

            Properties properties =
                    new Properties();

            try (
                    InputStream input =
                            new FileInputStream(
                                    "config.properties"
                            )
            ) {

                properties.load(input);
            }


            String apiKey =
                    properties.getProperty(
                            "BREVO_API_KEY"
                    );

            String senderEmail =
                    properties.getProperty(
                            "BREVO_SENDER_EMAIL"
                    );

            String senderName =
                    properties.getProperty(
                            "BREVO_SENDER_NAME"
                    );


            // -------------------------------------------------
            // CHECK CONFIGURATION
            // -------------------------------------------------

            if (
                    apiKey == null
                    || senderEmail == null
                    || senderName == null
                    || apiKey.isBlank()
                    || senderEmail.isBlank()
                    || senderName.isBlank()
            ) {

                System.out.println(
                        "Brevo configuration is missing!"
                );

                return false;
            }


            // -------------------------------------------------
            // CREATE EMAIL JSON
            // -------------------------------------------------

            JsonObject sender =
                    new JsonObject();

            sender.addProperty(
                    "name",
                    senderName
            );

            sender.addProperty(
                    "email",
                    senderEmail
            );


            JsonObject recipient =
                    new JsonObject();

            recipient.addProperty(
                    "email",
                    recipientEmail
            );


            JsonArray recipients =
                    new JsonArray();

            recipients.add(
                    recipient
            );


            JsonObject email =
                    new JsonObject();

            email.add(
                    "sender",
                    sender
            );

            email.add(
                    "to",
                    recipients
            );

            email.addProperty(
                    "subject",
                    "Smart Internship Tracker - Email Verification"
            );


            String message =
                    "Your Smart Internship Tracker verification code is: "
                    + otp
                    + "\n\n"
                    + "This OTP is valid for 5 minutes."
                    + "\n\n"
                    + "If you did not create an account, ignore this email.";


            email.addProperty(
                    "textContent",
                    message
            );


            // -------------------------------------------------
            // CREATE HTTP REQUEST
            // -------------------------------------------------

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "https://api.brevo.com/v3/smtp/email"
                                    )
                            )
                            .header(
                                    "accept",
                                    "application/json"
                            )
                            .header(
                                    "api-key",
                                    apiKey
                            )
                            .header(
                                    "content-type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            email.toString(),
                                            StandardCharsets.UTF_8
                                    )
                            )
                            .build();


            // -------------------------------------------------
            // SEND REQUEST
            // -------------------------------------------------

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );


            System.out.println(
                    "Brevo status code: "
                    + response.statusCode()
            );


            // Brevo normally returns 201 when
            // the email has been accepted.

            if (
                    response.statusCode() == 201
            ) {

                System.out.println(
                        "OTP email sent successfully!"
                );

                return true;
            }


            // -------------------------------------------------
            // FAILED REQUEST
            // -------------------------------------------------

            System.out.println(
                    "Brevo email failed!"
            );

            System.out.println(
                    response.body()
            );

            return false;

        } catch (Exception e) {

            System.out.println(
                    "Could not send OTP email!"
            );

            e.printStackTrace();

            return false;
        }
    }
    
}