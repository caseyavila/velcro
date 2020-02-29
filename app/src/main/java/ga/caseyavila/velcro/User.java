package ga.caseyavila.velcro;

import android.os.Build;
import android.util.SparseArray;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import android.util.Base64;
import java.util.UUID;

public class User {

    private String username;
    private String password;
    private String school_url = "ahs-fusd-ca";
    private String base_url = "https://" + this.school_url + ".schoolloop.com";
    private Document login_document;
    private String form_data_id;
    private Connection.Response login_response;
    private Connection.Response main_response;
    private Connection.Response studentIdResponse;
    private Connection.Response reportCardResponse;
    private Connection.Response period_response;
    private JSONObject loginJSON;
    private JSONObject reportCardJSON;
    private Long studentId;
    public static Long timeSinceLogin;
    public static SparseArray<String> teacherMap = new SparseArray<String>();
    public static SparseArray<String> gradeMap = new SparseArray<String>();
    public static SparseArray<String> classMap = new SparseArray<String>();
    public static SparseArray<String> percentageMap = new SparseArray<String>();
    public static SparseArray<String> linkMap = new SparseArray<String>();
    public static SparseArray<String> assignmentNames = new SparseArray<String>();
    public static SparseArray<Integer> pointsPossibleArray = new SparseArray<Integer>();
    public static SparseArray<Integer> pointsEarnedArray = new SparseArray<Integer>();
    public static SparseArray<String> categoryNames = new SparseArray<String>();
    public static int numberOfPeriods;
    public static boolean isLoggedIn;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
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

    void getStudentId() throws IOException, JSONException {
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
    }

    void getReportCard() throws IOException, JSONException {
        this.reportCardResponse = Jsoup.connect(base_url + "/mapi/report_card")
                .method(Connection.Method.GET)
                .data("studentID", studentId.toString())
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(username, password))
                .execute();
        reportCardJSON = new JSONObject(this.reportCardResponse.parse().text());
    }

    public void loginChecker() throws IOException {
//        if (this.main_response.parse().title().contains("Portal")) {
//            isLoggedIn = true;
//        }
    }

    public void infoFinder() throws IOException {
        int period = 0;
        Elements rows = this.main_response.parse().getElementsByClass("student_row");
        for (Element row : rows) {
            Elements grade = row.getElementsByClass("float_l grade");
            gradeMap.put(period, grade.text());
            Elements percent = row.getElementsByClass("float_l percent");
            percentageMap.put(period, percent.text());
            Elements teacher = row.getElementsByClass("teacher co-teacher");
            teacherMap.put(period, teacher.text());
            Elements classes = row.getElementsByAttributeValueMatching("data-track-link", "Academic Classroom");
            classMap.put(period, classes.text());
            Elements link = row.getElementsByClass("pr_link").select("a[href]");
            linkMap.put(period, base_url + link.attr("href"));
            period++;
        }
        numberOfPeriods = period;
    }

    public void getPeriodDocument(String periodUrl) throws IOException {
        period_response = Jsoup.connect(base_url + periodUrl)
                .method(Connection.Method.GET)
                .cookies(login_response.cookies())
                .execute();
        period_response.bufferUp();
    }

    public void assignmentFinder() throws IOException {
        int assignment = 0;
        Elements rows = this.period_response.parse().getElementsByClass("general_body").select("tr");
        for (Element row : rows) {
            Elements assignmentName = row.select("a");
            assignmentNames.put(assignment, assignmentName.text());
            assignment++;
        }
    }
}
