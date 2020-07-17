package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.CourseActivity;
import org.json.JSONException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<CourseActivity> courseActivityReference;
    private final int period;

    public CourseAsyncTask(CourseActivity courseActivity, int period) {
        courseActivityReference = new WeakReference<>(courseActivity);
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

        ProgressBar courseProgressBar = courseActivity.findViewById(R.id.course_progress_bar);

        courseProgressBar.setEnabled(true);  //Make progressbar appear
        courseActivity.addCards();  //Load cards
        courseProgressBar.setVisibility(View.INVISIBLE);  //Make progress bar disappear after loading cards
    }
}
