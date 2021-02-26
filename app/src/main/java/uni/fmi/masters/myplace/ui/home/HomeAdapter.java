package uni.fmi.masters.myplace.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uni.fmi.masters.myplace.Comment;
import uni.fmi.masters.myplace.MenuActivity;
import uni.fmi.masters.myplace.R;

public class HomeAdapter extends ArrayAdapter<Comment> {
    public HomeAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((MenuActivity)getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.comment_list, parent, false);
        }

        TextView ratingV = convertView.findViewById(R.id.ratingView);
        TextView commentV = convertView.findViewById(R.id.commentView);
        //TextView nameuserV = convertView.findViewById(R.id.nameuserView);
        //TextView nameplaceV = convertView.findViewById(R.id.nameplaceView);


        ratingV.setText(String.valueOf(getItem(position).getRating()));
        commentV.setText(getItem(position).getComment());
        //nameuserV.setText(String.valueOf(getItem(position).getUserID()));
        //nameplaceV.setText(String.valueOf(getItem(position).getPlaceID()));

        return convertView;
    }
}
