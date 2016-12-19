package com.ctsi.push.core;


import com.ctsi.push.core.pusher.IPusher;

import java.util.Set;

/**
 * Created by doulala on 16/8/30.
 */
public class PushInterface {
    private IPusher pusher;
    private MessageDispatcher dispatcher;

    protected PushInterface(IPusher pusher, MessageDispatcher dispatcher) {
        this.pusher = pusher;
        this.dispatcher = dispatcher;
    }

    public synchronized void start(String alias, Set<String> tags) {
        if (!pusher.isStarted()) {
            this.dispatcher.prepare();
            pusher.start(alias, tags);
        }
    }

    public synchronized void stop() {
        if (pusher.isStarted()) {
            this.dispatcher.clearJobQueue();
            pusher.stop();
        }
    }

    public boolean isStarted() {
        return this.pusher.isStarted();
    }



}
