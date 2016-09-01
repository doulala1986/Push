package com.ctsi.push.pushers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ctsi.push.core.pusher.ConnectionCallback;
import com.ctsi.push.core.pusher.IPusher;
import com.ctsi.push.core.pusher.MessageFilter;
import com.ctsi.push.core.pusher.RegisterCallback;
import com.ctsi.push.core.queue.message.PushMessage;
import com.ctsi.push.ztest.CtsiApplication;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by doulala on 16/8/29.
 */
public class JPusher implements IPusher {


    private static final IntentFilter actionsFilter = new IntentFilter();

    static {

        actionsFilter.addAction(JPushInterface.ACTION_REGISTRATION_ID);

        actionsFilter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);

        actionsFilter.addAction(JPushInterface.ACTION_CONNECTION_CHANGE);

    }


    private CtsiApplication application;
    RegisterCallback registerCallback;
    MessageFilter messageCallback;
    ConnectionCallback connectionCallback;

    public JPusher(Context context) {
        this.application = CtsiApplication.get(context);
    }

    @Override
    public void start() {
        this.application.registerReceiver(broadcastReceiver, actionsFilter);
        JPushInterface.init(this.application);
    }

    @Override
    public void stop() {
        JPushInterface.stopPush(this.application);
        this.application.unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {

                register(bundle);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {

                messager(bundle);
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
                connector(bundle);

            }
        }
    };

    private void register(Bundle bundle) {
        String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        if (this.registerCallback != null) {
            this.registerCallback.onRegisterSuccess(regId);
        }
    }

    private void messager(Bundle bundle) {
        String id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        PushMessage pushMessage = new PushMessage(id, message, extras);
        if (this.messageCallback != null) {
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
    public boolean isStart() {
        return !JPushInterface.isPushStopped(this.application);
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
        public void gotResult(int i, String s, Set<String> set) {

        }
    };
}
