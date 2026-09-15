package util;

import java.util.List;

public class SkillMatchResult {

    private int matchPercentage;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public SkillMatchResult(int matchPercentage,
                            List<String> matchedSkills,
                            List<String> missingSkills) {

        this.matchPercentage = matchPercentage;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }
}