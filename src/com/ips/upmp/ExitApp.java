package com.ips.upmp;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class ExitApp {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApp instance;
	
	
	private ExitApp() {

	}
	public static ExitApp getInstance() {
		if (null == instance) {
			instance = new ExitApp();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
