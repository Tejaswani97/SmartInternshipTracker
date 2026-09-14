package model;

public class User {

    private int userId;
    private String name;
    private String email;
    private String password;

    // New profile information
    private String bio;
    private String location;
    private String skills;
    private String profileImagePath;
    private String resumePath;


    // =========================================================
    // EXISTING CONSTRUCTOR
    // =========================================================

    public User(
            String name,
            String email,
            String password
    ) {

        this.name = name;
        this.email = email;
        this.password = password;

        this.bio = "";
        this.location = "";
        this.skills = "";
        this.profileImagePath = "";
        this.resumePath = "";
    }


    // =========================================================
    // EXISTING DATABASE CONSTRUCTOR
    // =========================================================

    public User(
            int userId,
            String name,
            String email,
            String password
    ) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;

        this.bio = "";
        this.location = "";
        this.skills = "";
        this.profileImagePath = "";
        this.resumePath = "";
    }


    // =========================================================
    // BASIC GETTERS
    // =========================================================

    public int getUserId() {
        return userId;
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


    // =========================================================
    // BASIC SETTERS
    // =========================================================

    public void setName(String name) {
        this.name = name;
    }


    // =========================================================
    // PROFILE GETTERS
    // =========================================================

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    public String getSkills() {
        return skills;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public String getResumePath() {
        return resumePath;
    }


    // =========================================================
    // PROFILE SETTERS
    // =========================================================

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }
}