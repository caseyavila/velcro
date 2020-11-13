package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;

public class Attachment {

    private final String title;
    private final String url;

    public Attachment(JSONObject linkJSON) throws JSONException {
        title = linkJSON.getString("Title");
        url = linkJSON.getString("URL");
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
