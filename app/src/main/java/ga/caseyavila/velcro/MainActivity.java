package ga.caseyavila.velcro;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.concurrent.*;

import static ga.caseyavila.velcro.LoginActivity.casey;

public class MainActivity extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private EditText input1;
    private EditText input2;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        input1 = (EditText) findViewById(R.id.input1);
        input2 = (EditText) findViewById(R.id.input2);
        button1 = (Button) findViewById(R.id.button1);
        try {
            find_website();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void find_website() throws IOException {
        text1.setText(casey.getTeachers());
        text2.setText(casey.getGrades());
        text3.setText(casey.getClasses());
    }
}
