package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.MainViewAdapter;
import ga.caseyavila.velcro.asynctasks.RefreshAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class CoursesFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private MainViewAdapter adapter;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    public CoursesFragment() {
        // Required empty public constructor
    }

    public static CoursesFragment newInstance() {
        CoursesFragment fragment = new CoursesFragment();
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
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshLayout = getView().findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(
                () -> new RefreshAsyncTask(this).execute()
        );
        refreshLayout.setEnabled(false);  //Disable refresh while starting up

        addCards();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Store scroll state of recyclerview
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    public void addCards() {
        recyclerView = getView().findViewById(R.id.main_recycler_view);

        recyclerView.setNestedScrollingEnabled(false); // Fix scrolling of RecyclerView

        adapter = new MainViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        refreshLayout.setEnabled(true);  //Enable refresh once cards have loaded

        if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    public void updateCards() {
        for (int i = 0; i < casey.getNumberOfPeriods(); i++) {
            adapter.notifyItemChanged(i);
        }
    }
}
