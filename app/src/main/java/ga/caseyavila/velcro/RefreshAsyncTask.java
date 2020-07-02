package ga.caseyavila.velcro;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.activities.MainActivity;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;

    public RefreshAsyncTask(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.getReportCard();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();

        ((MainActivity) activity).updateCards();

        SwipeRefreshLayout refreshLayout = activity.findViewById(R.id.refresh);
        refreshLayout.setRefreshing(false);
    }
}
