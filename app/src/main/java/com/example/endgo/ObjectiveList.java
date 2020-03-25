package com.example.endgo;

import java.util.ArrayList;
import java.util.List;

class ObjectiveList {
    public static final List<ObjectiveData> ITEMS = new ArrayList<>();

    static{
        int i = 0;
        while(i < 5) {
            ITEMS.add(new ObjectiveData("Objective "+ (i + 1), 0));
            i++;
        }
    }
    //TODO fetch all objectives
}
