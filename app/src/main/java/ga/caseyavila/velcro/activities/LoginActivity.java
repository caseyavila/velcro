package ga.caseyavila.velcro.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.asynctasks.LoginAsyncTask;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.User;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    private TextInputEditText usernameText;
    private TextInputLayout usernameLayout;
    private TextInputEditText passwordText;
    private TextInputLayout passwordLayout;
    private TextInputEditText subdomainText;
    private TextInputLayout subdomainLayout;
    private MaterialButton loginButton;
    private MaterialTextView loginNotification;
    private ProgressBar progressBar;
    public static User casey = new User();

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        Typeface manrope = ResourcesCompat.getFont(this, R.font.manrope_medium);

        subdomainText = findViewById(R.id.subdomain_input);
        subdomainLayout = findViewById(R.id.subdomain_layout);
        subdomainLayout.setTypeface(manrope);

        usernameText = findViewById(R.id.username_input);
        usernameLayout = findViewById(R.id.username_layout);
        usernameLayout.setTypeface(manrope);

        passwordText = findViewById(R.id.password_input);
        passwordLayout = findViewById(R.id.password_layout);
        passwordLayout.setTypeface(manrope);

        loginButton = findViewById(R.id.login_button);
        loginNotification = findViewById(R.id.login_notification);
        loginNotification.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        passwordText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO) {
                login(loginButton);
            }
            return false;
        });

        autoLogin();
    }

    public void login(View v) {
        if (!casey.isAutoLoginReady()) {
            casey.setSubdomain(subdomainText.getText().toString());
            casey.setUsername(usernameText.getText().toString());
            casey.setPassword(passwordText.getText().toString());
        }

        progressBar.setVisibility(View.VISIBLE);
        subdomainLayout.setEnabled(false);
        usernameLayout.setEnabled(false);
        passwordLayout.setEnabled(false);
        loginButton.setEnabled(false);

        new LoginAsyncTask(this).execute();
    }

    private void autoLogin() {
        if (casey.isAutoLoginReady()) {
            subdomainText.setText(sharedPreferences.getString("subdomain", ""));
            usernameText.setText(sharedPreferences.getString("username", ""));
            passwordText.setText("**********");  //Set fake text for password

            login(loginButton);
        }
    }

}
