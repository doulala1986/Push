package com.ctsi.push.lib;

import com.ctsi.push.lib.queue.JobQueue;
import com.ctsi.push.lib.pusher.MessageFilter;
import com.ctsi.push.message.PushMessage;

/**
 * Created by doulala on 16/8/29.
 * <p/>
 * 1.接收消息
 * 2.增加至消息队列（期间可以考虑持久化存储）.
 */
public class MessageDispatcher implements MessageFilter {


    MessageFilter messageFilter;

    JobQueue jobQueue;

    protected MessageDispatcher(JobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }


    protected void setMessageCallback(MessageFilter messageFilter) {
        this.messageFilter = messageFilter;
    }

    @Override
    public boolean onReceivedMessage(PushMessage pushMessage) {
        if (this.messageFilter == null || !this.messageFilter.onReceivedMessage(pushMessage)) {
            addToJobQueue(pushMessage);
        }
        return true;
    }

    /**
     * 启动dispatcher
     */
    protected void prepare() {
        if (this.jobQueue != null)
            this.jobQueue.prepare();
    }

    protected void addToJobQueue(PushMessage pushMessage) {

        if (jobQueue != null)
            jobQueue.addJob(pushMessage);
    }

    protected void clearJobQueue() {
        if (jobQueue != null)
            jobQueue.release();
    }

}
