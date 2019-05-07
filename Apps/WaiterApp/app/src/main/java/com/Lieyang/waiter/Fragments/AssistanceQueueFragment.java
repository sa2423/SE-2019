/**
 Created by Lieyang Chen
This is a fragment for the view that shows the list of customer assistance requests
 **/
package com.Lieyang.waiter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.Lieyang.waiter.Adapters.AssistanceRequestsAdapter;
import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.MainActivity;
import com.Lieyang.waiter.Models.AssistanceRequest;
import com.Lieyang.waiter.Models.AssistanceRequests;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.Network.RequestType;
import com.Lieyang.waiter.R;

public class AssistanceQueueFragment extends Fragment implements NetworkResponseListener {

    public static final String TAG = "AssistanceQueueFragment";
    private MainActivity mActivity = null;


    private ListView assistanceRequestsListView;
    private AssistanceRequestsAdapter mAssistanceRequestsAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity){
            mActivity = (MainActivity)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_requesting_assistance, container, false);

        Log.d(TAG, "onCreateView: ");

        assistanceRequestsListView = view.findViewById(R.id.lv_assistance_queue);
        mAssistanceRequestsAdapter = new AssistanceRequestsAdapter(getContext(), AssistanceRequests.sAssistanceRequests);
        assistanceRequestsListView.setAdapter(mAssistanceRequestsAdapter);



        NetworkManager.getInstance().addListener(this);
        NetworkManager.getInstance().getAssistanceQueue();

        Button homebutton = view.findViewById(R.id.assistance_home_button);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showHome();
            }
        });

        return view;
    }



    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {
        switch (REQUEST_TYPE){
            case GET_REQUESTS:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAssistanceRequestsAdapter.clear();
                        mAssistanceRequestsAdapter.addAll(((AssistanceRequests)result).sAssistanceRequests);
                        mAssistanceRequestsAdapter.notifyDataSetChanged();
                    }
                });

                break;
            case COMPLETE_REQUESTS:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AssistanceRequest assistanceRequest = (AssistanceRequest)result;

//                        AssistanceRequest toRemove = null;
//                        for(int i = 0; i < mAssistanceRequestsAdapter.getCount(); i++){
//                            if(assistanceRequest.id.equals(mAssistanceRequestsAdapter.getItem(i).id)){
//                                toRemove = mAssistanceRequestsAdapter.getItem(i);
//                                break;
//                            }
//                        }
//
//                        if(toRemove != null){
//                            mAssistanceRequestsAdapter.remove(toRemove);
//                            mAssistanceRequestsAdapter.notifyDataSetChanged();
//                        }
                        mAssistanceRequestsAdapter.remove(assistanceRequest);
                        mAssistanceRequestsAdapter.notifyDataSetChanged();
                    }
                });

                break;
        }
    }
}
