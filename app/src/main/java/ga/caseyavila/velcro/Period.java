package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

public class Period {

    private String courseId;
    private String score;
    private String grade;
    private String teacher;
    private String courseName;

    private String gradeUpdateDate;
    private int numberOfAssignments;
    private boolean hasTrends;

    private Assignment[] assignmentArray;
    private Trend trend;


    public Period(JSONObject jsonObject) throws JSONException {
        courseId = jsonObject.getString("periodID");
        score = jsonObject.getString("score").equals("null") ? "" : jsonObject.getString("score");
        grade = jsonObject.getString("grade").equals("null") ? "" : jsonObject.getString("grade");
        teacher = jsonObject.getString("teacherName");
        courseName = jsonObject.getString("courseName");
    }

    public void addProgressReport(JSONObject progressReport) throws JSONException {
        gradeUpdateDate = progressReport.getString("trendDate").split("T")[0];
        numberOfAssignments = progressReport.getJSONArray("grades").length();

        if (assignmentArray == null) {
            assignmentArray = new Assignment[numberOfAssignments];
        }

        for (int i = 0; i < assignmentArray.length; i++) {
            assignmentArray[i] = new Assignment(progressReport.getJSONArray("grades").getJSONObject(i));
        }

        hasTrends = progressReport.has("trendScores");

        if (hasTrends) {
            trend = new Trend(progressReport.getJSONArray("trendScores"));
        }
    }

    public Assignment getAssignment(int assignment) {
        return assignmentArray[assignment];
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
        return numberOfAssignments;
    }

    public boolean hasTrends() {
        return hasTrends;
    }

    public Trend getTrend() {
        return trend;
    }
}
