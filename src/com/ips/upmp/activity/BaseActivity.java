/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ips.upmp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ips.upmp.Constant;
import com.ips.upmp.R;
import com.ips.upmp.dialog.IPSDialog;
import com.ips.upmp.dialog.WebDialog;
import com.ips.upmp.nohttp.CallServer;
import com.ips.upmp.nohttp.HttpListener;
import com.ips.upmp.util.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;
import com.yolanda.nohttp.tools.HeaderUtil;

/**
 * Created in 2016/5/8 18:19.
 *
 * @author Yan Zhenjie.
 */
public abstract class BaseActivity extends Activity {
    private Dialog dialog;
//    private boolean cancelable;
    protected Request<String> request = null;
    protected HttpListener<String> httpListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request = NoHttp.createStringRequest(Constant.SERVER_URL, RequestMethod.POST);
    }

//    protected void initProgressDialog(Context context,String message) {
//        if (dialog == null) {
//            dialog = IPSDialog.show(context, message);
//
//            dialog.setCancelable(cancelable);
//
//            if (cancelable) {
//                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        Logger.e("取消任务进度....");
//                    }
//                });
//            }
//
//            if (!dialog.isShowing()) {
//                dialog.show();
//            }
//        }
//    }
//
//    protected void dismissProgressDialog() {
//        if (dialog != null) {
//            dialog.dismiss();
//            dialog = null;
//        }
//    }

    /**
     * 显示一个WebDialog。
     *
     * @param response
     *            响应。
     */
    public void showWebDialog(Response<?> response) {
        String result = StringRequest.parseResponseString(response.getHeaders(), response.getByteArray());
        WebDialog webDialog = new WebDialog(this);
        String contentType = response.getHeaders().getContentType();
        webDialog.loadUrl(result, contentType, HeaderUtil.parseHeadValue(contentType, "charset", "utf-8"));
        webDialog.show();
    }

    /**
     * Show message dialog.
     *
     * @param title
     *            title.
     * @param message
     *            message.
     */
    public void showMessageDialog(int title, int message) {
        showMessageDialog(getText(title), getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title
     *            title.
     * @param message
     *            message.
     */
    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(getText(title), message);
    }

    /**
     * Show message dialog.
     *
     * @param title
     *            title.
     * @param message
     *            message.
     */
    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title
     *            title.
     * @param message
     *            message.
     */
    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(this, what, request, callback, canCancel, isLoading);
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(this);
        super.onDestroy();
    }
}
