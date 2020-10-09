package ga.caseyavila.velcro.asynctasks;

import ga.caseyavila.velcro.activities.CourseActivity;

public class CourseRefreshAsyncTask extends CourseAsyncTask {

    public CourseRefreshAsyncTask(CourseActivity courseActivity, int period) {
        super(courseActivity, period);
    }

    @Override
    protected void onPostExecute(Void result) {
        CourseActivity courseActivity = getCourseActivityReference().get();

        courseActivity.updateCards(getPeriod());  // Load cards
    }
}

