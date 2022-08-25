package uni.fmi.masters.myplace.ui.adapter;

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
import uni.fmi.masters.myplace.R;

public class AllCommentAdapter extends ArrayAdapter<Comment> {
    public AllCommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_all_comments_view, parent, false);
        }

        TextView nameUserCALV = convertView.findViewById(R.id.nameUserCommentAllListView);
        TextView namePlaceCALV = convertView.findViewById(R.id.namePlaceCommentAllListView);
        TextView ratingCommentALV = convertView.findViewById(R.id.ratingCommentAllListView);
        TextView allCommentLVT = convertView.findViewById(R.id.allCommentListViewText);


        nameUserCALV.setText("User: " + String.valueOf(getItem(position).getUserID()));
        namePlaceCALV.setText("Place: " + String.valueOf(getItem(position).getPlaceID()));
        ratingCommentALV.setText("Rating: " + String.valueOf(getItem(position).getRating()));
        allCommentLVT.setText("COMMENT:   " + getItem(position).getComment());

        return convertView;
    }
}
