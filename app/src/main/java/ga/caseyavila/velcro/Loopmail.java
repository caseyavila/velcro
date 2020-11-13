package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class Loopmail {

    private final String id;
    private final boolean isRead;
    private final String sender;
    private final String sendDateTime;
    private final String sendDate;
    private final String subject;
    private String body;
    private final String recipient;
    private Attachment[] attachments;
    private boolean hasAttachments;

    public Loopmail(JSONObject loopmailJSON) throws JSONException {
        id = loopmailJSON.getString("ID");
        isRead = loopmailJSON.getBoolean("read");
        sender = loopmailJSON.getJSONObject("sender").getString("name");
        sendDateTime = DateFormat.getDateTimeInstance().format(new Date(loopmailJSON.getLong("date")));
        sendDate = DateFormat.getDateInstance().format(new Date(loopmailJSON.getLong("date")));
        subject = loopmailJSON.getString("subject");
        recipient = loopmailJSON.getString("shortRecipientString");
    }

    public void addAttachments(JSONArray linkArray) {
        try {
            attachments = new Attachment[linkArray.length()];
            for (int i = 0; i < attachments.length; i++) {
                attachments[i] = new Attachment(linkArray.getJSONObject(i));
            }
            hasAttachments = true;
        } catch (JSONException e) {
            hasAttachments = false;
        }
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

    public Attachment getAttachment(int index) {
        return attachments[index];
    }

    public boolean hasAttachments() {
        return hasAttachments;
    }

    public int getNumberOfAttachments() {
        return attachments.length;
    }
}
