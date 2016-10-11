package com.ctsi.push.lib.queue.message;

import com.ctsi.push.message.PushMessage;

/**
 * Created by doulala on 16/8/31.
 */
public abstract class DefaultMessageHandler implements IMessageHandler {


    @Override
    public boolean isMessageMatched(PushMessage message) {
        return true;
    }
}
