package dao;

import model.Application;
import util.DatabaseConnection;

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
public List<Application> searchByCompany(String companyName) {

    List<Application> applications = new ArrayList<>();

    String sql = """
            SELECT *
            FROM applications
            WHERE company_name LIKE ?
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, "%" + companyName + "%");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            Application application = new Application(
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
}