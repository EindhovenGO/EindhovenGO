package com.example.endgo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import com.example.endgo.ItemRecyclerViewAdapter.*;
import com.example.endgo.HintList;
import com.example.endgo.HintFragment;
import com.example.endgo.LocationFragment;


public class LocationRecyclerViewAdapter extends ItemRecyclerViewAdapter {

    public LocationRecyclerViewAdapter(List<HintList.HintItem> items, HintFragment.OnListFragmentInteractionListener listener) {
        super(items, listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        // set the name for the hint, this is what the user will see in front of
        // the buy button
        holder.mContentView.setText(mValues.get(position).name);
        holder.mView.findViewById(R.id.button_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_locationitem, parent, false);
        return new ViewHolder(view);
    }
}
