package ga.caseyavila.velcro.asynctasks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.MainActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;
import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Activity> activityReference;

    public LoginAsyncTask(Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //If sharedPreferences already exists...
            if (casey.isAutoLoginReady()) {
                //Set fields in User to match
                casey.setSubdomain(sharedPreferences.getString("subdomain", "error"));
                casey.setUsername(sharedPreferences.getString("username", "error"));
                casey.setHashedPassword(sharedPreferences.getString("hashedPassword", "error"));
                casey.setStudentId(sharedPreferences.getString("studentId", "error"));
                casey.setCookie(sharedPreferences.getString("cookie", "error"));
            } else {
                //Find values
                casey.findLoginData();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("subdomain", casey.getSubdomain());
                editor.putString("username", casey.getUsername());
                editor.putString("hashedPassword", casey.getHashedPassword());
                editor.putString("studentId", casey.getStudentId());
                editor.putString("cookie", casey.getCookie());
                editor.apply();
            }

            casey.findReportCard();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Activity activity = activityReference.get();

        if (casey.isLoggedIn()) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {  // Alert user if username and password doesn't match
            activity.findViewById(R.id.login_notification).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.login_button).setEnabled(true);
            activity.findViewById(R.id.subdomain_layout).setEnabled(true);
            activity.findViewById(R.id.username_layout).setEnabled(true);
            activity.findViewById(R.id.password_layout).setEnabled(true);
        }
        activity.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
    }
}
