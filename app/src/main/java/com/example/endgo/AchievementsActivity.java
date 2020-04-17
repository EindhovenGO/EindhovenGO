package com.example.endgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AchievementsActivity extends AppCompatActivity {

    ListView listView; //list view fragment of the layout
    String[] achievements_list; //array of the names of the achievements
    String[] descriptions; //array of descriptions for each achievement
    String[] locked; // array of statuses for each achievement


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        listView = (ListView)findViewById(R.id.listview);
        Resources res = getResources();  //reference to strings.xml

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        assert fUser != null;


        achievements_list = res.getStringArray(R.array.achievements);
        descriptions = res.getStringArray(R.array.description);
        locked = res.getStringArray(R.array.locked);

        Intent i = getIntent();
        int noPoints = i.getIntExtra("points", -1); //fetch the number of points of the user from main menu activity

        AchievementsAdapter achievementsAdapter = new AchievementsAdapter(this, achievements_list, descriptions, locked, noPoints);
        listView.setAdapter(achievementsAdapter); //adapt the listview

    }
}
