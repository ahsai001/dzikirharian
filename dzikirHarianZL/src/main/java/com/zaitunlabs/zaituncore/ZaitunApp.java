package com.zaitunlabs.zaituncore;

import com.zaitunlabs.zlcore.api.APIConstant;
import com.zaitunlabs.zlcore.core.BaseApplication;
import com.zaitunlabs.zlcore.utils.DebugUtil;

public class ZaitunApp extends BaseApplication {
	@Override
	public void onCreate() {
		APIConstant.setApiAppid("5");
		APIConstant.setApiKey("dhrerwer12414543kfkllm");
		APIConstant.setApiVersion("v1");
		DebugUtil.setDebugingLevel(DebugUtil.VERBOSE_LEVEL);
		super.onCreate();
	}

}
