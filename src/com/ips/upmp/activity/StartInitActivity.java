package com.ips.upmp.activity;

import com.alibaba.fastjson.JSON;
import com.ips.upmp.Constant;
import com.ips.upmp.R;
import com.ips.upmp.model.Mobile_Pay_rq;
import com.ips.upmp.model.Mobile_Pay_rs;
import com.ips.upmp.model.RequestMessage;
import com.ips.upmp.model.ResponseMessage;
import com.ips.upmp.nohttp.CallServer;
import com.ips.upmp.nohttp.HttpListener;
import com.ips.upmp.util.Tools;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.rest.Response;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartInitActivity extends BaseActivity {
    private RelativeLayout relativeLayout_loading;
    private TextView tv_version_name;
    private Animation animation;
    private Intent intent;
    private Bundle startExtras;
    private Mobile_Pay_rq mobilePayRequest;
    private String orderEncodeType;// 签名方式:5#采用MD5签名认证方式 ;6#MD5WithRsa的签名认证方式
    private String sign;// 签名值
    private String banktn;// 银联流水号:PBCS从银联移动支付系统获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tools.hideTitle(StartInitActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_init_logo);

        //initProgressDialog(StartInitActivity.this, "启动插件中...");
        intent = getIntent();
        if (intent != null) {
            startExtras = intent.getExtras();
            if (startExtras != null) {
                if (startExtras.getString("bankCard").length() == 0) {
                    Tools.showToast(StartInitActivity.this, "请输入银行卡号");
                    //dismissProgressDialog();
                    StartInitActivity.this.finish();
                }

                mobilePayRequest = new Mobile_Pay_rq();
                mobilePayRequest.setMerCode(startExtras.getString("merCode"));
                mobilePayRequest.setMerRequestInfo(startExtras.getString("merRequestInfo"));

                mobilePayRequest.setMobileDeviceType(Tools.getMobileDeviceModel(StartInitActivity.this));
                mobilePayRequest.setMac(Tools.getLocalMacAddress(StartInitActivity.this));
                mobilePayRequest.setClientIP(Tools.getLocalIpAddress());
                mobilePayRequest.setIMEI(Tools.getDeviceId(StartInitActivity.this));
                mobilePayRequest.setGpsCoordinate(Tools.getLocation(StartInitActivity.this));
                orderEncodeType = startExtras.getString("orderEncodeType");
                sign = startExtras.getString("sign");

                // 设置设备信息，以备返回用户使用
                startExtras.putString("mobileDeviceType", Tools.getMobileDeviceModel(StartInitActivity.this));
                startExtras.putString("mac", Tools.getLocalMacAddress(StartInitActivity.this));
                startExtras.putString("clientIP", Tools.getLocalIpAddress());
                startExtras.putString("IMEI", Tools.getDeviceId(StartInitActivity.this));
                startExtras.putString("gpsCoordinate", Tools.getLocation(StartInitActivity.this));
            }

            relativeLayout_loading = (RelativeLayout) findViewById(R.id.relativeLayout_startInitActivity_loading);
            tv_version_name = (TextView) findViewById(R.id.tv_start_init_version_name);

            String mVersionNameFormat = getResources().getString(R.string.ips_start_init_version);
            String mVersionName = String.format(mVersionNameFormat, Tools.getAppVersion(StartInitActivity.this));
            tv_version_name.setText(mVersionName);

            animation = new AlphaAnimation(0.1f, 1.0f);
            animation.setDuration(3000);
            relativeLayout_loading.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    getBanktn(orderEncodeType, sign);
                }
                public void onAnimationRepeat(Animation animation) {
                }
                public void onAnimationEnd(Animation animation) {
                }
            });
        } else {
            Tools.showToast(StartInitActivity.this, "环迅插件初始化失败");
        }
    }
    
    //获取银联流水号
    public void getBanktn(String orderEncodeType, String sign) {
        RequestMessage mRequestMessage = new RequestMessage();
        mRequestMessage.setOperationType(Constant.OperationType.移动无卡支付);
        mRequestMessage.setClientID(Tools.getClientId(StartInitActivity.this));
        mRequestMessage.setOrderEncodeType(orderEncodeType);
        mRequestMessage.setSign(sign);
        String requestParams = JSON.toJSONString(mobilePayRequest);
        mRequestMessage.setRequest(requestParams);
        String mobilePayInput = JSON.toJSONString(mRequestMessage);

        if (request != null) {
            request.add("params", mobilePayInput);
            httpListener = new UpmpHttpListener();
            // 添加到请求队列
            CallServer.getRequestInstance().add(StartInitActivity.this, 0, request, httpListener, true, true);
        }
    }
   
    //返回商户平台
    public void back2MerchantPlatform() {
        Intent backIntent = new Intent();
        Logger.e(startExtras.getParcelable("startComponent").toString());
        backIntent.setComponent((ComponentName) startExtras.getParcelable("startComponent"));
        backIntent.addCategory("android.intent.category.DEFAULT");
        backIntent.putExtras(startExtras);
        startActivity(backIntent);
        StartInitActivity.this.finish();
    }
    class UpmpHttpListener implements HttpListener<String> {

        @Override
        public void onSucceed(int what, Response<String> response) {
            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
            if (responseCode == 200) {
                ResponseMessage result = JSON.parseObject(response.get(), ResponseMessage.class);

                Logger.d(JSON.toJSON(response).toString());
                //dismissProgressDialog();
                
                if (Constant.ResultCode.操作成功.equals(result.getResultCode())) {
                    String responseJsonStr = result.getResponse();
                    if (responseJsonStr != null) {
                        Mobile_Pay_rs mobilePay = JSON.parseObject(responseJsonStr, Mobile_Pay_rs.class);
                        /*************************************************
                         * 步骤1：从网络开始,获取交易流水号即TN
                         ************************************************/
                        banktn = mobilePay.getBanktn();// 银联流水号
                        startExtras.putString("banktn", banktn);
                        startExtras.putString("merBillNo", mobilePay.getMerBillNo());// 商户订单号
                        startExtras.putString("ordBillNo", mobilePay.getOrdBillNo());// ips订单号
                        startExtras.putString("tradeBillNo", mobilePay.getTradeBillNo());// 交易流水号
                        startExtras.putString("tranAmt", mobilePay.getTranAmt());// 订单金额
                        startExtras.putString("orderDesc", mobilePay.getOrderDesc());// 订单描述

                        Logger.d("银联流水号==" + banktn);
                        intent.setComponent(new ComponentName("com.ips.upmp", "com.ips.upmp.activity.MobilePayActivity"));
                        intent.putExtras(startExtras);
                        startActivity(intent);
                        StartInitActivity.this.finish();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartInitActivity.this);
                    builder.setTitle(getText(R.string.init_failed));
                    //builder.setMessage(response.get());
                    builder.setMessage(result.getResultMsg());
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            back2MerchantPlatform();
                        }
                    });
                    builder.show();
                }
            }
        }

        /*
         * (non-Javadoc)
         * @see com.ips.upmp.nohttp.HttpListener#onFailed(int, java.lang.String,
         * java.lang.Object, java.lang.Exception, int, long)
         */
        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            showMessageDialog(R.string.request_failed, exception.getMessage());
            //dismissProgressDialog();
            Logger.i("what:" + what + "|url:" + url + "|tag:" + tag + "|responseCode:" + responseCode);
            Logger.e(exception);
        }
    };

}
