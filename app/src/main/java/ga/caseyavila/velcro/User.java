package ga.caseyavila.velcro;

import android.content.SharedPreferences;
import lecho.lib.hellocharts.model.AxisValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
import android.util.Base64;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;
import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class User {

    private String username;
    private String password;
    private String schoolUrl = "ahs-fusd-ca";
    private String baseUrl = "https://" + this.schoolUrl + ".schoolloop.com";
    private Connection.Response studentIdResponse;
    private Connection.Response reportCardResponse;
    private Connection.Response progressReportResponse;
    private JSONObject loginJSON;
    private JSONArray coursesJSON;
    private JSONArray progressReportJSON = new JSONArray();
    private String studentId;
    private boolean isLoggedIn;
    private int numberOfPeriods;
    private static final Pattern scoreEarnedPattern = Pattern.compile("[-+]?([0-9]*\\.[0-9]+|[0-9]+)(?=\\s/\\s)");
    private JSONArray trendJSON;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool_url() {
        return this.schoolUrl;
    }

    public void setSchool_url(String url) {
        this.schoolUrl = url;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    private String credentials(String username, String password) {
        String encodedInput = username + ":" + password;
        return Base64.encodeToString(encodedInput.getBytes(), Base64.NO_WRAP);
    }

    private String getVelcroUUID() {
        if (sharedPreferences.contains("UUID")) {
            return sharedPreferences.getString("UUID", "");
        } else {
            String uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UUID", uuid);
            editor.apply();
            return uuid;
        }
    }

    public void findStudentId() throws IOException, JSONException {
        isLoggedIn = false;
        studentIdResponse = Jsoup.connect(baseUrl + "/mapi/login")
                .method(Connection.Method.GET)
                .data("devOS", "Android")
                .data("hash", "false")
                .data("uuid", getVelcroUUID())
                .data("version", "3.2.4")
                .data("year", "2020")
                .header("Authorization", "Basic " + credentials(username, password))
                .execute();

        loginJSON = new JSONObject(studentIdResponse.body());
        studentId = loginJSON.getString("userID");
    }

    public void getReportCard() throws IOException, JSONException {
        reportCardResponse = Jsoup.connect(baseUrl + "/mapi/report_card")
                .method(Connection.Method.GET)
                .data("studentID", casey.getStudentId())
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(casey.getUsername(), casey.getPassword()))
                .execute();

        coursesJSON = new JSONArray(reportCardResponse.body());
        if (reportCardResponse.statusCode() == 200) {
            isLoggedIn = true;
        }
        setNumberOfPeriods(coursesJSON.length());
        System.out.println(casey.getVelcroUUID());
    }

    public void findProgressReport(int period) throws IOException, JSONException {
        progressReportResponse = Jsoup.connect(baseUrl + "/mapi/progress_report")
                .method(Connection.Method.GET)
                .data("periodID", casey.getCourseId(period))
                .data("studentID", sharedPreferences.getString("studentId", ""))
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", "")))
                .execute();

        progressReportJSON.put(period, (JSONObject) new JSONArray(progressReportResponse.body()).get(0));  // Place JSONObject directly in array, instead of array with one object
    }

    public String getScore(int period) {
        try {
            JSONObject periodJSON = coursesJSON.getJSONObject(period);
            // Return a blank string if the score is null
            return (periodJSON.getString("score").equals("null")) ? "" : periodJSON.getString("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getGrade(int period) {
        try {
            JSONObject periodJSON = coursesJSON.getJSONObject(period);
            // Return a blank string if the grade is null
            return (periodJSON.getString("grade").equals("null")) ? "" : periodJSON.getString("grade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getTeacher(int period) {
        try {
            JSONObject periodJSON = coursesJSON.getJSONObject(period);
            return periodJSON.getString("teacherName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getCourseName(int period) {
        try {
            JSONObject periodJSON = coursesJSON.getJSONObject(period);
            return periodJSON.getString("courseName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String getCourseId(int period) {
        try {
            JSONObject periodJSON = coursesJSON.getJSONObject(period);
            return periodJSON.getString("periodID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private void setNumberOfPeriods(int periods) {
        numberOfPeriods = periods;
    }

    public int getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public int getNumberOfAssignments(int period) {
        try {
            return getPeriodProgressReportJSON(period).getJSONArray("grades").length();  // Return the number of items in the array "grades"
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private JSONObject getPeriodProgressReportJSON(int period) {
        try {
            return progressReportJSON.getJSONObject(period);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getAssignmentJSONObject(int period, int assignment) {
        try {
            JSONArray assignmentsArray = getPeriodProgressReportJSON(period).getJSONArray("grades");
            return assignmentsArray.getJSONObject(assignment);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAssignmentName(int period, int assignment) {
        try {
            JSONObject assignmentObject = getAssignmentJSONObject(period, assignment);
            return assignmentObject.getJSONObject("assignment").getString("title");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getAssignmentCategory(int period, int assignment) {
        try {
            JSONObject assignmentObject = getAssignmentJSONObject(period, assignment);
            return assignmentObject.getJSONObject("assignment").getString("categoryName");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getAssignmentScoreEarned(int period, int assignment) {
        try {
            JSONObject assignmentObject = getAssignmentJSONObject(period, assignment);
            String scoreString = assignmentObject.getString("score");
            Matcher m = scoreEarnedPattern.matcher(scoreString);
            if (m.find()) {
                return m.group();
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    public String getAssignmentScorePossible(int period, int assignment) {
        try {
            JSONObject assignmentObject = getAssignmentJSONObject(period, assignment);
            return assignmentObject.getJSONObject("assignment").getString("maxPoints");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getAssignmentPercentage(int period, int assignment) {
        try {
            float scoreEarned = Float.parseFloat(getAssignmentScoreEarned(period, assignment));
            float scorePossible = Float.parseFloat(getAssignmentScorePossible(period, assignment));
            float percentage = ((scoreEarned / scorePossible) * 100);
            return String.valueOf((double) Math.round(percentage * 100) / 100);  // Round output to 2 decimal places
        } catch (NullPointerException e) {
            return "";
        }
    }

    private JSONArray getTrendJSON(int period) {
        try {
            return getPeriodProgressReportJSON(period).getJSONArray("trendScores");
        } catch (JSONException e) {
            return null;
        }
    }

    public boolean hasTrends(int period) {
        JSONObject periodObject = getPeriodProgressReportJSON(period);
        try {
            assert periodObject != null;
            return periodObject.has("trendScores");
        } catch (NullPointerException e) {
            return false;
        }
    }

    public ArrayList<Float> xTrendValues(int period) {
        ArrayList<Float> values = new ArrayList<>();
        try {
            for (int i = 0; i < getTrendJSON(period).length(); i++) {
                values.add(Float.parseFloat(getTrendJSON(period).getJSONObject(i).getString("dayID")));
            }
            return values;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<Float> yTrendValues(int period) {
        ArrayList<Float> values = new ArrayList<>();
        try {
            for (int i = 0; i < getTrendJSON(period).length(); i++) {
                values.add(Float.parseFloat(getTrendJSON(period).getJSONObject(i).getString("score")) * 100);
            }
            return values;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<AxisValue> dateLabels(int period) {
        ArrayList<AxisValue> arrayList = new ArrayList<>();
        Float difference = getTrendLastDate(period) - getTrendFirstDate(period);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d", Locale.US);
        Date date;
        for (int i = 5; i > 0; i--) {
            Float value = (getTrendFirstDate(period) + (difference * i/5));
            date = new Date(value.longValue());
            arrayList.add(new AxisValue(value).setLabel(dateFormat.format(date)));
        }
        return arrayList;
    }

    private Float getTrendFirstDate(int period) {
        Float firstDate = xTrendValues(period).get(xTrendValues(period).size() - 1);
        return firstDate;
    }

    private Float getTrendLastDate(int period) {
        return xTrendValues(period).get(0);
    }

    public Float getTrendMax(int period) {
        return Collections.max(yTrendValues(period));
    }

    public Float getTrendRange(int period) {
        Float maxValue = Collections.max(yTrendValues(period));
        Float minValue = Collections.min(yTrendValues(period));
        return maxValue - minValue;
    }

    public Float getTrendMin(int period) {
        return Collections.min(yTrendValues(period));
    }

}
