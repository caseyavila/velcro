package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Mailbox {

    private final ArrayList<Loopmail> loopmailArray = new ArrayList<>();

    public Mailbox(JSONArray mailboxJSON, int start, int max) throws JSONException {
        for (int i = start; i < max; i++) {
            loopmailArray.add(new Loopmail(mailboxJSON.getJSONObject(i)));
        }
    }

    public void addLoopMail(JSONArray mailboxJSON, int start, int max) throws JSONException {
        for (int i = 0; i < max; i++) {
            if (start + i > getNumberOfLoopMails() - 1) {
                loopmailArray.add(new Loopmail(mailboxJSON.getJSONObject(i)));
            } else {
                loopmailArray.set(start + i, new Loopmail(mailboxJSON.getJSONObject(i)));
            }
        }
    }

    public int getNumberOfLoopMails() {
        return loopmailArray.size();
    }

    public Loopmail getLoopmail(int index) {
        return loopmailArray.get(index);
    }
}
