package com.example.endgo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AchievementsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ListView myListView;
    String[] achievements;
    String[] points;
    String[] descriptions;
    String[] lock;
    int noPoints;

    public AchievementsAdapter(Context c, String[] achievements, String[] points, String[] descriptions, String[] locked, int noPoints) {
        this.achievements = achievements;
        this.points = points;
        this.descriptions = descriptions;
        lock = locked;
        this.noPoints = noPoints;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return achievements.length;
    }

    @Override
    public Object getItem(int position) {
        return achievements[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.achievements_listview_detailed, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
        TextView pointsTextView = (TextView) v.findViewById(R.id.pointsTextView);
        TextView locked = (TextView) v.findViewById(R.id.LockedUnlocked);


        String name = achievements[position];
        String description = descriptions[position];
        String point = points[position];
        String lockUnlock = lock[position];
        if (position == 0 && lockUnlock.equals("Locked") && noPoints == 100) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
        }

        nameTextView.setText(name);
        descriptionTextView.setText(description);
        pointsTextView.setText(point);
        locked.setText(lockUnlock);

        return v;
    }
}