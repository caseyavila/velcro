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

import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class User {

    private String username;
    private String password;
    private String schoolUrl = "ahs-fusd-ca";
    private String baseUrl = "https://" + schoolUrl + ".schoolloop.com";
    private String sessionCookie;
    private String hashedPassword;
    private JSONArray coursesJSON;
    private JSONArray progressReportJSON = new JSONArray();  //Initialize array for accepting different periods
    private JSONArray loopMailJSON = new JSONArray();  //Initialize array for accepting different folders
    private String studentId;
    private boolean isLoggedIn;
    private int numberOfPeriods;
    private static final Pattern scoreEarnedPattern = Pattern.compile("[-+]?([0-9]*\\.[0-9]+|[0-9]+)(?=\\s/\\s)");

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

    public String getSchool_url() {
        return schoolUrl;
    }

    public void setSchool_url(String url) {
        schoolUrl = url;
    }

    public boolean isAutoLoginReady() {
        return sharedPreferences.contains("username") && sharedPreferences.contains("hashedPassword") && sharedPreferences.contains("studentId") && sharedPreferences.contains("JSESSIONID");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    private String credentials(String username, String password) {
        String encodedInput = username + ":" + password;
        return Base64.encodeToString(encodedInput.getBytes(), Base64.NO_WRAP);
    }

    private String getVelcroUUID() {
        if (sharedPreferences.contains("UUID")) {  //If UUID already exists in shared preferences
            return sharedPreferences.getString("UUID", "");
        } else {
            String uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UUID", uuid);
            editor.apply();
            return uuid;
        }
    }

    public void findLoginData() throws IOException, JSONException {
        isLoggedIn = false;
        Connection.Response loginResponse = Jsoup.connect(baseUrl + "/mapi/login")
                .method(Connection.Method.GET)
                .data("devOS", "Android")
                .data("hash", "false")
                .data("uuid", getVelcroUUID())
                .data("version", "3.2.4")
                .data("year", "2020")
                .header("Authorization", "Basic " + credentials(username, password))
                .execute();

        password = null;  //Nullify plain-text password after request (ASAP!!!)

        JSONObject loginJSON = new JSONObject(loginResponse.body());

        sessionCookie = loginResponse.cookie("JSESSIONID");
        studentId = loginJSON.getString("userID");
        hashedPassword = loginJSON.getString("hashedPassword");
    }

    public void findReportCard() throws IOException, JSONException {
        Connection.Response reportCardResponse = Jsoup.connect(baseUrl + "/mapi/report_card")
                .method(Connection.Method.GET)
                .data("studentID", studentId)
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(username, hashedPassword))
                .header("SL-HASH", "true")
                .header("SL-UUID", getVelcroUUID())
                .cookie("JSESSIONID", sessionCookie)
                .cookie("slid", studentId)
                .execute();

        coursesJSON = new JSONArray(reportCardResponse.body());
        if (reportCardResponse.statusCode() == 200) {
            isLoggedIn = true;  //Set User to logged in status
        }
        setNumberOfPeriods(coursesJSON.length());
    }

    public void findProgressReport(int period) throws IOException, JSONException {
        Connection.Response progressReportResponse = Jsoup.connect(baseUrl + "/mapi/progress_report")
                .method(Connection.Method.GET)
                .data("periodID", getCourseId(period))
                .data("studentID", studentId)
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(username, hashedPassword))
                .header("SL-HASH", "true")
                .header("SL-UUID", getVelcroUUID())
                .cookie("JSESSIONID", sessionCookie)
                .cookie("slid", studentId)
                .execute();

        progressReportJSON.put(period, new JSONArray(progressReportResponse.body()).get(0));  // Place JSONObject directly in array, instead of array with one object
    }

    public void findLoopMailInbox(int folder) throws IOException, JSONException {
        Connection.Response loopMailResponse = Jsoup.connect(baseUrl + "/mapi/mail_messages")
                .method(Connection.Method.GET)
                .data("folderID", String.valueOf(folder))  //1 for inbox, 2 for sent
                .data("max", "20")
                .data("start", "0")
                .data("studentID", studentId)
                .header("Authorization", "Basic " + credentials(username, hashedPassword))
                .header("SL-HASH", "true")
                .header("SL-UUID", getVelcroUUID())
                .cookie("JSESSIONID", sessionCookie)
                .cookie("slid", studentId)
                .execute();

        loopMailJSON.put(folder, new JSONArray(loopMailResponse.body()));
        System.out.println(loopMailJSON);
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
            return coursesJSON.getJSONObject(period).getString("teacherName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getCourseName(int period) {
        try {
            return coursesJSON.getJSONObject(period).getString("courseName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getGradeUpdateDate(int period) {
        try {
            return getPeriodProgressReportJSON(period).getString("trendDate").split("T")[0];  //Return everything before "T"
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String getCourseId(int period) {
        try {
            return coursesJSON.getJSONObject(period).getString("periodID");
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
            return getPeriodProgressReportJSON(period).getJSONArray("grades").getJSONObject(assignment);
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
                //Convert milliseconds to days by multiplying by a large number
                values.add(Float.parseFloat(getTrendJSON(period).getJSONObject(i).getString("dayID")) / 86400000);
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
        float difference = getTrendLastDate(period) - getTrendFirstDate(period);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d", Locale.US);
        Date date;
        for (int i = 5; i > 0; i--) {
            Float value = (getTrendFirstDate(period) + (difference * i/5));
            date = new Date(value.longValue() * 86400000);  //Convert days back to milliseconds for date conversion
            arrayList.add(new AxisValue(value).setLabel(dateFormat.format(date)));
        }
        return arrayList;
    }

    private Float getTrendFirstDate(int period) {
        return xTrendValues(period).get(xTrendValues(period).size() - 1);
    }

    private Float getTrendLastDate(int period) {
        return xTrendValues(period).get(0);
    }

    public Float getTrendMin(int period) {
        return Collections.min(yTrendValues(period));
    }

    public Float getTrendMax(int period) {
        return Collections.max(yTrendValues(period));
    }

    public Float getTrendRange(int period) {
        Float maxValue = Collections.max(yTrendValues(period));
        Float minValue = Collections.min(yTrendValues(period));
        return maxValue - minValue;
    }

    public int getNumberOfLoopMails(int folder) {
        try {
            return loopMailJSON.getJSONArray(folder).length();
        } catch (JSONException e) {
            return 0;
        }
    }

    public Boolean isLoopMailRead(int folder, int index) {
        try {
            return loopMailJSON.getJSONArray(folder).getJSONObject(index).getBoolean("read");
        } catch (JSONException e) {
            return false;
        }
    }

    public String getLoopMailSender(int folder, int index) {
        try {
            return loopMailJSON.getJSONArray(folder).getJSONObject(index).getJSONObject("sender").getString("name");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getLoopMailSendDate(int folder, int index) {
        try {
            return loopMailJSON.getJSONArray(folder).getJSONObject(index).getString("date");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getLoopMailSubject(int folder, int index) {
        try {
            return loopMailJSON.getJSONArray(folder).getJSONObject(index).getString("subject");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getLoopMailId(int folder, int index) {
        try {
            return loopMailJSON.getJSONArray(folder).getJSONObject(index).getString("ID");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getLoopMailBody(int folder, int index) {
        return null;
    }
}
