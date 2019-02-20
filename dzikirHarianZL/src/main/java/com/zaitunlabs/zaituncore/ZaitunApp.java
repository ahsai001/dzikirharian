package com.zaitunlabs.zaituncore;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zaitunlabs.zlcore.api.APIConstant;
import com.zaitunlabs.zlcore.core.BaseApplication;
import com.zaitunlabs.zlcore.models.AppListDataModel;
import com.zaitunlabs.zlcore.models.AppListModel;
import com.zaitunlabs.zlcore.models.AppListPagingModel;
import com.zaitunlabs.zlcore.models.InformationModel;
import com.zaitunlabs.zlcore.models.StoreDataModel;
import com.zaitunlabs.zlcore.models.StoreModel;
import com.zaitunlabs.zlcore.models.StorePagingModel;

public class ZaitunApp extends BaseApplication {
	@Override
	public void onCreate() {
		addDBModelClass(InformationModel.class);

		addDBModelClass(AppListModel.class);
		addDBModelClass(AppListDataModel.class);
		addDBModelClass(AppListPagingModel.class);
		addDBModelClass(StoreModel.class);
		addDBModelClass(StoreDataModel.class);
		addDBModelClass(StorePagingModel.class);

		APIConstant.setApiAppid("5");
		APIConstant.setApiKey("dhrerwer12414543kfkllm");
		APIConstant.setApiVersion("v1");
		super.onCreate();


		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}

}
