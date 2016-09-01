package com.ctsi.push;

import android.app.Application;
import android.content.Context;

import com.ctsi.push.core.PushBuilder;
import com.ctsi.push.core.PushService;
import com.ctsi.push.lib.pusher.ConnectionCallback;
import com.ctsi.push.lib.pusher.MessageCallback;
import com.ctsi.push.lib.pusher.RegisterCallback;
import com.ctsi.push.lib.queue.message.IMessageHandler;
import com.ctsi.push.lib.queue.message.PushMessage;

/**
 * Created by doulala on 16/8/29.
 */
public class CtsiApplication extends Application {


    public static CtsiApplication get(Context context) {

        return (CtsiApplication) context.getApplicationContext();
    }

    private PushService pushService;

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

    public PushService getPushService() {
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

        }

        @Override
        public void onMessageFailed(PushMessage message, Throwable ex) {

        }

        @Override
        public boolean onReceivedMessage(PushMessage message) {
            return false;
        }
    };

    private RegisterCallback registerCallback = new RegisterCallback() {
        @Override
        public void onRegisterSuccess(String deviceId) {

        }

        @Override
        public void onRegisterFailed(String message) {

        }

    };

    private ConnectionCallback connectionCallback = new ConnectionCallback() {
        @Override
        public void onConnectionChanged(boolean connected) {

        }
    };

    private IMessageHandler messageHandlerA = new IMessageHandler() {
        @Override
        public boolean isMessageMatched(PushMessage message) {
            return false;
        }

        @Override
        public void execute(PushMessage message) {

        }
    };


}
