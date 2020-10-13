package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;

public class Upcoming {

    private String title;
    private String course;
    private String points;
    private String dueDate;
    private String description;
    private Link[] links;

    public Upcoming(JSONObject upcomingJSON) throws JSONException {
        title = upcomingJSON.getString("title");
        course = upcomingJSON.getString("courseName");
        points = upcomingJSON.getString("maxPoints");
        dueDate = DateFormat.getDateInstance().format(new Date(upcomingJSON.getLong("dueDate")));
        description = upcomingJSON.getString("description");

        try {
            links = new Link[upcomingJSON.getJSONArray("links").length()];
            for (int i = 0; i < links.length; i++) {
                links[i] = new Link(upcomingJSON.getJSONArray("links").getJSONObject(i));
            }
        } catch (JSONException e) {
            links = null;
        }
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

    public String getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasLinks() {
        return links != null;
    }

    public Link getLink(int index) {
        return links[index];
    }

    public int numberOfLinks() {
        return links.length;
    }
}
