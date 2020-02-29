package ga.caseyavila.velcro;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.appbar.AppBarLayout;

import static ga.caseyavila.velcro.LoginActivity.casey;

public class PeriodActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private MaterialToolbar appBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        int period = bundle.getInt("period");

        appBarLayout = findViewById(R.id.appbar_layout);
        appBar = findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle(casey.getCourseName(bundle.getInt("period")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add back button
        appBar.setNavigationOnClickListener(view -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AssignmentViewAdapter adapter = new AssignmentViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
