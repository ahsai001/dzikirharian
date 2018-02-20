package com.zaitunlabs.dzikirharian.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.dzikirharian.receivers.ManageDzikirReminderReceiver;
import com.zaitunlabs.zlcore.modules.about.AboutUs;
import com.zaitunlabs.zlcore.modules.shaum_sholat.CountDownSholatReminderUtils;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.modules.shaum_sholat.ManageShaumSholatReminderReceiver;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.ASAnimatedImageView;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;


public class HomePage extends CanvasActivity {
	ASTextView countDownTimerHeaderText;
	CountDownSholatReminderUtils countDownSholatReminderUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setPortrait();
		
		//create Canvas Page
		CanvasLayout canvas = new CanvasLayout(this);
		
		//create page background 
		canvas.setBackgroundResource(R.drawable.bgkayu);
		
		//create header
		ASTextView headerText = new ASTextView(this);
		headerText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/arab_dances/ArabDances.ttf"));
		headerText.setText("Dzikir Harian");
		headerText.setBackgroundResource(R.drawable.bgheader);
		headerText.setTextSize(30);
		headerText.setTextColor(Color.WHITE);
		headerText.setGravity(Gravity.CENTER);
		canvas.addViewWithFrame(headerText, 0, 0, 100, 12);

		//create subheader
		ASTextView subHeaderText = new ASTextView(this);
		subHeaderText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		subHeaderText.setText("Menu Utama");
		subHeaderText.setBackgroundResource(R.drawable.bgsubheader_2);
		subHeaderText.setTextSize(18);
		subHeaderText.setPadding(20,0,0,0);
		subHeaderText.setTextColor(Color.WHITE);
		subHeaderText.setGravity(Gravity.CENTER_VERTICAL);
		canvas.addViewWithFrame(subHeaderText, 0, 12, 100, 10);

		countDownTimerHeaderText = new ASTextView(this);
		countDownTimerHeaderText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		countDownTimerHeaderText.setText("");
		countDownTimerHeaderText.setTextSize(18);
		countDownTimerHeaderText.setTextColor(Color.WHITE);
		countDownTimerHeaderText.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		canvas.addViewWithFrame(countDownTimerHeaderText, 51, 12, 50, 10);


		//create 78 % area with canvassection
		CanvasSection mainSection = canvas.createNewSectionWithFrame(0, 22, 100, 78, false);
		//all of this animated must inside 78% fullscreen
		
		//mukadimah, kapan dzikir pagi dan petang di baca
		ASAnimatedImageView mukadimahImage = new ASAnimatedImageView(this);
		mukadimahImage.setImageResource(R.drawable.menu_dzikir_pagi_petang);
		mukadimahImage.setLeftImage(R.drawable.masjid);
		mukadimahImage.setRightImage(R.drawable.arrow_right);
		mukadimahImage.setScaleType(ScaleType.FIT_XY);
		mukadimahImage.setTextSize(20);
		mukadimahImage.setText("Mukadimah");
		mukadimahImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(mukadimahImage, 10, 4, 80, 20);
		mukadimahImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent mukadimahIntent = new Intent(HomePage.this, Mukadimah.class);
				startActivity(mukadimahIntent);
			}
		});
		
		
		ASAnimatedImageView dzpgImage = new ASAnimatedImageView(this);
		dzpgImage.setImageResource(R.drawable.menu_dzikir_pagi_petang);
		dzpgImage.setLeftImage(R.drawable.masjid);
		dzpgImage.setRightImage(R.drawable.arrow_right);
		dzpgImage.setScaleType(ScaleType.FIT_XY);
		dzpgImage.setTextSize(20);
		dzpgImage.setText("Dzikir Pagi dan Petang");
		dzpgImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzpgImage, 10, 28, 80, 20);
		dzpgImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent pagiPetangIntent = new Intent(HomePage.this, DzikirPagiPetang.class);
				startActivity(pagiPetangIntent);
			}
		});
		
		ASAnimatedImageView dzssImage = new ASAnimatedImageView(this);
		dzssImage.setImageResource(R.drawable.menu_dxikir_sesudah_sholat);
		dzssImage.setLeftImage(R.drawable.masjid_bulan);
		dzssImage.setRightImage(R.drawable.arrow_right);
		dzssImage.setScaleType(ScaleType.FIT_XY);
		dzssImage.setTextSize(20);
		dzssImage.setText("Dzikir Sesudah Sholat Fardhu");
		dzssImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzssImage, 10, 52, 80, 20);
		dzssImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent pagiPetangIntent = new Intent(HomePage.this, DzikirSetelahSholat.class);
				startActivity(pagiPetangIntent);
			}
		});

		ASAnimatedImageView dzfadrImage = new ASAnimatedImageView(this);
		dzfadrImage.setImageResource(R.drawable.menu_faedah_dan_referensi);
		dzfadrImage.setLeftImage(R.drawable.buku);
		dzfadrImage.setRightImage(R.drawable.arrow_right);
		dzfadrImage.setScaleType(ScaleType.FIT_XY);
		dzfadrImage.setTextSize(20);
		dzfadrImage.setText("Faedah dan Referensi");
		dzfadrImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzfadrImage, 10, 76, 80, 20);
		dzfadrImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent faedahReferensiIntent = new Intent(HomePage.this, FaedahDanReferensi.class);
				startActivity(faedahReferensiIntent);
			}
		});
		
		ASAnimatedImageView abc = new ASAnimatedImageView(this);
		abc.setImageResource(R.drawable.menu_faedah_dan_referensi);
		abc.setLeftImage(R.drawable.buku);
		abc.setRightImage(R.drawable.arrow_right);
		abc.setScaleType(ScaleType.FIT_XY);
		abc.setTextSize(20);
		abc.setText("Tentang Aplikasi");
		abc.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(abc, 10, 100, 80, 20);
		abc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AboutUs.start(HomePage.this,R.drawable.icon_apps,
						R.drawable.icon_share_to_friend, R.string.share_title, R.string.share_body_template,
						R.drawable.icon_share_to_friend, R.string.feedback_mail_to, R.string.feedback_title, R.string.feedback_body_template,
						R.drawable.icon_rate_this_app,
						R.raw.version_change_history, false,"https://zaitunlabs.com/dzikir-harian/");
			}
		});

		setContentView(canvas.getFillParentView());


		Intent setReminderIntent = new Intent(this, ManageDzikirReminderReceiver.class);
		sendBroadcast(setReminderIntent);

		Intent setShaumSholatReminderIntent = new Intent(this, ManageShaumSholatReminderReceiver.class);
		sendBroadcast(setShaumSholatReminderIntent);

		countDownSholatReminderUtils = new CountDownSholatReminderUtils();
	}


	@Override
	protected void onResume() {
		super.onResume();
		countDownSholatReminderUtils.startCountDown(this,countDownTimerHeaderText);

	}

	@Override
	protected void onPause() {
		super.onPause();
		countDownSholatReminderUtils.stopCountDown();
	}
}
