package ga.caseyavila.velcro;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.appbar.AppBarLayout;

import static ga.caseyavila.velcro.User.linkMap;

public class PeriodActivity extends AppCompatActivity {

    private MaterialTextView testView;
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
        testView = findViewById(R.id.test_view);
        testView.setText(linkMap.get(period));
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle(User.classMap.get(bundle.getInt("period")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Add back button
        getSupportActionBar().setElevation(4);
        appBar.setNavigationOnClickListener(view -> finish());
    }
}
