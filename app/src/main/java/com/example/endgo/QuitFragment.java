package com.example.endgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.endgo.MainMenu;
import com.example.endgo.R;


public class QuitFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quit, container, false);
    }

    //TODO implement
    private void configureButton() {
        //abandon mission button functionality
        Button button_abandon = (Button) this.getView().findViewById(R.id.button_abandon);
        button_abandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abandonMission();
            }
        });
    }

    public void abandonMission() {
        // TODO abandon mission functionality

        //Switch to main activity
        Intent i = new Intent( this.getContext(), MainMenu.class);
        startActivity(i);

    }
}
