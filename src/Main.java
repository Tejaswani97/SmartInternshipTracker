import dao.ApplicationDAO;
import model.Application;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApplicationDAO applicationDAO = new ApplicationDAO();

        // Get all applications from database
        List<Application> applications =
                applicationDAO.getAllApplications();

        System.out.println("========== MY APPLICATIONS ==========");

        for (Application application : applications) {

            System.out.println("Company: "
                    + application.getCompanyName());

            System.out.println("Role: "
                    + application.getJobRole());

            System.out.println("Application Date: "
                    + application.getApplicationDate());

            System.out.println("Deadline: "
                    + application.getDeadline());

            System.out.println("Status: "
                    + application.getStatus());

            System.out.println("Job Link: "
                    + application.getJobLink());

            System.out.println("Notes: "
                    + application.getNotes());

            System.out.println("-------------------------------------");
        }
    }
}