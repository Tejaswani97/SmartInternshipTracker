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
    // GET ALL INTERNSHIPS
    // =========================================================

    public List<Internship> getAllInternships() {

        List<Internship> internships =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                ORDER BY deadline
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                internships.add(
                        createInternshipFromResultSet(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to load internships!"
            );

            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // SEARCH INTERNSHIPS
    // =========================================================

    public List<Internship> searchInternships(
            String keyword
    ) {

        List<Internship> internships =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE company_name LIKE ?
                   OR job_role LIKE ?
                ORDER BY deadline
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String search =
                    "%" + keyword + "%";

            statement.setString(
                    1,
                    search
            );

            statement.setString(
                    2,
                    search
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                internships.add(
                        createInternshipFromResultSet(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Internship search failed!"
            );

            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // FILTER BY CATEGORY
    // =========================================================

    public List<Internship> filterByCategory(
            String category
    ) {

        List<Internship> internships =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE category = ?
                ORDER BY deadline
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    category
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                internships.add(
                        createInternshipFromResultSet(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Category filter failed!"
            );

            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // FILTER BY LOCATION
    // =========================================================

    public List<Internship> filterByLocation(
            String location
    ) {

        List<Internship> internships =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE location = ?
                ORDER BY deadline
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    location
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                internships.add(
                        createInternshipFromResultSet(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Location filter failed!"
            );

            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // FILTER BY WORK MODE
    // =========================================================

    public List<Internship> filterByWorkMode(
            String workMode
    ) {

        List<Internship> internships =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE work_mode = ?
                ORDER BY deadline
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    workMode
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                internships.add(
                        createInternshipFromResultSet(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Work mode filter failed!"
            );

            e.printStackTrace();
        }

        return internships;
    }


    // =========================================================
    // SAVE OR UPDATE INTERNSHIP
    // =========================================================

    public String saveOrUpdate(
            Internship internship
    ) {

        String checkSql = """
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
                    fetched_at = CURRENT_TIMESTAMP
                WHERE source_name = ?
                  AND external_job_id = ?
                """;

        String insertSql = """
                INSERT INTO internships (
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
                    posted_date
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement checkStatement =
                        connection.prepareStatement(
                                checkSql
                        )
        ) {

            checkStatement.setString(
                    1,
                    internship.getSourceName()
            );

            checkStatement.setString(
                    2,
                    internship.getExternalJobId()
            );

            try (
                    ResultSet resultSet =
                            checkStatement.executeQuery()
            ) {

                // =================================================
                // EXISTING RECORD → UPDATE
                // =================================================

                if (resultSet.next()) {

                    try (
                            PreparedStatement updateStatement =
                                    connection.prepareStatement(
                                            updateSql
                                    )
                    ) {

                        updateStatement.setString(
                                1,
                                internship.getCompanyName()
                        );

                        updateStatement.setString(
                                2,
                                internship.getJobRole()
                        );

                        updateStatement.setString(
                                3,
                                internship.getCategory()
                        );

                        updateStatement.setString(
                                4,
                                internship.getLocation()
                        );

                        updateStatement.setString(
                                5,
                                internship.getWorkMode()
                        );

                        updateStatement.setString(
                                6,
                                internship.getStipend()
                        );

                        updateStatement.setString(
                                7,
                                internship.getDuration()
                        );

                        updateStatement.setString(
                                8,
                                internship.getRequiredSkills()
                        );

                        if (
                                internship.getDeadline()
                                        != null
                        ) {

                            updateStatement.setDate(
                                    9,
                                    java.sql.Date.valueOf(
                                            internship.getDeadline()
                                    )
                            );

                        } else {

                            updateStatement.setNull(
                                    9,
                                    java.sql.Types.DATE
                            );
                        }

                        updateStatement.setString(
                                10,
                                internship.getJobLink()
                        );

                        updateStatement.setString(
                                11,
                                internship.getRecruiterName()
                        );

                        updateStatement.setString(
                                12,
                                internship.getRecruiterRole()
                        );

                        updateStatement.setString(
                                13,
                                internship.getRecruiterEmail()
                        );

                        updateStatement.setString(
                                14,
                                internship.getRecruiterLinkedin()
                        );

                        updateStatement.setString(
                                15,
                                internship.getContactSource()
                        );

                        updateStatement.setString(
                                16,
                                internship.getSourceUrl()
                        );

                        if (
                                internship.getPostedDate()
                                        != null
                        ) {

                            updateStatement.setDate(
                                    17,
                                    java.sql.Date.valueOf(
                                            internship.getPostedDate()
                                    )
                            );

                        } else {

                            updateStatement.setNull(
                                    17,
                                    java.sql.Types.DATE
                            );
                        }

                        updateStatement.setString(
                                18,
                                internship.getSourceName()
                        );

                        updateStatement.setString(
                                19,
                                internship.getExternalJobId()
                        );

                        updateStatement.executeUpdate();

                        return "UPDATED";
                    }
                }
            }


            // =====================================================
            // NEW RECORD → INSERT
            // =====================================================

            try (
                    PreparedStatement insertStatement =
                            connection.prepareStatement(
                                    insertSql
                            )
            ) {

                insertStatement.setString(
                        1,
                        internship.getCompanyName()
                );

                insertStatement.setString(
                        2,
                        internship.getJobRole()
                );

                insertStatement.setString(
                        3,
                        internship.getCategory()
                );

                insertStatement.setString(
                        4,
                        internship.getLocation()
                );

                insertStatement.setString(
                        5,
                        internship.getWorkMode()
                );

                insertStatement.setString(
                        6,
                        internship.getStipend()
                );

                insertStatement.setString(
                        7,
                        internship.getDuration()
                );

                insertStatement.setString(
                        8,
                        internship.getRequiredSkills()
                );

                if (
                        internship.getDeadline()
                                != null
                ) {

                    insertStatement.setDate(
                            9,
                            java.sql.Date.valueOf(
                                    internship.getDeadline()
                            )
                    );

                } else {

                    insertStatement.setNull(
                            9,
                            java.sql.Types.DATE
                    );
                }

                insertStatement.setString(
                        10,
                        internship.getJobLink()
                );

                insertStatement.setString(
                        11,
                        internship.getRecruiterName()
                );

                insertStatement.setString(
                        12,
                        internship.getRecruiterRole()
                );

                insertStatement.setString(
                        13,
                        internship.getRecruiterEmail()
                );

                insertStatement.setString(
                        14,
                        internship.getRecruiterLinkedin()
                );

                insertStatement.setString(
                        15,
                        internship.getContactSource()
                );

                insertStatement.setString(
                        16,
                        internship.getSourceName()
                );

                insertStatement.setString(
                        17,
                        internship.getExternalJobId()
                );

                insertStatement.setString(
                        18,
                        internship.getSourceUrl()
                );

                if (
                        internship.getPostedDate()
                                != null
                ) {

                    insertStatement.setDate(
                            19,
                            java.sql.Date.valueOf(
                                    internship.getPostedDate()
                            )
                    );

                } else {

                    insertStatement.setNull(
                            19,
                            java.sql.Types.DATE
                    );
                }

                insertStatement.executeUpdate();

                return "SAVED";
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to save or update internship!"
            );

            e.printStackTrace();

            return "FAILED";
        }
    }


    // =========================================================
    // RESULT SET → INTERNSHIP OBJECT
    // =========================================================

    private Internship createInternshipFromResultSet(
            ResultSet resultSet
    ) throws SQLException {

        java.sql.Date sqlDeadline =
                resultSet.getDate(
                        "deadline"
                );

        LocalDate deadline =
                sqlDeadline != null
                        ? sqlDeadline.toLocalDate()
                        : null;


        java.sql.Date sqlPostedDate =
                resultSet.getDate(
                        "posted_date"
                );

        LocalDate postedDate =
                sqlPostedDate != null
                        ? sqlPostedDate.toLocalDate()
                        : null;


        Timestamp sqlFetchedAt =
                resultSet.getTimestamp(
                        "fetched_at"
                );

        LocalDateTime fetchedAt =
                sqlFetchedAt != null
                        ? sqlFetchedAt.toLocalDateTime()
                        : null;


        return new Internship(

                resultSet.getInt(
                        "internship_id"
                ),

                resultSet.getString(
                        "company_name"
                ),

                resultSet.getString(
                        "job_role"
                ),

                resultSet.getString(
                        "category"
                ),

                resultSet.getString(
                        "location"
                ),

                resultSet.getString(
                        "work_mode"
                ),

                resultSet.getString(
                        "stipend"
                ),

                resultSet.getString(
                        "duration"
                ),

                resultSet.getString(
                        "required_skills"
                ),

                deadline,

                resultSet.getString(
                        "job_link"
                ),

                resultSet.getString(
                        "recruiter_name"
                ),

                resultSet.getString(
                        "recruiter_role"
                ),

                resultSet.getString(
                        "recruiter_email"
                ),

                resultSet.getString(
                        "recruiter_linkedin"
                ),

                resultSet.getString(
                        "contact_source"
                ),

                resultSet.getString(
                        "source_name"
                ),

                resultSet.getString(
                        "external_job_id"
                ),

                resultSet.getString(
                        "source_url"
                ),

                postedDate,

                fetchedAt
        );
    }
}