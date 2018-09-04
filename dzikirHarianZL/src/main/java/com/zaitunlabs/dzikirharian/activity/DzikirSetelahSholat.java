package com.zaitunlabs.dzikirharian.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.modules.shaum_sholat.CountDownSholatReminderUtils;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.views.ASAnimatedImageView;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;

public class DzikirSetelahSholat extends CanvasActivity {

	ASTextView countDownTimerHeaderText;
	CountDownSholatReminderUtils countDownSholatReminderUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
		subHeaderText.setText("Dzikir Sesudah Sholat Fardhu");
		subHeaderText.setBackgroundResource(R.drawable.bgsubheader_2);
		subHeaderText.setTextSize(18);
		subHeaderText.setTextColor(Color.WHITE);
		subHeaderText.setGravity(Gravity.CENTER_VERTICAL);
		canvas.addViewWithFrame(subHeaderText, 0, 12, 100, 10);

		countDownTimerHeaderText = new ASTextView(this);
		countDownTimerHeaderText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		countDownTimerHeaderText.setText("");
		countDownTimerHeaderText.setTextSize(18);
		countDownTimerHeaderText.setTextColor(Color.WHITE);
		countDownTimerHeaderText.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
		canvas.addViewWithFrame(countDownTimerHeaderText, 51, 12, 50, 10);

		//create 78 % area with canvassection
		CanvasSection mainSection = canvas.createNewSectionWithFrame(0, 22, 100, 78, true);
			

		ASAnimatedImageView dzSImage = new ASAnimatedImageView(this);
		dzSImage.setImageResource(R.drawable.menu_dzikir_pagi);
		dzSImage.setRightImage(R.drawable.arrow_right);
		dzSImage.setScaleType(ScaleType.FIT_XY);
		dzSImage.setTextSize(30);
		dzSImage.setTextColor(Color.WHITE);
		dzSImage.setText("Subuh");
		dzSImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzSImage, 10, 7, 80, 25);
		dzSImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent pagiIntent = new Intent(DzikirSetelahSholat.this,DzikirBoard.class);
				pagiIntent.putExtra("subTitle", "Subuh");
				pagiIntent.putExtra("waktu", "subuh");
				startActivity(pagiIntent);
			}
		});

		ASAnimatedImageView dzDzAIImage = new ASAnimatedImageView(this);
		dzDzAIImage.setImageResource(R.drawable.menu_dzikir_petang);
		dzDzAIImage.setRightImage(R.drawable.arrow_right);
		dzDzAIImage.setScaleType(ScaleType.FIT_XY);
		dzDzAIImage.setTextSize(30);
		dzDzAIImage.setTextColor(Color.WHITE);
		dzDzAIImage.setText("Zhuhur, 'Ashar, Isya");
		dzDzAIImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzDzAIImage, 10, 38, 80, 25);
		dzDzAIImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent petangIntent = new Intent(DzikirSetelahSholat.this,DzikirBoard.class);
				petangIntent.putExtra("subTitle", "Zhuhur, 'Ashar, Isya");
				petangIntent.putExtra("waktu", "dz-a-i");
				startActivity(petangIntent);
			}
		});



		ASAnimatedImageView dzMImage = new ASAnimatedImageView(this);
		dzMImage.setImageResource(R.drawable.menu_dzikir_pagi);
		dzMImage.setRightImage(R.drawable.arrow_right);
		dzMImage.setScaleType(ScaleType.FIT_XY);
		dzMImage.setTextSize(30);
		dzMImage.setTextColor(Color.WHITE);
		dzMImage.setText("Maghrib");
		dzMImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzMImage, 10, 69, 80, 25);
		dzMImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent pagiIntent = new Intent(DzikirSetelahSholat.this,DzikirBoard.class);
				pagiIntent.putExtra("subTitle", "Maghrib");
				pagiIntent.putExtra("waktu", "maghrib");
				startActivity(pagiIntent);
			}
		});


		setContentView(canvas.getFillParentView());

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
