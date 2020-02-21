package ga.caseyavila.velcro;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.LoginActivity.casey;

public class RefreshService extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;

    RefreshService(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            User.isLoggedIn = false;
            casey.getMainDocument();
            casey.loginChecker();
            if (!User.isLoggedIn) {
                return null;
            } else {
                casey.infoFinder();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();

        ((MainActivity) activity).addCards();

        SwipeRefreshLayout refreshLayout = activity.findViewById(R.id.refresh);
        refreshLayout.setRefreshing(false);
    }
}
