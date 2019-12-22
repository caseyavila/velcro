package ga.caseyavila.velcro;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static ga.caseyavila.velcro.LoginActivity.casey;

public class LoginService extends AsyncTask<Void, Void, Void> {

    private static WeakReference<Activity> activityReference;

    LoginService(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.getMainDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }
}
