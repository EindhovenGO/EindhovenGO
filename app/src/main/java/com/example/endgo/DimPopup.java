package com.example.endgo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class DimPopup extends PopupWindow {
    private Activity parent;

    public DimPopup(Activity parent, int width, int height, boolean focusable) {
        super(null, width, height, focusable);
        this.parent = parent;
        LayoutInflater layoutInflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // we create a view that contains the hint content
        View content = layoutInflater.inflate(R.layout.popup,null);
        setContentView(content);
    }

    // will dim the stuff outside of our DimPopup
    public void dimBehind() {
        View container = this.getContentView().getRootView();
        Context context = this.getContentView().getContext();
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = (WindowManager.LayoutParams)container.getLayoutParams();

        // we add the dim behind flag to dim the screen behind the popupWindow
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        manager.updateViewLayout(container, params);
    }
}
