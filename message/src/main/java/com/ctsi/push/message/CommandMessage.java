package com.ctsi.push.message;

import java.util.Set;

public class CommandMessage extends PushMessage {
	   
    private long expire=86400; // 过期时间.单位秒,默认1天

    private Set<String> alias; // 需要响应的用户别名,用来做本地二次确认

    private Set<String> tags;// 需要响应的标签
    
    
    protected CommandMessage(long expire, Set<String> alias, Set<String> tags, NoticeContent noticeContent, CommandAction commandAction) {
        super(noticeContent,commandAction);
    	this.expire = expire;
        this.alias = alias;
        this.tags = tags;
    }
    public boolean isHaveNoAlias() {

		return alias == null || alias.size() == 0;

	}

	public boolean isHaveNoTags() {

		return tags == null || tags.size() == 0;

	}
	public long getExpire() {
		return expire;
	}
	public void setExpire(long expire) {
		this.expire = expire;
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
	public static CommandMessageBuilder builder() {

        return new CommandMessageBuilder();
    }
	
}
