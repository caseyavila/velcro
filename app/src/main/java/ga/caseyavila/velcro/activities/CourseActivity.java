package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.appbar.AppBarLayout;
import ga.caseyavila.velcro.CourseAsyncTask;
import ga.caseyavila.velcro.adapters.CourseViewAdapter;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseActivity extends AppCompatActivity {

    private int period;
    private CourseViewAdapter adapter;
    private AppBarLayout appBarLayout;
    private MaterialToolbar appBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        period = bundle.getInt("period");

        appBarLayout = findViewById(R.id.appbar_layout);
        appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle(casey.getCourseName(bundle.getInt("period")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add back button
        appBar.setNavigationOnClickListener(view -> finish());  // Finish activity when back button is pressed

        new CourseAsyncTask(this, period).execute();
    }

    public void addCards() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new CourseViewAdapter(this, period);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
