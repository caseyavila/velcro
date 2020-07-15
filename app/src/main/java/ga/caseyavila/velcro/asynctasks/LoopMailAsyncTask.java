package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.LoopMailFragment;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<LoopMailFragment> fragmentWeakReference;
    private final int folder;

    public LoopMailAsyncTask(LoopMailFragment loopMailFragment, int folder) {
        fragmentWeakReference = new WeakReference<LoopMailFragment>(loopMailFragment);
        this.folder = folder;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findLoopMailInbox(folder);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        LoopMailFragment loopMailFragment = fragmentWeakReference.get();

        ProgressBar loopMailProgressBar = loopMailFragment.getView().findViewById(R.id.loopmail_progress_bar);

        loopMailProgressBar.setEnabled(true);  //Make progressbar appear
        loopMailFragment.addCards();  //Load cards
        loopMailProgressBar.setVisibility(View.INVISIBLE);  //Make progress bar disappear after loading cards
    }
}

