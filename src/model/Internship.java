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

    // Recruiter details
    private String recruiterName;
    private String recruiterRole;
    private String recruiterEmail;
    private String recruiterLinkedin;
    private String contactSource;


    // =========================================================
    // Constructor for new internship
    // =========================================================

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

        this.recruiterName = "";
        this.recruiterRole = "";
        this.recruiterEmail = "";
        this.recruiterLinkedin = "";
        this.contactSource = "";
    }


    // =========================================================
    // Constructor for new internship WITH recruiter details
    // =========================================================

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
            String jobLink,
            String recruiterName,
            String recruiterRole,
            String recruiterEmail,
            String recruiterLinkedin,
            String contactSource
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

        this.recruiterName = recruiterName;
        this.recruiterRole = recruiterRole;
        this.recruiterEmail = recruiterEmail;
        this.recruiterLinkedin = recruiterLinkedin;
        this.contactSource = contactSource;
    }


    // =========================================================
    // Constructor for database records
    // =========================================================

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

        this.recruiterName = "";
        this.recruiterRole = "";
        this.recruiterEmail = "";
        this.recruiterLinkedin = "";
        this.contactSource = "";
    }


    // =========================================================
    // Constructor for database records WITH recruiter details
    // =========================================================

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
            String jobLink,
            String recruiterName,
            String recruiterRole,
            String recruiterEmail,
            String recruiterLinkedin,
            String contactSource
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

        this.recruiterName = recruiterName;
        this.recruiterRole = recruiterRole;
        this.recruiterEmail = recruiterEmail;
        this.recruiterLinkedin = recruiterLinkedin;
        this.contactSource = contactSource;
    }


    // =========================================================
    // GETTERS
    // =========================================================

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

    public String getRecruiterName() {
        return recruiterName;
    }

    public String getRecruiterRole() {
        return recruiterRole;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public String getRecruiterLinkedin() {
        return recruiterLinkedin;
    }

    public String getContactSource() {
        return contactSource;
    }
}