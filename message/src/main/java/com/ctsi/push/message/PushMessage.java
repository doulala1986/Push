package com.ctsi.push.message;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by doulala on 16/9/28.
 * <p>
 * 推送服务消息
 */

public class PushMessage {

    private String id;  // Message Id


    private NoticeContent noticeContent;

    private CommandAction commandAction;

    public PushMessage() {
    }

    public PushMessage(NoticeContent noticeContent, CommandAction commandAction) {
		super();
		this.noticeContent = noticeContent;
		this.commandAction = commandAction;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public NoticeContent getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(NoticeContent noticeContent) {
        this.noticeContent = noticeContent;
    }

    public CommandAction getCommandAction() {
        return commandAction;
    }

    public void setCommandAction(CommandAction commandAction) {
        this.commandAction = commandAction;
    }



}
