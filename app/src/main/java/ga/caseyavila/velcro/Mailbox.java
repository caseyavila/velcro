package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;

public class Mailbox {

    private Loopmail[] loopmailArray;

    public Mailbox(JSONArray mailboxJSON) throws JSONException {
        if (loopmailArray == null) {
            loopmailArray = new Loopmail[mailboxJSON.length()];
        }
        for (int i = 0; i < loopmailArray.length; i++) {
            loopmailArray[i] = new Loopmail(mailboxJSON.getJSONObject(i));
        }
    }

    public int getNumberOfLoopMails() {
        return loopmailArray.length;
    }

    public Loopmail getLoopmail(int index) {
        return loopmailArray[index];
    }
}
