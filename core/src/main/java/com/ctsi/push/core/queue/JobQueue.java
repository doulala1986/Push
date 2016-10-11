package com.ctsi.push.core.queue;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ctsi.push.core.queue.message.MessageHandlerBus;
import com.ctsi.push.message.PushMessage;

/**
 * Created by doulala on 16/8/30.
 * <p/>
 * 制作一个消息队列
 */
public class JobQueue extends HandlerThread {


    private static final String Tag = "ctsi_push_job_queue";

    Handler handler;

    private int what_sequence = 1;

    private MessageHandlerBus bus;

    public JobQueue(MessageHandlerBus bus) {
        super(Tag);
        this.bus = bus;
    }

    public synchronized JobQueue prepare() {
        if (!this.isAlive()) {
            this.start();
            handler = new Handler(getLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (bus != null) {
                        PushMessage message = (PushMessage) msg.obj;
                        if (message != null && bus != null) {
                            bus.execute(message);
                        }
                    }
                    return true;
                }
            });
        }
        return this;
    }

    public synchronized JobQueue release() {
        if (this.isAlive()) {
            this.interrupt();
            clearJobs();
        }
        handler = null;
        return this;
    }

    public synchronized void addJob(PushMessage pushMessage) {

        if (handler != null) {
            Message msg = handler.obtainMessage();
            msg.obj = pushMessage;
            msg.what = what_sequence++;
            handler.sendMessage(msg);
        }
    }


    private void clearJobs() {

        if (handler != null) {
            while (what_sequence > 0 && handler.hasMessages(what_sequence)) {
                handler.removeMessages(what_sequence);
                what_sequence--;
            }
        }
    }


}

