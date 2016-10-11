package com.ctsi.message;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ctsi.message.jpush.JPusher;
import com.ctsi.push.core.PushBuilder;
import com.ctsi.push.core.PushInterface;
import com.ctsi.push.core.pusher.ConnectionCallback;
import com.ctsi.push.core.pusher.MessageCallback;
import com.ctsi.push.core.pusher.RegisterCallback;
import com.ctsi.push.core.queue.message.IMessageHandler;
import com.ctsi.push.message.PushMessage;

/**
 * Created by doulala on 16/8/29.
 */
public class CtsiApplication extends Application {


    public static CtsiApplication get(Context context) {

        return (CtsiApplication) context.getApplicationContext();
    }

    private PushInterface pushService;

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
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
                .pusher(new JPusher(CtsiApplication.this))
                .defaultMessageHandler(null)
                .addMessageHandler(messageHandlerA)
                .callback(messageCallback)
                .callback(registerCallback)
                .callback(connectionCallback)
                .build();

        pushService.start();
    }

    private MessageCallback messageCallback = new MessageCallback() {

        @Override
        public void onMessageBingo(PushMessage message) {


            Log.i("onMessageBingo", message.toString());
        }

        @Override
        public void onMessageFailed(PushMessage message, Throwable ex) {
            ex.printStackTrace();
            Log.e("onMessageFailed", message.toString());
        }

        @Override
        public boolean onReceivedMessage(PushMessage message) {

            Log.i ("onReceivedMessage", message.toString());
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

            Log.i ("messageHandler ", message.toString());

        }
    };


}
