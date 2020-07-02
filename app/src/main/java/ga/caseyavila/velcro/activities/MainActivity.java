package ga.caseyavila.velcro.activities;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ga.caseyavila.velcro.fragments.LogoutDialogFragment;
import ga.caseyavila.velcro.adapters.MainViewAdapter;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.asynctasks.RefreshAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;
import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;


public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;
    private MainViewAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorRefresh));
        refreshLayout.setOnRefreshListener(
                () -> new RefreshAsyncTask((Activity) findViewById(R.id.main_recycler_view).getContext()).execute()
        );
        refreshLayout.setEnabled(false);

        addCards();
    }

    void addCards() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);

        recyclerView.setNestedScrollingEnabled(false); // Fix scrolling of RecyclerView

        adapter = new MainViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setEnabled(true);
    }

    public void updateCards() {
        for (int i = 0; i < casey.getNumberOfPeriods(); i++) {
            adapter.notifyItemChanged(i);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.show(fragmentManager, "logout_dialog");
    }
}
