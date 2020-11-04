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
import java.util.Map;

public class Period {

    private final String courseId;
    private final String score;
    private final String grade;
    private final String teacher;
    private final String courseName;
    private String gradeUpdateDate;
    private boolean hasTrends;
    private List<Assignment> assignmentArray = new ArrayList<>();
    private HashMap<String, Double> weightMap = new HashMap<>();
    private Trend trend;


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

        // Fill category hash map
        for (int i = 0; i < categoryJSON.length(); i++) {
            weightMap.put(categoryJSON.getJSONObject(i).getString("name"),
                    categoryJSON.getJSONObject(i).getDouble("weight"));
        }

        assignmentArray.clear();

        for (int i = 0; i < gradeJSON.length(); i++) {
            assignmentArray.add(new Assignment(gradeJSON.getJSONObject(i)));
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

    public String getCalculatedPercentage() {
        double percentage = 0;
        HashMap<String, Category> categoryMap = new HashMap<>();

        for (Assignment assignment : assignmentArray) {
            try {
                double scoreEarned = Double.parseDouble(assignment.getScoreEarned());
                double scorePossible = Double.parseDouble(assignment.getScorePossible());

                if (categoryMap.containsKey(assignment.getCategory())) {
                    categoryMap.get(assignment.getCategory()).scoreEarned += scoreEarned;
                    categoryMap.get(assignment.getCategory()).scorePossible += scorePossible;
                } else {
                    categoryMap.put(assignment.getCategory(), new Category(scoreEarned, scorePossible));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, Category> entry : categoryMap.entrySet()) {
            percentage += entry.getValue().scoreEarned / entry.getValue().scorePossible * weightMap.get(entry.getKey());
        }

        return String.format(Locale.getDefault(), "%.2f", percentage / effectiveWeight() * 100);
    }

    private double effectiveWeight() {
        double accumulator = 0;
        for (double value : weightMap.values()) {
            accumulator += value;
        }
        return accumulator;
    }

    private static class Category {

        double scoreEarned;
        double scorePossible;

        Category(double scoreEarned, double scorePossible) {
            this.scoreEarned = scoreEarned;
            this.scorePossible = scorePossible;
        }
    }
}
