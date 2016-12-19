package com.ctsi.message;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ctsi.push.core.PushBuilder;
import com.ctsi.push.core.PushInterface;
import com.ctsi.push.core.pusher.ConnectionCallback;
import com.ctsi.push.core.pusher.MessageCallback;
import com.ctsi.push.core.pusher.RegisterCallback;
import com.ctsi.push.core.queue.message.IMessageHandler;
import com.ctsi.push.message.PushMessage;
import com.ctsi.pushers.tpush.TPusher;
import com.google.gson.Gson;

import java.util.Observable;

/**
 * Created by doulala on 16/8/29.
 */
public class CtsiApplication extends Application {


    public static CtsiApplication get(Context context) {

        return (CtsiApplication) context.getApplicationContext();
    }

    private PushInterface pushService;

    private Gson  gson=new Gson();
    private Observable observable = new Observable(){

        @Override
        public void notifyObservers(Object data) {
            setChanged();
            super.notifyObservers(data);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }

    public Observable getObservable() {
        return observable;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        pushService.stop();
    }

    public PushInterface getPushService() {
        return pushService;
    }

    private void initPush() {
        pushService = PushBuilder.builder()
                .pusher(new TPusher(CtsiApplication.this))
                .defaultMessageHandler(null)
                .addMessageHandler(messageHandlerA)
                .callback(messageCallback)
                .callback(registerCallback)
                .callback(connectionCallback)
                .build();
    }



    private MessageCallback messageCallback = new MessageCallback() {

        @Override
        public void onMessageBingo(PushMessage message) {


            Log.i("onMessageBingo", gson.toJson(message));
        }

        @Override
        public void onMessageFailed(PushMessage message, Throwable ex) {
            ex.printStackTrace();
            Log.e("onMessageFailed",  gson.toJson(message));
        }

        @Override
        public boolean onReceivedMessage(PushMessage message) {
            observable.notifyObservers( gson.toJson(message));
            Log.i("onReceivedMessage",  gson.toJson(message));
            return false;
        }
    };

    private RegisterCallback registerCallback = new RegisterCallback() {
        @Override
        public void onRegisterSuccess(String deviceId) {

            Log.i("onRegisterSuccess", "deviceId:" + deviceId);

        }

        @Override
        public void onRegisterFailed(String message) {
            Log.e("onRegisterFailed", message);
        }

    };

    private ConnectionCallback connectionCallback = new ConnectionCallback() {
        @Override
        public void onConnectionChanged(boolean connected) {
            Log.e("onConnectionChanged", connected ? "建立连接" : "失去连接");

        }
    };

    private IMessageHandler messageHandlerA = new IMessageHandler() {
        @Override
        public boolean isMessageMatched(PushMessage message) {
            return message.getCommandAction().getTopic().contains("location");
        }

        @Override
        public void execute(PushMessage message) {

            Log.i("messageHandler ", message.toString());


        }
    };


}
