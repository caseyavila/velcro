package ga.caseyavila.velcro.fragments;

import android.app.Activity;
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
import ga.caseyavila.velcro.adapters.MainViewAdapter;
import ga.caseyavila.velcro.asynctasks.RefreshAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private MainViewAdapter adapter;

    public CoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance(String param1, String param2) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshLayout = getView().findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), R.color.colorRefresh));
        refreshLayout.setOnRefreshListener(
                () -> new RefreshAsyncTask(this).execute()
        );
        refreshLayout.setEnabled(false);

        addCards();

    }


    public void addCards() {
        RecyclerView recyclerView = getView().findViewById(R.id.main_recycler_view);

        recyclerView.setNestedScrollingEnabled(false); // Fix scrolling of RecyclerView

        adapter = new MainViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setEnabled(true);
    }

    public void updateCards() {
        for (int i = 0; i < casey.getNumberOfPeriods(); i++) {
            adapter.notifyItemChanged(i);
        }
    }
}
