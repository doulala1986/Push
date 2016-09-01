package com.ctsi.push.lib;

import com.ctsi.push.lib.pusher.IPusher;

import java.util.Set;

/**
 * Created by doulala on 16/8/30.
 */
public class PushService {
    private IPusher pusher;
    private MessageDispatcher dispatcher;

    protected PushService(IPusher pusher, MessageDispatcher dispatcher) {
        this.pusher = pusher;
        this.dispatcher = dispatcher;
    }

    public synchronized void start() {
        if (!pusher.isStarted()) {
            this.dispatcher.prepare();
            pusher.start();
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

    public void setAliasAndTags(String alias, Set<String> tags) {
        this.dispatcher.clearJobQueue();
        pusher.setAliasAndTags(alias, tags);
    }


}
