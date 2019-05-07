/**
 * Created by Taras Tysovskyi
 * Enum that contains the types of requests NetworkManager can make
 * Used by classes that implement NetworkResponseListener interface when a response is called
 */
package com.tysovsky.customerapp.Network;

public enum RequestType {
    GET_MENU,
    PLACE_ORDER,
    LOGIN,
    LOGOUT,
    EDIT_PROFILE
}
