package com.tysovsky.customerapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tysovsky.customerapp.Interfaces.NetworkResponseListener;
import com.tysovsky.customerapp.Network.NetworkManager;
import com.tysovsky.customerapp.Network.RequestType;
import com.tysovsky.customerapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements NetworkResponseListener {

    public static final String TAG = "LoginFragment";

    TextView errorMsg;
    EditText etUsername, etPassword;
    Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        NetworkManager.getInstance().addListener(this);

        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);

        errorMsg = view.findViewById(R.id.tv_error_message);

        btnLogin = view.findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if(username.isEmpty()){
                errorMsg.setText("Username can't be empty");
                return;
            }
            if(password.isEmpty()){
                errorMsg.setText("Password can't be empty");
                return;
            }

            NetworkManager.getInstance().login(username, password);
        });


        return view;
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result){
        switch (REQUEST_TYPE){
            case LOGIN:
                JSONObject response = (JSONObject)result;
                try {
                    if(!response.getBoolean("success")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    errorMsg.setText(response.getString("error"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }
}
