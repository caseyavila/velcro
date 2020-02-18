package ga.caseyavila.velcro;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textview.MaterialTextView;

import static ga.caseyavila.velcro.User.linkMap;

public class PeriodActivity extends AppCompatActivity {

    private MaterialTextView testView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        int period = bundle.getInt("period");

        testView = findViewById(R.id.test_view);
        testView.setText(linkMap.get(period));
    }
}
