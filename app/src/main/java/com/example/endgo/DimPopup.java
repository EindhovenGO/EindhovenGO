package com.example.endgo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * A popup that has the functionality to dim the elements in the background
 */
public class DimPopup extends PopupWindow {

    public DimPopup(Activity parent, int width, int height, boolean focusable) {
        super(null, width, height, focusable);
        LayoutInflater layoutInflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // we create a view that contains the hint content
        View content = layoutInflater.inflate(R.layout.popup,null);
        setContentView(content);
    }

    /**
     * Dim the background elements behind the popup
     */
    public void dimBackground() {
        View root = getContentView().getRootView();
        // get the windowmanager and layout parameters
        WindowManager manager = (WindowManager)getContentView().getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = (WindowManager.LayoutParams)root.getLayoutParams();

        // we add the dim behind flag to dim the screen behind the popupWindow
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        manager.updateViewLayout(root, params);
    }
}
