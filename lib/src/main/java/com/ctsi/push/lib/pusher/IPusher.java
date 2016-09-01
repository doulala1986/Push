package com.ctsi.push.lib.pusher;

import java.util.Set;

/**
 * Created by doulala on 16/8/29.
 *
 * 推送服务能力接口,针对不同的推送服务会有不同的实现.
 */
public interface IPusher {

    void start();

    void stop();

    boolean isStart();

    void setAliasAndTags(String alias, Set<String> tags);


    void setCallbacks(RegisterCallback registerCallback, MessageFilter messageCallback, ConnectionCallback connectionCallback);


}
