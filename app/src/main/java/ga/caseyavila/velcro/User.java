package ga.caseyavila.velcro;

import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
import android.util.Base64;

import java.util.*;

import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class User {

    private String username;
    private String password;
    private String subdomain;
    private String sessionCookie;
    private String hashedPassword;
    private Period[] periodArray;
    private Mailbox[] mailboxArray;
    private String studentId;
    private boolean isLoggedIn;

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

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAutoLoginReady() {
        return sharedPreferences.contains("subdomain") &&
               sharedPreferences.contains("username") &&
               sharedPreferences.contains("hashedPassword") &&
               sharedPreferences.contains("studentId") &&
               sharedPreferences.contains("JSESSIONID");
    }

    private String baseUrl() {
        return "https://" + subdomain + ".schoolloop.com";
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
        Connection.Response loginResponse = Jsoup.connect(baseUrl() + "/mapi/login")
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

    // Contains information about overall schedule, classes, teachers, grades
    public void findReportCard() throws IOException, JSONException {
        Connection.Response reportCardResponse = Jsoup.connect(baseUrl() + "/mapi/report_card")
                .method(Connection.Method.GET)
                .data("studentID", studentId)
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(username, hashedPassword))
                .header("SL-HASH", "true")
                .header("SL-UUID", getVelcroUUID())
                .cookie("JSESSIONID", sessionCookie)
                .cookie("slid", studentId)
                .execute();

        if (reportCardResponse.statusCode() == 200) {
            isLoggedIn = true;  //Set User to logged in status
        }

        JSONArray jsonArray = new JSONArray(reportCardResponse.body());

        if (periodArray == null) {
            periodArray = new Period[jsonArray.length()];
        }

        for (int i = 0; i < periodArray.length; i++) {
            periodArray[i] = new Period(jsonArray.getJSONObject(i));
        }
    }

    // Contains information within an individual class
    public void findProgressReport(int period) throws IOException, JSONException {
        Connection.Response progressReportResponse = Jsoup.connect(baseUrl() + "/mapi/progress_report")
                .method(Connection.Method.GET)
                .data("periodID", periodArray[period].getCourseId())
                .data("studentID", studentId)
                .data("trim", "true")
                .header("Authorization", "Basic " + credentials(username, hashedPassword))
                .header("SL-HASH", "true")
                .header("SL-UUID", getVelcroUUID())
                .cookie("JSESSIONID", sessionCookie)
                .cookie("slid", studentId)
                .execute();

        JSONArray jsonArray = new JSONArray(progressReportResponse.body());

        periodArray[period].addProgressReport(jsonArray.getJSONObject(0));
    }

    public void findLoopMailInbox(int folder) throws IOException, JSONException {
        Connection.Response loopMailResponse = Jsoup.connect(baseUrl() + "/mapi/mail_messages")
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

        if (mailboxArray == null) {
            mailboxArray = new Mailbox[3];
        }
        mailboxArray[folder] = new Mailbox(new JSONArray(loopMailResponse.body()));
    }

    public void findLoopMailBody(int folder, int index) throws IOException, JSONException {
        Connection.Response loopMailBodyResponse = Jsoup.connect(baseUrl() + "/mapi/mail_messages")
                .method(Connection.Method.GET)
                .data("ID", getMailBox(folder).getLoopmail(index).getId())
                .data("studentID", studentId)
                .data("url", baseUrl())
                .header("Authorization", "Basic " + credentials(username, hashedPassword))
                .header("SL-HASH", "true")
                .header("SL-UUID", getVelcroUUID())
                .cookie("JSESSIONID", sessionCookie)
                .cookie("slid", studentId)
                .execute();

        mailboxArray[folder].getLoopmail(index).addBody(new JSONObject(loopMailBodyResponse.body()).getString("message"));
    }

    public Period getPeriod(int period) {
        return periodArray[period];
    }

    public int getNumberOfPeriods() {
        return periodArray.length;
    }

    public Mailbox getMailBox(int index) {
        return mailboxArray[index];
    }

}
