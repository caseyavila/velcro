package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.appbar.MaterialToolbar;
import org.json.JSONException;
import java.io.IOException;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.LoopMailAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private LoopMailAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;
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

        MaterialToolbar appBar = getView().findViewById(R.id.loopmail_appbar);
        progressBar = getView().findViewById(R.id.loopmail_progress_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(appBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Loopmail");

        refreshLayout = getView().findViewById(R.id.loopmail_refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(
                () -> loadMail(true)
        );
        refreshLayout.setEnabled(false);

        loadMail(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Store scroll state of recyclerview
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    private void addCards() {
        recyclerView = getView().findViewById(R.id.loopmail_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new LoopMailAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setEnabled(true);

        if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void updateCards() {
        for (int i = 0; i < casey.getMailBox(folder).getNumberOfLoopMails(); i++) {
            adapter.notifyItemChanged(i);
        }
    }

    private void loadMail(boolean refresh) {
        final Runnable runnable = () -> {
            try {
                casey.findLoopMailInbox(folder);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(() -> {
                if (refresh) {
                    updateCards();
                    SwipeRefreshLayout refreshLayout = getView().findViewById(R.id.loopmail_refresh);
                    refreshLayout.setRefreshing(false);
                } else {
                    progressBar.setEnabled(true);
                    addCards();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
