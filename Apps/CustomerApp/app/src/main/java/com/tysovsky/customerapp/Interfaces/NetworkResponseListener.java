package com.tysovsky.customerapp.Interfaces;

import com.tysovsky.customerapp.Network.RequestType;

public interface NetworkResponseListener {
    void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result);
}
