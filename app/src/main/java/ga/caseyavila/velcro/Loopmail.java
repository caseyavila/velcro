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
    private Link[] links;
    private boolean hasLinks;

    public Loopmail(JSONObject loopmailJSON) throws JSONException {
        id = loopmailJSON.getString("ID");
        isRead = loopmailJSON.getBoolean("read");
        sender = loopmailJSON.getJSONObject("sender").getString("name");
        sendDateTime = DateFormat.getDateTimeInstance().format(new Date(loopmailJSON.getLong("date")));
        sendDate = DateFormat.getDateInstance().format(new Date(loopmailJSON.getLong("date")));
        subject = loopmailJSON.getString("subject");
        recipient = loopmailJSON.getString("shortRecipientString");
    }

    public void addLinks(JSONArray linkArray) {
        try {
            links = new Link[linkArray.length()];
            for (int i = 0; i < links.length; i++) {
                System.out.println("Hello THERE");
                links[i] = new Link(linkArray.getJSONObject(i));
            }
            hasLinks = true;
        } catch (JSONException e) {
            hasLinks = false;
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

    public Link getLink(int index) {
        return links[index];
    }

    public boolean hasLinks() {
        return hasLinks;
    }

    public int getNumberOfLinks() {
        return links.length;
    }
}
