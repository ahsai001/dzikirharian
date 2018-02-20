package com.zaitunlabs.dzikirharian.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.api.APIResponse;
import com.zaitunlabs.zlcore.api.models.CheckVersionModel;
import com.zaitunlabs.zlcore.utils.HttpClientUtils;
import com.zaitunlabs.zlcore.utils.PermissionUtils;
import com.zaitunlabs.zlcore.utils.audio.BackSoundService;
import com.zaitunlabs.zlcore.utils.WebAccess;
import com.zaitunlabs.zaituncore.ZaitunSplashActivity;
import com.zaitunlabs.zlcore.utils.CommonUtils;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class InitApp extends ZaitunSplashActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPortrait();
        setImageSplash(R.drawable.splashscreen);
        setMilisInFuture(200);
        setMilisInterval(100);

		final Class nextClass = (Class) CommonUtils.getSerializableIntent(getIntent(),ARG_NEXT_ACTIVITY_CLASS,NewPage.class);

        setRunnableCodeAfterSplashExpired(new Runnable() {
			@Override
			public void run() {
				if(CommonUtils.isOnline(InitApp.this)){
					AndroidNetworking.get(getString(R.string.app_update_checking_url))
							.setOkHttpClient(HttpClientUtils.getHTTPClient(InitApp.this,"v1"))
							.setTag("checkversion")
							.setPriority(Priority.HIGH)
							.build()
							.getAsString(new StringRequestListener() {
								@Override
								public void onResponse(String response) {
									try {
										int serverVersion = Integer.parseInt(response);
										if(CommonUtils.getVersionCode(InitApp.this) < serverVersion){
											setSplashDestroyedWhenFinish(false);
											CommonUtils.showDialog3Option(InitApp.this, getString(R.string.title_dialog_init), getString(R.string.message_dialog_init),
													getString(R.string.download_option_dialog_init), new Runnable() {

														@Override
														public void run() {
															CommonUtils.openBrowser(InitApp.this, getString(R.string.download_link));
															closeSplashScreen(true);
														}
													}, getString(R.string.close_option_dialog_init), new Runnable() {

														@Override
														public void run() {
															closeSplashScreen(true);
														}
													}, getString(R.string.use_existing_option_dialog_init), new Runnable() {

														@Override
														public void run() {
															nextStep(nextClass);
														}
													});
										}else{
											nextStep(nextClass);
										}
									} catch (NumberFormatException e){
										nextStep(nextClass);
									}
								}

								@Override
								public void onError(ANError anError) {
									nextStep(nextClass);
								}
							});
				} else {
					nextStep(nextClass);
				}
			}
			
		});
    }

    private void nextStep(Class nextClass){
		setNextPageClass(nextClass);
		navigateToNextPage();
		closeSplashScreen(true);
		//play audio
		BackSoundService.startBackSound(InitApp.this, new String[]{InitApp.this.getString(R.string.backsound_1)});
	}

	protected void navigateToNextPage(){
		Class nextPageClass = getNextPageClass();
		if(nextPageClass != null){
			try {
				Intent nextPageIntent = new Intent(InitApp.this, nextPageClass);

				String data = CommonUtils.getStringIntent(getIntent(),ARG_DATA, null);
				if(data != null && nextPageClass == DzikirBoard.class) {
					nextPageIntent.putExtra("subTitle","Dzikir di waktu "+data);
					nextPageIntent.putExtra("waktu",data);
				}
				startActivity(nextPageIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}