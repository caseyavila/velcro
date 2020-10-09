package ga.caseyavila.velcro.asynctasks;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.LoopMailFragment;

public class LoopMailRefreshAsyncTask extends LoopMailAsyncTask {

    public LoopMailRefreshAsyncTask(LoopMailFragment loopMailFragment, int folder) {
        super(loopMailFragment, folder);
    }

    @Override
    protected void onPostExecute(Void result) {
        LoopMailFragment loopMailFragment = getFragmentWeakReference().get();

        loopMailFragment.updateCards();

        SwipeRefreshLayout refreshLayout = loopMailFragment.getView().findViewById(R.id.loopmail_refresh);
        refreshLayout.setRefreshing(false);
    }
}
