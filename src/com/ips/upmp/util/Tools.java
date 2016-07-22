package com.ips.upmp.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class Tools {
	static ProgressDialog progressDialog;

	/**
	 * @param activity
	 * @return
	 */
	public static String getCachePath(Activity activity) {
		File fileDir = activity.getCacheDir();
		return fileDir.getParent();
	}

	/**
	 * @param activity
	 * @return
	 */
	public static String getLocalPath(Activity activity) {
		File fileDir = activity.getFilesDir();
		return fileDir.getParent();
	}

	/**
	 * @param activity
	 * @param phoneNum
	 */
	public static void sms(Activity activity, String phoneNum) {
		Uri uri = Uri.parse("smsto:" + phoneNum);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		// it.putExtra("sms_body", "102");
		activity.startActivity(it);
	}

	/**
	 * @param activity
	 * @param phoneNum
	 */
	public static void call(Activity activity, String phoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNum));

		activity.startActivity(intent);
	}

	/**
	 * 关闭程序
	 * @param activity
	 */
	public static void restartPackage(Activity activity) {
		activity.finish();
		System.exit(0);
	}

	/**
	 * 获取当前的设备编号(移动设备国际身份码IMEI：GSM手机的 IMEI和 CDMA手机的 MEID.)
	 * @param context
	 * @return Return null if device ID is not available.
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String deviceid = tm.getDeviceId();
		//String tel = tm.getLine1Number();
		//String imei = tm.getSimSerialNumber();//IMEI国际移动设备识别码
		//String imsi = tm.getSubscriberId();//IMSI国际移动用户识别码
		return deviceid;
	}
	
	
	public static String getLocation(Context context){
		double longitude =0.0;
		double latitude=0.0;  
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
		        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
		            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
		            if(location != null){  
		                latitude = location.getLatitude();  
		                longitude = location.getLongitude();
		                System.out.println("longitude:"+longitude+" latitude:"+latitude);
		                }  
		        }else{  
		            LocationListener locationListener = new LocationListener() {  
		                  
		                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
		                @Override  
		                public void onStatusChanged(String provider, int status, Bundle extras) {  
		                	Log.v("Location", "onStatusChanged:provider="+provider+" status="+status);
		                	if(extras!=null){
		                		for(String key:extras.keySet()){
		                			System.out.println(key+"="+extras.getString(key));
		                		}
		                	}
		                }  
		                  
		                // Provider被enable时触发此函数，比如GPS被打开  
		                @Override  
		                public void onProviderEnabled(String provider) {  
		                	Log.i("Location", "onProviderEnabled:provider="+provider);
		                }  
		                
		                // Provider被disable时触发此函数，比如GPS被关闭   
		                @Override  
		                public void onProviderDisabled(String provider) {  
		                      Log.v("","onLocationChanged");
		                }  
		                  
		                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
		                @Override  
		                public void onLocationChanged(Location location) {  
		                    if (location != null) {     
		                        Log.i("Location", "Location changed : Lat: "    
		                        + location.getLatitude() + " Lng: "    
		                        + location.getLongitude());
		                    }
		                }
		            };
		            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
		            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
		            if(location != null){     
		                latitude = location.getLatitude(); //经度     
		                longitude = location.getLongitude(); //纬度  
		                System.out.println("longitude:"+longitude+" latitude:"+latitude);
		            }
		        }
				return longitude+","+latitude;
	}
	
	/**
	 * 隐藏标题
	 * 
	 * @param activity
	 */
	public static void hideTitle(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 全屏
	 * 
	 * @param activity
	 */
	public static void fullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * @param activity
	 * @param content
	 */
	public static void showToast(Activity activity, String content) {
		Toast.makeText(activity, content, Toast.LENGTH_LONG).show();
	}

	/**
	 * @param activity
	 * @param content
	 */
	public static void showToast(Activity activity, int resId) {
		Toast.makeText(activity, activity.getString(resId), Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showProgressDialog(Context context, String title,
			String msg) {

		if (progressDialog != null && progressDialog.isShowing() == true) {
			return;
		}
		progressDialog = ProgressDialog.show(context, title, msg, true, true);
	}
	
	
	/**
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showProgressDialog(Context context, int titleId,
			int msgId) {

		showProgressDialog(context, context.getString(titleId), context
				.getString(msgId));
	}

	/**
	 * 
	 */
	public static void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

//	/**
//	 * 显示基本的AlertDialog
//	 * 
//	 * @param context
//	 * @param content
//	 * @param title
//	 */
//	public static void showDialog(final Context context, String content,
//			String title) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		// builder.setIcon(R.drawable.icon);
//		builder.setTitle(title);
//		builder.setMessage(content);
//		builder.setPositiveButton(context.getString(R.string.btn_confirm),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						// dialog.dismiss();
//						if (Global.NeedUpdate == 1) {
//							Utils.startActivity(context, LoginActivity.class);
//						} else if (Global.NeedUpdate == 2) {
//							Intent startMain = new Intent(Intent.ACTION_MAIN);
//							startMain.addCategory(Intent.CATEGORY_HOME);
//							startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							context.startActivity(startMain);
//						}
//
//					}
//				});
//		builder.show();
//	}
//
//	public static void showDialogAgain(Context context, String content,
//			String title) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		// builder.setIcon(R.drawable.icon);
//		builder.setTitle(title);
//		builder.setMessage(content);
//		builder.setPositiveButton(context.getString(R.string.btn_close),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//					}
//				});
//		builder.show();
//	}
//
//	/**
//	 * 显示基本的AlertDialog
//	 * 
//	 * @param context
//	 * @param content
//	 * @param title
//	 */
//	public static void showDialog(Context context, int contentId) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		// builder.setIcon(R.drawable.icon);
//		builder.setTitle(context.getString(R.string.prompt));
//		builder.setMessage(context.getString(contentId));
//		builder.setPositiveButton(context.getString(R.string.btn_confirm),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//					}
//				});
//		builder.show();
//	}
//
//	/**
//	 * 显示基本的AlertDialog
//	 * 
//	 * @param context
//	 * @param content
//	 * @param title
//	 */
//	public static void showDialog(Context context, String content,
//			String title, final Runnable runable) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		// builder.setIcon(R.drawable.icon);
//		builder.setTitle(title);
//		builder.setMessage(content);
//		builder.setPositiveButton(context.getString(R.string.btn_confirm),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						runable.run();
//					}
//				});
//		builder.show();
//	}

//	/**
//	 * 显示基本的AlertDialog
//	 * 
//	 * @param context
//	 * @param content
//	 * @param title
//	 */
//	public static void showDialog(Context context, int contentId, int titleId,
//			final Runnable raConfirm, final Runnable raCancel) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		// builder.setIcon(R.drawable.icon);
//		builder.setTitle(context.getString(titleId));
//		builder.setMessage(context.getString(contentId));
//		builder.setPositiveButton(context.getString(R.string.btn_confirm),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						if (raConfirm != null) {
//							raConfirm.run();
//						}
//					}
//				});
//
//		builder.setNegativeButton(context.getString(R.string.btn_cancel),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						if (raCancel != null) {
//							raCancel.run();
//						}
//					}
//				});
//		builder.show();
//	}

//	/**
//	 * 显示基本的AlertDialog
//	 * 
//	 * @param context
//	 * @param content
//	 * @param title
//	 */
//	public static void showDialog(Context context, int contentId, int titleId,
//			final Runnable raConfirm) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		// builder.setIcon(R.drawable.icon);
//		builder.setTitle(context.getString(titleId));
//		builder.setMessage(context.getString(contentId));
//		builder.setPositiveButton(context.getString(R.string.btn_confirm),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						if (raConfirm != null) {
//							raConfirm.run();
//						}
//					}
//				});
//		builder.show();
//	}


	/**
	 * @param activity
	 * @param name
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Activity activity,
			String name) {
		return activity.getSharedPreferences(name, 0);
	}

	/**
	 * @param activity
	 */
	public static void error(final Activity activity) {
		// Utils.error(activity, R.string.APP_ERROR);
	}

//	/**
//	 * @param activity
//	 * @param msg
//	 */
//	public static void error(final Activity activity, String msg) {
//		Utils.showDialog(activity, msg, activity.getString(R.string.prompt),
//				new Runnable() {
//
//					public void run() {
//						Utils.startActivity(activity, LoginActivity.class);
//						activity.finish();
//					}
//				});
//	}

//	/**
//	 * @param activity
//	 * @param msg
//	 */
//	public static void error(final Activity activity, int resId) {
//		error(activity, activity.getString(resId));
//	}

	public static void killProcess() {
		// android.os.Process.
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void checkNetworkInfo(final Activity activity) {
		ConnectivityManager conMan = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// txt3G.setText(mobile.toString());
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// txtWifi.setText(wifi.toString());
		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return;

		// 进入无线网络配置界面: Wi-Fi,Bluetooth and Mobile networks
		activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		
		// 进入手机中的wifi网络设置界面 }
		//activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		
	}

	// 正则表达式数字验证֤
	public static boolean isNumber(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("[0-9]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	//是字母与数字组合
	public static boolean isNumberAndStr(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^(((\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+))[0-9a-zA-Z]*)$");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	// 获取ip
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {

				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		    ex.printStackTrace();
		}
		return null;
	}

	//获取移动设备物理地址
	public static String getLocalMacAddress(Context context) { 
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
        WifiInfo info = wifi.getConnectionInfo(); 
        return info.getMacAddress(); 
   }  
	
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

//	public static boolean isEmail(String email) {
//		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
//		Pattern p = Pattern.compile(str);
//		Matcher m = p.matcher(email);
//		return m.matches();
//	}
	
    public static boolean isEmail(String email) {
        boolean isEmail = false;
        if(email!=null){
            isEmail = email.contains("@");
        }
        return isEmail;
    }
	

	public static boolean isGPS(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean GPS_status = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return GPS_status;
	}

	public static boolean isNetworkAvailable(Context ctx) {
		Context context = ctx.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	public static boolean isRecordAudioAvailable(Context ctx) {
		Context context = ctx.getApplicationContext();
		AudioManager  audioManager = (AudioManager ) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager == null) {
			return false;
		} else {
//			NetworkInfo[] info = audioManager.getMode();
//			if (info != null) {
//				for (int i = 0; i < info.length; i++) {
//					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//						return true;
//					}
//				}
//			}
		}
		return false;
	}

//	public static void retryLogin(String message, final Context content) {
//		AlertDialog.Builder builder = new Builder(content);
//		builder.setMessage(message);
//		builder.setTitle(message);
//		builder.setPositiveButton("重新登录",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						Utils.startActivity(content, LoginActivity.class);
//					}
//				});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}

//	public static void reLoginForce(String message, final Context content) {//强制重新登陆
//		AlertDialog.Builder builder = new Builder(content);
//		builder.setMessage(message);
//		builder.setTitle(message);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				Utils.startActivity(content, LoginActivity.class);
//			}
//		});
//		builder.create().show();
//	}
	
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		String ly_time = sdf.format(new java.util.Date());
		return ly_time;
	}

	//将字符串转换成日期
	public static Date StringToDate(String dateStr,String formatStr){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		DateFormat dd=new SimpleDateFormat(formatStr,Locale.getDefault());
		Date date=null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	// 计算日期差
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	//开始日期与结束日期是否为间隔3个月
    public static boolean isRange3Months(String stdate, String endate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(endate));
        cal.add(Calendar.MONTH, -3);
        Date start = cal.getTime();//获取到结束日期前三个月时的日期
        Date date =sdf.parse(stdate);
        return start.after(date);
    }
    // 计算月份差
    public static int monthsBetween(String startDate, String endDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Calendar stCal = Calendar.getInstance();
        Calendar edCal = Calendar.getInstance();
        stCal.setTime(sdf.parse(startDate));
        edCal.setTime(sdf.parse(endDate));
        int result = edCal.get(Calendar.MONTH) - stCal.get(Calendar.MONTH);
        return result;
    }

//	public static String md5One(String s) {
//		MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//		md.update(s.getBytes());
//		return ByteUtils.byteArray2HexString(md.digest());
//	}

	public static String getMD5(String str, String encoding) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(encoding));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			sb.append(Integer.toHexString(val));
		}
		return sb.toString();
	}

	public static String getDigest(String content, String key, String algorithm) {
		try {
			byte[] plainText = content.getBytes("utf-8");
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(plainText);
			byte[] digest = messageDigest.digest(key.getBytes("utf-8"));
			return byte2hex(digest);
		} catch (NoSuchAlgorithmException ex) {
			Log.e("Utils.getDigest Error digest algorithm: " + algorithm,ex.getMessage(),ex);
		} catch (UnsupportedEncodingException e) {
		    Log.e("Utils.getDigest Error digest algorithm: " + algorithm,e.getMessage(),e);
		}
		return null;
	}

	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0" + stmp);
			} else {
				hs.append(stmp);
			}
		}
		return hs.toString();
	}
	public static int getLength(String amt){
		int len=amt.length()-amt.indexOf(".")+1;
		return len;
	}
	
	public static String getAmt(String amt){
		String s="";
		int len=getLength(amt);
		if(len<=2){
			s=amt;
		}else{
			String strAmt="";
			strAmt=String.format(Locale.getDefault(),"%.2f", Double.valueOf(amt));
			if(Double.valueOf(strAmt)<Double.valueOf(amt)){
				s=String.format(Locale.getDefault(),"%.2f", Double.valueOf(strAmt)+0.01);
			}else{
				s=strAmt;
			}
		}
		return s;
	}
	
	
	public static void closeSoftInputFromWindow(Context context,EditText et){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0); //关闭软键盘
	}
	
	
	/**
     * true: null or empty; false: not empty
    */
    public static boolean checkNullOrEmpty(String param) {
        if (param == null || param.trim().length() == 0) {
            return true;
        }
        return false;
    }
    
    //是否为有效手机号码
    public static boolean isValidPhone(String phone) {
       return phone.matches("1\\d{10}");
    }
    
    //是否为11位数字的手机号码
    public static boolean isValidPhone2(String phone) {
        return phone.matches("\\d{11}");
     }
   
    
    //是否为有效电子邮箱
    public static boolean isValidEmail(String email) {
      //return email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
      //String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
      String str="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
      Pattern p = Pattern.compile(str);
      Matcher m = p.matcher(email);
      return m.matches();
    }
    
    //是否为有效固定电话号码
    public static boolean isValidTelephone(String telephone) {
        //return telephone.matches("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}");
        return telephone.matches("^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$");
    }
    
    //姓名规则：只能是数字、字母和汉字且不能大于15位
    public static boolean isValidPersonName(String name){
    	return name.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$");
    }
    
    //获取Android系统的SDK版本号
    public static int getAndroidOSVersion()
    {
     	 int osVersion;
     	 try{
     		osVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
     	 }catch (NumberFormatException e)
     	 {
     		osVersion = 0;
     	 }
     	 return osVersion;
    }

	// 获取当前设备系统名与版本号
	public static String getSystemNo() {
		// logger.e(android.os.Build.MODEL);
		// logger.e(android.os.Build.VERSION.SDK);
		// logger.e(android.os.Build.VERSION.RELEASE);
		// logger.w(android.os.Build.VERSION_CODES.LOLLIPOP);
		// logger.v("currentapiVersion:" + android.os.Build.VERSION.SDK_INT);
		return "Android" + android.os.Build.VERSION.RELEASE;
	}
	
	
	//获取移动设备型号
    public static String getMobileDeviceModel(Context context) {
            String mobileDeviceModel = android.os.Build.MODEL;// 手机型号;
            return mobileDeviceModel;
    }
	
    
	
     //获取app版本信息
     public static String getAppVersion(Context context){
    	String versionName = "";
        PackageInfo pkg;
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            String appName = pkg.applicationInfo.loadLabel(context.getPackageManager()).toString(); 
            versionName = pkg.versionName; 
            System.out.println("appName:" + appName);
            System.out.println("versionName:" + versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
     }
     
     
     
     

 	/**
 	 * 获得客户设备标识
 	 * 
 	 * @param context 客户设备标识
 	 * @return 客户设备标识
 	 */
 	public static String getClientId(Context context) {
 		return PhoneManager.getSubscriberIMSI(context) + "|"
 				+ PhoneManager.getDeviceIMEI(context);
 	}

 	/**
 	 * 获取EditText的值
 	 * 
 	 * @param editText
 	 * @return
 	 */
 	public static String getEditTextValue(EditText editText) {
 		String value = editText.getText().toString();
 		if (!TextUtils.isEmpty(value))
 			return value;
 		return "";
 	}

 	/**
 	 * 获取TextView的值
 	 * 
 	 * @param textView
 	 * @return
 	 */
 	public static String getTextViewValue(TextView textView) {
 		String value = textView.getText().toString();
 		if (!TextUtils.isEmpty(value))
 			return value;
 		return "";
 	}
 	
 	
 	/**
 	 * 判断当前应用程序处于前台还是后台
 	 * @param context
 	 * @return
 	 */
 	public static boolean isBackground(Context context) {
 		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
 		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
 		for (RunningAppProcessInfo appProcess : appProcesses) {
 			if (appProcess.processName.equals(context.getPackageName())) {
 				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
 					Log.i("后台", appProcess.processName);
 					return true;
 				} else {
 					Log.i("前台", appProcess.processName);
 					return false;
 				}
 			}
 		}
 		return false;
 	}
 	
 	/** 
 	 * 显示Toast
 	 * @param activity
 	 * @param content
 	 */
 	public static void showToast(Context context, String content) {
 		if(!isBackground(context)){
 			Toast.makeText(context, content, Toast.LENGTH_LONG).show();
 		}
 	}
 	
 	
 	/**
 	 * @param context
 	 * @param clazz
 	 */
 	public static void startActivity(Context context, Class<?> clazz) {
 		Intent intent = new Intent();
 		intent.setClass(context, clazz);
 		context.startActivity(intent);

 	}

 	/**
 	 * @param context
 	 * @param clazz
 	 * @param bundle
 	 */
 	public static void startActivity(Context context, Class<?> clazz, Bundle bundle) {
 		Intent intent = new Intent();
 		intent.setClass(context, clazz);

 		if (bundle != null) {
 			intent.putExtras(bundle);
 		}

 		context.startActivity(intent);
 		// ((Activity) context).finish();
 	}

 	/**
 	 * @param activity
 	 * @param clazz
 	 * @param bundle
 	 * @param requestCode
 	 */
 	public static void startActivityForResult(Activity activity, Class<?> clazz, Bundle bundle, int requestCode) {
 		Intent intent = new Intent();
 		intent.setClass(activity, clazz);

 		if (bundle != null) {
 			intent.putExtras(bundle);
 		}

 		activity.startActivityForResult(intent, requestCode);
 	}

 	/**
 	 * @param activity
 	 * @param clazz
 	 * @param requestCode
 	 */
 	public static void startActivityForResult(Activity activity, Class<?> clazz, int requestCode) {
 		startActivityForResult(activity, clazz, null, requestCode);
 	}
}
