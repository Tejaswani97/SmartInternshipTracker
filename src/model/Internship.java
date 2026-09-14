package model;

import java.time.LocalDate;

public class Internship {

    private int internshipId;
    private String companyName;
    private String jobRole;
    private String category;
    private String location;
    private String workMode;
    private String stipend;
    private String duration;
    private String requiredSkills;
    private LocalDate deadline;
    private String jobLink;

    // Constructor for new internship
    public Internship(
            String companyName,
            String jobRole,
            String category,
            String location,
            String workMode,
            String stipend,
            String duration,
            String requiredSkills,
            LocalDate deadline,
            String jobLink
    ) {
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.category = category;
        this.location = location;
        this.workMode = workMode;
        this.stipend = stipend;
        this.duration = duration;
        this.requiredSkills = requiredSkills;
        this.deadline = deadline;
        this.jobLink = jobLink;
    }

    // Constructor for database records
    public Internship(
            int internshipId,
            String companyName,
            String jobRole,
            String category,
            String location,
            String workMode,
            String stipend,
            String duration,
            String requiredSkills,
            LocalDate deadline,
            String jobLink
    ) {
        this.internshipId = internshipId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.category = category;
        this.location = location;
        this.workMode = workMode;
        this.stipend = stipend;
        this.duration = duration;
        this.requiredSkills = requiredSkills;
        this.deadline = deadline;
        this.jobLink = jobLink;
    }

    public int getInternshipId() {
        return internshipId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getStipend() {
        return stipend;
    }

    public String getDuration() {
        return duration;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getJobLink() {
        return jobLink;
    }
}