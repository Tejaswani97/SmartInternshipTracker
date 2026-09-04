package model;

import java.time.LocalDate;

public class Application {

    private int applicationId;
    private int userId;
    private String companyName;
    private String jobRole;
    private LocalDate applicationDate;
    private LocalDate deadline;
    private String status;
    private String jobLink;
    private String notes;

    // Constructor for adding a new application
    public Application(
            int userId,
            String companyName,
            String jobRole,
            LocalDate applicationDate,
            LocalDate deadline,
            String status,
            String jobLink,
            String notes) {

        this.userId = userId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.applicationDate = applicationDate;
        this.deadline = deadline;
        this.status = status;
        this.jobLink = jobLink;
        this.notes = notes;
    }

    // Constructor for applications retrieved from database
    public Application(
            int applicationId,
            int userId,
            String companyName,
            String jobRole,
            LocalDate applicationDate,
            LocalDate deadline,
            String status,
            String jobLink,
            String notes) {

        this.applicationId = applicationId;
        this.userId = userId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.applicationDate = applicationDate;
        this.deadline = deadline;
        this.status = status;
        this.jobLink = jobLink;
        this.notes = notes;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getNotes() {
        return notes;
    }
}