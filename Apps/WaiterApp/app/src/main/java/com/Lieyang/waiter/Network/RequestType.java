/**
 * Created by Taras Tysovskyi. Edited by Lieyang Chen
 * Enum that contains the types of requests NetworkManager can make
 * Used by classes that implement NetworkResponseListener interface when a response is called
 */
package com.Lieyang.waiter.Network;

public enum RequestType {
    GET_CURRENTORDERS,
    GET_COMPLETEDORDERS,
    COMPLETE_ORDER,
    GET_REQUESTS,
    COMPLETE_REQUESTS,
    ORDER_INFO,
    LOGIN,
    LOGOUT
}
