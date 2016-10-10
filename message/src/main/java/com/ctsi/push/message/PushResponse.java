package com.ctsi.push.message;

public class PushResponse {

	public static final int ERROR_CODE_NONE = -1;
	public static final int ERROR_CODE_OK = 0;

	private int response_code;
	private String message;
	private String msg_id;

	public PushResponse(String msg_id, int response_code, String message) {
		super();
		this.response_code = response_code;
		this.message = message;
		this.msg_id = msg_id;
	}

	
	public int getResponse_code() {
		return response_code;
	}


	public void setResponse_code(int response_code) {
		this.response_code = response_code;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

}
