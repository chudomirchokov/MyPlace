package uni.fmi.masters.myplace.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uni.fmi.masters.myplace.Place;
import uni.fmi.masters.myplace.R;

public class AllPlaceAdapter extends ArrayAdapter<Place> {
    public AllPlaceAdapter(@NonNull Context context, int resource, @NonNull List<Place> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_all_place_view, parent, false);
        }

        TextView allPlaceName = convertView.findViewById(R.id.nameOfNewPlaceView);
        TextView allPlaceAddress = convertView.findViewById(R.id.addressOfNewPlaceView);
        TextView allPlacePhone = convertView.findViewById(R.id.phoneOfNewPlaceView);

        allPlaceName.setText("Place: " + getItem(position).getName());
        allPlaceAddress.setText("Address: " + getItem(position).getAddress());
        allPlacePhone.setText("Phone: " + getItem(position).getPhone());

        return convertView;
    }
}
