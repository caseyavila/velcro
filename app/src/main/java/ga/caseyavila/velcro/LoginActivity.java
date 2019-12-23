package ga.caseyavila.velcro;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView passwordText;
    private TextView loginButton;
    private TextView loginNotification;
    private ProgressBar progressBar;
    public static User casey = new User();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.username_input);
        passwordText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        loginNotification = findViewById(R.id.login_notification);
        progressBar = findViewById(R.id.progress_bar);
        loginNotification.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void login (View v) {
        casey.setUsername(usernameText.getText().toString());
        casey.setPassword(passwordText.getText().toString());

        progressBar.setVisibility(View.VISIBLE);

        new LoginService(this).execute();
    }
}
