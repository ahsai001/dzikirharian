package com.zaitunlabs.zaituncore;


import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.zaitunlabs.zlcore.core.BaseActivity;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.utils.PermissionUtils;

public abstract class ZaitunSplashActivity extends BaseActivity {
	public static final String ARG_NEXT_ACTIVITY_CLASS = "arg_activity_class";
	public static final String ARG_DATA = "arg_data";
	CountDownTimer timer;
	RelativeLayout rellay;
	TextView tv;

	// setting timer splash, next page class, code to run before start next activity
	int milisInFuture = Constant.splashtime;
	int milisInterval = Constant.splashtime;
	Class nextPageClass = null;
	Runnable codeToRunAfterSplashExpired = null;
	boolean isSplashDestroyedWhenFinish = true; //defaultnya destroyed


	PermissionUtils permissionUtils;
	
	public void setSplashDestroyedWhenFinish(boolean isSplashDestroyedWhenFinish) {
		this.isSplashDestroyedWhenFinish = isSplashDestroyedWhenFinish;
	}

	protected void setPortrait(){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		rellay = new RelativeLayout(this);
		rellay.setBackgroundColor(Color.GRAY);
		rellay.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		tv = new TextView(this);
		tv.setText("This is splash screen");
		tv.setTextColor(Color.BLACK);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		tv.setLayoutParams(param);
		rellay.addView(tv);
		setContentView(rellay);

		permissionUtils = PermissionUtils.checkPermissionAndGo(this, 1052, new Runnable() {
			@Override
			public void run() {
				timer = new CountDownTimer(milisInFuture,milisInterval){
					@Override
					public void onFinish() {
						if(codeToRunAfterSplashExpired != null){
							try {
								codeToRunAfterSplashExpired.run();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							navigateToNextPage();
							closeSplashScreen(isSplashDestroyedWhenFinish);
						}
					}

					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
					}
				};
				timer.start();
			}
		}, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

	}

	@Override
    protected void onResume() {
    	super.onResume();
    }
	
	protected abstract void navigateToNextPage();
	
	protected void closeSplashScreen(boolean isSplashDestroyedWhenFinish){
		setSplashDestroyedWhenFinish(isSplashDestroyedWhenFinish);
		if(this.isSplashDestroyedWhenFinish) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		if(timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	// Zaitun Splash API methods
	protected void setImageSplash(int resid) {
		tv.setVisibility(View.GONE);
		rellay.setBackgroundResource(resid);
	}

	protected void setRunnableCodeAfterSplashExpired(Runnable codeToRun) {
		codeToRunAfterSplashExpired = codeToRun;
	}
	
	protected void setNextPageClass(Class nextPageClass) {
		this.nextPageClass = nextPageClass;
	}

	public Class getNextPageClass() {
		return nextPageClass;
	}

	protected void setMilisInFuture(int milisInFuture) {
		this.milisInFuture = milisInFuture;
	}
	
	protected void setMilisInterval(int milisInterval) {
		this.milisInterval = milisInterval;
	}
}