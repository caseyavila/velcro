package ga.caseyavila.velcro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;

import com.google.android.material.textview.MaterialTextView;

import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.UpcomingAttachmentAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class UpcomingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;

        int index = bundle.getInt("index");

        MaterialTextView title = findViewById(R.id.upcoming_activity_title);
        MaterialTextView dueDate = findViewById(R.id.upcoming_activity_due_date);
        MaterialTextView info = findViewById(R.id.upcoming_activity_info);
        MaterialTextView description = findViewById(R.id.upcoming_activity_description);

        title.setText(casey.getUpcoming(index).getTitle());
        dueDate.setText(getString(R.string.due, casey.getUpcoming(index).getDueDate()));
        info.setText(getString(R.string.info, casey.getUpcoming(index).getCourse(), casey.getUpcoming(index).getPoints()));
        description.setText(HtmlCompat.fromHtml(casey.getUpcoming(index).getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        if (casey.getUpcoming(index).hasAttachments()) {
            RecyclerView recyclerView = findViewById(R.id.upcoming_attachment_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            UpcomingAttachmentAdapter adapter = new UpcomingAttachmentAdapter(index);
            recyclerView.setAdapter(adapter);
        }
    }
}