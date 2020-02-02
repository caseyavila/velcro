package ga.caseyavila.velcro;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.LoginActivity.casey;

public class AutoLoginService extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;

    AutoLoginService(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        Activity activity = activityReference.get();
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
        activity.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
        ((MainActivity) activity).addCards();
    }
}
