package com.ctsi.push.core.pusher;

import java.util.Set;

/**
 * Created by doulala on 16/8/29.
 * <p/>
 * 推送服务能力接口,针对不同的推送服务会有不同的实现.
 */
public interface IPusher {

    void start(String alias, Set<String> tags);

    void stop();

    boolean isStarted();

    String getAlias();

    String getDeviceId();

    Set<String> getTags();

    void setCallbacks(RegisterCallback registerCallback, MessageFilter messageCallback, ConnectionCallback connectionCallback);








}
