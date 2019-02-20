package com.zaitunlabs.dzikirharian.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;

import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.activities.BaseSplashActivity;
import com.zaitunlabs.zlcore.api.APIConstant;
import com.zaitunlabs.zlcore.utils.CommonUtils;
import com.zaitunlabs.zlcore.utils.PermissionUtils;
import com.zaitunlabs.zlcore.utils.audio.BackSoundService;

public class InitAppNew extends BaseSplashActivity {
    public static final String ARG_NEXT_ACTIVITY_CLASS = "arg_activity_class";
    public static final String ARG_DATA = "arg_data";
    private PermissionUtils permissionUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackgroundPaneImage(R.drawable.splashscreen);
    }

    @Override
    protected String getCheckVersionUrl() {
        return APIConstant.API_CHECK_VERSION;
    }

    @Override
    protected boolean doNextAction() {
        permissionUtils = PermissionUtils.checkPermissionAndGo(this, 1052, new Runnable() {
            @Override
            public void run() {
                final Class nextPageClass = (Class) CommonUtils.getSerializableIntent(getIntent(),ARG_NEXT_ACTIVITY_CLASS,HomePage.class);
                BackSoundService.startBackSound(InitAppNew.this, new String[]{InitAppNew.this.getString(R.string.backsound_1)});

                try {
                    Intent nextPageIntent = new Intent(InitAppNew.this, nextPageClass);

                    String data = CommonUtils.getStringIntent(getIntent(),ARG_DATA, null);
                    if(data != null && nextPageClass == DzikirBoard.class) {
                        nextPageIntent.putExtra("subTitle","Dzikir di waktu "+data);
                        nextPageIntent.putExtra("waktu",data);
                    }
                    startActivity(nextPageIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionUtils != null){
            permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected int getMinimumSplashTimeInMS() {
        return 3000;
    }
}
