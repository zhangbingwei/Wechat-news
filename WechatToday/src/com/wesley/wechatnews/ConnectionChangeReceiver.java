package com.wesley.wechatnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		boolean isConnected = checkNetworkConnection(context);

		Intent i = new Intent(context, MainActivity.class);
		i.putExtra("network", isConnected);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		System.out.println("广播到底执行了没有呢：：");
		System.out.println("网络练级的值是：：："+isConnected);
	}

	public static boolean checkNetworkConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
