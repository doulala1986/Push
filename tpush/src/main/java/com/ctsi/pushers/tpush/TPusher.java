package com.ctsi.pushers.tpush;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.ctsi.push.core.pusher.ConnectionCallback;
import com.ctsi.push.core.pusher.IPusher;
import com.ctsi.push.core.pusher.MessageFilter;
import com.ctsi.push.core.pusher.RegisterCallback;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.util.Set;

/**
 * Created by doulala on 2016/12/18.
 */

public class TPusher implements IPusher {

    Application application;
    String deviceId;
    RegisterCallback registerCallback;
    MessageFilter messageCallback;
    ConnectionCallback connectionCallback;
    String alias;
    Set<String> tags;

    private static String ACTION_PUSH_MESSAGE = "com.tencent.android.tpush.action.PUSH_MESSAGE";

    private static String ACTION_PUSH_FEEDBACK = "com.tencent.android.tpush.action.FEEDBACK";


    public TPusher(Context context) {
        this.application = (Application) context.getApplicationContext();
    }

    @Override
    public void start() {
        XGPushManager.registerPush(this.application, "*");//取消
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PUSH_MESSAGE);
        intentFilter.addAction(ACTION_PUSH_FEEDBACK);
        this.application.registerReceiver(xgPushBaseReceiver, intentFilter);
    }

    @Override
    public void stop() {
        this.application.unregisterReceiver(xgPushBaseReceiver);
        deviceId = null;
    }

    @Override
    public boolean isStarted() {
        return !TextUtils.isEmpty(deviceId);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public void setAliasAndTags(String alias, Set<String> tags) {
        this.alias = alias;
        this.tags = tags;
        XGPushManager.registerPush(this.application, alias);
        for (String tag : tags) {
            XGPushManager.setTag(this.application, tag);
        }
    }

    @Override
    public void setCallbacks(RegisterCallback registerCallback, MessageFilter messageCallback, ConnectionCallback connectionCallback) {
        this.registerCallback = registerCallback;
        this.messageCallback = messageCallback;
        this.connectionCallback = connectionCallback;
    }


    /****************
     * 消息穿透
     ********************/


    private XGPushBaseReceiver xgPushBaseReceiver = new XGPushBaseReceiver() {
        @Override
        public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

        }

        @Override
        public void onUnregisterResult(Context context, int i) {

        }

        @Override
        public void onSetTagResult(Context context, int i, String s) {

        }

        @Override
        public void onDeleteTagResult(Context context, int i, String s) {

        }

        @Override
        public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

        }

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

        }
    };
}
