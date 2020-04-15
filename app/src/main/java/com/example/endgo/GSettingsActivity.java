package com.example.endgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class GSettingsActivity extends AppCompatActivity implements HintFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gsettings);

        // we set the difficulty fragment as the default fragment to be loaded
        Fragment newFragment = new DifficultyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, newFragment);
        transaction.commit();

        final Button buttonQuit = findViewById(R.id.button_quit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment newFragment = new QuitFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace the fragment with the quitfragment
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.commit();

            }
        });

        final Button buttonDifficulty = findViewById(R.id.button_difficulty);
        buttonDifficulty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment newFragment = new DifficultyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace the fragment with the difficulty fragment
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.commit();

            }
        });

        final Button buttonHints = findViewById(R.id.button_hints);
        buttonHints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment newFragment = new HintFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace the fragment with the hintfragment
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.commit();

            }
        });
    }

    @Override
    public void onListFragmentInteraction(HintList.ListItem item) {
        // we create a popupWindow that displays the hintContent view
        DimPopup popupWindow = new DimPopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        ConstraintLayout layout = findViewById(R.id.layout);
        View hintContent = popupWindow.getContentView();

        TextView text = hintContent.findViewById(R.id.text_hint);

        //display the popup window
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        popupWindow.dimBackground();

        // set the popupWindow text to the hint details
        text.setText(item.content);
    }
}
