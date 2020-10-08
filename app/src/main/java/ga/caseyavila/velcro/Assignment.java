package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment {

    private String name;
    private String category;
    private String scoreEarned;
    private String scorePossible;
    private String assignmentPercentage;

    // Match everything in front of whitespace slash combination
    private static final Pattern scoreEarnedPattern = Pattern.compile("^[^\\s/]*");

    public Assignment(JSONObject assignmentJSONObject) throws JSONException {
        name = assignmentJSONObject.getJSONObject("assignment").getString("title");
        category = assignmentJSONObject.getJSONObject("assignment").getString("categoryName");

        Matcher m = scoreEarnedPattern.matcher(assignmentJSONObject.getString("score"));
        if (m.find()) {
            scoreEarned = m.group();
        }

        scorePossible = assignmentJSONObject.getJSONObject("assignment").getString("maxPoints");

        try {
            float scoreEarned = Float.parseFloat(getAssignmentScoreEarned());
            float scorePossible = Float.parseFloat(getAssignmentScorePossible());
            float percentage = ((scoreEarned / scorePossible) * 100);
            assignmentPercentage = String.valueOf((double) Math.round(percentage * 100) / 100);  // Round output to 2 decimal places
        } catch (NumberFormatException e) {
            assignmentPercentage = "";
        }
    }

    public String getAssignmentName() {
        return name;
    }

    public String getAssignmentCategory() {
        return category;
    }

    public String getAssignmentScoreEarned() {
        return scoreEarned;
    }

    public String getAssignmentScorePossible() {
        return scorePossible;
    }

    public String getAssignmentPercentage() {
        return assignmentPercentage;
    }
}
