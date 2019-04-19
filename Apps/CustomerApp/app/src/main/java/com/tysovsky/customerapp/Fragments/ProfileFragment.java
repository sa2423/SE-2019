package com.tysovsky.customerapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.R;
import com.tysovsky.customerapp.Models.User;

public class ProfileFragment extends Fragment {

    public final static String TAG = "ProfileFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        Log.d(TAG, "onCreateView: ");

        User user = User.getCurrentUser();

        TextView firstName = view.findViewById(R.id.first_name_txtV);
        TextView lastName = view.findViewById(R.id.last_name_txtV);
        TextView userName = view.findViewById(R.id.user_name_txtV);

        firstName.setText(user.FirstName);
        lastName.setText(user.LastName);
        userName.setText(user.Username);

        Button edit_profile = view.findViewById(R.id.edit_profile_btn);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showEditFragment();
            }
        });

        return view;
    }
}
