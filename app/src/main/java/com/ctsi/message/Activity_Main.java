package com.ctsi.message;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by doulala on 16/9/1.
 */
public class Activity_Main extends Activity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Set<String> tags=new HashSet<>();
        tags.add("android");


        if (CtsiApplication.get(Activity_Main.this).getPushService().isStarted()) {
            CtsiApplication.get(Activity_Main.this).getPushService().setAliasAndTags("18911552163", tags);
        }

        textView = new TextView(this);
        setContentView(textView);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLUE);


    }


}
