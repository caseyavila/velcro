package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import org.json.JSONException;
import java.io.IOException;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.UpcomingAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class UpcomingFragment extends Fragment {

    private UpcomingAdapter adapter;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    public static UpcomingFragment newInstance() {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        progressBar = requireView().findViewById(R.id.upcoming_progress_bar);

        refreshLayout = requireView().findViewById(R.id.upcoming_refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(
                () -> loadUpcoming(true)
        );
        refreshLayout.setEnabled(false);

        loadUpcoming(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Store scroll state of recyclerview
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    private void addCards() {
        recyclerView = requireView().findViewById(R.id.upcoming_recycler_view);

        recyclerView.setNestedScrollingEnabled(false);

        adapter = new UpcomingAdapter(getContext());
        recyclerView.setAdapter(adapter);

        refreshLayout.setEnabled(true);  // Enable refresh once cards have loaded

        if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void updateCards() {
        for (int i = 0; i < casey.getNumberOfUpcoming(); i++) {
            adapter.notifyItemChanged(i);
        }
    }

    private void loadUpcoming(boolean refresh) {
        new Thread(() -> {
            try {
                casey.findUpcoming();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            requireActivity().runOnUiThread(() -> {
                if (refresh) {
                    updateCards();
                    refreshLayout.setRefreshing(false);
                } else {
                    progressBar.setEnabled(true);
                    addCards();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }).start();
    }
}