package ga.caseyavila.velcro;

import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android.text.TextUtils;
import android.util.Base64;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class User {

    private String subdomain;
    private String username;
    private String password;
    private String hashedPassword;
    private String studentId;
    private String cookie;
    private Period[] periodArray;
    private Mailbox[] mailboxArray;
    private boolean isLoggedIn;

    public String getSubdomain() {
        return subdomain;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
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

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAutoLoginReady() {
        return sharedPreferences.contains("subdomain") &&
               sharedPreferences.contains("username") &&
               sharedPreferences.contains("hashedPassword") &&
               sharedPreferences.contains("studentId") &&
               sharedPreferences.contains("cookie");
    }

    public int getNumberOfPeriods() {
        return periodArray.length;
    }

    public Period getPeriod(int period) {
        return periodArray[period];
    }

    public Mailbox getMailBox(int index) {
        return mailboxArray[index];
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

    private static String inputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    public void findLoginData() throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/login" +
                "?devOS=Android" +
                "&hash=false" +
                "&uuid=" + getVelcroUUID() +
                "&version=3.2.4" +
                "&year=2020" +
                "&trim=true")
                .openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, password));
        urlConnection.connect();

        password = null;  //Nullify plain-text password after request (ASAP!!!)

        JSONObject loginJSON = new JSONObject(inputStreamToString(urlConnection.getInputStream()));

        //Build cookie string from Set-Cookie headers
        List<String> cookieList = urlConnection.getHeaderFields().get("Set-Cookie");
        List<String> freshCookies = new ArrayList<>();
        if (cookieList != null) {
            for (String cookie : cookieList) {
                freshCookies.add(cookie.split(";")[0]);
            }
            cookie = TextUtils.join("; ", freshCookies);
        }

        studentId = loginJSON.getString("userID");
        hashedPassword = loginJSON.getString("hashedPassword");

        urlConnection.disconnect();
    }


    public void findReportCard() throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/report_card" +
                "?studentID=" + studentId +
                "&trim=true")
                .openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, hashedPassword));
        urlConnection.setRequestProperty("SL-HASH", "true");
        urlConnection.setRequestProperty("SL-UUID", getVelcroUUID());
        urlConnection.addRequestProperty("Cookie", cookie);
        urlConnection.connect();

        if (urlConnection.getResponseCode() == 200) {
            isLoggedIn = true;
        }

        JSONArray jsonArray = new JSONArray(inputStreamToString(urlConnection.getInputStream()));

        if (periodArray == null) {
            periodArray = new Period[jsonArray.length()];
        }

        for (int i = 0; i < periodArray.length; i++) {
            periodArray[i] = new Period(jsonArray.getJSONObject(i));
        }

        urlConnection.disconnect();
    }

    public void findProgressReport(int period) throws IOException, JSONException, ParseException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/progress_report" +
                "?periodID=" + periodArray[period].getCourseId() +
                "&studentID=" + studentId +
                "&trim=true")
                .openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, hashedPassword));
        urlConnection.setRequestProperty("SL-HASH", "true");
        urlConnection.setRequestProperty("SL-UUID", getVelcroUUID());
        urlConnection.addRequestProperty("Cookie", cookie);
        urlConnection.connect();

        JSONArray jsonArray = new JSONArray(inputStreamToString(urlConnection.getInputStream()));
        periodArray[period].addProgressReport(jsonArray.getJSONObject(0));

        urlConnection.disconnect();
    }

    public void findLoopMailInbox(int folder) throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/mail_messages" +
                "?folderID=" + folder +
                "&max=" + 20 +
                "&start=" + 0 +
                "&studentID=" + studentId +
                "&trim=true")
                .openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, hashedPassword));
        urlConnection.setRequestProperty("SL-HASH", "true");
        urlConnection.setRequestProperty("SL-UUID", getVelcroUUID());
        urlConnection.addRequestProperty("Cookie", cookie);
        urlConnection.connect();

        if (mailboxArray == null) {
            mailboxArray = new Mailbox[3];
        }
        mailboxArray[folder] = new Mailbox(new JSONArray(inputStreamToString(urlConnection.getInputStream())));

        urlConnection.disconnect();
    }

    public void findLoopMailBody(int folder, int index) throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/mail_messages" +
                "?ID=" + mailboxArray[folder].getLoopmail(index).getId() +
                "&studentID=" + studentId +
                "&trim=true")
                .openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, hashedPassword));
        urlConnection.setRequestProperty("SL-HASH", "true");
        urlConnection.setRequestProperty("SL-UUID", getVelcroUUID());
        urlConnection.addRequestProperty("Cookie", cookie);
        urlConnection.connect();

        mailboxArray[folder].getLoopmail(index).addBody(new JSONObject(inputStreamToString(urlConnection.getInputStream())).getString("message"));

        urlConnection.disconnect();
    }
}
