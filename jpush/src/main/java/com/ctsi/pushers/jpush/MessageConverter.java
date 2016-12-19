package com.ctsi.pushers.jpush;

import android.os.Bundle;
import android.text.TextUtils;

import com.ctsi.push.message.CommandAction;
import com.ctsi.push.message.NoticeContent;
import com.ctsi.push.message.PushMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by doulala on 2016/10/11.
 */

public class MessageConverter {


    /**
     * 极光推送的消息会以Bundle的方式通过广播传递
     * 该方法用来把极光消息转换成PushMessage
     *
     * @param bundle
     * @return
     */


    protected static PushMessage converter(Bundle bundle) {

        String id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Gson gson = new Gson();
        HashMap<String, String> map = null;
        if (!TextUtils.isEmpty(extras)) {
            map = gson.fromJson(extras, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
        if (map == null)
            map = new HashMap<>();
        PushMessage pushMessage = new PushMessage();
        if (!TextUtils.isEmpty(id)) {
            pushMessage.setId(id);
        }
        if (!TextUtils.isEmpty(title)) {
            NoticeContent noticeContent = new NoticeContent(title, message);
            pushMessage.setNoticeContent(noticeContent);
        }

        if (map != null && map.size() != 0) {
            CommandAction action = new CommandAction(map);
            pushMessage.setCommandAction(action);
        }

        return pushMessage;
    }
}