package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import ga.caseyavila.velcro.activities.MainActivity;
import ga.caseyavila.velcro.adapters.LoopMailAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailFragment extends Fragment {

    private MaterialToolbar appBar;
    private LoopMailAdapter adapter;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private int mailBox = 1;

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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_loopmail, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mailboxes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_inbox) {
            mailBox = 1;
            loadLoopMail(false);
            setAppBar();
            return true;
        } else if (item.getItemId() == R.id.navigation_sent) {
            mailBox = 2;
            loadLoopMail(false);
            setAppBar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        appBar = requireActivity().findViewById(R.id.loopmail_appbar);
        setAppBar();
        ((MainActivity) requireActivity()).setSupportActionBar(appBar);

        progressBar = requireActivity().findViewById(R.id.loopmail_progress_bar);

        refreshLayout = requireActivity().findViewById(R.id.loopmail_refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(() -> loadLoopMail(true));
        refreshLayout.setEnabled(false);

        recyclerView = requireActivity().findViewById(R.id.loopmail_recycler_view);

        loadLoopMail(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Store scroll state of recyclerview
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    private void addCards() {
        adapter = new LoopMailAdapter(getContext(), mailBox, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setEnabled(true);

        if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void updateCards() {
        for (int i = 0; i < casey.getMailBox(mailBox).getNumberOfLoopMails(); i++) {
            adapter.notifyItemChanged(i);
        }
    }

    private void loadLoopMail(boolean refresh) {
        new Thread(() -> {
            try {
                if (refresh) {
                    casey.findLoopMailInbox(mailBox, 0, casey.getMailBox(mailBox).getNumberOfLoopMails());
                    requireActivity().runOnUiThread(() -> {
                        updateCards();
                        refreshLayout.setRefreshing(false);
                    });
                } else {
                    casey.findLoopMailInbox(mailBox, 0, 20);
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setEnabled(true);
                        addCards();
                        progressBar.setVisibility(View.INVISIBLE);
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void loadMore() {
        new Thread(() -> {
            try {
                casey.findLoopMailInbox(mailBox, casey.getMailBox(mailBox).getNumberOfLoopMails(), 20);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            requireActivity().runOnUiThread(this::updateCards);
        }).start();
    }

    private void setAppBar() {
        switch (mailBox) {
            case 1:
                appBar.setTitle(R.string.inbox);
                break;
            case 2:
                appBar.setTitle(R.string.sent);
                break;
        }
    }
}
