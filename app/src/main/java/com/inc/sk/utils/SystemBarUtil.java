package com.inc.sk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * 
 * @author Holy-Spirit
 *		
 */

public class SystemBarUtil {

	
	public static void setSystemBartint(Activity activity, int id){
	       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	            setTranslucentStatus(true,activity);
	            System.out.println("-->>android 4.4 heigher");
	        }
	       
	        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
	        tintManager.setStatusBarTintEnabled(true);
	        tintManager.setStatusBarTintResource(id);
		
	}
	
    @TargetApi(19)
    private static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
