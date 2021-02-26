package uni.fmi.masters.myplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView welcome;
    TextView entryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome = findViewById(R.id.welcome);
        entryTV = findViewById(R.id.welcomeTextView);
        entryTV.setOnClickListener(onClick);
    }
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    };
}