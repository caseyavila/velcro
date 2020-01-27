package ga.caseyavila.velcro;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorRefresh));

        refreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new RefreshService((Activity) findViewById(R.id.linear_layout).getContext()).execute();
                }
            }
        );
        addCards();
    }

    public void addCards() {

        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.setMargins(20, 20, 20, 0);

        for (int i = 0; i < User.numberOfPeriods; i++) {

            MaterialCardView cardView = new MaterialCardView(this);
            cardView.setCardElevation(4f);
            cardView.setLayoutParams(cardViewLayoutParams);

            ConstraintLayout constraintLayout = new ConstraintLayout(cardView.getContext());

            ConstraintLayout.LayoutParams periodLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            periodLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            periodLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            ConstraintLayout.LayoutParams teacherLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            teacherLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

            ConstraintLayout.LayoutParams gradesLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            gradesLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            gradesLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            gradesLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

            ConstraintLayout.LayoutParams classLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            classLayoutParams.width = 350;
            classLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            classLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            ConstraintLayout.LayoutParams percentageLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            percentageLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            percentageLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            percentageLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            percentageLayoutParams.horizontalBias = 0f;

            TextView period = new TextView(this);
            period.setPadding(15,10,0,0);
            period.setText(String.valueOf(i + 1));
            period.setLayoutParams(periodLayoutParams);

            TextView teachers = new TextView(this);
            teachers.setPadding(20, 20, 40, 30);
            teachers.setText(User.teacherMap.get(i).toUpperCase());
            teachers.setTextSize(10);
            teachers.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            teachers.setId(View.generateViewId());
            teachers.setLayoutParams(teacherLayoutParams);

            TextView grades = new TextView(this);
            grades.setWidth(250);
            grades.setPadding(60, 15, 20, 20);
            grades.setText(User.gradeMap.get(i));
            grades.setTextSize(34);
            grades.setLayoutParams(gradesLayoutParams);
            grades.setId(View.generateViewId());

            TextView classes = new TextView(this);
            classes.setPadding(20, 20, 40, 20);
            classes.setGravity(Gravity.END);
            classes.setText(User.classMap.get(i));
            classes.setId(View.generateViewId());
            classes.setLayoutParams(classLayoutParams);

            TextView percentages = new TextView(this);
            percentages.setPadding(20, 20, 20, 20);
            percentages.setText(User.percentageMap.get(i));
            percentages.setTextSize(16);
            percentages.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            percentages.setLayoutParams(percentageLayoutParams);

            classLayoutParams.bottomToTop = teachers.getId();  // Apply constraints to LayoutParams after TextViews are created
            teacherLayoutParams.topToBottom = classes.getId();
            percentageLayoutParams.startToEnd = grades.getId();

            constraintLayout.addView(period);
            constraintLayout.addView(teachers);
            constraintLayout.addView(grades);
            constraintLayout.addView(classes);
            constraintLayout.addView(percentages);
            cardView.addView(constraintLayout);
            linearLayout.addView(cardView);
        }
    }
}
