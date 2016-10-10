package com.ctsi.push.message;

import java.util.HashMap;

/**
 * Created by doulala on 16/9/28.
 * <p>
 * 命令定义,主要用来描述客户端行为
 */

public class CommandAction {

	private static final String KEY_INEXTRA_TYPE = "command_type";
	private static final String KEY_INEXTRA_TOPIC = "command_topic";

	public static final int COMMAND_TYPE_VIEW = 1;

	public static final int COMMAND_TYPE_URL = 2;

	public static final int COMMAND_TYPE_ACTION = 3;

	private int type; // 命令类型,包括:View(视图)、URL(超链接访问)、ACTION(动作)

	private String topic;// 如果 type=COMMAND_TYPE_URL, 该字段显示url,否则显示模块、动作。

	private HashMap<String, String> extra;

	public CommandAction(int type, String topic, HashMap<String, String> extra) {
		this.type = type;
		this.topic = topic;
		this.extra = extra;
	}

	public CommandAction(HashMap<String, String> extra) {
		this.type = Integer.valueOf(extra.remove(KEY_INEXTRA_TYPE));
		this.topic = extra.remove(KEY_INEXTRA_TOPIC);
		this.extra = extra;

	}

	public CommandAction(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashMap<String, String> getExtra() {
		return extra;
	}

	public void setExtra(HashMap<String, String> extra) {
		this.extra = extra;
	}

	public HashMap<String, String> toExtra() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(KEY_INEXTRA_TYPE, String.valueOf(type));
		map.put(KEY_INEXTRA_TOPIC, topic);
		copyMap(extra, map);
		return map;
	}

	private void copyMap(HashMap<String, String> source, HashMap<String, String> to) {

		if (source != null && !source.isEmpty())
			for (String key : source.keySet()) {
				if (!to.containsKey(key)) {
					to.put(key, source.get(key));
				}
			}
	}

}
