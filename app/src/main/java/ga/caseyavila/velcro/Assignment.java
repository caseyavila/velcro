package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment {

    private final String name;
    private final String category;
    private String scoreEarned;
    private final String scorePossible;
    private String percentage;
    private double weight;

    // Match everything in front of whitespace slash combination
    private static final Pattern scoreEarnedPattern = Pattern.compile("^[^\\s/]*");

    public Assignment(JSONObject assignmentJSONObject, HashMap<String, Double> weightMap) throws JSONException {
        name = assignmentJSONObject.getJSONObject("assignment").getString("title");
        category = assignmentJSONObject.getJSONObject("assignment").getString("categoryName");

        Matcher m = scoreEarnedPattern.matcher(assignmentJSONObject.getString("score"));
        if (m.find()) {
            scoreEarned = m.group();
        }

        scorePossible = assignmentJSONObject.getJSONObject("assignment").getString("maxPoints");

        try {
            double doublePercentage = Double.parseDouble(scoreEarned) / Double.parseDouble(scorePossible) * 100;
            // Round to two decimal places
            percentage = String.format(Locale.getDefault(), "%.2f", doublePercentage);
        } catch (NumberFormatException e) {
            percentage = "";
        }

        if (weightMap.containsKey(category)) {
            weight = weightMap.get(category);
        } else {
            weight = 0;
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getScoreEarned() {
        return scoreEarned;
    }

    public String getScorePossible() {
        return scorePossible;
    }

    public String getPercentage() {
        return percentage;
    }

    public double getWeight() {
        return weight;
    }
}
