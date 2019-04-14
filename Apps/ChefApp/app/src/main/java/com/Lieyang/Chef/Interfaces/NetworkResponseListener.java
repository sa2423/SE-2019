package com.Lieyang.Chef.Interfaces;

import com.Lieyang.Chef.Network.RequestType;

public interface NetworkResponseListener {
    void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result);
}
