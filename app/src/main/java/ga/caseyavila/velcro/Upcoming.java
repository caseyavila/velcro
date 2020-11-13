package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;

public class Upcoming {

    private final String title;
    private final String course;
    private final String points;
    private final String dueDate;
    private final String description;
    private Attachment[] attachments;
    private boolean hasAttachments;

    public Upcoming(JSONObject upcomingJSON) throws JSONException {
        title = upcomingJSON.getString("title");
        course = upcomingJSON.getString("courseName");
        points = upcomingJSON.getString("maxPoints");
        dueDate = DateFormat.getDateInstance().format(new Date(upcomingJSON.getLong("dueDate")));
        description = upcomingJSON.getString("description");

        try {
            attachments = new Attachment[upcomingJSON.getJSONArray("links").length()];
            for (int i = 0; i < attachments.length; i++) {
                attachments[i] = new Attachment(upcomingJSON.getJSONArray("links").getJSONObject(i));
            }
            hasAttachments = true;
        } catch (JSONException e) {
            hasAttachments = false;
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

    public boolean hasAttachments() {
        return hasAttachments;
    }

    public Attachment getAttachment(int index) {
        return attachments[index];
    }

    public int getNumberOfAttachments() {
        return attachments.length;
    }
}
