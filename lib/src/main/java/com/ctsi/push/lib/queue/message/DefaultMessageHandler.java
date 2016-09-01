package com.ctsi.push.lib.queue.message;

/**
 * Created by doulala on 16/8/31.
 */
public abstract class DefaultMessageHandler implements IMessageHandler {


    @Override
    public boolean isMessageMatched(PushMessage message) {
        return true;
    }
}
