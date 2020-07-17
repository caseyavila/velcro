package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.CoursesFragment;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<CoursesFragment> coursesFragmentReference;

    public RefreshAsyncTask(CoursesFragment coursesFragment) {
        coursesFragmentReference = new WeakReference<>(coursesFragment);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findReportCard();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        CoursesFragment coursesFragment = coursesFragmentReference.get();

        coursesFragment.updateCards();

        SwipeRefreshLayout refreshLayout = coursesFragment.getView().findViewById(R.id.refresh);
        refreshLayout.setRefreshing(false);
    }
}
