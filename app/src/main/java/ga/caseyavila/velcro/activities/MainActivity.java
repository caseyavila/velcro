package ga.caseyavila.velcro.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ga.caseyavila.velcro.fragments.CoursesFragment;
import ga.caseyavila.velcro.fragments.LogoutDialogFragment;
import ga.caseyavila.velcro.adapters.MainViewAdapter;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.asynctasks.RefreshAsyncTask;
import ga.caseyavila.velcro.fragments.LoopMailFragment;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_courses:
                                openFragment(CoursesFragment.newInstance());
                                return true;
                            case R.id.navigation_loopmail:
                                openFragment(LoopMailFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        openFragment(CoursesFragment.newInstance());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.show(fragmentManager, "logout_dialog");
    }
}
