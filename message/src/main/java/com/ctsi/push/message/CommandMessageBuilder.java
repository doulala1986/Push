package com.ctsi.push.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by doulala on 16/9/29.
 *
 * PushMessageBuilder
 */

public class CommandMessageBuilder {

	private Set<String> toAlias; // 需要响应的用户别名,用来做本地二次确认

	private Set<String> toTags;// 需要响应的标签

	private long timeExpire = 0l;// 离线状态下的有效时间

	private NoticeContent noticeContent;//

	private CommandAction commandAction;

	protected CommandMessageBuilder() {

	}

	/**
	 * @param to
	 *            用户别名,一般是系统ID、UserName、Email或者手机号
	 * @return
	 */
	public CommandMessageBuilder toAlias(String alias) {
		if (StringUtils.isEmpty(alias)) {
			return this;
		}
		if (toAlias == null)
			toAlias = new HashSet<String>();

		if (!toAlias.contains(alias)) {
			toAlias.add(alias);
		}
		return this;
	}

	public CommandMessageBuilder toAlias(Set<String> aliases) {
		if (aliases == null || aliases.size() == 0) {

			return this;
		}
		for (String alias : aliases) {
			toAlias(alias);
		}
		return this;
	}

	/**
	 * @param tag
	 *            标签
	 * @return
	 */
	public CommandMessageBuilder toTag(String tag) {
		if (StringUtils.isEmpty(tag)) {

			return this;
		}
		if (toTags == null)
			toTags = new HashSet<String>();

		if (!toTags.contains(tag)) {
			toTags.add(tag);
		}
		return this;
	}

	/**
	 * @param tag
	 *            标签
	 * @return
	 */
	public CommandMessageBuilder toTag(Set<String> tags) {
		if (tags == null || tags.size() == 0) {

			return this;
		}
		for (String tag : tags) {
			toTag(tag);
		}
		return this;
	}

	/**
	 * @param time
	 *            超时时间,单位毫秒
	 * @return
	 */
	public CommandMessageBuilder timeExpire(long time) {

		this.timeExpire = time;
		return this;
	}

	/**
	 * @param noticeContent
	 *            通知内容,主要定义在接收通知的样式
	 * @return
	 */
	protected CommandMessageBuilder noticeContent(NoticeContent noticeContent) {

		this.noticeContent = noticeContent;
		return this;
	}

	/**
	 * @param title
	 *            通知标题
	 * @param subTitle
	 *            通知内容
	 * @return
	 */
	public CommandMessageBuilder noticeContent(String title, String subTitle) {

		return noticeContent(new NoticeContent(title, subTitle));
	}

	/**
	 * @param commandAction
	 *            命令内容,主要定义在接收或者启动通知时要执行动作
	 * @return
	 */
	protected CommandMessageBuilder commandAction(CommandAction commandAction) {

		this.commandAction = commandAction;
		return this;
	}

	/**
	 * 1.COMMAND_TYPE_VIEW: topic为订阅主题,定义打开唯一界面 2.COMMAND_TYPE_URL topic为目标URL
	 * 3.COMMAND_TYPE_ACTION topic为订阅主题,定义打开唯一动作
	 *
	 * @param actionType
	 *            COMMAND_TYPE_VIEW:1,COMMAND_TYPE_URL:2,COMMAND_TYPE_ACTION:3
	 * @param topic
	 *            根据type不一样,有不同意义
	 * @param extra
	 *            附加参数
	 * @return
	 */
	public CommandMessageBuilder commandAction(int actionType, String topic, HashMap<String, String> extra) {

		return commandAction(new CommandAction(actionType, topic, extra));
	}

	public CommandMessage build() {

		if (isHaveNoAlias() && isHaveNoTags()) {

			throw new RuntimeException("需要指定推送目标:别名或者标签!");
		}
		if (noticeContent == null && commandAction == null) {

			throw new RuntimeException("需要设置noticeContent、commandAction!");
		}
		if (noticeContent != null && StringUtils.isEmpty(noticeContent.getTitle())) {

			throw new RuntimeException("notice.title不可为空");
		}
		if (commandAction != null && (commandAction.getType() != CommandAction.COMMAND_TYPE_ACTION
				&& commandAction.getType() != CommandAction.COMMAND_TYPE_URL
				&& commandAction.getType() != CommandAction.COMMAND_TYPE_VIEW)) {

			throw new RuntimeException("非法 commandAction.type!");
		}

		return new CommandMessage(timeExpire, toAlias, toTags, noticeContent, commandAction);

	}

	private boolean isHaveNoAlias() {

		return toAlias == null || toAlias.size() == 0;

	}

	private boolean isHaveNoTags() {

		return toTags == null || toTags.size() == 0;

	}

}
