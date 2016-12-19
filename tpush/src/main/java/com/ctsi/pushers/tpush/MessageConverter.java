package com.ctsi.pushers.tpush;

import android.os.Bundle;
import android.text.TextUtils;

import com.ctsi.push.message.CommandAction;
import com.ctsi.push.message.NoticeContent;
import com.ctsi.push.message.PushMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.android.tpush.XGPushTextMessage;
import java.util.HashMap;

/**
 * Created by doulala on 2016/10/11.
 */

public class MessageConverter {


    /**
     * 把XGPushTextMessage转换成PushMessage
     * 该方法用来把极光消息转换成PushMessage
     *
     * @param messgage
     * @return
     */


    protected static PushMessage converter(XGPushTextMessage messgage) {

        String title = messgage.getTitle();
        String message = messgage.getContent();
        String extras = messgage.getCustomContent();

        Gson gson = new Gson();
        HashMap<String, String> map = null;
        if (!TextUtils.isEmpty(extras)) {
            map = gson.fromJson(extras, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
        if (map == null)
            map = new HashMap<>();
        PushMessage pushMessage = new PushMessage();
//        if (!TextUtils.isEmpty(id)) {
//            pushMessage.setId(id);
//        }
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