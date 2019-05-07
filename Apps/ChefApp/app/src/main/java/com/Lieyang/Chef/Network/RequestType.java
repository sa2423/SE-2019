/**
 * Created by Taras Tysovskyi
 * Enum that contains the types of requests NetworkManager can make
 * Used by classes that implement NetworkResponseListener interface when a response is called
 */
package com.Lieyang.Chef.Network;

public enum RequestType {
    GET_CURRENTORDERS,
    GET_COMPLETEDORDERS,
    COMLETE_ORDER
}
