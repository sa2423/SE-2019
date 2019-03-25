package com.Lieyang.waiter.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Lieyang.waiter.MainActivity;
import com.Lieyang.waiter.R;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView menuImage = view.findViewById(R.id.MenuImage);
        ImageView AssistanceRequestsImage = view.findViewById(R.id.AssistanceRequestImage);
        ImageView ordersImage = view.findViewById(R.id.OrderImage);

        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showMenu();
            }
        });

        AssistanceRequestsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showAssistanceQueue();
            }
        });

        ordersImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showCurrentOrder();
            }
        });


        return view;

    }
}
