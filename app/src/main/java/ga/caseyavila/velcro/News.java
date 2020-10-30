package ga.caseyavila.velcro;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;

public class News {

    private final String title;
    private final String author;
    private final String date;
    private final boolean isNew;

    public News(JSONObject newsJSON) throws JSONException {
        title = newsJSON.getString("title");
        author = newsJSON.getString("authorName");
        date = DateFormat.getDateInstance().format(new Date(newsJSON.getLong("createdDate")));
        isNew = newsJSON.getBoolean("isNew");
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public boolean isNew() {
        return isNew;
    }
}
