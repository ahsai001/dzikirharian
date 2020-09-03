package com.zaitunlabs.zaituncore;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zaitunlabs.zlcore.api.APIConstant;
import com.zaitunlabs.zlcore.core.BaseApplication;

public class ZaitunApp extends BaseApplication {
	@Override
	public void onCreate() {
		APIConstant.setApiAppid("5");
		APIConstant.setApiKey("dhrerwer12414543kfkllm");
		APIConstant.setApiVersion("v1");
		super.onCreate();


		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}

}
