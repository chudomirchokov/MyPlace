package uni.fmi.masters.myplace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class
RegisterActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;
    EditText repeatPasswordET;
    EditText firstNameET;
    EditText lastNameET;
    Button registerB;
    Button cancelB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        repeatPasswordET = findViewById(R.id.repeatPasswordEditText);
        firstNameET = findViewById(R.id.firstNameEditText);
        lastNameET = findViewById(R.id.lastNameEditText);
        registerB = findViewById(R.id.registerButton);
        cancelB = findViewById(R.id.cancelButton);

        registerB.setOnClickListener(onClick);
        cancelB.setOnClickListener(onClick);


    }
    // OnClick на бутоните
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.registerButton){
                if(usernameET.getText().length() > 0
                        && passwordET.getText().length() > 0
                        && passwordET.getText().toString().equals(repeatPasswordET.getText().toString())){


                    String username = usernameET.getText().toString();
                    String password = passwordET.getText().toString();
                    String firstName = firstNameET.getText().toString();
                    String lastName = lastNameET.getText().toString();

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);

                    new RegisterAT(user).execute();


                }else{
                    Toast.makeText(RegisterActivity.this,
                            "Please insert all information!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };
    // Асинхронна задача
    private  class RegisterAT extends AsyncTask<Void, Void, Void>{

        User user;
        boolean isSuccess = false;

        ProgressDialog dialog;
        RegisterAT(User user){
            this.user = user;
            dialog = new ProgressDialog(RegisterActivity.this);
        }

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Registration in progress....");
            dialog.show();
        }

        //Създаване на конекцията
        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = String.format("http://192.168.0.104:8585/RegisterUser?" +
                            "username=%s&password=%s&fname=%s&lname=%s", user.getUsername(),
                    user.getPassword(), user.getFirstName(), user.getLastName());

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String result = reader.readLine();

                if(result != null){
                    if(result.contains("true")){
                        isSuccess = true;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.hide();
            if (isSuccess){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }
}