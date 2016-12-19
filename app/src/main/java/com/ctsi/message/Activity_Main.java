package com.ctsi.message;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by doulala on 16/9/1.
 */
public class Activity_Main extends Activity {

    TextView textView;

    CtsiApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = CtsiApplication.get(this);


        Set<String> tags = new HashSet<>();
        tags.add("android");


        if (CtsiApplication.get(Activity_Main.this).getPushService().isStarted()) {
            CtsiApplication.get(Activity_Main.this).getPushService().setAliasAndTags("18911552161", tags);
        }

        application.getObservable().addObserver(observer);
        textView = new TextView(this);
        setContentView(textView);
        textView.setTextSize(16);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.getObservable().deleteObserver(observer);
    }

    Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object data) {



            textView.append(data.toString());
        }
    };


}
