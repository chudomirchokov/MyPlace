package uni.fmi.masters.myplace.ui.home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;

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

import uni.fmi.masters.myplace.Comment;
import uni.fmi.masters.myplace.R;

public class HomeFragment extends Fragment {

   // private HomeViewModel homeViewModel;
    ListView commentLV;
    HomeAdapter adapter;
    ArrayList<Comment> comments;
    ProgressDialog dialog;
    FloatingActionButton addCB;
    Dialog addDialog;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addDialog = new Dialog(getContext());
            addDialog.setContentView(R.layout.add_comment);
            addDialog.setCanceledOnTouchOutside(false);

            EditText addCTV = addDialog.findViewById(R.id.editTextTextMultiLine);
            EditText ratingTN = addDialog.findViewById(R.id.ratingTextNumber);
            Button addCB = addDialog.findViewById(R.id.addCommentButton);
            Button cancelCB = addDialog.findViewById(R.id.cancelAdd);

            cancelCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialog.cancel();
                }
            });

            addCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String urlString = String.format("http://192.168.0.104:8585/AddComment?comment=%s&rating=%s&id=%s",
                                    addCTV.getText(), ratingTN.getText().toString(), 0);

                            HttpURLConnection connection = null;
                            try {
                                 URL url = new URL(urlString);
                                 connection = (HttpURLConnection) url.openConnection();

                                InputStream stream = new BufferedInputStream(connection.getInputStream());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                                String response = reader.readLine();

                                if (response != null){

                                    int resievedID = Integer.parseInt(response);

                                    Comment comment = new Comment();
                                    comment.setID(resievedID);
                                    comment.setComment(addCTV.getText().toString());
                                    comment.setRating(Integer.parseInt(ratingTN.getText().toString()));

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            comments.add(comment);
                                            adapter.notifyDataSetChanged();

                                            addDialog.hide();
                                        }
                                    });
                                }

                            } catch (MalformedURLException e) {
                                    e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            } finally {
                                if (connection != null)
                                    connection.disconnect();
                            }
                        }
                    }).start();
                }
            });
            addDialog.show();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        comments = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        commentLV = root.findViewById(R.id.commentListView);
        adapter = new HomeAdapter(getContext(), R.layout.comment_list,comments);
        commentLV.setAdapter(adapter);
        addCB = root.findViewById(R.id.floatingActionButton);
        addCB.setOnClickListener(onClick);

        dialog = new ProgressDialog(getContext());

        //нова нишка за вземане на коментарите
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
                            //comment.setPlaceID(jsonObject.getInt("PlaceID"));

                            comments.add(comment);
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
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
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }

            }
        }).start();

        return root;
    }
}
