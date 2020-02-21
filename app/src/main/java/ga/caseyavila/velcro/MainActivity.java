package ga.caseyavila.velcro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.time.Period;

import static ga.caseyavila.velcro.LoginActivity.casey;


public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    static SharedPreferences sharedPreferences;
    private SwipeRefreshLayout refreshLayout;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

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

        if (User.isLoggedIn) {
            addCards();
        } else if (sharedPreferences.contains("username")) {
            progressBar.setVisibility(View.VISIBLE);
            casey.setUsername(sharedPreferences.getString("username", "error"));
            casey.setPassword(sharedPreferences.getString("password", "error"));
            new AutoLoginService(this).execute();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void addCards() {

        refreshLayout.setEnabled(true);

        Typeface manrope = Typeface.createFromAsset(getAssets(), "fonts/manrope_medium.ttf");

        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        PeriodViewAdapter periodViewAdapter = new PeriodViewAdapter(this, manrope);
        recyclerView.setAdapter(periodViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.show(fragmentManager, "logout_dialog");
    }
}
