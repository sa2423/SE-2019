package com.Lieyang.waiter.Models;

import java.util.ArrayList;
import java.util.List;

public class AssistanceRequests {
    public static List<AssistanceRequest> sAssistanceRequests = new ArrayList<>();

    public AssistanceRequests(){
        sAssistanceRequests = new ArrayList<>();
    }

    public AssistanceRequests(List<AssistanceRequest> items){
        sAssistanceRequests = items;
    }
}
