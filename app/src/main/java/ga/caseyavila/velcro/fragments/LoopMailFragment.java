package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.LoopMailAdapter;
import ga.caseyavila.velcro.asynctasks.LoopMailAsyncTask;
import ga.caseyavila.velcro.asynctasks.LoopMailRefreshAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailFragment extends Fragment {

    private SwipeRefreshLayout loopMailRefreshLayout;
    private LoopMailAdapter adapter;
    private final int folder = 1;

    public LoopMailFragment() {
        // Required empty public constructor
    }

    public static LoopMailFragment newInstance() {
        LoopMailFragment fragment = new LoopMailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loopmail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        loopMailRefreshLayout = getView().findViewById(R.id.loopmail_refresh);
        loopMailRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        loopMailRefreshLayout.setOnRefreshListener(
                () -> new LoopMailRefreshAsyncTask(this, folder).execute()
        );
        loopMailRefreshLayout.setEnabled(false);

        new LoopMailAsyncTask(this, folder).execute();
    }

    public void addCards() {
        RecyclerView recyclerView = getView().findViewById(R.id.loopmail_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new LoopMailAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loopMailRefreshLayout.setEnabled(true);
    }

    public void updateCards() {
        for (int i = 0; i < casey.getNumberOfLoopMails(folder); i++) {
            adapter.notifyItemChanged(i);
        }
    }
}
