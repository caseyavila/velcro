package ga.caseyavila.velcro.asynctasks;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.UpcomingFragment;

public class UpcomingRefreshAsyncTask extends UpcomingAsyncTask {

    public UpcomingRefreshAsyncTask(UpcomingFragment upcomingFragment) {
        super(upcomingFragment);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        UpcomingFragment upcomingFragment = getFragmentWeakReference().get();

        upcomingFragment.updateCards();

        SwipeRefreshLayout refreshLayout = upcomingFragment.getView().findViewById(R.id.upcoming_refresh);
        refreshLayout.setRefreshing(false);
    }
}
