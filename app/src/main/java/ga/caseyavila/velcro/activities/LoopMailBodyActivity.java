package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.asynctasks.LoopMailBodyAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailBodyActivity extends AppCompatActivity {

    private int folder;
    private int index;
    private ProgressBar loopMailBodyProgressBar;
    private MaterialTextView bodyTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loopmail_body);

        bodyTextView = findViewById(R.id.loopmail_body);

        Bundle bundle = this.getIntent().getExtras();
        folder = bundle.getInt("folder");
        index = bundle.getInt("index");

        new LoopMailBodyAsyncTask(this, folder, index).execute();
    }

    public void addCards() {
        bodyTextView.setText(HtmlCompat.fromHtml(casey.getMailBox(folder).getLoopmail(index).getBody(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        Linkify.addLinks(bodyTextView, Linkify.WEB_URLS);
    }
}
