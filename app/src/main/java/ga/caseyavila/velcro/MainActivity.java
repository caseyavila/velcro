package ga.caseyavila.velcro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static ga.caseyavila.velcro.LoginActivity.sharedPreferences;
import static ga.caseyavila.velcro.User.numberOfPeriods;


public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private PeriodViewAdapter adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorRefresh));
        refreshLayout.setOnRefreshListener(
                () -> new RefreshService((Activity) findViewById(R.id.main_recycler_view).getContext()).execute()
        );
        refreshLayout.setEnabled(false);

        progressBar = findViewById(R.id.progress_bar);

        addCards();
    }

    void addCards() {
        refreshLayout.setEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        adapter = new PeriodViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void updateCards() {
        for (int i = 0; i < numberOfPeriods; i++) {
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
