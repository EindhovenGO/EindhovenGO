package com.example.endgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.endgo.R;
import com.example.endgo.ObjectiveList;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that contains a list of hints which the player can buy
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocationFragment extends HintFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new LocationRecyclerViewAdapter(objectivesToHints(ObjectiveList.ITEMS), mListener));
        }
        return view;
    }

    private List<HintList.HintItem> objectivesToHints(List<ObjectiveData> items) {
        List<HintList.HintItem> hints = new ArrayList<>();
        for (ObjectiveData o: items ) {

            // Convert difficulty integer to String entries
            String diff = getString(R.string.diff_morethanthree);
            switch(o.difficulty) {
                case 0:
                    diff = getString(R.string.diff_zero);
                    break;
                case 1:
                    diff = getString(R.string.diff_one);
                    break;
                case 2:
                    diff = getString(R.string.diff_two);
                    break;
                case 3:
                    diff = getString(R.string.diff_three);
                    break;
                default:
            }
            hints.add(new HintList.HintItem(o.name + "\nDifficulty: " + diff, o.name));
            // The second name is actually used to transfer to the MapsActivity. DO NOT CHANGE
        }

        return hints;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
