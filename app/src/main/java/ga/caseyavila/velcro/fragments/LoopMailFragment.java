package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.LoopMailAdapter;
import ga.caseyavila.velcro.asynctasks.LoopMailAsyncTask;

public class LoopMailFragment extends Fragment {

    private LoopMailAdapter adapter;

    public LoopMailFragment() {
        // Required empty public constructor
    }

    public static LoopMailFragment newInstance(String param1, String param2) {
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
        return inflater.inflate(R.layout.fragment_loop_mail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        new LoopMailAsyncTask(this, 1).execute();
    }

    public void addCards() {
        RecyclerView recyclerView = getView().findViewById(R.id.loopmail_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new LoopMailAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
