package com.ips.upmp.model;

/**
 * 请求报文对象封装
 * @author IH847
 *
 */
public class RequestMessage{
	private String operationType;//操作类型
	private String clientID;//客户设备标识
	private String orderEncodeType;//签名方式:5#采用MD5签名认证方式  
	private String sign;//签名
	private String request;//自定义数据区
	
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	public String getOrderEncodeType() {
		return orderEncodeType;
	}

	public void setOrderEncodeType(String orderEncodeType) {
		this.orderEncodeType = orderEncodeType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
