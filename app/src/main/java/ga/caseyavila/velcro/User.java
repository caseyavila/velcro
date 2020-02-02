package ga.caseyavila.velcro;

import android.util.SparseArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class User {

    private String username;
    private String password;
    private String school_url = "ahs-fusd-ca";
    private String full_url = "https://" + this.school_url + ".schoolloop.com/portal/login?etarget=login_form";
    private Document login_document;
    private String form_data_id;
    private Connection.Response login_response;
    private Connection.Response main_response;
    public static Long timeSinceLogin;
    public static SparseArray<String> teacherMap = new SparseArray<String>();
    public static SparseArray<String> gradeMap = new SparseArray<String>();
    public static SparseArray<String> classMap = new SparseArray<String>();
    public static SparseArray<String> percentageMap = new SparseArray<String>();
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

    private void getLoginResponse() {
        try {
            this.login_response = Jsoup.connect(full_url)
                    .method(Connection.Method.GET)
                    .execute();
            timeSinceLogin = System.nanoTime();
            this.login_response = login_response.bufferUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLoginDocument() throws IOException {
        login_document = login_response.parse();
    }

    private void getFormDataId() {
        Element form_data_id = this.login_document.getElementById("form_data_id");
        this.form_data_id = form_data_id.val();
    }

    public void getMainDocument() throws IOException {
        if (timeSinceLogin == null) {  // Check for first time login
            getLoginResponse();
            getLoginDocument();
            getFormDataId();
        } else if (System.nanoTime() - timeSinceLogin > 3.6e12) {  // Check if previous login was with in the previous hour
            getLoginResponse();
            getLoginDocument();
            getFormDataId();
        }
        main_response = Jsoup.connect(full_url)
                .method(Connection.Method.POST)
                .data("login_name", getUsername())
                .data("password", getPassword())
                .data("event_override", "login")
                .data("form_data_id", form_data_id)
                .cookies(login_response.cookies())
                .execute();
        main_response = main_response.bufferUp();
    }

    public void loginChecker() throws IOException {
        if (this.main_response.parse().title().contains("Portal")) {
            isLoggedIn = true;
        }
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
            period++;
        }
        numberOfPeriods = period;
    }
}
