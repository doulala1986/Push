package com.ctsi.push.lib;

import com.ctsi.push.lib.pusher.MessageCallback;
import com.ctsi.push.lib.queue.JobQueue;
import com.ctsi.push.lib.pusher.ConnectionCallback;
import com.ctsi.push.lib.pusher.IPusher;
import com.ctsi.push.lib.pusher.RegisterCallback;
import com.ctsi.push.lib.queue.message.DefaultMessageHandler;
import com.ctsi.push.lib.queue.message.IMessageHandler;
import com.ctsi.push.lib.queue.message.MessageHandlerBus;

import java.util.ArrayList;

/**
 * Created by doulala on 16/8/29.
 * <p/>
 * 推送服务Builder,用来生成PushService.
 */
public class PushBuilder {

    private IPusher pusher;
    private DefaultMessageHandler defaultMessageHandler;
    private ArrayList<IMessageHandler> handlers = new ArrayList<>();

    private RegisterCallback registerCallback;
    private MessageCallback messageCallback;
    private ConnectionCallback connectionCallback;

    private PushBuilder() {


    }

    public static PushBuilder builder() {

        return new PushBuilder();
    }

    public PushInterface build() {

        //初始化Pusher,MessageQueuue
        MessageHandlerBus messageHandlerBus = new MessageHandlerBus(defaultMessageHandler, messageCallback);
        for (IMessageHandler handler : handlers) {
            messageHandlerBus.addMessageHandler(handler);
        }
        JobQueue jobQueue = new JobQueue(messageHandlerBus);
        MessageDispatcher dispatcher = new MessageDispatcher(jobQueue);
        dispatcher.setMessageCallback(messageCallback);
        pusher.setCallbacks(registerCallback, dispatcher, connectionCallback);
        return new PushInterface(pusher, dispatcher);
    }


    public PushBuilder pusher(IPusher pusher) {
        setPusher(pusher);
        return this;
    }

    public PushBuilder defaultMessageHandler(DefaultMessageHandler handler) {

        setDefaultMessageHandler(handler);
        return this;
    }

    public PushBuilder addMessageHandler(IMessageHandler handler) {

        this.handlers.add(handler);
        return this;
    }

    public PushBuilder callback(RegisterCallback callback) {

        setRegisterCallback(callback);
        return this;
    }

    public PushBuilder callback(MessageCallback callback) {
        setMessageCallback(callback);
        return this;
    }

    public PushBuilder callback(ConnectionCallback callback) {
        setConnectionCallback(callback);
        return this;
    }


    private void setPusher(IPusher iPusher) {
        this.pusher = iPusher;
    }


    private void setDefaultMessageHandler(DefaultMessageHandler defaultMessageHandler) {
        this.defaultMessageHandler = defaultMessageHandler;
    }

    private void setRegisterCallback(RegisterCallback registerCallback) {
        this.registerCallback = registerCallback;
    }

    private void setMessageCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }


    private void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.connectionCallback = connectionCallback;
    }
}
