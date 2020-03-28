package com.example.endgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AchievementsActivity extends AppCompatActivity {

    ListView listView;
    String[] achievements_list;
    String[] descriptions;
    String[] points;
    String[] locked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        listView = (ListView)findViewById(R.id.listview);
        Resources res = getResources();

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent i = getIntent();
        int noPoints = i.getIntExtra("points", -1);
        assert fUser != null;


        achievements_list = res.getStringArray(R.array.achievements);
        points = res.getStringArray(R.array.points);
        descriptions = res.getStringArray(R.array.description);
        locked = res.getStringArray(R.array.locked);

        AchievementsAdapter achievementsAdapter = new AchievementsAdapter(this, achievements_list, points, descriptions, locked, noPoints);
        listView.setAdapter(achievementsAdapter);
    }
}
