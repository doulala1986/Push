package com.ctsi.push.lib.pusher;

import com.ctsi.push.message.PushMessage;

/**
 * Created by doulala on 16/8/29.
 */
public interface MessageFilter {


    /**
     * @param message
     * @return true if message bean consumed
     */
    boolean onReceivedMessage(PushMessage message);


}
