package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ga.caseyavila.velcro.fragments.CoursesFragment;
import ga.caseyavila.velcro.fragments.LockerFragment;
import ga.caseyavila.velcro.fragments.LogoutDialogFragment;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.LoopMailFragment;
import ga.caseyavila.velcro.fragments.UpcomingFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String currentFragmentTag = "courses";  // Set default fragment tag
    private FragmentManager manager = this.getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                item -> {
                    switch (item.getItemId()) {
                        case R.id.navigation_courses:
                            attachFragment(CoursesFragment.newInstance(), "courses");
                            return true;
                        case R.id.navigation_assignments:
                            attachFragment(UpcomingFragment.newInstance(), "assignments");
                            return true;
                        case R.id.navigation_loopmail:
                            attachFragment(LoopMailFragment.newInstance(), "loopmail");
                            return true;
                        case R.id.navigation_locker:
                            attachFragment(LockerFragment.newInstance(), "locker");
                            return true;
                    }
                    return false;
                };
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            // Do nothing when re-selecting on bottom navigation
        });

        openFragment(CoursesFragment.newInstance());  // Initial fragment
    }

    private void openFragment(Fragment fragment) {  // Use currentFragmentTag as the initial fragment tag
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment, currentFragmentTag);
        transaction.commit();
    }

    public void attachFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        if (manager.findFragmentByTag(tag) == null) { // If no fragment in the backStack has the same tag, create a new one
            fragmentTransaction.add(R.id.container, fragment, tag);
            fragmentTransaction.hide(manager.findFragmentByTag(currentFragmentTag));
            fragmentTransaction.commit();
        } else {  // Simply switch fragments
            fragmentTransaction.hide(manager.findFragmentByTag(currentFragmentTag));
            fragmentTransaction.show(manager.findFragmentByTag(tag)).commit();
        }
        currentFragmentTag = tag;  // Set the tag of the current fragment
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.show(fragmentManager, "logout_dialog");
    }
}
