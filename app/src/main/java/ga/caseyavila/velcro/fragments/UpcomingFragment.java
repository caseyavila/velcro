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

import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.UpcomingAdapter;
import ga.caseyavila.velcro.asynctasks.UpcomingAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class UpcomingFragment extends Fragment {

    private RecyclerView recyclerView;
    private UpcomingAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private Parcelable recyclerViewState;

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
        refreshLayout = getView().findViewById(R.id.upcoming_refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        refreshLayout.setOnRefreshListener(
//                () -> new RefreshAsyncTask(this).execute()
//        );
        refreshLayout.setEnabled(false);  // Disable refresh while starting up

        new UpcomingAsyncTask(this).execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Store scroll state of recyclerview
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    public void addCards() {
        recyclerView = getView().findViewById(R.id.upcoming_recycler_view);

        recyclerView.setNestedScrollingEnabled(false);

        adapter = new UpcomingAdapter(getContext());
        recyclerView.setAdapter(adapter);

        refreshLayout.setEnabled(true);  //Enable refresh once cards have loaded

        if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    public void updateCards() {
        for (int i = 0; i < casey.getNumberOfUpcoming(); i++) {
            adapter.notifyItemChanged(i);
        }
    }
}