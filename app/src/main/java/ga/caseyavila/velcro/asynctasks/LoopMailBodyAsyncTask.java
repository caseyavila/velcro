package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.LoopMailBodyActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailBodyAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<LoopMailBodyActivity> activityWeakReference;
    private final int folder;
    private final int index;

    public LoopMailBodyAsyncTask(LoopMailBodyActivity loopMailBodyActivity, int folder, int index) {
        activityWeakReference = new WeakReference<>(loopMailBodyActivity);
        this.folder = folder;
        this.index = index;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findLoopMailBody(folder, index);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        LoopMailBodyActivity loopMailBodyActivity = activityWeakReference.get();
//
//        ProgressBar loopMailProgressBar = loopMailBodyActivity.findViewById(R.id.loopmail_progress_bar);
//
//        loopMailProgressBar.setEnabled(true);  //Make progressbar appear
        loopMailBodyActivity.addCards();  //Load cards
//        loopMailProgressBar.setVisibility(View.INVISIBLE);  //Make progress bar disappear after loading cards
    }
}
