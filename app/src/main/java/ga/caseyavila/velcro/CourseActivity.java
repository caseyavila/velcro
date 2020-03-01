package ga.caseyavila.velcro;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.appbar.AppBarLayout;

import static ga.caseyavila.velcro.LoginActivity.casey;


public class CourseActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private MaterialToolbar appBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        int period = bundle.getInt("period");

        appBarLayout = findViewById(R.id.appbar_layout);
        appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle(casey.getCourseName(bundle.getInt("period")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add back button
        appBar.setNavigationOnClickListener(view -> finish());  // Finish activity when back button is pressed

        new CourseService(this, period).execute();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        CourseViewAdapter adapter = new CourseViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
