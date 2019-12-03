package ga.caseyavila.velcro;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.concurrent.*;

public class User {

    private String username;
    private String password;
    private String school_url = "ahs-fusd-ca";
    private String full_url = "https://" + this.school_url + ".schoolloop.com/portal/login?etarget=login_form";
    private Document login_document;
    private String form_data_id;
    private Connection.Response login_response;
    private Connection.Response main_response;

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

    public Connection.Response getMainDocument() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Connection.Response> callable = () -> {
            getLoginResponse();
            getLoginDocument();
            getFormDataId();
            Connection.Response main_response = Jsoup.connect(full_url)
                    .method(Connection.Method.POST)
                    .data("login_name", getUsername())
                    .data("password", getPassword())
                    .data("event_override", "login")
                    .data("form_data_id", form_data_id)
                    .cookies(this.login_response.cookies())
                    .execute();
            main_response.bufferUp();
            return main_response;
        };
        Future<Connection.Response> future = executorService.submit(callable);
        return future.get();
    }

    public String getClasses() throws ExecutionException, InterruptedException, IOException {
        Elements classes = getMainDocument().parse().getElementsByAttributeValueMatching("data-track-link", "Academic Classroom");
        return classes.text();
    }

    public String getTeachers() throws ExecutionException, InterruptedException, IOException {
        Elements teachers = getMainDocument().parse().getElementsByClass("teacher co-teacher");
        return teachers.text();
    }

    public String getGrades() throws ExecutionException, InterruptedException, IOException {
        Elements grades = getMainDocument().parse().getElementsByClass("float_l grade");
        return grades.text();
    }
}
