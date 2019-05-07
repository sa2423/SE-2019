/**
 * Created by Taras Tysovskyi.
 * Interface to be implemented by every class that wants to listen to network events
 */
package com.Lieyang.Chef.Interfaces;

import com.Lieyang.Chef.Network.RequestType;

public interface NetworkResponseListener {
    void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result);
}
