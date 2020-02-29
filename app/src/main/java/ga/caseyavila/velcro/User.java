package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
import android.util.Base64;
import java.util.UUID;

import static ga.caseyavila.velcro.LoginActivity.sharedPreferences;

public class User {

    private String username;
    private String password;
    private String school_url = "ahs-fusd-ca";
    private String base_url = "https://" + this.school_url + ".schoolloop.com";
    private Connection.Response studentIdResponse;
    private Connection.Response reportCardResponse;
    private Connection.Response period_response;
    private JSONObject loginJSON;
    private JSONArray reportCardJSON;
    private Long studentId;
    private boolean isLoggedIn;
    private int numberOfPeriods;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStudentId() {
        return this.studentId.toString();
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public String getSchool_url() {
        return this.school_url;
    }

    public void setSchool_url(String school_url) {
        this.school_url = school_url;
    }

    private String credentials(String username, String password) {
        String encodedInput = username + ":" + password;
        return Base64.encodeToString(encodedInput.getBytes(), Base64.NO_WRAP);
    }

    void findStudentId() throws IOException, JSONException {
        isLoggedIn = false;
        this.studentIdResponse = Jsoup.connect(base_url + "/mapi/login")
                .method(Connection.Method.GET)
                .data("devOS", "Android")
                .data("hash", "false")
                .data("uuid", UUID.randomUUID().toString())
                .data("version", "3.2.4")
                .data("year", "2020")
                .header("Authorization", "Basic " + credentials(username, password))
                .execute();

        loginJSON = new JSONObject(this.studentIdResponse.parse().text());
        studentId = loginJSON.getLong("userID");
        if (studentIdResponse.statusCode() == 200) {
            isLoggedIn = true;
        }
    }

    void getReportCard() throws IOException, JSONException {
        this.reportCardResponse = Jsoup.connect(base_url + "/mapi/report_card")
                .method(Connection.Method.GET)
                .data("studentID", sharedPreferences.getString("studentId", ""))
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", "")))
                .execute();

        reportCardJSON = new JSONArray(this.reportCardResponse.parse().text());
        this.setNumberOfPeriods(reportCardJSON.length());
    }

    String getScore(int period) {
        try {
            JSONObject periodJSON = reportCardJSON.getJSONObject(period);
            // Return a blank string if the score is null
            return (periodJSON.getString("score").equals("null")) ? "" : periodJSON.getString("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    String getGrade(int period) {
        try {
            JSONObject periodJSON = reportCardJSON.getJSONObject(period);
            // Return a blank string if the grade is null
            return (periodJSON.getString("grade").equals("null")) ? "" : periodJSON.getString("grade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    String getTeacher(int period) {
        try {
            JSONObject periodJSON = reportCardJSON.getJSONObject(period);
            return periodJSON.getString("teacherName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    String getCourseName(int period) {
        try {
            JSONObject periodJSON = reportCardJSON.getJSONObject(period);
            return periodJSON.getString("courseName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private void setNumberOfPeriods(int periods) {
        numberOfPeriods = periods;
    }

    int getNumberOfPeriods() {
        return numberOfPeriods;
    }

    boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}
