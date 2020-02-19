package ga.caseyavila.velcro;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import static ga.caseyavila.velcro.MainActivity.sharedPreferences;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameText;
    private TextInputEditText passwordText;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private MaterialButton loginButton;
    private MaterialTextView loginNotification;
    private ProgressBar progressBar;
    public static User casey = new User();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface manrope = Typeface.createFromAsset(getAssets(), "fonts/manrope_medium.ttf");

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

        usernameText.setOnFocusChangeListener((v, hasFocus) -> {  // Hide keyboard when user clicks off TextView
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        passwordText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    login(loginButton);
                }
                return false;
            }
        });

        usernameText.setText(sharedPreferences.getString("username", ""));
        passwordText.setText(sharedPreferences.getString("password", ""));
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void login(View v) {
        casey.setUsername(usernameText.getText().toString());
        casey.setPassword(passwordText.getText().toString());
        User.timeSinceLogin = null;

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        new LoginService(this).execute();
    }
}
