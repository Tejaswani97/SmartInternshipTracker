package dao;

import model.Application;
import util.DatabaseConnection;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    // Add a new application
    public boolean addApplication(Application application) {

        String sql = """
                INSERT INTO applications
                (user_id, company_name, job_role, application_date,
                 deadline, status, job_link, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, application.getUserId());
            statement.setString(2, application.getCompanyName());
            statement.setString(3, application.getJobRole());

            statement.setDate(
                    4,
                    java.sql.Date.valueOf(
                            application.getApplicationDate()
                    )
            );

            statement.setDate(
                    5,
                    java.sql.Date.valueOf(
                            application.getDeadline()
                    )
            );

            statement.setString(6, application.getStatus());
            statement.setString(7, application.getJobLink());
            statement.setString(8, application.getNotes());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Failed to add application!");
            e.printStackTrace();

            return false;
        }
    }


    // View all applications
    public List<Application> getAllApplications() {

        List<Application> applications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Application application = new Application(
                        resultSet.getInt("application_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("job_role"),
                        resultSet.getDate("application_date").toLocalDate(),
                        resultSet.getDate("deadline").toLocalDate(),
                        resultSet.getString("status"),
                        resultSet.getString("job_link"),
                        resultSet.getString("notes")
                );

                applications.add(application);
            }

        } catch (SQLException e) {

            System.out.println("Failed to fetch applications!");
            e.printStackTrace();
        }

        return applications;
    }
    // Search applications by company name
public List<Application> searchByCompany(String companyName,int userId) {

    List<Application> applications = new ArrayList<>();

    String sql = """
            SELECT *
            FROM applications
            WHERE company_name LIKE ?
            AND user_id = ?
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, "%" + companyName + "%");
        statement.setInt(2, userId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            Application application = new Application(
                    resultSet.getInt("application_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("company_name"),
                    resultSet.getString("job_role"),
                    resultSet.getDate("application_date").toLocalDate(),
                    resultSet.getDate("deadline").toLocalDate(),
                    resultSet.getString("status"),
                    resultSet.getString("job_link"),
                    resultSet.getString("notes")
            );

            applications.add(application);
        }

    } catch (SQLException e) {

        System.out.println("Search failed!");
        e.printStackTrace();
    }

    return applications;
}
// Update an existing application
public boolean updateApplication(
        int applicationId,
        int userId,
        String companyName,
        String jobRole,
        String status,
        String jobLink,
        String notes){

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

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, companyName);
        statement.setString(2, jobRole);
        statement.setString(3, status);
        statement.setString(4, jobLink);
        statement.setString(5, notes);
        statement.setInt(6, applicationId);
        statement.setInt(7, userId);

        int rowsUpdated = statement.executeUpdate();

        return rowsUpdated > 0;

    } catch (SQLException e) {

        System.out.println("Failed to update application!");
        e.printStackTrace();

        return false;
    }
}
// Delete an application
public boolean deleteApplication(int applicationId,int userId) {

    String sql = """
            DELETE FROM applications
            WHERE application_id = ?
            AND user_id = ?
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, applicationId);
        statement.setInt(2, userId);

        int rowsDeleted = statement.executeUpdate();

        return rowsDeleted > 0;

    } catch (SQLException e) {

        System.out.println("Failed to delete application!");
        e.printStackTrace();

        return false;
    }
}
// Get applications for a specific user
public List<Application> getApplicationsByUser(int userId) {

    List<Application> applications = new ArrayList<>();

    String sql = """
            SELECT *
            FROM applications
            WHERE user_id = ?
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            Application application = new Application(
                    resultSet.getInt("application_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("company_name"),
                    resultSet.getString("job_role"),
                    resultSet.getDate("application_date").toLocalDate(),
                    resultSet.getDate("deadline").toLocalDate(),
                    resultSet.getString("status"),
                    resultSet.getString("job_link"),
                    resultSet.getString("notes")
            );

            applications.add(application);
        }

    } catch (SQLException e) {

        System.out.println("Failed to fetch user applications!");
        e.printStackTrace();
    }

    return applications;
}
// Get application statistics for a specific user
public Map<String, Integer> getApplicationStatistics(int userId) {

    Map<String, Integer> statistics = new HashMap<>();

    String sql = """
            SELECT status, COUNT(*) AS count
            FROM applications
            WHERE user_id = ?
            GROUP BY status
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            String status = resultSet.getString("status");
            int count = resultSet.getInt("count");

            statistics.put(status, count);
        }

    } catch (SQLException e) {

        System.out.println("Failed to fetch application statistics!");
        e.printStackTrace();
    }

    return statistics;
}
// Get upcoming deadlines for a specific user
public List<Application> getUpcomingDeadlines(int userId) {

    List<Application> applications = new ArrayList<>();

    String sql = """
            SELECT *
            FROM applications
            WHERE user_id = ?
            AND deadline >= CURDATE()
            ORDER BY deadline ASC
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            Application application = new Application(
                    resultSet.getInt("application_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("company_name"),
                    resultSet.getString("job_role"),
                    resultSet.getDate("application_date").toLocalDate(),
                    resultSet.getDate("deadline").toLocalDate(),
                    resultSet.getString("status"),
                    resultSet.getString("job_link"),
                    resultSet.getString("notes")
            );

            applications.add(application);
        }

    } catch (SQLException e) {

        System.out.println("Failed to fetch upcoming deadlines!");
        e.printStackTrace();
    }

    return applications;
}
}