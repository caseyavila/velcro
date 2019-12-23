package ga.caseyavila.velcro;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.IOException;

import static ga.caseyavila.velcro.LoginActivity.casey;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            addCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCards() throws IOException {

        int numberOfCards = casey.getNumberOfPeriods();

        linearLayout = findViewById(R.id.linear_layout);

        for (int i = 0; i < numberOfCards; i++) {

            CardView cardView = new CardView(this);

            LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            cardViewLayoutParams.setMargins(10, 10, 10, 0);

            cardView.setLayoutParams(cardViewLayoutParams);

            TextView textView = new TextView(this);
            textView.setText(casey.getTeachers(i));

            cardView.addView(textView);

            linearLayout.addView(cardView);
        }
    }
}
