package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.json.JSONException;
import java.io.IOException;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.MainViewAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class CoursesFragment extends Fragment {

    private MainViewAdapter adapter;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    private SwipeRefreshLayout refreshLayout;

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
        refreshLayout.setOnRefreshListener(this::refreshCourses);
        refreshLayout.setEnabled(false);  // Disable refresh while starting up

        addCards();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Store scroll state of recyclerview
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
    }

    private void addCards() {
        recyclerView = getView().findViewById(R.id.main_recycler_view);

        recyclerView.setNestedScrollingEnabled(false); // Fix scrolling of RecyclerView

        adapter = new MainViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        refreshLayout.setEnabled(true);  // Enable refresh once cards have loaded

        if (recyclerViewState != null && recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    private void updateCards() {
        for (int i = 0; i < casey.getNumberOfPeriods(); i++) {
            adapter.notifyItemChanged(i);
        }
    }

    private void refreshCourses() {
        new Thread(() -> {
            try {
                casey.findReportCard();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(() -> {
                updateCards();
                refreshLayout.setRefreshing(false);
            });
        }).start();
    }
}
