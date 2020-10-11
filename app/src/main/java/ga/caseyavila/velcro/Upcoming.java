package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class Upcoming {

    private String title;
    private String course;
    private String points;
    private String description;
    private String dueDate;

    public Upcoming(JSONObject upcomingJSON) throws JSONException {
        title = upcomingJSON.getString("title");
        course = upcomingJSON.getString("courseName");
        points = upcomingJSON.getString("maxPoints");
        description = upcomingJSON.getString("description");
        dueDate = DateFormat.getDateInstance().format(new Date(upcomingJSON.getLong("dueDate")));
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public String getPoints() {
        return points;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }
}
