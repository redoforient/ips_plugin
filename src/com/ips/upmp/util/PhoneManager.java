package com.ips.upmp.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 手机状态工具 Created by sunnybear on 2015/3/6.
 */
public class PhoneManager {

	private PhoneManager() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断当前应用程序是否后台运行 在android5.0以上失效！请使用isApplicationBackground()方法代替！
	 *
	 * @param context
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@Deprecated
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					// Logger.d("后台程序: " + appProcess.processName);
					return true;
				} else {
					// Logger.d("前台程序: " + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前应用程序处于前台还是后台 需要添加权限: <uses-permission
	 * android:name="android.permission.GET_TASKS" />
	 */
	public static boolean isApplicationBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				// Logger.d("isBackground: " + true);
				return true;
			}
		}
		// Logger.d("isBackground: " + false);
		return false;
	}

	/**
	 * 判断手机是否处理睡眠
	 *
	 * @param context
	 * @return
	 */
	public static boolean isSleeping(Context context) {
		KeyguardManager kgMgr = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
		// Logger.d(isSleeping ? "手机睡眠中.." : "手机未睡眠...");
		return isSleeping;
	}

	/**
	 * 检查网络是否已连接
	 *
	 * @param context
	 * @return
	 * @author com.tiantian
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected())
			return true;
		return false;
	}

	/**
	 * 判断当前是否是wifi状态
	 *
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected())
			return true;
		return false;
	}

	/**
	 * 安装apk
	 *
	 * @param context
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("application/vnd.android.package-archive");
		intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 初始化View的高宽
	 *
	 * @param view
	 */
	@Deprecated
	public static void initViewWH(final View view) {
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						view.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						System.out.println(view + ", width: " + view.getWidth()
								+ "; height: " + view.getHeight());
					}
				});
	}

	/**
	 * 初始化View的高宽并显示不可见
	 *
	 * @param view
	 */
	@Deprecated
	public static void initViewWHAndGone(final View view) {
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						view.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						view.setVisibility(View.GONE);
					}
				});
	}

	/**
	 * 使用Properties来保存设备的信息和错误堆栈信息
	 */
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";

	/**
	 * 判断是否为手机
	 *
	 * @param context
	 * @return
	 * @author wangjie
	 */
	public static boolean isPhone(Context context) {
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			// Logger.i("Current device is Tablet!");
			return false;
		} else {
			// Logger.i("Current device is phone!");
			return true;
		}
	}

	/**
	 * 获得设备的屏幕宽度
	 *
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getDeviceWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay().getWidth();
	}

	/**
	 * 获得设备的屏幕高度
	 *
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getDeviceHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay().getHeight();
	}

	/**
	 * 获取设备id（IMSI）
	 *
	 * @param context
	 * @return
	 * @author wangjie
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public static String getSubscriberIMSI(Context context) {
		String subscriberId;
		if (isPhone(context)) {
			TelephonyManager telephonyMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			subscriberId = telephonyMgr.getSubscriberId();
		} else {
			subscriberId = Settings.Secure.getString(
					context.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		Logger.d("当前设备IMSI码: " + subscriberId);
		return subscriberId;
	}

	
	public static String getPhoneIMSI(Context context){
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
            Log.d("getImsi", "get mTelephonyMgr " + mTelephonyMgr.toString());  
            String imsi = mTelephonyMgr.getSubscriberId();  
            String imei = mTelephonyMgr.getDeviceId();  
            Logger.d("设备IMSI码: " + imsi+" 设备IMEI码："+imei);
            return imsi;
        
	}
	
	/**
	 * 获取设备id（IMEI）
	 *
	 * @param context
	 * @return
	 * @author wangjie
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public static String getDeviceIMEI(Context context) {
		String deviceId;
		if (isPhone(context)) {
			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephony.getDeviceId();
		} else {
			deviceId = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID);
		}
		// Logger.d("当前设备IMEI码: " + deviceId);
		return deviceId;
	}

	/**
	 * 获取设备mac地址
	 *
	 * @param context
	 * @return
	 * @author wangjie
	 */
	public static String getMacAddress(Context context) {
		String macAddress;
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();
		// Logger.d("当前mac地址: " + (null == macAddress ? "null" : macAddress));
		if (null == macAddress)
			return "";
		macAddress = macAddress.replace(":", "");
		return macAddress;
	}

	/**
	 * 获取当前应用程序的版本号
	 *
	 * @param context
	 * @return
	 * @author wangjie
	 */
	public static String getAppVersion(Context context) {
		String version = "0";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// Logger.e("getAppVersion", e);
		}
		// Logger.d("该应用的版本号: " + version);
		return version;
	}

	/**
	 * 收集设备信息
	 *
	 * @param context
	 */
	public static Properties collectDeviceInfo(Context context) {
		Properties mDeviceCrashInfo = new Properties();
		try {
			// Class for retrieving various kinds of information related to the
			// application packages that are currently installed on the device.
			// You can find this class through getPackageManager().
			PackageManager pm = context.getPackageManager();
			// getPackageInfo(String packageName, int flags)
			// Retrieve overall information about an application package that is
			// installed on the system.
			// public static final int GET_ACTIVITIES
			// Since: API Level 1 PackageInfo flag: return information about
			// activities in the package in activities.
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				// public String versionName The version name of this package,
				// as specified by the <manifest> tag's versionName attribute.
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				// public int versionCode The version number of this package,
				// as specified by the <manifest> tag's versionCode attribute.
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			// Logger.e("Error while collect package info", e);
		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		// 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				// setAccessible(boolean flag)
				// 将此对象的 accessible 标志设置为指示的布尔值。
				// 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null)
						.toString());
				// if (DEBUG) {
				// Logger.d(field.getName() + " : " + field.get(null));
				// }
			} catch (Exception e) {
				Log.e("PhoneManager",
						"an error occured when collect crash info", e);
			}
		}
		return mDeviceCrashInfo;
	}

	/**
	 * 收集设备信息
	 *
	 * @param context
	 * @return
	 */
	public static String collectDeviceInfoStr(Context context) {
		Properties prop = collectDeviceInfo(context);
		Set<Object> deviceInfos = prop.keySet();
		StringBuilder deviceInfoStr = new StringBuilder("{\n");
		for (Iterator<Object> iter = deviceInfos.iterator(); iter.hasNext();) {
			Object item = iter.next();
			deviceInfoStr.append("\t\t\t" + item + ":" + prop.get(item)
					+ ", \n");
		}
		deviceInfoStr.append("}");
		return deviceInfoStr.toString();
	}

	/**
	 * 回到home，后台运行
	 *
	 * @param context
	 */
	public static void goHome(Context context) {
		// Logger.d("返回键回到HOME，程序后台运行...");
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(mHomeIntent);
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param activity
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public static int getStatusBarHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	/**
	 * 获取状态栏高度＋标题栏高度
	 *
	 * @param activity
	 * @return
	 */
	public static int getTopBarHeight(Activity activity) {
		return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
				.getTop();
	}

	/**
	 * 获取网络类型
	 *
	 * @return
	 */
	public static int getNetworkType(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getNetworkType();
	}

	/**
	 * MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码) 仅当用户已在网络注册时有效, CDMA 可能会无效
	 *
	 * @return （中国移动：46000 46002, 中国联通：46001,中国电信：46003）
	 */
	public static String getNetworkOperator(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getNetworkOperator();
	}

	/**
	 * 返回移动网络运营商的名字(例：中国联通、中国移动、中国电信) 仅当用户已在网络注册时有效, CDMA 可能会无效
	 *
	 * @return
	 */
	public static String getNetworkOperatorName(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getNetworkOperatorName();
	}

	/**
	 * 返回移动终端类型 PHONE_TYPE_NONE :0手机制式未知 PHONE_TYPE_GSM :1手机制式为GSM，移动和联通
	 * PHONE_TYPE_CDMA :2手机制式为CDMA，电信 PHONE_TYPE_SIP:3
	 *
	 * @return
	 */
	public static int getPhoneType(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getPhoneType();
	}

	/**
	 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
	 *
	 * @return 2G、3G、4G、未知四种状态
	 */
	public static int getNetWorkClass(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return NetWorkState.NETWORK_CLASS_2_G;
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return NetWorkState.NETWORK_CLASS_3_G;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return NetWorkState.NETWORK_CLASS_4_G;
		default:
			return NetWorkState.NETWORK_CLASS_UNKNOWN;
		}
	}

	/**
	 * 返回状态：当前的网络链接状态
	 *
	 * @return 没有网络，2G，3G，4G，WIFI
	 */
	public static int getNetWorkStatus(Context context) {
		int netWorkType = NetWorkState.NETWORK_CLASS_UNKNOWN;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			int type = networkInfo.getType();

			if (type == ConnectivityManager.TYPE_WIFI)
				netWorkType = NetWorkState.NETWORK_WIFI;
			else if (type == ConnectivityManager.TYPE_MOBILE)
				netWorkType = getNetWorkClass(context);
		}
		return netWorkType;
	}

	public class NetWorkState {
		/**
		 * Unknown network class
		 */
		public static final int NETWORK_CLASS_UNKNOWN = 0;

		/**
		 * wifi net work
		 */
		public static final int NETWORK_WIFI = 1;

		/**
		 * "2G" networks
		 */
		public static final int NETWORK_CLASS_2_G = 2;

		/**
		 * "3G" networks
		 */
		public static final int NETWORK_CLASS_3_G = 3;

		/**
		 * "4G" networks
		 */
		public static final int NETWORK_CLASS_4_G = 4;
	}
}
