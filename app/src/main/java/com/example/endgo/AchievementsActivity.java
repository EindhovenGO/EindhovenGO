package com.example.endgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AchievementsActivity extends AppCompatActivity {

    ListView listView;
    String[] achievements_list;
    String[] descriptions;
    String[] points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        listView = (ListView)findViewById(R.id.listview);
        Resources res = getResources();

        achievements_list = res.getStringArray(R.array.achievements);
        points = res.getStringArray(R.array.points);
        descriptions = res.getStringArray(R.array.description);

        AchievementsAdapter achievementsAdapter = new AchievementsAdapter(this, achievements_list, points, descriptions);
        listView.setAdapter(achievementsAdapter);
    }
}
