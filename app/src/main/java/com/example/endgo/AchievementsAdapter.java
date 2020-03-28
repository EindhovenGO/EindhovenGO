package com.example.endgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AchievementsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ListView myListView;
    String[] achievements;
    String[] points;
    String[] descriptions;

    public AchievementsAdapter(Context c, String[] achievements, String[] points, String[] descriptions) {
        this.achievements = achievements;
        this.points = points;
        this.descriptions = descriptions;
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

        String name = achievements[position];
        String description = descriptions[position];
        String point = points[position];

        nameTextView.setText(name);
        descriptionTextView.setText(description);
        pointsTextView.setText(point);

        return v;
    }
}