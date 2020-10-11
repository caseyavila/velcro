package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

public class Upcoming {

    private String title;
    private String course;
    private String points;
    private String description;

    public Upcoming(JSONObject upcomingJSON) throws JSONException {
        title = upcomingJSON.getString("title");
        course = upcomingJSON.getString("courseName");
        points = upcomingJSON.getString("maxPoints");
        description = upcomingJSON.getString("description");
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
}
