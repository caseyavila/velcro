package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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

public class CoursesFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private MainViewAdapter adapter;

    private Boolean animate;

    private static final String ANIMATE = "animate";

    public CoursesFragment() {
        // Required empty public constructor
    }

    public static CoursesFragment newInstance(Boolean animate) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putBoolean(ANIMATE, animate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            animate = getArguments().getBoolean(ANIMATE);
        }
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

        if (animate) {
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_fall_down);
            recyclerView.setLayoutAnimation(layoutAnimationController);
        }

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
