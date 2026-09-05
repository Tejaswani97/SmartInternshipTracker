import dao.ApplicationDAO;
import dao.UserDAO;
import model.Application;
import model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        UserDAO userDAO = new UserDAO();
        ApplicationDAO applicationDAO = new ApplicationDAO();

        // =========================
        // LOGIN
        // =========================

        System.out.println("========================================");
        System.out.println("       SMART INTERNSHIP TRACKER");
        System.out.println("========================================");

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User loggedInUser = userDAO.loginUser(email, password);

        if (loggedInUser == null) {

            System.out.println();
            System.out.println("Invalid email or password.");
            System.out.println("Login failed.");

            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("Login successful!");
        System.out.println("Welcome, " + loggedInUser.getName() + "!");

        int userId = loggedInUser.getUserId();

        // =========================
        // MAIN MENU
        // =========================

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("       SMART INTERNSHIP TRACKER");
            System.out.println("========================================");
            System.out.println("1. Add Application");
            System.out.println("2. View My Applications");
            System.out.println("3. Search by Company");
            System.out.println("4. Update Application");
            System.out.println("5. Delete Application");
            System.out.println("6. Dashboard");
            System.out.println("7. Upcoming Deadlines");
            System.out.println("8. Exit");
            System.out.println("========================================");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:

                    System.out.println();
                    System.out.println("----- ADD APPLICATION -----");

                    System.out.print("Company name: ");
                    String companyName = scanner.nextLine();

                    System.out.print("Job role: ");
                    String jobRole = scanner.nextLine();

                    System.out.print("Application date (YYYY-MM-DD): ");
                    LocalDate applicationDate =
                            LocalDate.parse(scanner.nextLine());

                    System.out.print("Deadline (YYYY-MM-DD): ");
                    LocalDate deadline =
                            LocalDate.parse(scanner.nextLine());

                    System.out.print("Status: ");
                    String status = scanner.nextLine();

                    System.out.print("Job link: ");
                    String jobLink = scanner.nextLine();

                    System.out.print("Notes: ");
                    String notes = scanner.nextLine();

                    Application application = new Application(
                            userId,
                            companyName,
                            jobRole,
                            applicationDate,
                            deadline,
                            status,
                            jobLink,
                            notes
                    );

                    boolean added =
                            applicationDAO.addApplication(application);

                    if (added) {
                        System.out.println(
                                "Application added successfully!"
                        );
                    } else {
                        System.out.println(
                                "Failed to add application."
                        );
                    }

                    break;


                case 2:

                    System.out.println();
                    System.out.println("----- MY APPLICATIONS -----");

                    List<Application> applications =
                            applicationDAO.getApplicationsByUser(userId);

                    if (applications.isEmpty()) {

                        System.out.println(
                                "You have no applications yet."
                        );

                    } else {

                        for (Application app : applications) {

                            System.out.println();
                            System.out.println(
                                    "ID: "
                                    + app.getApplicationId()
                            );

                            System.out.println(
                                    "Company: "
                                    + app.getCompanyName()
                            );

                            System.out.println(
                                    "Role: "
                                    + app.getJobRole()
                            );

                            System.out.println(
                                    "Application Date: "
                                    + app.getApplicationDate()
                            );

                            System.out.println(
                                    "Deadline: "
                                    + app.getDeadline()
                            );

                            System.out.println(
                                    "Status: "
                                    + app.getStatus()
                            );

                            System.out.println(
                                    "Job Link: "
                                    + app.getJobLink()
                            );

                            System.out.println(
                                    "Notes: "
                                    + app.getNotes()
                            );

                            System.out.println(
                                    "--------------------------------"
                            );
                        }
                    }

                    break;


                case 3:

                    System.out.println();
                    System.out.println("----- SEARCH APPLICATIONS -----");

                    System.out.print("Enter company name: ");
                    String searchCompany = scanner.nextLine();

                    List<Application> searchResults =
                            applicationDAO.searchByCompany(searchCompany, userId);

                    if (searchResults.isEmpty()) {

                        System.out.println("No applications found.");

                    } else {

                        for (Application app : searchResults) {

                            System.out.println();
                            System.out.println(
                                    "ID: "
                                    + app.getApplicationId()
                            );

                            System.out.println(
                                    "Company: "
                                    + app.getCompanyName()
                            );

                            System.out.println(
                                    "Role: "
                                    + app.getJobRole()
                            );

                            System.out.println(
                                    "Status: "
                                    + app.getStatus()
                            );

                            System.out.println(
                                    "Deadline: "
                                    + app.getDeadline()
                            );

                            System.out.println(
                                    "--------------------------------"
                            );
                        }
                    }

                    break;


                case 4:

                    System.out.println();
                    System.out.println("----- UPDATE APPLICATION -----");

                    System.out.print("Enter application ID: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("New company name: ");
                    String newCompany = scanner.nextLine();

                    System.out.print("New job role: ");
                    String newRole = scanner.nextLine();

                    System.out.print("New status: ");
                    String newStatus = scanner.nextLine();

                    System.out.print("New job link: ");
                    String newLink = scanner.nextLine();

                    System.out.print("New notes: ");
                    String newNotes = scanner.nextLine();

                    boolean updated =
                            applicationDAO.updateApplication(
                                    updateId,
                                    userId,
                                    newCompany,
                                    newRole,
                                    newStatus,
                                    newLink,
                                    newNotes
                            );

                    if (updated) {
                        System.out.println(
                                "Application updated successfully!"
                        );
                    } else {
                        System.out.println(
                                "Application not found or update failed."
                        );
                    }

                    break;


                case 5:

                    System.out.println();
                    System.out.println("----- DELETE APPLICATION -----");

                    System.out.print("Enter application ID: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print(
                            "Are you sure? (yes/no): "
                    );

                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("yes")) {

                        boolean deleted =
                                applicationDAO.deleteApplication(deleteId, userId);

                        if (deleted) {
                            System.out.println(
                                    "Application deleted successfully!"
                            );
                        } else {
                            System.out.println(
                                    "Application not found."
                            );
                        }

                    } else {

                        System.out.println("Delete cancelled.");
                    }

                    break;


                case 6:

    System.out.println();
    System.out.println("========== DASHBOARD ==========");

    Map<String, Integer> statistics =
            applicationDAO.getApplicationStatistics(userId);

    int applied =
            statistics.getOrDefault("Applied", 0);

    int shortlisted =
            statistics.getOrDefault("Shortlisted", 0);

    int interview =
            statistics.getOrDefault("Interview", 0);

    int rejected =
            statistics.getOrDefault("Rejected", 0);

    int total =
            applied + shortlisted + interview + rejected;

    System.out.println();
    System.out.println("Total Applications : " + total);
    System.out.println();
    System.out.println("Applied            : " + applied);
    System.out.println("Shortlisted        : " + shortlisted);
    System.out.println("Interview          : " + interview);
    System.out.println("Rejected           : " + rejected);

    System.out.println();
    System.out.println("===============================");

    break;


    case 7:

    System.out.println();
    System.out.println("===== UPCOMING DEADLINES =====");

    List<Application> upcoming =
            applicationDAO.getUpcomingDeadlines(userId);

    if (upcoming.isEmpty()) {

        System.out.println("No upcoming deadlines!");

    } else {

        LocalDate today = LocalDate.now();

        for (Application app : upcoming) {

            long daysLeft =
                    java.time.temporal.ChronoUnit.DAYS.between(
                            today,
                            app.getDeadline()
                    );

            System.out.println();
            System.out.println(
                    "Company: " + app.getCompanyName()
            );

            System.out.println(
                    "Role: " + app.getJobRole()
            );

            System.out.println(
                    "Deadline: " + app.getDeadline()
            );

            System.out.println(
                    "Days Left: " + daysLeft
            );

            System.out.println(
                    "Status: " + app.getStatus()
            );

            System.out.println(
                    "--------------------------------"
            );
        }
    }

    break;


                default:

                    System.out.println(
                            "Invalid choice. Please select 1-6."
                    );
            }
        }

        scanner.close();
    }
}