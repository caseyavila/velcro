package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment {

    private final String name;
    private String category;
    private String scoreEarned;
    private String scorePossible;

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
    }

    public Assignment(String name, String category, String scoreEarned, String scorePossible) {
        this.name = name;
        this.category = category;
        this.scoreEarned = scoreEarned;
        this.scorePossible = scorePossible;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScoreEarned() {
        return scoreEarned;
    }

    public void setScoreEarned(String scoreEarned) {
        this.scoreEarned = scoreEarned;
    }

    public String getScorePossible() {
        return scorePossible;
    }

    public void setScorePossible(String scorePossible) {
        this.scorePossible = scorePossible;
    }

    public String getPercentage() {
        try {
            double doublePercentage = Double.parseDouble(scoreEarned) / Double.parseDouble(scorePossible) * 100;
            // Round to two decimal places
            return String.format(Locale.getDefault(), "%.2f", doublePercentage);
        } catch (NumberFormatException e) {
            return "";
        }
    }
}
