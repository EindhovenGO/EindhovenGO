package com.example.endgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AchievementsAdapter extends BaseAdapter {

    LayoutInflater mInflater; //inflater for the layout
    String[] achievements; //array of the names of the achievements
    String[] descriptions; //array of descriptions for each achievement
    String[] lock; // array of statuses for each achievement
    int noPoints; // number of points of the user

    public AchievementsAdapter(Context c, String[] achievements, String[] descriptions, String[] locked, int noPoints) {
        this.achievements = achievements;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.achievements_listview_detailed, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
        TextView pointsTextView = (TextView) v.findViewById(R.id.pointsTextView);
        TextView locked = (TextView) v.findViewById(R.id.LockedUnlocked);
        ImageView check = (ImageView) v.findViewById(R.id.checkBox);

        String name = achievements[position];
        String description = descriptions[position];
        String lockUnlock = lock[position];
        if (position == 0 && lockUnlock.equals("Locked") && noPoints >= 100) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
        } //first achievement unlocked if the user has more than 100 points

        if (position == 1 && lockUnlock.equals("Locked") && noPoints >= 500) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
        } //second achievement unlocked if the user has more than 500 points

        if (position == 2 && lockUnlock.equals("Locked") && noPoints >= 800) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
        } //third achievement unlocked if the user has more than 800 points

        if (position == 3 && lockUnlock.equals("Locked") && noPoints >= 1000) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
        } //fourth achievement unlocked if the user has more than 1000 points

        if (position == 4 && lockUnlock.equals("Locked") && noPoints >= 1500) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
        } //fifth achievement unlocked if the user has more than 1500 points

        if (lockUnlock.equals("Unlocked")) { //display the light bulb in case the achievement is unlocked
            check.setVisibility(View.VISIBLE);
        }
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        locked.setText(lockUnlock);

        return v;
    }

}