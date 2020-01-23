package ga.caseyavila.velcro;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;

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

        int numberOfCards = User.numberOfPeriods;

        linearLayout = findViewById(R.id.linear_layout);

        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.setMargins(20, 20, 20, 0);

        for (int i = 0; i < numberOfCards; i++) {

            CardView cardView = new CardView(this);
            cardView.setLayoutParams(cardViewLayoutParams);

            ConstraintLayout constraintLayout = new ConstraintLayout(cardView.getContext());

            ConstraintLayout.LayoutParams teacherLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            teacherLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

            ConstraintLayout.LayoutParams gradesLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            gradesLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;

            TextView teachers = new TextView(this);
            teachers.setPadding(20, 20, 20, 20);
            TextView grades = new TextView(this);
            grades.setPadding(20, 20, 20, 20);
            grades.setLayoutParams(gradesLayoutParams);
            grades.setText(User.gradeMap.get(i));
            teachers.setLayoutParams(teacherLayoutParams);
            teachers.setText(User.teacherMap.get(i));

            constraintLayout.addView(teachers);
            constraintLayout.addView(grades);
            cardView.addView(constraintLayout);
            linearLayout.addView(cardView);
        }
    }
}
