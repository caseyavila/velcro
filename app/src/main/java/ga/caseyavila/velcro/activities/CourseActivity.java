package ga.caseyavila.velcro.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import java.io.IOException;
import java.text.ParseException;
import ga.caseyavila.velcro.adapters.CourseViewAdapter;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.fragments.AssignmentAddDialogFragment;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseActivity extends AppCompatActivity {

    private int period;
    private CourseViewAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle bundle = this.getIntent().getExtras();
        period = bundle.getInt("period");

        progressBar = findViewById(R.id.course_progress_bar);

        MaterialToolbar appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle(casey.getPeriod(bundle.getInt("period")).getCourseName());  // Add title of toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add back button
        appBar.setNavigationOnClickListener(view -> finish());  // Finish activity when back button is pressed

        floatingActionButton = findViewById(R.id.add_assignment_button);
        floatingActionButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = new AssignmentAddDialogFragment(adapter);

            dialogFragment.setArguments(bundle);

            dialogFragment.show(fragmentManager, "assignment_add_dialog");
        });

        loadCourses(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  // Add icons to toolbar
        getMenuInflater().inflate(R.menu.toolbar_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        loadCourses(true);
        return super.onOptionsItemSelected(item);
    }

    private void addCards() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new CourseViewAdapter(this, period);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateCards() {
        for (int i = 0; i < casey.getPeriod(period).getNumberOfAssignments() + 1; i++) {  // Add one for header card
            adapter.notifyItemChanged(i);
        }
    }

    private void loadCourses(boolean refresh) {
        new Thread(() -> {
            try {
                casey.findProgressReport(period);
            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                if (refresh) {
                    updateCards();
                } else {
                    progressBar.setEnabled(true);
                    addCards();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }).start();
    }
}
