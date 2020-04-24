package ga.caseyavila.velcro;

import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.LoginActivity.casey;


public class CourseService extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityReference;
    private int period;

    CourseService(Activity activity, int courseNumber) {
        activityReference = new WeakReference<>(activity);
        period = courseNumber;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findProgressReport(period);
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

        ((CourseActivity) activity).addCards();
    }
}

