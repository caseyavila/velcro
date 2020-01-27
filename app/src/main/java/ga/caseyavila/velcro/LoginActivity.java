package ga.caseyavila.velcro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


public class LoginActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView passwordText;
    private TextView loginButton;
    private TextView loginNotification;
    private ProgressBar progressBar;
    public static User casey = new User();
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);

        usernameText = findViewById(R.id.username_input);
        passwordText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        loginNotification = findViewById(R.id.login_notification);
        progressBar = findViewById(R.id.progress_bar);
        loginNotification.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    login(loginButton);
                }
                return false;
            }
        });
        if(sharedPreferences.contains("username")) {
            autoLogin();
        }
    }

    public void login(View v) {
        casey.setUsername(usernameText.getText().toString());
        casey.setPassword(passwordText.getText().toString());

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        new LoginService(this).execute();
    }

    public void autoLogin() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        usernameText.setText(sharedPreferences.getString("username", "error"));
        casey.setUsername(sharedPreferences.getString("username", "error"));

        passwordText.setText(sharedPreferences.getString("password", "error"));
        casey.setPassword(sharedPreferences.getString("password", "error"));
        new LoginService(this).execute();
    }
}
