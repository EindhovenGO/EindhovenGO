package com.example.endgo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

class ObjectiveList<db> {
    public static final List<ObjectiveData> ITEMS = new ArrayList<>();

    static{
        /*
        int i = 0;
        while(i < 5) {
            ITEMS.add(new ObjectiveData("Objective "+ (i + 1), 0));
            i++;
        }*/
    }
    //TODO fetch all objectives
    public static void write(ObjectiveData o) {
        ITEMS.add( o );
    }

    public static void clear() {
        ITEMS.clear();
    }

    //Filter locations
}
