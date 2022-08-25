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

import uni.fmi.masters.myplace.Category;
import uni.fmi.masters.myplace.R;

public class AllCategoryAdapter extends ArrayAdapter<Category> {
    public AllCategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_all_category_view, parent, false);
        }

        TextView allCategoryName = convertView.findViewById(R.id.allCategoryNameView);

        allCategoryName.setText("Ctegoty : "+ getItem(position).getName());

        return convertView;
    }
}
