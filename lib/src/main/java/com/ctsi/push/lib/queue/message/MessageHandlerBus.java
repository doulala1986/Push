package com.ctsi.push.lib.queue.message;

import com.ctsi.push.lib.pusher.MessageHandleCallback;

import java.util.ArrayList;

/**
 * Created by doulala on 16/8/31.
 */
public class MessageHandlerBus {

    ArrayList<IMessageHandler> messageHandlers = new ArrayList<>();

    IMessageHandler defaultMessageHandler;
    MessageHandleCallback callback;

    public MessageHandlerBus(IMessageHandler defaultMessageHandler, MessageHandleCallback callback) {

        this.defaultMessageHandler = defaultMessageHandler;
        this.callback = callback;
    }

    public void addMessageHandler(IMessageHandler messageHandler) {

        if (messageHandler != null && !messageHandlers.contains(messageHandler)) {

            messageHandlers.add(messageHandler);
        }
    }

    public void execute(PushMessage message) {
        if (message == null) {
            if (callback != null)
                callback.onMessageFailed(message, new NullPointerException("message is null"));
            return;
        }

        IMessageHandler handler_bigon = null;
        for (IMessageHandler handler : messageHandlers) {
            if (handler.isMessageMatched(message)) {
                handler_bigon = handler;
                break;
            }
        }
        if (handler_bigon == null && defaultMessageHandler.isMessageMatched(message)) {
            handler_bigon = defaultMessageHandler;
        }
        if (handler_bigon != null) {
            if (callback != null)
                callback.onMessageBingo(message);
            try {
                handler_bigon.equals(message);
            } catch (Throwable ex) {
                if (callback != null)
                    callback.onMessageFailed(message, ex);
            }
        }

    }

}
