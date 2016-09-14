package com.ctsi.push.lib.queue.message;

import java.util.Date;

/**
 * Created by doulala on 16/8/30.
 */
public class PushMessage {

    private String id;//用于持久化
    private String message;
    private String extras;
    private long time;


    public PushMessage(String id, String message, String extras, long time) {
        this.id = id;
        this.message = message;
        this.extras = extras;
        this.time = time;
    }

    public PushMessage(String id, String message, String extras) {
        this(id, message, extras, new Date().getTime());


    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n")
                .append(" id:").append(getId()).append(",\n")
                .append(" message:").append(getMessage()).append(",\n")
                .append(" extras:").append(getExtras()).append(",\n")
                .append(" time:").append(getTime()).append("\n")
                .append("}");
        return sb.toString();
    }
}
