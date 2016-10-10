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
   
    private long expire=86400; // 过期时间.单位秒,默认1天

    private Set<String> alias; // 需要响应的用户别名,用来做本地二次确认

    private Set<String> tags;// 需要响应的标签

    private NoticeContent noticeContent;

    private CommandAction commandAction;

    protected PushMessage(long expire, Set<String> alias, Set<String> tags, NoticeContent noticeContent, CommandAction commandAction) {
        this.expire = expire;
        this.alias = alias;
        this.tags = tags;
        this.noticeContent = noticeContent;
        this.commandAction = commandAction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
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
	public Set<String> getAlias() {
		return alias;
	}

	public void setAlias(Set<String> alias) {
		this.alias = alias;
	}



	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public static PushMessageBuilder builder() {

        return new PushMessageBuilder();
    }
	
	public boolean isHaveNoAlias() {

		return alias == null || alias.size() == 0;

	}

	public boolean isHaveNoTags() {

		return tags == null || tags.size() == 0;

	}

}
