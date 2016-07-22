/**
 * 
 */
package com.ips.upmp.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * @author IH847
 *
 */
public class MakeToast {
    /**
     * @param activity
     * @param content
     */
    public static void show(Activity activity, String content) {
            Toast.makeText(activity, content, Toast.LENGTH_LONG).show();
    }

    /**
     * @param activity
     * @param content
     */
    public static void show(Activity activity, int resId) {
        Toast.makeText(activity, activity.getString(resId), Toast.LENGTH_LONG).show();
    }
}
