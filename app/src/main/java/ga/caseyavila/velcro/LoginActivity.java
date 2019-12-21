package ga.caseyavila.velcro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView passwordText;
    private TextView loginButton;
    private ProgressBar progressBar;
    public static User casey = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.username_input);
        passwordText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);

    }

    public void login(View v) throws InterruptedException {

        casey.setUsername(usernameText.getText().toString());
        casey.setPassword(passwordText.getText().toString());

        new Login(this).execute();

    }
}
