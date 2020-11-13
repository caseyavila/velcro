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
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class Student {

    private String subdomain;
    private String username;
    private String password;
    private String hashedPassword;
    private String studentId;
    private String cookie;
    private Period[] periodArray;
    private Upcoming[] upcomingArray;
    private final Mailbox[] mailboxArray = new Mailbox[3];
    private News[] newsArray;
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

    public void logOut() {
        isLoggedIn = false;
    }

    public int getNumberOfPeriods() {
        return periodArray.length;
    }

    public Period getPeriod(int period) {
        return periodArray[period];
    }

    public int getNumberOfUpcoming() {
        return upcomingArray.length;
    }

    public Upcoming getUpcoming(int upcoming) {
        return upcomingArray[upcoming];
    }

    public Mailbox getMailBox(int index) {
        return mailboxArray[index - 1];
    }

    public int getNumberOfNews() {
        return newsArray.length;
    }

    public News getNews(int index) {
        return newsArray[index];
    }

    public boolean isAutoLoginReady() {
        return sharedPreferences.contains("subdomain") &&
               sharedPreferences.contains("username") &&
               sharedPreferences.contains("hashedPassword") &&
               sharedPreferences.contains("studentId") &&
               sharedPreferences.contains("cookie");
    }

    private String baseUrl() {
        return "https://" + subdomain + ".schoolloop.com";
    }

    private String credentials(String username, String password) {
        return Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
    }

    // Apply authorization, headers, and cookies to a connection
    private void setRequestProperties(HttpURLConnection urlConnection) throws ProtocolException {
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, hashedPassword));
        urlConnection.setRequestProperty("SL-HASH", "true");
        urlConnection.setRequestProperty("SL-UUID", getVelcroUUID());
        urlConnection.addRequestProperty("Cookie", cookie);
    }

    private static String inputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    private String getVelcroUUID() {
        if (sharedPreferences.contains("UUID")) {  // If UUID already exists in shared preferences
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
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/login" +
                "?devOS=Android" +
                "&hash=false" +
                "&uuid=" + getVelcroUUID() +
                "&version=3.2.5" +
                "&year=" + Calendar.getInstance().get(Calendar.YEAR) +
                "&trim=true")
                .openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic " + credentials(username, password));
        urlConnection.connect();

        password = null;  // Nullify plain-text password after request (ASAP!!!)

        JSONObject loginJSON = new JSONObject(inputStreamToString(urlConnection.getInputStream()));

        // Build cookie string from Set-Cookie headers
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

        setRequestProperties(urlConnection);
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

        setRequestProperties(urlConnection);
        urlConnection.connect();

        JSONArray jsonArray = new JSONArray(inputStreamToString(urlConnection.getInputStream()));
        periodArray[period].addProgressReport(jsonArray.getJSONObject(0));

        urlConnection.disconnect();
    }

    public void findUpcoming() throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/assignments" +
                "?studentID=" + studentId)
                .openConnection();

        setRequestProperties(urlConnection);
        urlConnection.connect();

        JSONArray jsonArray = new JSONArray(inputStreamToString(urlConnection.getInputStream()));
        if (upcomingArray == null) {
            upcomingArray = new Upcoming[jsonArray.length()];
        }

        for (int i = 0; i < upcomingArray.length; i++) {
            upcomingArray[i] = new Upcoming(jsonArray.getJSONObject(i));
        }

        urlConnection.disconnect();
    }

    public void findLoopMailInbox(int mailBox, int start, int max) throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/mail_messages" +
                "?folderID=" + mailBox +
                "&max=" + max +
                "&start=" + start +
                "&studentID=" + studentId +
                "&trim=true")
                .openConnection();

        setRequestProperties(urlConnection);
        urlConnection.connect();

        mailboxArray[mailBox - 1] = new Mailbox(new JSONArray(inputStreamToString(urlConnection.getInputStream())));

        urlConnection.disconnect();
    }

    public void findLoopMailBody(int mailBox, int index) throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/mail_messages" +
                "?ID=" + mailboxArray[mailBox - 1].getLoopmail(index).getId() +
                "&studentID=" + studentId +
                "&trim=true")
                .openConnection();

        setRequestProperties(urlConnection);
        urlConnection.connect();

        JSONObject response = new JSONObject(inputStreamToString(urlConnection.getInputStream()));

        mailboxArray[mailBox - 1].getLoopmail(index).addBody(response.getString("message"));
        mailboxArray[mailBox - 1].getLoopmail(index).addLinks(response.getJSONArray("links"));

        urlConnection.disconnect();
    }

    public void findNews() throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl() + "/mapi/news" +
                "?alerts=true" +
                // I couldn't figure out how the api works, but this seemed to work
                "&lastRequest=" + (System.currentTimeMillis() - 2628000000L) + // One month before the current time...
                "&studentID=" + studentId)
                .openConnection();

        setRequestProperties(urlConnection);
        urlConnection.connect();

        JSONArray jsonArray = new JSONArray(inputStreamToString(urlConnection.getInputStream()));
        if (newsArray == null) {
            newsArray = new News[jsonArray.length()];
        }

        for (int i = 0; i < newsArray.length; i++) {
            newsArray[i] = new News(jsonArray.getJSONObject(i));
        }

        urlConnection.disconnect();
    }
}
