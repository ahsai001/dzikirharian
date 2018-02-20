package com.zaitunlabs.zaitunutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zaitunlabs.zlcore.utils.ApplicationWacther;
import com.zaitunlabs.zlcore.utils.CommonUtils;

public class ConnectionChangeReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//DebugUtils.logI("ConnectionChangeReceiver", "connection changed");
		if(CommonUtils.isAppInForeground(arg0)){
			ApplicationWacther.getInstance(arg0).connectivityChanged();
		}
	}



}
