package ga.caseyavila.velcro.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import org.json.JSONException;
import java.io.IOException;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.Student;
import ga.caseyavila.velcro.fragments.HelpDialogFragment;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    private TextInputEditText subdomainText;
    private TextInputLayout subdomainLayout;
    private ImageButton helpButton;
    private TextInputEditText usernameText;
    private TextInputLayout usernameLayout;
    private TextInputEditText passwordText;
    private TextInputLayout passwordLayout;
    private MaterialButton loginButton;
    private ProgressBar progressBar;
    private MaterialTextView loginNotification;
    public static Student casey = new Student();

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        Typeface manrope = ResourcesCompat.getFont(this, R.font.manrope_medium);

        subdomainText = findViewById(R.id.subdomain_input);
        subdomainLayout = findViewById(R.id.subdomain_layout);
        subdomainLayout.setTypeface(manrope);

        helpButton = findViewById(R.id.icon_help);

        usernameText = findViewById(R.id.username_input);
        usernameLayout = findViewById(R.id.username_layout);
        usernameLayout.setTypeface(manrope);

        passwordText = findViewById(R.id.password_input);
        passwordLayout = findViewById(R.id.password_layout);
        passwordLayout.setTypeface(manrope);

        loginButton = findViewById(R.id.login_button);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        loginNotification = findViewById(R.id.login_notification);
        loginNotification.setVisibility(View.INVISIBLE);

        passwordText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO) {
                login(loginButton);
            }
            return false;
        });

        if (casey.isAutoLoginReady()) {
            autoLogin();
        }
    }

    public void login(View v) {
        // Enable progress bar and fade widgets
        progressBar.setVisibility(View.VISIBLE);
        subdomainLayout.setEnabled(false);
        helpButton.setEnabled(false);
        usernameLayout.setEnabled(false);
        passwordLayout.setEnabled(false);
        loginButton.setEnabled(false);

        new Thread(() -> {
            try {
                // If sharedPreferences already exists...
                if (casey.isAutoLoginReady()) {
                    // Set fields in User to match
                    casey.setSubdomain(sharedPreferences.getString("subdomain", "error"));
                    casey.setUsername(sharedPreferences.getString("username", "error"));
                    casey.setHashedPassword(sharedPreferences.getString("hashedPassword", "error"));
                    casey.setStudentId(sharedPreferences.getString("studentId", "error"));
                    casey.setCookie(sharedPreferences.getString("cookie", "error"));
                } else {
                    casey.setSubdomain(subdomainText.getText().toString());
                    casey.setUsername(usernameText.getText().toString());
                    casey.setPassword(passwordText.getText().toString());

                    // Find values and enter them into sharedPreferences
                    casey.findLoginData();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("subdomain", casey.getSubdomain());
                    editor.putString("username", casey.getUsername());
                    editor.putString("hashedPassword", casey.getHashedPassword());
                    editor.putString("studentId", casey.getStudentId());
                    editor.putString("cookie", casey.getCookie());
                    editor.apply();
                }

                casey.findReportCard();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                if (casey.isLoggedIn()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {  // Alert user if username and password doesn't match
                    loginNotification.setVisibility(View.VISIBLE);
                    loginButton.setEnabled(true);
                    subdomainLayout.setEnabled(true);
                    helpButton.setEnabled(true);
                    usernameLayout.setEnabled(true);
                    passwordLayout.setEnabled(true);
                }
                progressBar.setVisibility(View.INVISIBLE);
            });
        }).start();
    }

    private void autoLogin() {
        subdomainText.setText(sharedPreferences.getString("subdomain", ""));
        usernameText.setText(sharedPreferences.getString("username", ""));
        // Set fake text for password
        passwordText.setText("**********");

        login(loginButton);
    }

    public void showHelp(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new HelpDialogFragment();
        dialogFragment.show(fragmentManager, "help_dialog");
    }
}
