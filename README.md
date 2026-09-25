# Smart Internship Tracker

Smart Internship Tracker is a Java desktop application that helps students discover real internship opportunities, save interesting roles, apply through the employer's official application page, and track their internship applications in one place.

The project is designed around real public internship listings rather than fake/demo job data.

## Features

- User registration and login
- Email OTP verification during registration
- Student profile management
- Profile photo support
- Resume information
- Real internship listings from public Greenhouse and Lever job boards
- Internship search and filtering
- Filter by category, location, work mode, and source
- Skill-based internship matching
- Save and unsave internships
- View internship details
- Open the official employer application page
- Mark an internship as applied only after the user confirms the application was submitted
- Prevent duplicate applications
- Track application status
- Application search and filtering
- Upcoming application deadlines
- Dashboard statistics
- Deadline reminders
- CSV export for applications
- Public recruiter contact information when provided by the source
- Automatic refreshing of supported internship sources

## Tech Stack

### Frontend / GUI
- Java Swing

### Backend
- Java
- JDBC

### Database
- MySQL

### APIs / Data Sources
- Greenhouse public job board API
- Lever public postings API

### Libraries
- MySQL Connector/J
- Gson

### Development Tools
- IntelliJ IDEA / VS Code
- MySQL
- Git
- GitHub

## How the Application Works

### 1. Register

A student creates an account using their name, email, and password.

An OTP is sent to the registered email address for verification.

### 2. Login

After registration and verification, the student can log into the application.

### 3. Build a Profile

The student can maintain profile information such as:

- Name
- Location
- Skills
- Bio
- Resume information
- Profile photo

### 4. Discover Internships

The application retrieves internship opportunities from supported public Greenhouse and Lever job boards.

The internship listings are stored in the MySQL database so they can be searched and displayed efficiently.

### 5. Search and Filter

Students can search for internships and filter opportunities using information such as:

- Role
- Category
- Location
- Work mode
- Source
- Skills
- Latest postings
- Deadline
- Skill match

### 6. Save Internships

Students can save internships that they are interested in and access them later from the Saved Internships section.

### 7. Apply

The application does not submit applications on behalf of the student.

Instead, the student is taken to the official employer application page.

After submitting the application on the employer's website, the student can confirm the submission in Smart Internship Tracker and record it as an application.

### 8. Track Applications

Students can track applications and update their status, for example:

- Applied
- Shortlisted
- Interview
- Selected
- Rejected

The application also shows upcoming deadlines and provides dashboard statistics.

## Data Sources

The project currently integrates with publicly available job board APIs.

### Greenhouse

Public Greenhouse job boards are retrieved through the Greenhouse job board API.

### Lever

Public Lever postings are retrieved through the Lever postings API.

Only opportunities returned by the supported public sources are imported.

The application does not generate fake internship listings.

## Database

The project uses MySQL.

The main tables include:

- `users`
- `applications`
- `internships`
- `saved_internships`
- `email_verifications`

The `internships` table also stores source and external-job information to support synchronization and duplicate prevention.

## Project Structure

```text
SmartInternshipTracker/
│
├── src/
│   ├── api/
│   │   ├── GreenhouseSource.java
│   │   └── LeverSource.java
│   │
│   ├── dao/
│   │   ├── ApplicationDAO.java
│   │   ├── InternshipDAO.java
│   │   ├── SavedInternshipDAO.java
│   │   ├── UserDAO.java
│   │   └── ...
│   │
│   ├── gui/
│   │   ├── LoginFrame.java
│   │   ├── RegisterFrame.java
│   │   ├── DashboardFrame.java
│   │   ├── InternshipsFrame.java
│   │   ├── ApplicationsFrame.java
│   │   ├── ProfileFrame.java
│   │   └── ...
│   │
│   ├── model/
│   │   ├── User.java
│   │   ├── Internship.java
│   │   ├── Application.java
│   │   └── ...
│   │
│   └── service/
│       ├── EmailService.java
│       └── ...
│
├── lib/
│   ├── mysql-connector-j-26.7.0.jar
│   └── gson-2.14.0.jar
│
├── .gitignore
├── config.properties   # local only, not committed
└── README.md

## Architecture

Smart Internship Tracker follows a layered Java application structure.

```text
User
  ↓
GUI Layer (Java Swing)
  ↓
DAO Layer
  ↓
MySQL Database

External internship data
  ↓
API Layer (Greenhouse / Lever)
  ↓
DAO Layer
  ↓
MySQL Database