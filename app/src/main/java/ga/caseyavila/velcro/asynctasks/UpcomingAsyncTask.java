package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.UpcomingFragment;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class UpcomingAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<UpcomingFragment> fragmentWeakReference;

    WeakReference<UpcomingFragment> getFragmentWeakReference() {
        return fragmentWeakReference;
    }

    public UpcomingAsyncTask(UpcomingFragment upcomingFragment) {
        this.fragmentWeakReference = new WeakReference<>(upcomingFragment);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findUpcoming();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        UpcomingFragment upcomingFragment = fragmentWeakReference.get();

        ProgressBar progressBar = upcomingFragment.getView().findViewById(R.id.upcoming_progress_bar);

        progressBar.setEnabled(true);
        upcomingFragment.addCards();
        progressBar.setVisibility(View.INVISIBLE);
    }
}
