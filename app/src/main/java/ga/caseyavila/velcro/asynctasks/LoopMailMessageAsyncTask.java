package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.LoopMailMessageActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailMessageAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<LoopMailMessageActivity> activityWeakReference;
    private final int folder;
    private final int index;

    public LoopMailMessageAsyncTask(LoopMailMessageActivity loopMailMessageActivity, int folder, int index) {
        activityWeakReference = new WeakReference<>(loopMailMessageActivity);
        this.folder = folder;
        this.index = index;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findLoopMailBody(folder, index);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        LoopMailMessageActivity loopMailMessageActivity = activityWeakReference.get();

        ProgressBar progressBar = loopMailMessageActivity.findViewById(R.id.loopmail_message_progress_bar);

        progressBar.setEnabled(true);  // Make progressbar appear
        loopMailMessageActivity.addCards();  // Load cards
        progressBar.setVisibility(View.INVISIBLE);  // Make progress bar disappear after loading cards
    }
}
