package com.zaitunlabs.dzikirharian.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.modules.shaum_sholat.CountDownSholatReminderUtils;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.views.ASImageButtonView;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;

public class FaedahDanReferensi extends CanvasActivity {

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
		subHeaderText.setText("Faedah dan Referensi");
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
		CanvasSection faedahSection = canvas.createNewSectionWithFrame(10, 45, 35, 30, true).setSectionAsLinearLayout(LinearLayout.VERTICAL);
		CanvasSection referensiSection = canvas.createNewSectionWithFrame(55, 45, 35, 30, true).setSectionAsLinearLayout(LinearLayout.VERTICAL);

		
		ASImageButtonView faedahView = new ASImageButtonView(this);
		faedahView.setScaleType(ScaleType.FIT_XY);
		faedahView.setAdjustViewBounds(true);
		faedahView.setImageDrawable(R.drawable.menu_faedah);
		
		
		faedahView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent faedahIntent = new Intent(FaedahDanReferensi.this,Faedah.class);
				startActivity(faedahIntent);
			}
		});
		
		//create faedahViewText
		ASTextView faedahViewText = new ASTextView(this);
		faedahViewText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		faedahViewText.setText("Faedah");
		faedahViewText.setTextSize(20);
		faedahViewText.setTextColor(Color.WHITE);
		faedahViewText.setGravity(Gravity.CENTER);
		
		faedahSection.addViewInLinearLayout(faedahViewText);
		faedahSection.addViewInLinearLayout(faedahView);
		
		ASImageButtonView refView = new ASImageButtonView(this);
		refView.setScaleType(ScaleType.FIT_XY);
		refView.setAdjustViewBounds(true);
		refView.setImageDrawable(R.drawable.menu_referensi);
		
		refView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent referensiIntent = new Intent(FaedahDanReferensi.this,Referensi.class);
				startActivity(referensiIntent);
			}
		});
		
		//create refViewText
		ASTextView refViewText = new ASTextView(this);
		refViewText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		refViewText.setText("Referensi");
		refViewText.setTextSize(20);
		refViewText.setTextColor(Color.WHITE);
		refViewText.setGravity(Gravity.CENTER);
		
		
		referensiSection.addViewInLinearLayout(refViewText);
		referensiSection.addViewInLinearLayout(refView);

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
