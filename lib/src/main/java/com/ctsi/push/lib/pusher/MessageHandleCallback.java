package com.ctsi.push.lib.pusher;

import com.ctsi.push.lib.queue.message.PushMessage;

/**
 * Created by doulala on 16/9/1.
 */

public interface MessageHandleCallback {

    void onMessageBingo(PushMessage message);

    void onMessageFailed(PushMessage message, Throwable ex);


}
