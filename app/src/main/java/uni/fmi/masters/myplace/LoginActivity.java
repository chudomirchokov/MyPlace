package uni.fmi.masters.myplace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uni.fmi.masters.myplace.ui.home.HomeFragment;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;
    Button loginB;
    Button registerB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        loginB = findViewById(R.id.loginButton);
        registerB = findViewById(R.id.registerButton);

        loginB.setOnClickListener(onClick);
        registerB.setOnClickListener(onClick);

    }

    //Създаване на следене за клик на бутоните
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.loginButton){

                String usernameInput = usernameET.getText().toString();
                String passwordInput = passwordET.getText().toString();

                String username;
                String password;

                ProgressDialog dialog;
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Login in.....");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlString = String.format("http://192.168.0.104:8585/Login?username=%s&password=%s",
                                usernameInput, passwordInput);

                        int loggedUserID = -1;
                        String nameLoggedUser = "";
                        HttpURLConnection urlConnection = null;
                        try {
                            URL url = new URL(urlString);
                            urlConnection = (HttpURLConnection) url.openConnection();

                            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                            String line = reader.readLine();

                            if(line != null && !line.equals("")){
                                JSONObject object = new JSONObject(line);
                                loggedUserID = object.getInt("ID");
                                nameLoggedUser = object.getString("Username");

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.hide();
                                }
                            });

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if(urlConnection != null)
                                urlConnection.disconnect();
                        }

                        if(loggedUserID != -1){
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            //intent.putExtra("userID", loggedUserID);

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("userID", loggedUserID);
                            editor.putString("nameUser", nameLoggedUser);

                            editor.commit();


                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this, "Wrong username or password!", Toast.LENGTH_LONG).show();
                        }
                    }

                }).start();

            }else{
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }
    };
}