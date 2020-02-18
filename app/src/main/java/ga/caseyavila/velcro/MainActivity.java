package ga.caseyavila.velcro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import static ga.caseyavila.velcro.LoginActivity.casey;


public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    static SharedPreferences sharedPreferences;
    private SwipeRefreshLayout refreshLayout;

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorRefresh));
        refreshLayout.setOnRefreshListener(
                () -> new RefreshService((Activity) findViewById(R.id.linear_layout).getContext()).execute()
        );
        refreshLayout.setEnabled(false);

        progressBar = findViewById(R.id.progress_bar);

        if (User.isLoggedIn) {
            addCards();
        } else if (sharedPreferences.contains("username")) {
            progressBar.setVisibility(View.VISIBLE);
            casey.setUsername(sharedPreferences.getString("username", "error"));
            casey.setPassword(sharedPreferences.getString("password", "error"));
            new AutoLoginService(this).execute();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void addCards() {

        refreshLayout.setEnabled(true);
        Typeface manrope = Typeface.createFromAsset(getAssets(), "fonts/Manrope-Medium.ttf");

        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.setMargins(20, 20, 20, 0);

        for (int i = 0; i < User.numberOfPeriods; i++) {

            MaterialCardView cardView = new MaterialCardView(this);
            cardView.setStrokeWidth(2);
            cardView.setStrokeColor(ContextCompat.getColor(this, R.color.colorCardBorder));
            cardView.setCardElevation(1f);
            cardView.setLayoutParams(cardViewLayoutParams);
            cardView.setId(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, PeriodActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("period", view.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

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

            MaterialTextView period = new MaterialTextView(this);
            period.setPadding(15,10,0,0);
            period.setText(String.valueOf(i + 1));
            period.setTextColor(ContextCompat.getColor(this, R.color.textMediumEmphasis));
            period.setLayoutParams(periodLayoutParams);

            MaterialTextView teachers = new MaterialTextView(this);
            teachers.setPadding(20, 20, 40, 30);
            teachers.setText(User.teacherMap.get(i).toUpperCase());
            teachers.setTextSize(10);
            teachers.setTextColor(ContextCompat.getColor(this, R.color.textMediumEmphasis));
            teachers.setTypeface(Typeface.create("sans-serif-regular", Typeface.NORMAL));
            teachers.setId(View.generateViewId());
            teachers.setLayoutParams(teacherLayoutParams);

            MaterialTextView grades = new MaterialTextView(this);
            grades.setWidth(250);
            grades.setPadding(60, 15, 20, 20);
            grades.setText(User.gradeMap.get(i));
            grades.setTypeface(manrope);
            grades.setTextSize(34);
            grades.setLayoutParams(gradesLayoutParams);
            grades.setId(View.generateViewId());

            MaterialTextView classes = new MaterialTextView(this);
            classes.setPadding(20, 20, 40, 20);
            classes.setGravity(Gravity.END);
            classes.setText(User.classMap.get(i));
            classes.setId(View.generateViewId());
            classes.setLayoutParams(classLayoutParams);

            MaterialTextView percentages = new MaterialTextView(this);
            percentages.setPadding(20, 20, 20, 20);
            percentages.setText(User.percentageMap.get(i));
            percentages.setTextSize(18);
            percentages.setTypeface(manrope);
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

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.show(fragmentManager, "logout_dialog");
    }
}
