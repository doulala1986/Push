package com.ctsi.push.lib.pusher;

/**
 * Created by doulala on 16/8/29.
 */
public interface RegisterCallback {


    void onRegisterSuccess(String deviceId);

    void onRegisterFailed(String message);

}
