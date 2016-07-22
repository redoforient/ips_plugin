package com.ips.upmp.model;

import java.io.Serializable;
/**
 * 移动无卡支付(回应)
 * @author IH847
 */
@SuppressWarnings("serial")
public class Mobile_Pay_rs implements Serializable {
	private String 	merCode;//商户号
	private String 	accCode;//交易账户号
	private String 	merBillNo;//商户订单号
	private String 	ordBillNo;//Ips订单号
	private String 	tradeBillNo;//交易流水号
	private String 	banktn;//银联流水号
	private String 	ccyCode;//币种:156(人民币)
	private String 	tranAmt;//订单金额
	private String orderDesc;//订单描述
	
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	
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
	public String getOrdBillNo() {
		return ordBillNo;
	}
	public void setOrdBillNo(String ordBillNo) {
		this.ordBillNo = ordBillNo;
	}
	public String getTradeBillNo() {
		return tradeBillNo;
	}
	public void setTradeBillNo(String tradeBillNo) {
		this.tradeBillNo = tradeBillNo;
	}
	public String getBanktn() {
		return banktn;
	}
	public void setBanktn(String banktn) {
		this.banktn = banktn;
	}
	public String getCcyCode() {
		return ccyCode;
	}
	public void setCcyCode(String ccyCode) {
		this.ccyCode = ccyCode;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

}
