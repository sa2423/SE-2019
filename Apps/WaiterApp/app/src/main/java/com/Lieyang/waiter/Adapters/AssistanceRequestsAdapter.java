/**
 Created by Lieyang Chen
 This is an adapter to the list of Assistance Requests
 **/
package com.Lieyang.waiter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.Lieyang.waiter.Models.AssistanceRequest;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.R;

import java.util.List;

public class AssistanceRequestsAdapter extends ArrayAdapter<AssistanceRequest> {

    public AssistanceRequestsAdapter(Context context, List<AssistanceRequest> assistanceRequests) {
        super(context, 0, assistanceRequests);
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {


        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_assistance_request, parent, false);
        }

        AssistanceRequest assistanceRequest = getItem(position);

        TextView requestTableId = itemView.findViewById(R.id.request_table_id);
        TextView requestGuestId = itemView.findViewById(R.id.request_guest_id);
        TextView requestDate = itemView.findViewById(R.id.request_date);

        requestTableId.setText(assistanceRequest.id);
        requestGuestId.setText(assistanceRequest.userid);
        requestDate.setText(assistanceRequest.datetime);

        Button requestButton = itemView.findViewById(R.id.request_complete_button);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().removeAssistanceRequest(assistanceRequest);
            }
        });

        return itemView;
    }



}
