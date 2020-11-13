package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import org.json.JSONException;
import java.io.IOException;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.LoopMailAttachmentAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class LoopMailMessageActivity extends AppCompatActivity {

    private int mailBox;
    private int index;
    private MaterialTextView subject;
    private MaterialTextView sender;
    private MaterialTextView recipient;
    private MaterialTextView date;
    private MaterialTextView body;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loopmail_message);

        subject = findViewById(R.id.loopmail_message_subject);
        sender = findViewById(R.id.loopmail_message_sender);
        recipient = findViewById(R.id.loopmail_message_recipient);
        date = findViewById(R.id.loopmail_message_date);
        body = findViewById(R.id.loopmail_body);
        progressBar = findViewById(R.id.loopmail_message_progress_bar);
        recyclerView = findViewById(R.id.loopmail_attachment_recycler_view);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;

        mailBox = bundle.getInt("mail_box");
        index = bundle.getInt("index");

        loadLoopMailMessage();
    }

    private void showMessage() {
        subject.setText(casey.getMailBox(mailBox).getLoopmail(index).getSubject());
        sender.setText(getString(R.string.loopmail_message_sender, casey.getMailBox(mailBox).getLoopmail(index).getSender()));
        recipient.setText(getString(R.string.loopmail_message_recipient, casey.getMailBox(mailBox).getLoopmail(index).getRecipient()));
        date.setText(casey.getMailBox(mailBox).getLoopmail(index).getSendDate());
        body.setText(HtmlCompat.fromHtml(casey.getMailBox(mailBox).getLoopmail(index).getBody(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        body.setMovementMethod(LinkMovementMethod.getInstance());

        if (casey.getMailBox(mailBox).getLoopmail(index).hasAttachments()) {
            LoopMailAttachmentAdapter adapter = new LoopMailAttachmentAdapter(mailBox, index);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
        }
    }

    private void loadLoopMailMessage() {
        new Thread(() -> {
            try {
                casey.findLoopMailBody(mailBox, index);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                progressBar.setEnabled(true);
                showMessage();
                progressBar.setVisibility(View.INVISIBLE);
            });
        }).start();
    }
}
