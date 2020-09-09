package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class Loopmail {

    private String id;
    private boolean isRead;
    private String sender;
    private String sendDateTime;
    private String sendDate;
    private String subject;
    private String body;
    private String recipient;

    public Loopmail(JSONObject loopmailJSON) throws JSONException {
        id = loopmailJSON.getString("ID");
        isRead = loopmailJSON.getBoolean("read");
        sender = loopmailJSON.getJSONObject("sender").getString("name");
        sendDateTime = DateFormat.getDateTimeInstance().format(new Date(loopmailJSON.getLong("date")));
        sendDate = DateFormat.getDateInstance().format(new Date(loopmailJSON.getLong("date")));
        subject = loopmailJSON.getString("subject");
        recipient = loopmailJSON.getString("shortRecipientString");
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

    public String getSendDateTime() {
        return sendDateTime;
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

    public String getRecipient() {
        return recipient;
    }
}
