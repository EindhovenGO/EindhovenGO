package com.example.endgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;


public class AchievementsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ListView myListView;
    String[] achievements;
    String[] points;
    String[] descriptions;
    String[] lock;
    int noPoints;
    int achievedOnce = 0;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.achievements_listview_detailed, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
        TextView pointsTextView = (TextView) v.findViewById(R.id.pointsTextView);
        TextView locked = (TextView) v.findViewById(R.id.LockedUnlocked);
        ImageView check = (ImageView) v.findViewById(R.id.checkBox);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        String name = achievements[position];
        String description = descriptions[position];
        String point = points[position];
        String lockUnlock = lock[position];
        if (position == 0 && lockUnlock.equals("Locked") && noPoints >= 100) {
            lock[position] = "Unlocked";
            lockUnlock = lock[position];
            /*
            noPoints += Integer.parseInt(point.substring(0, point.length() - 7));
            Query query = db.child("Users").child(fUser.getUid());
            final int finalNoPoints = noPoints;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     //put the queried data into a LocationData object
                     dataSnapshot.getRef().child("points").setValue(finalNoPoints);
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {
                }
             });

             */
        }

        if (lockUnlock.equals("Unlocked")) {
            check.setVisibility(View.VISIBLE);
        }
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        pointsTextView.setText(point);
        locked.setText(lockUnlock);

        return v;
    }

    public int getNoPoints() {
        return noPoints;
    }
}