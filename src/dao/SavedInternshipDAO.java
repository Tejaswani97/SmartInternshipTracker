package dao;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavedInternshipDAO {

    public List<Integer> getSavedInternshipIds(int userId) {

        List<Integer> ids = new ArrayList<>();

        String sql = """
                SELECT internship_id
                FROM saved_internships
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ids.add(resultSet.getInt("internship_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to load saved internships!");
            e.printStackTrace();
        }

        return ids;
    }


    public boolean saveInternship(int userId, int internshipId) {

        String sql = """
                INSERT INTO saved_internships
                    (user_id, internship_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, internshipId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            if (isDuplicateKey(e)) {
                return true;
            }

            System.out.println("Failed to save internship!");
            e.printStackTrace();
            return false;
        }
    }


    public boolean removeSavedInternship(
            int userId,
            int internshipId
    ) {

        String sql = """
                DELETE FROM saved_internships
                WHERE user_id = ?
                  AND internship_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, internshipId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to remove saved internship!");
            e.printStackTrace();
            return false;
        }
    }


    private boolean isDuplicateKey(SQLException exception) {

        String state = exception.getSQLState();

        return "23000".equals(state)
                || exception.getMessage() != null
                && exception.getMessage()
                        .toLowerCase()
                        .contains("duplicate");
    }
}
