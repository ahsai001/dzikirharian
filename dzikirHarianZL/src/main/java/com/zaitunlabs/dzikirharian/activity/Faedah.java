package com.zaitunlabs.dzikirharian.activity;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.modules.about.SimpleExpandableDataModel;
import com.zaitunlabs.zlcore.modules.about.SimpleExpandableListAdapter;
import com.zaitunlabs.zlcore.modules.about.SimpleItemDescriptionModel;
import com.zaitunlabs.zlcore.modules.shaum_sholat.CountDownSholatReminderUtils;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.utils.FileUtils;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;
import com.zaitunlabs.zlcore.views.GoToTopView;
import com.zaitunlabs.zlcore.views.GoToTopView.IGoToTopAction;

public class Faedah extends CanvasActivity {


	ASTextView countDownTimerHeaderText;
	CountDownSholatReminderUtils countDownSholatReminderUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setPortrait();

		// create Canvas Page
		CanvasLayout canvas = new CanvasLayout(this);

		// create page background
		canvas.setBackgroundResource(R.drawable.bgkayu);

		// create header
		ASTextView headerText = new ASTextView(this);
		headerText.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"fonts/arab_dances/ArabDances.ttf"));
		headerText.setText("Dzikir Harian");
		headerText.setBackgroundResource(R.drawable.bgheader);
		headerText.setTextSize(30);
		headerText.setTextColor(Color.WHITE);
		headerText.setGravity(Gravity.CENTER);
		canvas.addViewWithFrame(headerText, 0, 0, 100, 12);

		// create subheader
		ASTextView subHeaderText = new ASTextView(this);
		subHeaderText.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"fonts/albino/albino.ttf"));
		subHeaderText.setText("Faedah");
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

		// create 78 % area with canvassection
		CanvasSection mainSection = canvas.createNewSectionWithFrame(0, 22,
				100, 78, true).setSectionAsLinearLayout(LinearLayout.VERTICAL);

		final ExpandableListView listView = new ExpandableListView(this);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(3);
		mainSection.addViewInLinearLayout(listView);

		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, createData(), true);
		
		if (VERSION.SDK_INT > 19) {
			AnimationSet set = new AnimationSet(true);
			set.setDuration(200);
			
			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			set.addAnimation(animation);
			
			animation = new TranslateAnimation(
			    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
			    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			set.addAnimation(animation);
			
			LayoutAnimationController controller = new LayoutAnimationController(set, 1.0f);
			listView.setLayoutAnimation(controller);
		}
		listView.setAdapter(adapter);
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int arg0) {
				/*
				for(int i = 0 ; i < listView.getCount(); i++){
					if(i != arg0)listView.collapseGroup(i);
				}*/
			}
		});
				
		GoToTopView scrollToTop = new GoToTopView(this, new IGoToTopAction() {
			@Override
			public void goToTopAction() {
				listView.smoothScrollToPosition(0);
			}
		});
		
		mainSection.addViewWithFrame(scrollToTop, 90, 90, 10, 10);

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

	public SparseArray<SimpleExpandableDataModel> createData() {
		SparseArray<SimpleExpandableDataModel> groups = new SparseArray<SimpleExpandableDataModel>();

		// set data
		List<SimpleItemDescriptionModel> data = null;

		data = new Gson().fromJson(FileUtils.getReaderFromRawFile(this, R.raw.faedah), new TypeToken<List<SimpleItemDescriptionModel>>(){}.getType());


		Iterator<SimpleItemDescriptionModel> iterator = data.iterator();
	    int i = 0;
	    while(iterator.hasNext()){
	    	SimpleItemDescriptionModel item = iterator.next();
	    	SimpleExpandableDataModel group = new SimpleExpandableDataModel(item.getItem());
	    	group.children.add(item.getDescription());
	    	groups.append(i++, group);
	    }
		return groups;
	}
}
