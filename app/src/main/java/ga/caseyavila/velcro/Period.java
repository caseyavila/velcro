package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Period {

    private final String courseId;
    private final String score;
    private String grade;
    private final String teacher;
    private final String courseName;
    private String gradeUpdateDate;
    private boolean hasTrends;
    private Trend trend;
    private boolean hasWeights;
    private List<Assignment> assignmentArray = new ArrayList<>();
    private HashMap<String, Double> weightMap = new HashMap<>();

    public Period(JSONObject jsonObject) throws JSONException {
        courseId = jsonObject.getString("periodID");
        score = jsonObject.getString("score").equals("null") ? "" : jsonObject.getString("score");
        grade = jsonObject.getString("grade").equals("null") ? "" : jsonObject.getString("grade");
        teacher = jsonObject.getString("teacherName");
        courseName = jsonObject.getString("courseName");
    }

    public void addProgressReport(JSONObject progressReport) throws JSONException, ParseException {
        JSONArray gradeJSON = progressReport.getJSONArray("grades");
        JSONArray categoryJSON = progressReport.getJSONArray("categories");
        Date date = (new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault()))
                .parse(progressReport.getString("trendDate"));

        if (date != null) {
            gradeUpdateDate = DateFormat.getDateTimeInstance().format(date);
        }

        hasWeights = progressReport.getBoolean("useWeighting");

        // Fill weight hash map
        for (int i = 0; i < categoryJSON.length(); i++) {
            weightMap.put(categoryJSON.getJSONObject(i).getString("name"),
                    categoryJSON.getJSONObject(i).getDouble("weight"));
        }

        assignmentArray.clear();

        for (int i = 0; i < gradeJSON.length(); i++) {
            assignmentArray.add(new Assignment(gradeJSON.getJSONObject(i), weightMap));
        }

        hasTrends = progressReport.has("trendScores");

        if (hasTrends) {
            trend = new Trend(progressReport.getJSONArray("trendScores"));
        }
    }

    public Assignment getAssignment(int assignment) {
        return assignmentArray.get(assignment);
    }

    public String getCourseId() {
        return courseId;
    }

    public String getScore() {
        return score;
    }

    public String getGrade() {
        return grade;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getGradeUpdateDate() {
        return gradeUpdateDate;
    }

    public int getNumberOfAssignments() {
        return assignmentArray.size();
    }

    public boolean hasTrends() {
        return hasTrends;
    }

    public Trend getTrend() {
        return trend;
    }

    public String[] getCategories() {
        return weightMap.keySet().toArray(new String[0]);
    }

    public String getCalculatedPercentage() {
        double percentage = 0;
        HashMap<String, Category> categoryMap = new HashMap<>();

        // Fill hash map with the amount of points earned and possible in each category
        for (Assignment assignment : assignmentArray) {
            try {
                double scoreEarned = Double.parseDouble(assignment.getScoreEarned());
                double scorePossible = Double.parseDouble(assignment.getScorePossible());

                if (hasWeights) {
                    if (categoryMap.containsKey(assignment.getCategory())) {
                        categoryMap.get(assignment.getCategory()).scoreEarned += scoreEarned;
                        categoryMap.get(assignment.getCategory()).scorePossible += scorePossible;
                    } else {
                        categoryMap.put(assignment.getCategory(), new Category(assignment.getWeight(), scoreEarned, scorePossible));
                    }
                // Create a "main" category that contains all assignments when no weights are involved
                } else {
                    if (categoryMap.containsKey("main")) {
                        categoryMap.get("main").scoreEarned += scoreEarned;
                        categoryMap.get("main").scorePossible += scorePossible;
                    } else {
                        categoryMap.put("main", new Category(1, scoreEarned, scorePossible));
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        for (Category value : categoryMap.values()) {
            percentage += value.scoreEarned / value.scorePossible * value.weight;
        }

        return String.format(Locale.getDefault(), "%.2f", percentage * 100 / effectiveWeight());
    }

    public String getCalculatedGrade() {
        if (Double.parseDouble(getCalculatedPercentage()) >= 97) {
            return "A+";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 93) {
            return "A";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 90) {
            return "A-";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 87) {
            return "B+";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 83) {
            return "B";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 80) {
            return "B-";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 77) {
            return "C+";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 73) {
            return "C";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 70) {
            return "C-";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 67) {
            return "D+";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 63) {
            return "D";
        } else if (Double.parseDouble(getCalculatedPercentage()) >= 60) {
            return "D-";
        } else {
            return "F";
        }
    }

    // Combined weight of categories that have assignments listed
    private double effectiveWeight() {
        double accumulator = 0;
        if (hasWeights) {
            for (double value : weightMap.values()) {
                accumulator += value;
            }
            return accumulator;
        } else {
            return 1;
        }
    }

    private static class Category {

        double weight;
        double scoreEarned;
        double scorePossible;

        Category(double weight, double scoreEarned, double scorePossible) {
            this.weight = weight;
            this.scoreEarned = scoreEarned;
            this.scorePossible = scorePossible;
        }
    }
}
