package com.example.endgo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class AchievementsActivity extends AppCompatActivity {

    ListView list;
    String[] achievementsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        //list = (ListView) findViewById(R.id.ListOfAchievements);
        //achievementsList = getResources().getStringArray(R.array.achievements);

        //list.setAdapter(new ArrayAdapter<String>(this, R.layout.achievements_listview_layout, achievementsList));

    }
}
