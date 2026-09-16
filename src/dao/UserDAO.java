package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Register a new user
    public boolean registerUser(User user) {

        String sql = """
                INSERT INTO users (name, email, password)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Registration failed!");
            e.printStackTrace();

            return false;
        }
    }


    // Login an existing user
    public User loginUser(String email, String password) {

        String sql = """
                SELECT user_id,
                       name,
                       email,
                       password,
                       bio,
                       location,
                       skills,
                       resume_path,
                       profile_image_path
                FROM users
                WHERE email = ? AND password = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );

                user.setBio(
                        resultSet.getString("bio")
                );

                user.setLocation(
                        resultSet.getString("location")
                );

                user.setSkills(
                        resultSet.getString("skills")
                );

                user.setResumePath(
                        resultSet.getString("resume_path")
                );

                user.setProfileImagePath(
                        resultSet.getString("profile_image_path")
                );

                return user;
            }

        } catch (SQLException e) {

            System.out.println("Login failed!");
            e.printStackTrace();
        }

        return null;
    }


    // Update profile information
    public boolean updateProfile(User user) {

        String sql = """
                UPDATE users
                SET name = ?,
                    bio = ?,
                    location = ?,
                    skills = ?,
                    resume_path = ?,
                    profile_image_path = ?
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getBio());
            statement.setString(3, user.getLocation());
            statement.setString(4, user.getSkills());
            statement.setString(5, user.getResumePath());
            statement.setString(6, user.getProfileImagePath());
            statement.setInt(7, user.getUserId());

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.out.println("Profile update failed!");
            e.printStackTrace();

            return false;
        }
    }
}