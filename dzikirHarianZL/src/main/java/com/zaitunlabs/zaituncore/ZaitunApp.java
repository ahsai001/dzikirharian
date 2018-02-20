package com.zaitunlabs.zaituncore;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.core.BaseApplication;
import com.zaitunlabs.zlcore.utils.ApplicationWacther;
import com.zaitunlabs.zaitunutils.DzikirHarianUtils;
import com.zaitunlabs.zlcore.utils.CommonUtils;
import com.zaitunlabs.zlcore.utils.DebugUtils;
import com.zaitunlabs.zlcore.utils.PlayServiceUtils;
import com.zaitunlabs.zlcore.utils.audio.BackSoundService;

public class ZaitunApp extends BaseApplication {
	@Override
	public void onCreate() {
		super.onCreate();

		DzikirHarianUtils.setReleaseMode();
		// inisialisasi untuk remote image loading
		//DisplayImageOptions displayimageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
		//ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(displayimageOptions).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
	}

}
