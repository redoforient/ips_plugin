package com.ips.upmp.model;
/**
 * 移动无卡支付(请求)
 * 
 * @author IH847
 *
 */
public class Mobile_Pay_rq{
	private String 	merCode;//商户号
	private String 	accCode;//交易账户号
	private String 	merBillNo;//商户订单号
	private String 	ccyCode;//币种
	private String 	prdCode;//产品类型
	private String 	tranAmt;//订单金额
	private String 	requestTime;//请求时间
	private String 	ordPerVal;//订单有效期
	private String 	merNoticeUrl;//商户自己系统后台通知URL
	private String 	mobileDeviceType;//移动设备型号
	private String 	mac;//移动设备物理地址
	private String 	clientIP;//用户IP
	private String 	IMEI;//移动设备国际身份码IMEI
	private String 	gpsCoordinate;//GPS坐标
	private String 	orderDesc;//订单描述
	//add start
	private String bankCard;//银行卡号
	private String merRequestInfo;
	private String sign;
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getMerRequestInfo() {
		return merRequestInfo;
	}
	public void setMerRequestInfo(String merRequestInfo) {
		this.merRequestInfo = merRequestInfo;
	}
	public String getbankCard() {
		return bankCard;
	}
	public void setbankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	//add end
	public String getMerCode() {
		return merCode;
	}
	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getMerBillNo() {
		return merBillNo;
	}
	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}
	public String getCcyCode() {
		return ccyCode;
	}
	public void setCcyCode(String ccyCode) {
		this.ccyCode = ccyCode;
	}
	public String getPrdCode() {
		return prdCode;
	}
	public void setPrdCode(String prdCode) {
		this.prdCode = prdCode;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getOrdPerVal() {
		return ordPerVal;
	}
	public void setOrdPerVal(String ordPerVal) {
		this.ordPerVal = ordPerVal;
	}
	public String getMerNoticeUrl() {
		return merNoticeUrl;
	}
	public void setMerNoticeUrl(String merNoticeUrl) {
		this.merNoticeUrl = merNoticeUrl;
	}
	public String getMobileDeviceType() {
		return mobileDeviceType;
	}
	public void setMobileDeviceType(String mobileDeviceType) {
		this.mobileDeviceType = mobileDeviceType;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getGpsCoordinate() {
		return gpsCoordinate;
	}
	public void setGpsCoordinate(String gpsCoordinate) {
		this.gpsCoordinate = gpsCoordinate;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

}
