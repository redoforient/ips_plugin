package com.ips.upmp.model;

import java.io.Serializable;

/**
 * 响应报文对象封装
 * @author IH847
 */
@SuppressWarnings("serial")
public class ResponseMessage implements Serializable {
	private String resultCode;//状态码
	private String resultMsg;//状态描述
	private String sign;//自定义数据区签名
	private String response;//自定义数据区

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
