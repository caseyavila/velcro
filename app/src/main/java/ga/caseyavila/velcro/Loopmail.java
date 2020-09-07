package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class Loopmail {

    private String id;
    private boolean isRead;
    private String sender;
    private String sendDate;
    private String subject;
    private String body;

    public Loopmail(JSONObject loopmailJSON) throws JSONException {
        id = loopmailJSON.getString("ID");
        isRead = loopmailJSON.getBoolean("read");
        sender = loopmailJSON.getJSONObject("sender").getString("name");
        sendDate = DateFormat.getDateTimeInstance().format(new Date(loopmailJSON.getLong("date")));
        subject = loopmailJSON.getString("subject");
    }

    public void addBody(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getSender() {
        return sender;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
