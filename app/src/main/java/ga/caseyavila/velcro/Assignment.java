package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Locale;
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
            double percentage = Double.parseDouble(scoreEarned) / Double.parseDouble(scorePossible) * 100;
            // Round to two decimal places
            assignmentPercentage = String.format(Locale.getDefault(), "%.2f", percentage);
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
