package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Mailbox {

    private final ArrayList<Loopmail> loopmailArray = new ArrayList<>();

    public Mailbox(JSONArray mailboxJSON) throws JSONException {
        addLoopMail(mailboxJSON);
    }

    public void addLoopMail(JSONArray mailboxJSON) throws JSONException {
        for (int i = 0; i < mailboxJSON.length(); i++) {
            loopmailArray.add(new Loopmail(mailboxJSON.getJSONObject(i)));
        }
    }

    public int getNumberOfLoopMails() {
        return loopmailArray.size();
    }

    public Loopmail getLoopmail(int index) {
        return loopmailArray.get(index);
    }
}
