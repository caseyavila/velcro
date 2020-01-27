package ga.caseyavila.velcro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
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
                casey.findNumberOfPeriods();
                casey.teacherFinder();
                casey.gradeFinder();
                casey.classFinder();
                casey.percentageFinder();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();

        SwipeRefreshLayout refreshLayout = activity.findViewById(R.id.refresh);

        activity.finish();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);

        refreshLayout.setRefreshing(false);
    }
}
