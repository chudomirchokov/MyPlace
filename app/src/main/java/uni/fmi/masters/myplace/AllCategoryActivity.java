package uni.fmi.masters.myplace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.ArrayList;

import uni.fmi.masters.myplace.ui.adapter.AllCategoryAdapter;

public class AllCategoryActivity extends AppCompatActivity {

    ListView categoryLVInOne;
    AllCategoryAdapter categoryAdapter;
    ArrayList<Category> allCategoryInOne;
    ProgressDialog progressDialogCategory;
    FloatingActionButton addCategoryB;
    Dialog addCategoryDialog;

    public int categoryGetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category_list);

        allCategoryInOne = new ArrayList<>();
        categoryAdapter = new AllCategoryAdapter(this, R.layout.activity_all_category_view, allCategoryInOne);
        categoryLVInOne = findViewById(R.id.allCategoryViewInOne);
        addCategoryB = findViewById(R.id.floatingButtonCategoryListInOne);
        addCategoryB.setOnClickListener(onClick);
        categoryLVInOne.setAdapter(categoryAdapter);

        progressDialogCategory = new ProgressDialog(AllCategoryActivity.this);
        progressDialogCategory.show();



        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "http://192.168.0.104:8585/GetAllCategory";


                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    BufferedInputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                    String response = reader.readLine();

                    if (response != null){
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            Category category = new Category();
                            category.setID(jsonObject.getInt("Id"));
                            category.setName(jsonObject.getString("Name"));
                            categoryGetId = jsonObject.getInt("Id");

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AllCategoryActivity.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("Id", categoryGetId);
                            editor.commit();

                            allCategoryInOne.add(category);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryAdapter.notifyDataSetChanged();
                            progressDialogCategory.hide();
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
        }).start();

        categoryLVInOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(AllCategoryActivity.this, AllPlaceActivity.class);
                //intent.putExtra("Id", categoryGetId);
                startActivity(new Intent(AllCategoryActivity.this, AllPlaceActivity.class));
            }
        });
    }

    public void backFromCategory(View view){
        Intent intent = new Intent(AllCategoryActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            addCategoryDialog = new Dialog(AllCategoryActivity.this);
            addCategoryDialog.setContentView(R.layout.activity_add_category);
            addCategoryDialog.setCanceledOnTouchOutside(false);

            EditText addNameOfNewCategory = addCategoryDialog.findViewById(R.id.addNameOfNewCategory);

            Button addCategoryButton = addCategoryDialog.findViewById(R.id.addCategoryButton);
            Button cancelAddCategoryButton = addCategoryDialog.findViewById(R.id.cancelAddCategoryButton);

            cancelAddCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCategoryDialog.cancel();
                }
            });

            addCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String urlString = String.format("http://192.168.0.104:8585/AddCategory?id=%s&name=%s",
                                    0, addNameOfNewCategory.getText().toString());

                            HttpURLConnection connection = null;
                            try {
                                URL url = new URL(urlString);
                                connection = (HttpURLConnection) url.openConnection();

                                InputStream stream = new BufferedInputStream(connection.getInputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));


                                String response = reader.readLine();

                                if (response != null){

                                    int recievedID = Integer.parseInt(response);

                                    final Category category = new Category();

                                    category.setID(recievedID);
                                    category.setName(addNameOfNewCategory.getText().toString());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            allCategoryInOne.add(category);
                                            categoryAdapter.notifyDataSetChanged();

                                            addCategoryDialog.hide();
                                        }
                                    });
                                }

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NumberFormatException e){
                                e.printStackTrace();
                            } finally {
                                if (connection != null)
                                    connection.disconnect();
                            }
                        }
                    }).start();
                }
            });
            addCategoryDialog.show();

        }
    };
}