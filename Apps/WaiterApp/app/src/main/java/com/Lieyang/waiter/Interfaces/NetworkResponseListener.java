package com.Lieyang.waiter.Interfaces;

import com.Lieyang.waiter.Network.RequestType;

public interface NetworkResponseListener {
    void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result);
}
