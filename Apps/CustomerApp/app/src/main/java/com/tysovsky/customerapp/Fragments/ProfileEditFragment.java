/**
 * Created by Lieyang Chen
 * Fragment for the view to edit user info
 */
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
import android.widget.Toast;

import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.Models.User;
import com.tysovsky.customerapp.Network.NetworkManager;
import com.tysovsky.customerapp.R;

public class ProfileEditFragment extends Fragment {

    public final static String TAG = "ProfileEditFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_edit, container, false);

        Log.d(TAG, "onCreateView: ");

        EditText firstName = view.findViewById(R.id.first_name_EditTxt);
        EditText lastName = view.findViewById(R.id.last_name_EditTxt);
        EditText userName = view.findViewById(R.id.user_name_EditTxt);
        EditText password = view.findViewById(R.id.password_EditTxt);

        User user = User.getCurrentUser();
        firstName.setText(user.FirstName);


        Button confirm_btn = view.findViewById(R.id.confirm_edit_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FirstName = firstName.getText().toString();
                String LastName = lastName.getText().toString();
                String UserName = userName.getText().toString();
                String Password = password.getText().toString();
                User user = User.getCurrentUser();
                String UserId = user.Id;

                Log.d(TAG, FirstName+" " + LastName + " " + UserName + " " + Password);

                if(FirstName.matches("") || LastName.matches("") || UserName.matches("") || Password.matches("")) {
                    Toast.makeText(getContext(), "Not Enough Input", Toast.LENGTH_SHORT).show();
                    return;
                }
                User.updateCurrentUser(UserId,UserName,FirstName,LastName);

                String userJson = "{\"_id\": " + "\"" + UserId + "\"" + " }";
                String setJson = "{ \"$set\": { \"name\": \"" + UserName + "\"" +
                        ", \"first_name\": " + "\"" + FirstName + "\"" +
                        ", \"last_name\": " + "\"" + LastName + "\"" +
                        ", \"password\": " + "\"" + Password + "\"} }";

                Log.d("json", userJson+", " + setJson);

                NetworkManager.getInstance().EditProfile(userJson,setJson);
                Toast.makeText(getContext(), "Profile Edited", Toast.LENGTH_LONG).show();
                ((MainActivity)getContext()).updateNavigationDrawer();
            }
        });

        return view;
    }
}
