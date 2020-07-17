package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import ga.caseyavila.velcro.activities.CourseActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseRefreshAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<CourseActivity> courseActivityReference;
    private final int period;

    public CourseRefreshAsyncTask(CourseActivity courseActivity, int period) {
        courseActivityReference = new WeakReference<CourseActivity>(courseActivity);
        this.period = period;
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
        CourseActivity courseActivity = courseActivityReference.get();

        courseActivity.updateCards(period);  //Load cards
    }
}

