package dao;

import model.Internship;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InternshipDAO {

    // =========================================================
    // GET ALL INTERNSHIPS
    // =========================================================

    public List<Internship> getAllInternships() {

        List<Internship> internships = new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                ORDER BY deadline ASC
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Internship internship = new Internship(
                        resultSet.getInt("internship_id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("job_role"),
                        resultSet.getString("category"),
                        resultSet.getString("location"),
                        resultSet.getString("work_mode"),
                        resultSet.getString("stipend"),
                        resultSet.getString("duration"),
                        resultSet.getString("required_skills"),
                        resultSet.getDate("deadline").toLocalDate(),
                        resultSet.getString("job_link")
                );

                internships.add(internship);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error loading internships: "
                            + e.getMessage()
            );
        }

        return internships;
    }

    // =========================================================
    // SEARCH BY COMPANY OR JOB ROLE
    // =========================================================

    public List<Internship> searchInternships(String keyword) {

        List<Internship> internships = new ArrayList<>();

        String sql = """
                SELECT *
                FROM internships
                WHERE company_name LIKE ?
                   OR job_role LIKE ?
                ORDER BY deadline ASC
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String searchPattern =
                    "%" + keyword + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    internships.add(
                            createInternshipFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Search failed: "
                            + e.getMessage()
            );
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
                ORDER BY deadline ASC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, category);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    internships.add(
                            createInternshipFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Category filter failed: "
                            + e.getMessage()
            );
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
                ORDER BY deadline ASC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, location);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    internships.add(
                            createInternshipFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Location filter failed: "
                            + e.getMessage()
            );
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
                ORDER BY deadline ASC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, workMode);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    internships.add(
                            createInternshipFromResultSet(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Work mode filter failed: "
                            + e.getMessage()
            );
        }

        return internships;
    }

    // =========================================================
    // HELPER METHOD
    // =========================================================

    private Internship createInternshipFromResultSet(
            ResultSet resultSet
    ) throws SQLException {

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
                resultSet.getDate("deadline").toLocalDate(),
                resultSet.getString("job_link")
        );
    }
}