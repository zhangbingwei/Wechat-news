package com.wesley.wechatnews;

import android.app.Activity;
import android.view.WindowManager;

public class Utils {

	public static void setStyle(Activity mActivity, float brightnessValue) {
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
		if (brightnessValue > 1.0f) {
			lp.screenBrightness = 1.0f;
		} else if (brightnessValue <= 0.0f) {
			lp.screenBrightness = 0.0f;
		} else {
			lp.screenBrightness = brightnessValue;
		}
		mActivity.getWindow().setAttributes(lp);
	}
}
