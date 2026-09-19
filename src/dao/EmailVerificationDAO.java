package dao;

import util.DatabaseConnection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EmailVerificationDAO {

    // =========================================================
    // SAVE OTP VERIFICATION
    // =========================================================

    public boolean saveVerification(
            String name,
            String email,
            String password,
            String otp
    ) {

        // Remove any previous pending verification
        // for the same email.
        String deleteSql = """
                DELETE FROM email_verifications
                WHERE email = ?
                """;

        String insertSql = """
                INSERT INTO email_verifications
                (
                    name,
                    email,
                    password,
                    otp_hash,
                    expires_at
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            // -------------------------------------------------
            // DELETE OLD OTP
            // -------------------------------------------------

            try (
                    PreparedStatement deleteStatement =
                            connection.prepareStatement(
                                    deleteSql
                            )
            ) {

                deleteStatement.setString(
                        1,
                        email
                );

                deleteStatement.executeUpdate();
            }


            // -------------------------------------------------
            // CREATE EXPIRY TIME
            // OTP VALID FOR 5 MINUTES
            // -------------------------------------------------

            LocalDateTime expiresAt =
                    LocalDateTime.now()
                            .plusMinutes(5);


            // -------------------------------------------------
            // HASH OTP
            // -------------------------------------------------

            String otpHash =
                    hashOtp(otp);


            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(
                                    insertSql
                            )
            ) {

                insertStatement.setString(
                        1,
                        name
                );

                insertStatement.setString(
                        2,
                        email
                );

                insertStatement.setString(
                        3,
                        password
                );

                insertStatement.setString(
                        4,
                        otpHash
                );

                insertStatement.setObject(
                        5,
                        expiresAt
                );

                int rows =
                        insertStatement.executeUpdate();

                return rows > 0;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to save email verification!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET PENDING VERIFICATION
    // =========================================================

    public VerificationData getVerification(
            String email
    ) {

        String sql = """
                SELECT
                    verification_id,
                    name,
                    email,
                    password,
                    otp_hash,
                    expires_at
                FROM email_verifications
                WHERE email = ?
                ORDER BY verification_id DESC
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    email
            );

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return new VerificationData(

                        resultSet.getInt(
                                "verification_id"
                        ),

                        resultSet.getString(
                                "name"
                        ),

                        resultSet.getString(
                                "email"
                        ),

                        resultSet.getString(
                                "password"
                        ),

                        resultSet.getString(
                                "otp_hash"
                        ),

                        resultSet.getObject(
                                "expires_at",
                                LocalDateTime.class
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to get verification!"
            );

            e.printStackTrace();
        }

        return null;
    }


    // =========================================================
    // DELETE VERIFICATION AFTER SUCCESS
    // =========================================================

    public boolean deleteVerification(
            String email
    ) {

        String sql = """
                DELETE FROM email_verifications
                WHERE email = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    email
            );

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Failed to delete verification!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // CHECK OTP
    // =========================================================

    public boolean verifyOtp(
            String email,
            String otp
    ) {

        VerificationData data =
                getVerification(email);

        if (data == null) {
            return false;
        }


        // OTP expired
        if (
                data.getExpiresAt()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {
            return false;
        }


        String enteredOtpHash =
                hashOtp(otp);

        return enteredOtpHash.equals(
                data.getOtpHash()
        );
    }


    // =========================================================
    // HASH OTP USING SHA-256
    // =========================================================

    private String hashOtp(
            String otp
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            otp.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder hex =
                    new StringBuilder();

            for (byte b : hash) {

                hex.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return hex.toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to hash OTP",
                    e
            );
        }
    }


    // =========================================================
    // INNER DATA CLASS
    // =========================================================

    public static class VerificationData {

        private final int verificationId;
        private final String name;
        private final String email;
        private final String password;
        private final String otpHash;
        private final LocalDateTime expiresAt;


        public VerificationData(
                int verificationId,
                String name,
                String email,
                String password,
                String otpHash,
                LocalDateTime expiresAt
        ) {

            this.verificationId =
                    verificationId;

            this.name =
                    name;

            this.email =
                    email;

            this.password =
                    password;

            this.otpHash =
                    otpHash;

            this.expiresAt =
                    expiresAt;
        }


        public int getVerificationId() {
            return verificationId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getOtpHash() {
            return otpHash;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }
    }
}
