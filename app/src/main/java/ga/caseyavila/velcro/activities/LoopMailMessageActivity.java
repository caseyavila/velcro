package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.asynctasks.LoopMailMessageAsyncTask;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailMessageActivity extends AppCompatActivity {

    private int folder;
    private int index;
    private ProgressBar loopMailMessageProgressBar;
    private MaterialTextView subjectTextView;
    private MaterialTextView senderTextView;
    private MaterialTextView recipientTextView;
    private MaterialTextView dateTextView;
    private MaterialTextView bodyTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loopmail_message);

        subjectTextView = findViewById(R.id.loopmail_message_subject);
        senderTextView = findViewById(R.id.loopmail_message_sender);
        recipientTextView = findViewById(R.id.loopmail_message_recipient);
        dateTextView = findViewById(R.id.loopmail_message_date);
        bodyTextView = findViewById(R.id.loopmail_body);

        Bundle bundle = this.getIntent().getExtras();
        folder = bundle.getInt("folder");
        index = bundle.getInt("index");

        new LoopMailMessageAsyncTask(this, folder, index).execute();
    }

    public void addCards() {
        subjectTextView.setText(casey.getMailBox(folder).getLoopmail(index).getSubject());
        senderTextView.setText(getString(R.string.loopmail_message_sender, casey.getMailBox(folder).getLoopmail(index).getSender()));
        recipientTextView.setText(getString(R.string.loopmail_message_recipient, casey.getMailBox(folder).getLoopmail(index).getRecipient()));
        dateTextView.setText(casey.getMailBox(folder).getLoopmail(index).getSendDate());

        bodyTextView.setText(HtmlCompat.fromHtml(casey.getMailBox(folder).getLoopmail(index).getBody(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
