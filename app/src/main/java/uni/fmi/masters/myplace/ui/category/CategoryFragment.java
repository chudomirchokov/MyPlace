package uni.fmi.masters.myplace.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import uni.fmi.masters.myplace.AllCategoryActivity;
import uni.fmi.masters.myplace.AllCommentsActivity;
import uni.fmi.masters.myplace.AllPlaceActivity;
import uni.fmi.masters.myplace.R;


public class CategoryFragment extends Fragment {
    private CategoryViewModel categoryViewModel;

    Button categoryB;
    Button placeB;
    Button allCommentIO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);


        categoryB = root.findViewById(R.id.categoryButton);
        placeB = root.findViewById(R.id.placeButton);
        allCommentIO = root.findViewById(R.id.allComentInOne);


        allCommentIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AllCommentsActivity.class));
            }
        });

        categoryB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AllCategoryActivity.class));
                //Navigation.findNavController(v).navigate(R.id.barListFragment);
            }
        });

        placeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AllPlaceActivity.class));
            }
        });

        return root;
    }
}



