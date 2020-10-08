package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import ga.caseyavila.velcro.asynctasks.CourseAsyncTask;
import ga.caseyavila.velcro.adapters.CourseViewAdapter;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.asynctasks.CourseRefreshAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseActivity extends AppCompatActivity {

    private int period;
    private CourseViewAdapter adapter;
    private ProgressBar courseProgressBar;
    private MaterialToolbar appBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle bundle = this.getIntent().getExtras();
        period = bundle.getInt("period");

        appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle(casey.getPeriod(bundle.getInt("period")).getCourseName());  // Add title of toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add back button
        appBar.setNavigationOnClickListener(view -> finish());  // Finish activity when back button is pressed

        new CourseAsyncTask(this, period).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  // Add icons to toolbar
        getMenuInflater().inflate(R.menu.toolbar_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        new CourseRefreshAsyncTask(this, period).execute();
        return super.onOptionsItemSelected(item);
    }

    public void addCards() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new CourseViewAdapter(this, period);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateCards(int period) {
        for (int i = 0; i < casey.getPeriod(period).getNumberOfAssignments() + 1; i++) {  // Add one for header card
            adapter.notifyItemChanged(i);
        }
    }
}
