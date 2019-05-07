/**
 Created by Lieyang Chen
 This is a fragment for the view that shows the list of notifications the waiter has received
 **/
package com.Lieyang.waiter.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Lieyang.waiter.R;

public class NotificationsFragment extends Fragment {

    public static final String TAG = "NotificationsFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        return view;
    }
}
