package com.ctsi.pushers.jpush;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ctsi.push.core.pusher.ConnectionCallback;
import com.ctsi.push.core.pusher.IPusher;
import com.ctsi.push.core.pusher.MessageFilter;
import com.ctsi.push.core.pusher.RegisterCallback;
import com.ctsi.push.message.PushMessage;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by doulala on 16/8/29.
 */
public class JPusher implements IPusher {


    private static final IntentFilter actionsFilter = new IntentFilter();

    static {

        actionsFilter.addAction("cn.jpush.android.intent.INIT");


        actionsFilter.addAction(JPushInterface.ACTION_REGISTRATION_ID);

        actionsFilter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);

        actionsFilter.addAction(JPushInterface.ACTION_CONNECTION_CHANGE);

        actionsFilter.addCategory("com.ctsi.push");

    }

    boolean isStarted = false;

    private Application application;
    RegisterCallback registerCallback;
    MessageFilter messageCallback;
    ConnectionCallback connectionCallback;
    String alias;
    Set<String> tags;

    public JPusher(Context context) {
        this.application = (Application) context.getApplicationContext();
        //this.application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            JPushInterface.onResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            JPushInterface.onPause(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };


    @Override
    public void start() {
        this.application.registerReceiver(broadcastReceiver, actionsFilter);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this.application);
        isStarted = true;
    }

    @Override
    public void stop() {
        JPushInterface.stopPush(this.application);
        isStarted = false;
        this.application.unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
                messager(bundle);
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
                connector(bundle);
            }
        }
    };

    private void register(Bundle bundle) {
        String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        if (!TextUtils.isEmpty(regId)) {
            isStarted = true;
            if (this.registerCallback != null) {
                this.registerCallback.onRegisterSuccess(regId);
            }
        } else {
            if (this.registerCallback != null) {
                this.registerCallback.onRegisterFailed("注册失败");
            }
        }

    }

    private void messager(Bundle bundle) {
        String id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        for (String key : bundle.keySet()) {
            Log.e(key, String.valueOf(bundle.get(key)));
        }
        PushMessage pushMessage = MessageConverter.converter(bundle);
        if (this.messageCallback != null && pushMessage != null) {
            this.messageCallback.onReceivedMessage(pushMessage);
        }
    }


    private void connector(Bundle bundle) {
        boolean connected = bundle.getBoolean(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        if (this.connectionCallback != null) {
            this.connectionCallback.onConnectionChanged(connected);
        }
    }


    @Override
    public void setAliasAndTags(String alias, Set<String> tags) {
        JPushInterface.setAliasAndTags(this.application, alias, tags, tagAliasCallback);
    }

    @Override
    public void setCallbacks(RegisterCallback registerCallback, MessageFilter messageCallback, ConnectionCallback connectionCallback) {
        this.registerCallback = registerCallback;
        this.messageCallback = messageCallback;
        this.connectionCallback = connectionCallback;
    }


    private TagAliasCallback tagAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String s, Set<String> set) {

            if (code == 0) {
                //success;
                alias = s;
                tags = set;
            }

        }
    };

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getDeviceId() {
        return JPushInterface.getRegistrationID(this.application);
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }
}
