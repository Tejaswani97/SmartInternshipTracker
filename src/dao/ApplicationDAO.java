package dao;

import model.Application;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationDAO {

    // =========================================================
    // CHECK IF USER ALREADY APPLIED
    // =========================================================

    public boolean hasApplied(
            int userId,
            String jobLink
    ) {

        String sql = """
                SELECT application_id
                FROM applications
                WHERE user_id = ?
                  AND job_link = ?
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    userId
            );

            statement.setString(
                    2,
                    jobLink
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to check existing application!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // ADD A NEW APPLICATION
    // =========================================================

    public boolean addApplication(
            Application application
    ) {

        // -----------------------------------------------------
        // DUPLICATE CHECK
        // -----------------------------------------------------

        if (
                hasApplied(
                        application.getUserId(),
                        application.getJobLink()
                )
        ) {

            System.out.println(
                    "Application already exists!"
            );

            return false;
        }


        String sql = """
                INSERT INTO applications
                (
                    user_id,
                    company_name,
                    job_role,
                    application_date,
                    deadline,
                    status,
                    job_link,
                    notes
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    application.getUserId()
            );

            statement.setString(
                    2,
                    application.getCompanyName()
            );

            statement.setString(
                    3,
                    application.getJobRole()
            );


            // -------------------------------------------------
            // APPLICATION DATE
            // -------------------------------------------------

            if (
                    application.getApplicationDate()
                            != null
            ) {

                statement.setDate(
                        4,
                        java.sql.Date.valueOf(
                                application
                                        .getApplicationDate()
                        )
                );

            } else {

                statement.setNull(
                        4,
                        java.sql.Types.DATE
                );
            }


            // -------------------------------------------------
            // DEADLINE
            // -------------------------------------------------

            if (
                    application.getDeadline()
                            != null
            ) {

                statement.setDate(
                        5,
                        java.sql.Date.valueOf(
                                application
                                        .getDeadline()
                        )
                );

            } else {

                statement.setNull(
                        5,
                        java.sql.Types.DATE
                );
            }


            statement.setString(
                    6,
                    application.getStatus()
            );

            statement.setString(
                    7,
                    application.getJobLink()
            );

            statement.setString(
                    8,
                    application.getNotes()
            );


            int rowsInserted =
                    statement.executeUpdate();


            return rowsInserted > 0;


        } catch (SQLException e) {

            System.out.println(
                    "Failed to add application!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // VIEW ALL APPLICATIONS
    // =========================================================

    public List<Application> getAllApplications() {

        List<Application> applications =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (
                    resultSet.next()
            ) {

                applications.add(
                        createApplicationFromResultSet(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to fetch applications!"
            );

            e.printStackTrace();
        }


        return applications;
    }


    // =========================================================
    // SEARCH APPLICATIONS BY COMPANY
    // =========================================================

    public List<Application> searchByCompany(
            String companyName,
            int userId
    ) {

        List<Application> applications =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                WHERE company_name LIKE ?
                  AND user_id = ?
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
                    "%" + companyName + "%"
            );

            statement.setInt(
                    2,
                    userId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    applications.add(
                            createApplicationFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Search failed!"
            );

            e.printStackTrace();
        }


        return applications;
    }


    // =========================================================
    // UPDATE AN EXISTING APPLICATION
    // =========================================================

    public boolean updateApplication(
            int applicationId,
            int userId,
            String companyName,
            String jobRole,
            String status,
            String jobLink,
            String notes
    ) {

        String sql = """
                UPDATE applications
                SET company_name = ?,
                    job_role = ?,
                    status = ?,
                    job_link = ?,
                    notes = ?
                WHERE application_id = ?
                  AND user_id = ?
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
                    companyName
            );

            statement.setString(
                    2,
                    jobRole
            );

            statement.setString(
                    3,
                    status
            );

            statement.setString(
                    4,
                    jobLink
            );

            statement.setString(
                    5,
                    notes
            );

            statement.setInt(
                    6,
                    applicationId
            );

            statement.setInt(
                    7,
                    userId
            );


            int rowsUpdated =
                    statement.executeUpdate();


            return rowsUpdated > 0;


        } catch (SQLException e) {

            System.out.println(
                    "Failed to update application!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // DELETE AN APPLICATION
    // =========================================================

    public boolean deleteApplication(
            int applicationId,
            int userId
    ) {

        String sql = """
                DELETE FROM applications
                WHERE application_id = ?
                  AND user_id = ?
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    applicationId
            );

            statement.setInt(
                    2,
                    userId
            );


            int rowsDeleted =
                    statement.executeUpdate();


            return rowsDeleted > 0;


        } catch (SQLException e) {

            System.out.println(
                    "Failed to delete application!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET APPLICATIONS FOR A USER
    // =========================================================

    public List<Application> getApplicationsByUser(
            int userId
    ) {

        List<Application> applications =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                WHERE user_id = ?
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    userId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    applications.add(
                            createApplicationFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to fetch user applications!"
            );

            e.printStackTrace();
        }


        return applications;
    }


    // =========================================================
    // APPLICATION STATISTICS
    // =========================================================

    public Map<String, Integer> getApplicationStatistics(
            int userId
    ) {

        Map<String, Integer> statistics =
                new HashMap<>();

        String sql = """
                SELECT status,
                       COUNT(*) AS count
                FROM applications
                WHERE user_id = ?
                GROUP BY status
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    userId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    String status =
                            resultSet.getString(
                                    "status"
                            );

                    int count =
                            resultSet.getInt(
                                    "count"
                            );


                    statistics.put(
                            status,
                            count
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to fetch application statistics!"
            );

            e.printStackTrace();
        }


        return statistics;
    }


    // =========================================================
    // GET UPCOMING DEADLINES
    // =========================================================

    public List<Application> getUpcomingDeadlines(
            int userId
    ) {

        List<Application> applications =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                WHERE user_id = ?
                  AND deadline >= CURDATE()
                ORDER BY deadline ASC
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    userId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    applications.add(
                            createApplicationFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to fetch upcoming deadlines!"
            );

            e.printStackTrace();
        }


        return applications;
    }


    // =========================================================
    // FILTER APPLICATIONS BY STATUS
    // =========================================================

    public List<Application> filterByStatus(
            String status,
            int userId
    ) {

        List<Application> applications =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                WHERE user_id = ?
                  AND status = ?
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    userId
            );

            statement.setString(
                    2,
                    status
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    applications.add(
                            createApplicationFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Filter failed!"
            );

            e.printStackTrace();
        }


        return applications;
    }


    // =========================================================
    // GET ONE APPLICATION BY ID
    // =========================================================

    public Application getApplicationById(
            int applicationId,
            int userId
    ) {

        String sql = """
                SELECT *
                FROM applications
                WHERE application_id = ?
                  AND user_id = ?
                """;


        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    applicationId
            );

            statement.setInt(
                    2,
                    userId
            );


            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (
                        resultSet.next()
                ) {

                    return createApplicationFromResultSet(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to fetch application details!"
            );

            e.printStackTrace();
        }


        return null;
    }


    // =========================================================
    // RESULT SET → APPLICATION
    // =========================================================

    private Application createApplicationFromResultSet(
            ResultSet resultSet
    ) throws SQLException {

        java.sql.Date applicationDateSql =
                resultSet.getDate(
                        "application_date"
                );

        java.sql.Date deadlineSql =
                resultSet.getDate(
                        "deadline"
                );


        return new Application(

                resultSet.getInt(
                        "application_id"
                ),

                resultSet.getInt(
                        "user_id"
                ),

                resultSet.getString(
                        "company_name"
                ),

                resultSet.getString(
                        "job_role"
                ),

                applicationDateSql != null
                        ? applicationDateSql.toLocalDate()
                        : null,

                deadlineSql != null
                        ? deadlineSql.toLocalDate()
                        : null,

                resultSet.getString(
                        "status"
                ),

                resultSet.getString(
                        "job_link"
                ),

                resultSet.getString(
                        "notes"
                )
        );
    }
}