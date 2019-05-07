/**
 * Created by Taras Tysovskyi.
 * Interface to be implemented by every class that wants to listen to network events
 */
package com.tysovsky.customerapp.Interfaces;

import com.tysovsky.customerapp.Network.RequestType;

public interface NetworkResponseListener {
    void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result);
}
