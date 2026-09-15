package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.User;
import model.Internship;

public class SkillMatcher {

    public static Set<String> convertToSet(String skills) {

        Set<String> skillSet = new HashSet<>();

        if (skills == null || skills.trim().isEmpty()) {
            return skillSet;
        }

        String[] skillArray = skills.split("[,/]+");

        for (String skill : skillArray) {
            skillSet.add(skill.trim().toLowerCase());
        }

        return skillSet;
    }

    public static SkillMatchResult calculateMatch(String userSkills, String requiredSkills) {

        Set<String> userSkillSet = convertToSet(userSkills);
        Set<String> requiredSkillSet = convertToSet(requiredSkills);

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String skill : requiredSkillSet) {

            if (userSkillSet.contains(skill)) {
                matchedSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        int matchPercentage = 0;

        if (!requiredSkillSet.isEmpty()) {
            matchPercentage =
                    (matchedSkills.size() * 100) / requiredSkillSet.size();
        }

        return new SkillMatchResult(
                matchPercentage,
                matchedSkills,
                missingSkills
        );
    }

    public static void main(String[] args) {

    User user = new User(
            1,
            "Tejaswani",
            "test@gmail.com",
            "1234"
    );

    user.setSkills("Java, SQL, DSA, Git");

    Internship internship = new Internship(
            1,
            "Microsoft",
            "Software Engineer Intern",
            "Software Development",
            "Hyderabad",
            "Hybrid",
            "₹45,000/month",
            "6 months",
            "Java/C++/DSA/Git/SQL/Spring Boot",
            java.time.LocalDate.of(2026, 9, 25),
            "https://www.microsoft.com/"
    );

    SkillMatchResult result = calculateMatch(
            user.getSkills(),
            internship.getRequiredSkills()
    );

    System.out.println("Internship: " + internship.getCompanyName());
    System.out.println("Match: " + result.getMatchPercentage() + "%");
    System.out.println("Matched Skills: " + result.getMatchedSkills());
    System.out.println("Missing Skills: " + result.getMissingSkills());
}
}