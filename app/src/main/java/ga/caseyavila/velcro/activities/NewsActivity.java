package ga.caseyavila.velcro.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Bundle bundle = this.getIntent().getExtras();
        int index = bundle.getInt("index");

        MaterialTextView title = findViewById(R.id.news_activity_title);
        MaterialTextView author = findViewById(R.id.news_activity_author);
        MaterialTextView description = findViewById(R.id.news_activity_description);

        title.setText(casey.getNews(index).getTitle());
        author.setText(casey.getNews(index).getAuthor());
        description.setText(HtmlCompat.fromHtml(casey.getNews(index).getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        description.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
