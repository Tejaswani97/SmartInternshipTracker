package dao;

import model.Internship;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InternshipDAO {

    // =========================================================
    // GET ALL ACTIVE / CURRENT INTERNSHIPS
    // =========================================================

    public List<Internship> getAllInternships() {

        List<Internship> internships = new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE is_active = TRUE
                  AND source_name <> 'Manual'
                  AND (deadline IS NULL OR deadline >= CURDATE())
                ORDER BY
                    CASE WHEN posted_date IS NULL THEN 1 ELSE 0 END,
                    posted_date DESC,
                    fetched_at DESC,
                    internship_id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                internships.add(createInternshipFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Failed to fetch internships!");
            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // SEARCH
    // =========================================================

    public List<Internship> searchInternships(String keyword) {

        List<Internship> internships = new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE is_active = TRUE
                  AND source_name <> 'Manual'
                  AND (deadline IS NULL OR deadline >= CURDATE())
                  AND (
                        company_name LIKE ?
                        OR job_role LIKE ?
                        OR category LIKE ?
                        OR location LIKE ?
                        OR work_mode LIKE ?
                        OR stipend LIKE ?
                        OR required_skills LIKE ?
                        OR source_name LIKE ?
                  )
                ORDER BY
                    CASE WHEN posted_date IS NULL THEN 1 ELSE 0 END,
                    posted_date DESC,
                    fetched_at DESC,
                    internship_id DESC
                """;

        String value = "%" + (keyword == null ? "" : keyword.trim()) + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 8; i++) {
                statement.setString(i, value);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    internships.add(createInternshipFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Search internships failed!");
            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // CATEGORY FILTER
    // =========================================================

    public List<Internship> filterByCategory(String category) {
        return filterBySingleColumn("category", category);
    }


    // =========================================================
    // LOCATION FILTER
    // =========================================================

    public List<Internship> filterByLocation(String location) {
        return filterBySingleColumn("location", location);
    }


    // =========================================================
    // WORK MODE FILTER
    // =========================================================

    public List<Internship> filterByWorkMode(String workMode) {
        return filterBySingleColumn("work_mode", workMode);
    }


    private List<Internship> filterBySingleColumn(
            String column,
            String value
    ) {

        List<Internship> internships = new ArrayList<>();

        if (!column.equals("category")
                && !column.equals("location")
                && !column.equals("work_mode")) {
            return internships;
        }

        String sql = """
                SELECT *
                FROM internships
                WHERE is_active = TRUE
                  AND source_name <> 'Manual'
                  AND (deadline IS NULL OR deadline >= CURDATE())
                  AND %s = ?
                ORDER BY
                    CASE WHEN posted_date IS NULL THEN 1 ELSE 0 END,
                    posted_date DESC,
                    fetched_at DESC,
                    internship_id DESC
                """.formatted(column);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    internships.add(createInternshipFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Internship filter failed!");
            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // SAVE / UPDATE ONE INTERNSHIP
    // =========================================================

    public String saveOrUpdate(Internship internship) {

        if (internship == null) {
            return "FAILED";
        }

        String sourceName = safe(internship.getSourceName(), "Unknown");
        String externalJobId = internship.getExternalJobId();

        if (externalJobId == null || externalJobId.isBlank()) {
            return insertWithoutExternalId(internship, sourceName);
        }

        String findSql = """
                SELECT internship_id
                FROM internships
                WHERE source_name = ?
                  AND external_job_id = ?
                LIMIT 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement findStatement = connection.prepareStatement(findSql)) {

            findStatement.setString(1, sourceName);
            findStatement.setString(2, externalJobId);

            try (ResultSet resultSet = findStatement.executeQuery()) {

                if (resultSet.next()) {

                    int internshipId = resultSet.getInt("internship_id");

                    String updateSql = """
                            UPDATE internships
                            SET company_name = ?,
                                job_role = ?,
                                category = ?,
                                location = ?,
                                work_mode = ?,
                                stipend = ?,
                                duration = ?,
                                required_skills = ?,
                                deadline = ?,
                                job_link = ?,
                                recruiter_name = ?,
                                recruiter_role = ?,
                                recruiter_email = ?,
                                recruiter_linkedin = ?,
                                contact_source = ?,
                                source_url = ?,
                                posted_date = ?,
                                fetched_at = CURRENT_TIMESTAMP,
                                is_active = TRUE
                            WHERE internship_id = ?
                            """;

                    try (PreparedStatement statement =
                                 connection.prepareStatement(updateSql)) {

                        bindInternshipFields(statement, internship, sourceName);
                        statement.setString(16, internship.getSourceUrl());
                        if (internship.getPostedDate() == null) {
                            statement.setNull(17, java.sql.Types.DATE);
                        } else {
                            statement.setDate(17, java.sql.Date.valueOf(internship.getPostedDate()));
                        }
                        statement.setInt(18, internshipId);

                        int rows = statement.executeUpdate();
                        return rows > 0 ? "UPDATED" : "FAILED";
                    }
                }
            }

            String insertSql = """
                    INSERT INTO internships
                    (
                        company_name,
                        job_role,
                        category,
                        location,
                        work_mode,
                        stipend,
                        duration,
                        required_skills,
                        deadline,
                        job_link,
                        recruiter_name,
                        recruiter_role,
                        recruiter_email,
                        recruiter_linkedin,
                        contact_source,
                        source_name,
                        external_job_id,
                        source_url,
                        posted_date,
                        fetched_at,
                        is_active
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, TRUE)
                    """;

            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {

                bindInternshipFields(statement, internship, sourceName);
                statement.setString(16, sourceName);
                statement.setString(17, externalJobId);
                statement.setString(18, internship.getSourceUrl());
                if (internship.getPostedDate() == null) {
                    statement.setNull(19, java.sql.Types.DATE);
                } else {
                    statement.setDate(19, java.sql.Date.valueOf(internship.getPostedDate()));
                }

                int rows = statement.executeUpdate();
                return rows > 0 ? "INSERTED" : "FAILED";
            }

        } catch (SQLException e) {
            System.out.println("Failed to save or update internship!");
            e.printStackTrace();
            return "FAILED";
        }
    }


    private String insertWithoutExternalId(
            Internship internship,
            String sourceName
    ) {

        String sql = """
                INSERT INTO internships
                (
                    company_name,
                    job_role,
                    category,
                    location,
                    work_mode,
                    stipend,
                    duration,
                    required_skills,
                    deadline,
                    job_link,
                    recruiter_name,
                    recruiter_role,
                    recruiter_email,
                    recruiter_linkedin,
                    contact_source,
                    source_name,
                    external_job_id,
                    source_url,
                    posted_date,
                    fetched_at,
                    is_active
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, TRUE)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            bindInternshipFields(statement, internship, sourceName);
            statement.setString(16, sourceName);
            statement.setNull(17, java.sql.Types.VARCHAR);
            statement.setString(18, internship.getSourceUrl());
            if (internship.getPostedDate() == null) {
                statement.setNull(19, java.sql.Types.DATE);
            } else {
                statement.setDate(19, java.sql.Date.valueOf(internship.getPostedDate()));
            }

            int rows = statement.executeUpdate();
            return rows > 0 ? "INSERTED" : "FAILED";

        } catch (SQLException e) {
            System.out.println("Failed to insert internship!");
            e.printStackTrace();
            return "FAILED";
        }
    }


    private void bindInternshipFields(
            PreparedStatement statement,
            Internship internship,
            String sourceName
    ) throws SQLException {

        statement.setString(1, internship.getCompanyName());
        statement.setString(2, internship.getJobRole());
        statement.setString(3, internship.getCategory());
        statement.setString(4, internship.getLocation());
        statement.setString(5, internship.getWorkMode());
        statement.setString(6, internship.getStipend());
        statement.setString(7, internship.getDuration());
        statement.setString(8, internship.getRequiredSkills());

        if (internship.getDeadline() == null) {
            statement.setNull(9, java.sql.Types.DATE);
        } else {
            statement.setDate(9, java.sql.Date.valueOf(internship.getDeadline()));
        }

        statement.setString(10, internship.getJobLink());
        statement.setString(11, internship.getRecruiterName());
        statement.setString(12, internship.getRecruiterRole());
        statement.setString(13, internship.getRecruiterEmail());
        statement.setString(14, internship.getRecruiterLinkedin());
        statement.setString(15, internship.getContactSource());

        // For update statements, the next parameter is source_url.
        // For insert statements, the next parameter is also external_job_id,
        // which is set by the caller after this method.
        // Therefore bindInternshipFields always stops at contact_source.
    }


    // =========================================================
    // SYNC ONE SOURCE WITH ITS CURRENT LIVE DATA
    // =========================================================

    public int syncSource(
            String sourceName,
            List<Internship> liveInternships
    ) {

        if (sourceName == null || sourceName.isBlank() || liveInternships == null) {
            return 0;
        }

        String deactivateSql = """
                UPDATE internships
                SET is_active = FALSE
                WHERE source_name = ?
                """;

        String findSql = """
                SELECT internship_id
                FROM internships
                WHERE source_name = ?
                  AND external_job_id = ?
                LIMIT 1
                """;

        String updateSql = """
                UPDATE internships
                SET company_name = ?,
                    job_role = ?,
                    category = ?,
                    location = ?,
                    work_mode = ?,
                    stipend = ?,
                    duration = ?,
                    required_skills = ?,
                    deadline = ?,
                    job_link = ?,
                    recruiter_name = ?,
                    recruiter_role = ?,
                    recruiter_email = ?,
                    recruiter_linkedin = ?,
                    contact_source = ?,
                    source_url = ?,
                    posted_date = ?,
                    fetched_at = CURRENT_TIMESTAMP,
                    is_active = TRUE
                WHERE internship_id = ?
                """;

        String insertSql = """
                INSERT INTO internships
                (
                    company_name,
                    job_role,
                    category,
                    location,
                    work_mode,
                    stipend,
                    duration,
                    required_skills,
                    deadline,
                    job_link,
                    recruiter_name,
                    recruiter_role,
                    recruiter_email,
                    recruiter_linkedin,
                    contact_source,
                    source_name,
                    external_job_id,
                    source_url,
                    posted_date,
                    fetched_at,
                    is_active
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, TRUE)
                """;

        int synced = 0;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement deactivate = connection.prepareStatement(deactivateSql)) {
                deactivate.setString(1, sourceName);
                deactivate.executeUpdate();
            }

            try (PreparedStatement find = connection.prepareStatement(findSql);
                 PreparedStatement update = connection.prepareStatement(updateSql);
                 PreparedStatement insert = connection.prepareStatement(insertSql)) {

                for (Internship internship : liveInternships) {

                    if (internship == null) {
                        continue;
                    }

                    String externalId = internship.getExternalJobId();

                    if (externalId == null || externalId.isBlank()) {
                        continue;
                    }

                    int internshipId = -1;

                    find.setString(1, sourceName);
                    find.setString(2, externalId);

                    try (ResultSet resultSet = find.executeQuery()) {
                        if (resultSet.next()) {
                            internshipId = resultSet.getInt("internship_id");
                        }
                    }

                    if (internshipId > 0) {

                        bindSyncUpdateFields(update, internship);
                        update.setInt(18, internshipId);

                        if (update.executeUpdate() > 0) {
                            synced++;
                        }

                    } else {

                        bindSyncInsertFields(insert, internship, sourceName, externalId);

                        if (insert.executeUpdate() > 0) {
                            synced++;
                        }
                    }
                }
            }

            connection.commit();
            return synced;

        } catch (SQLException e) {
            System.out.println("Source sync failed for " + sourceName + ".");
            e.printStackTrace();
            return 0;
        }
    }


    private void bindSyncUpdateFields(
            PreparedStatement statement,
            Internship internship
    ) throws SQLException {

        statement.setString(1, internship.getCompanyName());
        statement.setString(2, internship.getJobRole());
        statement.setString(3, internship.getCategory());
        statement.setString(4, internship.getLocation());
        statement.setString(5, internship.getWorkMode());
        statement.setString(6, internship.getStipend());
        statement.setString(7, internship.getDuration());
        statement.setString(8, internship.getRequiredSkills());

        if (internship.getDeadline() == null) {
            statement.setNull(9, java.sql.Types.DATE);
        } else {
            statement.setDate(9, java.sql.Date.valueOf(internship.getDeadline()));
        }

        statement.setString(10, internship.getJobLink());
        statement.setString(11, internship.getRecruiterName());
        statement.setString(12, internship.getRecruiterRole());
        statement.setString(13, internship.getRecruiterEmail());
        statement.setString(14, internship.getRecruiterLinkedin());
        statement.setString(15, internship.getContactSource());
        statement.setString(16, internship.getSourceUrl());

        if (internship.getPostedDate() == null) {
            statement.setNull(17, java.sql.Types.DATE);
        } else {
            statement.setDate(17, java.sql.Date.valueOf(internship.getPostedDate()));
        }
    }


    private void bindSyncInsertFields(
            PreparedStatement statement,
            Internship internship,
            String sourceName,
            String externalId
    ) throws SQLException {

        statement.setString(1, internship.getCompanyName());
        statement.setString(2, internship.getJobRole());
        statement.setString(3, internship.getCategory());
        statement.setString(4, internship.getLocation());
        statement.setString(5, internship.getWorkMode());
        statement.setString(6, internship.getStipend());
        statement.setString(7, internship.getDuration());
        statement.setString(8, internship.getRequiredSkills());

        if (internship.getDeadline() == null) {
            statement.setNull(9, java.sql.Types.DATE);
        } else {
            statement.setDate(9, java.sql.Date.valueOf(internship.getDeadline()));
        }

        statement.setString(10, internship.getJobLink());
        statement.setString(11, internship.getRecruiterName());
        statement.setString(12, internship.getRecruiterRole());
        statement.setString(13, internship.getRecruiterEmail());
        statement.setString(14, internship.getRecruiterLinkedin());
        statement.setString(15, internship.getContactSource());
        statement.setString(16, sourceName);
        statement.setString(17, externalId);
        statement.setString(18, internship.getSourceUrl());

        if (internship.getPostedDate() == null) {
            statement.setNull(19, java.sql.Types.DATE);
        } else {
            statement.setDate(19, java.sql.Date.valueOf(internship.getPostedDate()));
        }
    }


    // =========================================================
    // RESULT SET -> MODEL
    // =========================================================

    private Internship createInternshipFromResultSet(
            ResultSet resultSet
    ) throws SQLException {

        java.sql.Date deadlineDate = resultSet.getDate("deadline");
        java.sql.Date postedDateSql = resultSet.getDate("posted_date");
        Timestamp fetchedTimestamp = resultSet.getTimestamp("fetched_at");

        LocalDate deadline =
                deadlineDate == null ? null : deadlineDate.toLocalDate();

        LocalDate postedDate =
                postedDateSql == null ? null : postedDateSql.toLocalDate();

        LocalDateTime fetchedAt =
                fetchedTimestamp == null ? null : fetchedTimestamp.toLocalDateTime();

        return new Internship(
                resultSet.getInt("internship_id"),
                resultSet.getString("company_name"),
                resultSet.getString("job_role"),
                resultSet.getString("category"),
                resultSet.getString("location"),
                resultSet.getString("work_mode"),
                resultSet.getString("stipend"),
                resultSet.getString("duration"),
                resultSet.getString("required_skills"),
                deadline,
                resultSet.getString("job_link"),
                resultSet.getString("recruiter_name"),
                resultSet.getString("recruiter_role"),
                resultSet.getString("recruiter_email"),
                resultSet.getString("recruiter_linkedin"),
                resultSet.getString("contact_source"),
                resultSet.getString("source_name"),
                resultSet.getString("external_job_id"),
                resultSet.getString("source_url"),
                postedDate,
                fetchedAt
        );
    }


    private String safe(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }
}
