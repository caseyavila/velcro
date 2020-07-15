package ga.caseyavila.velcro.asynctasks;

import android.os.AsyncTask;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.LoopMailFragment;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailRefreshAsyncTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<LoopMailFragment> loopMailFragmentWeakReference;

    public LoopMailRefreshAsyncTask(LoopMailFragment loopMailFragment) {
        loopMailFragmentWeakReference = new WeakReference<>(loopMailFragment);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            casey.findLoopMailInbox(1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        LoopMailFragment loopMailFragment = loopMailFragmentWeakReference.get();

        loopMailFragment.updateCards();

        SwipeRefreshLayout refreshLayout = loopMailFragment.getView().findViewById(R.id.loopmail_refresh);
        refreshLayout.setRefreshing(false);
    }
}
