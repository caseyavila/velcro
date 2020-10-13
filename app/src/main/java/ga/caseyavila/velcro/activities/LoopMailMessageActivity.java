package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;

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
    private MaterialTextView subject;
    private MaterialTextView sender;
    private MaterialTextView recipient;
    private MaterialTextView date;
    private MaterialTextView body;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loopmail_message);

        subject = findViewById(R.id.loopmail_message_subject);
        sender = findViewById(R.id.loopmail_message_sender);
        recipient = findViewById(R.id.loopmail_message_recipient);
        date = findViewById(R.id.loopmail_message_date);
        body = findViewById(R.id.loopmail_body);

        Bundle bundle = this.getIntent().getExtras();
        folder = bundle.getInt("folder");
        index = bundle.getInt("index");

        new LoopMailMessageAsyncTask(this, folder, index).execute();
    }

    public void addCards() {
        subject.setText(casey.getMailBox(folder).getLoopmail(index).getSubject());
        sender.setText(getString(R.string.loopmail_message_sender, casey.getMailBox(folder).getLoopmail(index).getSender()));
        recipient.setText(getString(R.string.loopmail_message_recipient, casey.getMailBox(folder).getLoopmail(index).getRecipient()));
        date.setText(casey.getMailBox(folder).getLoopmail(index).getSendDate());

        body.setText(HtmlCompat.fromHtml(casey.getMailBox(folder).getLoopmail(index).getBody(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        body.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
