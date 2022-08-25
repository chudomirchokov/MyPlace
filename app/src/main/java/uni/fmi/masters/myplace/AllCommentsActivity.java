package uni.fmi.masters.myplace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

import uni.fmi.masters.myplace.ui.adapter.AllCommentAdapter;

public class AllCommentsActivity extends AppCompatActivity {

    ListView commentsLVInOne;
    AllCommentAdapter commentA;
    ArrayList<Comment> allCommentsInOne;
    ProgressDialog progressDialog;
    FloatingActionButton addCommentB;
    Dialog addDialogCL;


    public int placeIdNewId;
    public int userNewIdValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments_list);

        ////// vzemane na Id na usera i na mqstoto
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Integer loggedUserID = sharedPref.getInt("userID", 0);
        userNewIdValue = loggedUserID;
        Integer placeGetIdFromDb = sharedPref.getInt("Id", 0);
        placeIdNewId = placeGetIdFromDb;

        allCommentsInOne = new ArrayList<>();

        commentA = new AllCommentAdapter(this, R.layout.activity_all_comments_view, allCommentsInOne);
        commentsLVInOne = findViewById(R.id.allCommentListViewInOne);
        addCommentB = findViewById(R.id.floatingButtonCommentListInOne);
        addCommentB.setOnClickListener(onClick);
        commentsLVInOne.setAdapter(commentA);

        progressDialog = new ProgressDialog(AllCommentsActivity.this);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "http://192.168.0.104:8585/GetAllComments";

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

                            Comment comment = new Comment();
                            comment.setComment(jsonObject.getString("Comment"));
                            comment.setID(jsonObject.getInt("ID"));
                            comment.setRating(jsonObject.getInt("Rating"));
                            comment.setUserID(jsonObject.getInt("UserID"));
                            comment.setPlaceID(jsonObject.getInt("PlaceID"));

                            allCommentsInOne.add(comment);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentA.notifyDataSetChanged();
                            progressDialog.hide();
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
    }

    public void backFromComment(View view){
        Intent intent = new Intent(AllCommentsActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addDialogCL = new Dialog(AllCommentsActivity.this);
            addDialogCL.setContentView(R.layout.add_comment);
            addDialogCL.setCanceledOnTouchOutside(false);

            EditText addCTV = addDialogCL.findViewById(R.id.nameNewCategory);
            EditText ratingTN = addDialogCL.findViewById(R.id.ratingTextNumber);


            Button addCommentB = addDialogCL.findViewById(R.id.addCommentButton);
            Button cancelCB = addDialogCL.findViewById(R.id.cancelAdd);

            cancelCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialogCL.cancel();
                }
            });

            addCommentB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String urlString = String.format("http://192.168.0.104:8585/AddComment?comment=%s&rating=%s&userID=%s&id=%s&placeID=%s",
                                    addCTV.getText(), ratingTN.getText().toString(), userNewIdValue, 0, placeIdNewId);

                            HttpURLConnection connection = null;
                            try {
                                URL url = new URL(urlString);
                                connection = (HttpURLConnection) url.openConnection();

                                InputStream stream = new BufferedInputStream(connection.getInputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                                String response = reader.readLine();

                                if (response != null){

                                    int recievedID = Integer.parseInt(response);

                                    final Comment comment = new Comment();

                                    comment.setID(recievedID);
                                    comment.setComment(addCTV.getText().toString());
                                    comment.setRating(Integer.parseInt(ratingTN.getText().toString()));
                                    comment.setPlaceID(placeIdNewId);
                                    comment.setUserID(userNewIdValue);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            allCommentsInOne.add(comment);
                                            commentA.notifyDataSetChanged();

                                            addDialogCL.hide();
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
            addDialogCL.show();
        }
    };
}