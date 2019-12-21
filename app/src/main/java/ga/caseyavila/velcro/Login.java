package ga.caseyavila.velcro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static ga.caseyavila.velcro.LoginActivity.casey;

public class Login extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;
    private Dialog dialog;

    Login (Activity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(activityReference.get());
        dialog.setContentView(R.layout.dialog);
        dialog.show();
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
        dialog.dismiss();
    }
}
