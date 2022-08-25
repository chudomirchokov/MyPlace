package uni.fmi.masters.myplace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import uni.fmi.masters.myplace.ui.adapter.AllPlaceAdapter;

public class AllPlaceActivity extends AppCompatActivity {

    ListView placeLVInOne;
    AllPlaceAdapter placeAdapter;
    ArrayList<Place> allPlaceInOne;
    ProgressDialog progressDialogPlace;
    FloatingActionButton addPlaceB;
    Dialog addPlaceDialog;

    public int categoryIdNewId;
    public int placeGetIdFromDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_place_list);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Integer categoryGetId = sharedPref.getInt("Id", 0);
        categoryIdNewId = categoryGetId;

        allPlaceInOne = new ArrayList<>();
        placeAdapter = new AllPlaceAdapter(this, R.layout.activity_all_place_view, allPlaceInOne);
        placeLVInOne = findViewById(R.id.allPlaceListViewInOne);
        addPlaceB = findViewById(R.id.floatingButtonPlaceListInOne);
        addPlaceB.setOnClickListener(onClick);
        placeLVInOne.setAdapter(placeAdapter);

        progressDialogPlace = new ProgressDialog(AllPlaceActivity.this);
        progressDialogPlace.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "http://192.168.0.104:8585/GetAllPlace";

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

                            Place place = new Place();
                            place.setName(jsonObject.getString("Name"));
                            place.setID(jsonObject.getInt("Id"));
                            place.setAddress(jsonObject.getString("Address"));
                            place.setPhone(jsonObject.getString("Phone"));
                            placeGetIdFromDb = jsonObject.getInt("Id");

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AllPlaceActivity.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("Id", placeGetIdFromDb);

                            editor.commit();

                            allPlaceInOne.add(place);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            placeAdapter.notifyDataSetChanged();
                            progressDialogPlace.hide();
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

        placeLVInOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AllPlaceActivity.this, AllCommentsActivity.class));
            }
        });
    }

    public void backFromPlace(View view){
        Intent intent = new Intent(AllPlaceActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addPlaceDialog = new Dialog(AllPlaceActivity.this);
            addPlaceDialog.setContentView(R.layout.activity_add_place);
            addPlaceDialog.setCanceledOnTouchOutside(false);

            ImageView addImageNewPlace = addPlaceDialog.findViewById(R.id.imageViewForAddPlace);
            TextView addNameOfNewPlace = addPlaceDialog.findViewById(R.id.addNameOfNewPlace);
            TextView addAddressOfNewPlace = addPlaceDialog.findViewById(R.id.addAddresOfNewPlace);
            TextView addPhoneOfNewPlace = addPlaceDialog.findViewById(R.id.addPhoneOfNewPlace);

            Button addPlaceButton = addPlaceDialog.findViewById(R.id.addPlaceButton);
            Button cancelAddPlaceB = addPlaceDialog.findViewById(R.id.cancelAddPlaceButton);

            cancelAddPlaceB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPlaceDialog.cancel();
                }
            });

            addPlaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String urlString = String.format("http://192.168.0.104:8585/AddPlace?id=%s&name=%s&address=%s&phone=%s&categoryID=%s",//&imagePath=%s",//&categoryID=%s",
                                    0, addNameOfNewPlace.getText(), addAddressOfNewPlace.getText(), addPhoneOfNewPlace.getText(), categoryIdNewId);//, addImageNewPlase.getResources());

                            HttpURLConnection connection = null;
                            try {
                                URL url = new URL(urlString);
                                connection = (HttpURLConnection) url.openConnection();

                                InputStream stream = new BufferedInputStream(connection.getInputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));


                                String response = reader.readLine();

                                if (response != null){

                                    int receivedID = Integer.parseInt(response);

                                    Place place = new Place();

                                    place.setID(receivedID);
                                    place.setName(addNameOfNewPlace.getText().toString());
                                    place.setAddress(addAddressOfNewPlace.getText().toString());
                                    place.setPhone(addPhoneOfNewPlace.getText().toString());
                                    place.setCategoryID(categoryIdNewId);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            allPlaceInOne.add(place);
                                            placeAdapter.notifyDataSetChanged();

                                            addPlaceDialog.hide();
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
            addPlaceDialog.show();
        }
    };
}