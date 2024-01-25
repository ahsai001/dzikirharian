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

public class DzikirPagiPetang extends CanvasActivity {

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
		subHeaderText.setText("Dzikir Pagi dan Petang");
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
			

		ASAnimatedImageView dzpImage = new ASAnimatedImageView(this);
		dzpImage.setImageResource(R.drawable.menu_dzikir_pagi);
		dzpImage.setRightImage(R.drawable.arrow_right);
		dzpImage.setScaleType(ScaleType.FIT_XY);
		dzpImage.setTextSize(30);
		dzpImage.setTextColor(Color.WHITE);
		dzpImage.setText("Dzikir Pagi");
		dzpImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzpImage, 10, 15, 80, 30);
		dzpImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent pagiIntent = new Intent(DzikirPagiPetang.this,DzikirBoard.class);
				pagiIntent.putExtra("subTitle", "Dzikir di waktu Pagi");
				pagiIntent.putExtra("waktu", "pagi");
				startActivity(pagiIntent);
			}
		});

		ASAnimatedImageView dzpetImage = new ASAnimatedImageView(this);
		dzpetImage.setImageResource(R.drawable.menu_dzikir_petang);
		dzpetImage.setRightImage(R.drawable.arrow_right);
		dzpetImage.setScaleType(ScaleType.FIT_XY);
		dzpetImage.setTextSize(30);
		dzpetImage.setTextColor(Color.WHITE);
		dzpetImage.setText("Dzikir Petang");
		dzpetImage.setTypeFace(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		mainSection.addViewWithFrame(dzpetImage, 10, 55, 80, 30);
		dzpetImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent petangIntent = new Intent(DzikirPagiPetang.this,DzikirBoard.class);
				petangIntent.putExtra("subTitle", "Dzikir di waktu Petang");
				petangIntent.putExtra("waktu", "petang");
				startActivity(petangIntent);
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
