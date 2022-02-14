package com.example.dummymvvmproject.util;

import android.app.Dialog;
import android.content.Context;

import com.example.dummymvvmproject.R;


public class MyDialog extends Dialog
{
    public MyDialog(final Context context)
    {
        // Set your theme here
        super(context, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);

        // This is the layout XML file that describes your Dialog layout
        this.setContentView(R.layout.activity_splash);
    }


}