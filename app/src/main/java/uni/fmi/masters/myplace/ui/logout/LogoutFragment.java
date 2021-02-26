package uni.fmi.masters.myplace.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import uni.fmi.masters.myplace.MainActivity;
import uni.fmi.masters.myplace.R;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;
    Button logoutB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                new ViewModelProvider(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);


        logoutB = root.findViewById(R.id.logoutButton);


        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        final TextView textView = root.findViewById(R.id.text_slideshow);
        logoutViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}