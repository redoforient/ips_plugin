package com.ips.upmp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ips.upmp.Constant;
import com.ips.upmp.ExitApp;
import com.ips.upmp.R;
import com.ips.upmp.util.Tools;
import com.unionpay.UPPayAssistEx;
import com.yolanda.nohttp.Logger;

/**
 * 移动无卡支付
 * 
 * @author IH847
 *
 */
public class MobilePayActivity extends BaseActivity implements OnClickListener {
	TextView tv_center;
	TextView tv_merCode;
	TextView tv_orderCode;
	TextView tv_orderDesc;
	TextView tv_tranAmt;
	Button btnMobilePaySubmit;
	
	private Bundle startExtras;
	private String banktn;// 银联流水号:PBCS从银联移动支付系统获取

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    Tools.hideTitle(MobilePayActivity.this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_pay);
		tv_center=(TextView) findViewById(R.id.textView_center);
		tv_merCode=(TextView) findViewById(R.id.textView_mobile_pay_merCode);
		tv_orderCode=(TextView) findViewById(R.id.textView_mobile_orderCode);
		tv_orderDesc=(TextView) findViewById(R.id.tv_mobile_pay_orderDesc);
		tv_tranAmt=(TextView) findViewById(R.id.tv_mobile_pay_tranAmt);
		btnMobilePaySubmit=(Button) findViewById(R.id.btn_mobile_pay_submit);
		startExtras = getIntent().getExtras();
		btnMobilePaySubmit.setOnClickListener(this);
		if (startExtras != null) {
			tv_center.setText("环迅支付");
			//商户号
			tv_merCode.setText(startExtras.getString("merCode"));
			tv_orderCode.setText(startExtras.getString("merBillNo"));
			tv_orderDesc.setText(startExtras.getString("orderDesc"));
			tv_tranAmt.setText(startExtras.getString("tranAmt"));
			banktn = startExtras.getString("banktn");//获得流水号
		}
	}
	
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mobile_pay_submit:
			if (Tools.checkNullOrEmpty(banktn)) {
				Tools.showToast(MobilePayActivity.this, "ˋ︿ˊ获取银联订单号失败！"+banktn);
			} else {
				Tools.showToast(MobilePayActivity.this, "^_^获取银联订单号成功!\n" + banktn);
				/*************************************************
				 * 步骤2：通过银联工具类启动支付插件
				 ************************************************/
				int ret = UPPayAssistEx.startPay(MobilePayActivity.this, null, null, banktn, Constant.mMode);
				Logger.e(unionPluginStatus(ret));
			}

			break;
		}
	}

	//银联支付组件状态
	private String unionPluginStatus(int ret){
	    switch(ret){
	    case 0:
	        return "UNION PLUGIN VALID";
        case -1:
            return "PLUGIN NOT INSTALLED";
        case 2:
            return "PLUGIN NEED UPGRADE";
        default:
            return "UNKNOW PLUGIN STATUS";
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";

		String payResult = data.getExtras().getString("pay_result");
		// 支付控件返回字符串:
		// fail、success、cancel 分别代表支付失败、支付成功、支付取消
		if (payResult.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
			startExtras.putString("pStatus", "9");
		} else if (payResult.equalsIgnoreCase("success")) {
		 // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verify(dataOrg, sign, Constant.mMode);
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";
            }
		    
		    
			msg = "支付成功！";
			startExtras.putString("pStatus", "10");
		} else if (payResult.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
			startExtras.putString("pStatus", "11");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("支付结果通知");
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		// builder.setCustomTitle();
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Utils.startActivity(MobilePayActivity.this, ResultActivity.class, startExtras);
				back2MerchantPlatform();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }
    
	
	public void back2MerchantPlatform() {//返回商户平台
		Intent backIntent = new Intent();
		Logger.e(startExtras.getParcelable("startComponent").toString());
		backIntent.setComponent((ComponentName) startExtras.getParcelable("startComponent"));
		backIntent.addCategory("android.intent.category.DEFAULT");
		
		Bundle backExtras = new Bundle();
		backExtras.putString("pStatus", startExtras.getString("pStatus"));
		backExtras.putString("merCode", startExtras.getString("merCode"));
		//backExtras.putString("accCode", startExtras.getString("accCode"));
		backExtras.putString("merBillNo", startExtras.getString("merBillNo"));
		//backExtras.putString("ordBillNo", startExtras.getString("ordBillNo"));
		//backExtras.putString("tradeBillNo", startExtras.getString("tradeBillNo"));
		//backExtras.putString("banktn", startExtras.getString("banktn"));
		//backExtras.putString("ccyCode", startExtras.getString("ccyCode"));
		backExtras.putString("tranAmt", startExtras.getString("tranAmt"));
		
		backIntent.putExtras(backExtras);
		//backIntent.putExtras(startExtras);
		startActivity(backIntent);
		
		MobilePayActivity.this.finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MobilePayActivity.this.finish();
			return false;
		}
		return false;
	}

	   /* (non-Javadoc)
     * @see com.ips.upmp.activity.BaseActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExitApp.getInstance().exit();
    }
}
