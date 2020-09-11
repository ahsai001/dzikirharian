package com.zaitunlabs.dzikirharian.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.dzikirharian.adapter.PageListAdapter;
import com.zaitunlabs.dzikirharian.model.DzikirModel;
import com.zaitunlabs.zlcore.modules.shaum_sholat.CountDownSholatReminderUtils;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.utils.DateStringUtil;
import com.zaitunlabs.zlcore.utils.FileUtil;
import com.zaitunlabs.zlcore.utils.NavigationHandler;
import com.zaitunlabs.zlcore.utils.ViewUtil;
import com.zaitunlabs.zlcore.utils.audio.AudioService;
import com.zaitunlabs.zlcore.utils.audio.AudioServiceCallBack;
import com.zaitunlabs.zlcore.utils.audio.BackSoundService;
import com.zaitunlabs.zlcore.utils.CommonUtil;
import com.zaitunlabs.zlcore.utils.DebugUtil;
import com.zaitunlabs.zlcore.utils.Prefs;
import com.zaitunlabs.zlcore.views.ASGestureDetector;
import com.zaitunlabs.zlcore.views.ASImageButtonView;
import com.zaitunlabs.zlcore.views.ASImageView;
import com.zaitunlabs.zlcore.views.ASMovableMenu;
import com.zaitunlabs.zlcore.views.ASMovableMenu.ASMovableMenuListener;
import com.zaitunlabs.zlcore.views.ASGestureListener;
import com.zaitunlabs.zlcore.views.ASScrollingTextView;
import com.zaitunlabs.zlcore.views.ASShiftableImageView;
import com.zaitunlabs.zlcore.views.ASSlidingLayer;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;
import com.zaitunlabs.zlcore.views.DimensionStateListener;
import com.zaitunlabs.zlcore.views.GoToTopView;


public class DzikirBoard extends CanvasActivity {
	ArrayList<String> bacaanList;
	ArrayList<String> terjemahList;
	ArrayList<Integer> counterList;
	ArrayList<String> dalilList;
	ArrayList<String> soundList;
	ArrayList<String> runningTextList;

	ASTextView tv;
	ASImageView iv;
	ASImageView iv2;
	ASImageView iv3;
	ASImageView iv4;
	ASShiftableImageView countImageView;
	
	TextView pageView;
	ASTextView translatePart;
	ASImageView soundView;
	ASScrollingTextView runText;

	CanvasSection gotoSection;
	GoToTopView gotoUp;

	ASSlidingLayer slidingLayer;
	
	String soundLink;
	ASTextView dalilFullText;
	
	NavigationHandler navHandler;
	
	CanvasSection slidingPageSelectorCanvas;
	CanvasSection headerSection;
	CanvasSection subHeaderSection;
	CanvasSection mainSection;
	CanvasSection sliderSection;
	
	int previousSavedPage = 0;
	String savedPageKey;


	ASTextView countDownTimerHeaderText;
	SwitchCompat keepScreenOnSwitch;

	CountDownSholatReminderUtils countDownSholatReminderUtils;

	DelayedAction delayedAction;

	String subHeaderTitle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//DebugUtil.logW("SIZE", "screen 1: "+CommonUtil.getScreenWidth(this)+"x"+CommonUtil.getScreenHeight(this));
		super.onCreate(savedInstanceState);

		//DebugUtil.logW("SIZE", "screen 2: "+CommonUtil.getScreenWidth(this)+"x"+CommonUtil.getScreenHeight(this));
		final CanvasLayout canvas = new CanvasLayout(this);

		canvas.setBackgroundResource(R.drawable.bgkayu);

		subHeaderTitle = CommonUtil.getStringIntent(getIntent(), "subTitle", "Dzikir Harian");

		// header
		// create header
 		ASTextView headerText = new ASTextView(this);
		headerText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/arab_dances/ArabDances.ttf"));
		headerText.setText("Dzikir Harian");
		headerText.setBackgroundResource(R.drawable.bgheader);
		headerText.setTextSize(30);
		headerText.setTextColor(Color.WHITE);
		headerText.setGravity(Gravity.CENTER);
		
		headerSection = canvas.createNewSectionWithFrame(0, 0, 100, 12);
		headerSection.getShiftPositionHandler().addRectToDimensionState(0, 0, 100, 12);
		headerSection.getShiftPositionHandler().addRectToDimensionState(0, -12, 100, 12);
		headerSection.addViewWithFrame(headerText, 0, 0, 100, 100);
		

		// subheader
		ASTextView subHeaderText = new ASTextView(this);
		subHeaderText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		subHeaderText.setText(subHeaderTitle);
		subHeaderText.setBackgroundResource(R.drawable.bgsubheader_2);
		subHeaderText.setTextSize(18);
		subHeaderText.setTextColor(Color.WHITE);
		subHeaderText.setGravity(Gravity.CENTER_VERTICAL);
		
		subHeaderSection = canvas.createNewSectionWithFrame(0, 12, 100, 10);
		subHeaderSection.getShiftPositionHandler().addRectToDimensionState(0, 12, 100, 10);
		subHeaderSection.getShiftPositionHandler().addRectToDimensionState(0, -10, 100, 10);
		subHeaderSection.addViewWithFrame(subHeaderText, 0, 0, 100, 100);


		countDownTimerHeaderText = new ASTextView(this);
		countDownTimerHeaderText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		countDownTimerHeaderText.setText("");
		countDownTimerHeaderText.setTextSize(18);
		countDownTimerHeaderText.setTextColor(Color.WHITE);
		countDownTimerHeaderText.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
		subHeaderSection.addViewWithFrame(countDownTimerHeaderText, 0, 0, 100, 100);
		
		
		// main section : tabMenu, sound/running text, dzikir view, nav View
		mainSection = canvas.createNewSectionWithFrame("mainSection",0, 22, 100, 68, true);
		
		mainSection.getShiftPositionHandler().addRectToDimensionState(0, 22, 100, 68);
		mainSection.getShiftPositionHandler().addRectToDimensionState(0, 0, 100, 100);
		
		mainSection.setBackgroundResource(R.drawable.bgkayu);

		mainSection.setUpdateLayoutChildren(false);


		navHandler = new NavigationHandler();

		// sound and running text
		CanvasSection soundAndInfoArea = mainSection.addSubSectionWithFrame("soundAndInfoArea", 0, 0, 100, 10, true);
		soundView = new ASImageView(this);
		soundView.setScaleType(ScaleType.FIT_XY);
		soundView.setAdjustViewBounds(true);
		soundView.setImageResource(R.drawable.play);
		soundView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (AudioService.isPlaying()) {
					soundView.setImageResource(R.drawable.play);
					AudioService.stopAudioSound(DzikirBoard.this);
					if(BackSoundService.getLastAction() == BackSoundService.ACTION_PAUSE){
						BackSoundService.resumeBackSound(DzikirBoard.this);
					}
				} else {
					soundView.setImageResource(R.drawable.stop);
					AudioService.startAudioSound(DzikirBoard.this,
							new String[] { soundLink });
					if(BackSoundService.isPlaying()){
						BackSoundService.pauseBackSound(DzikirBoard.this);
					}
				}
			}
		});
		
		
		runText = new ASScrollingTextView(this);
		runText.setTextColor(Color.GREEN);
		runText.setRndDuration(7000);


		keepScreenOnSwitch = new SwitchCompat(this);
		keepScreenOnSwitch.setShowText(false);
		keepScreenOnSwitch.setText("standby");
		keepScreenOnSwitch.setTextColor(Color.WHITE);
		keepScreenOnSwitch.setHighlightColor(Color.GREEN);
		keepScreenOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					Prefs.with(buttonView.getContext()).save("standby",true);
				}else{
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					Prefs.with(buttonView.getContext()).save("standby",false);
				}
			}
		});

		soundAndInfoArea.addViewWithFrame(soundView, 10, 0, 10, 100);
		soundAndInfoArea.addViewWithFrame(runText, 22, 0, 38, 100);
		soundAndInfoArea.addViewWithFrame(keepScreenOnSwitch, 62, 0, 30, 100);
		

		// bagian inti dzikir
		final CanvasSection dzikirView = mainSection.addSubSectionWithFrame("dzikirView", 5,10, 90, 90, false).setSectionAsLinearLayout(LinearLayout.VERTICAL);

		dzikirView.getShiftPositionHandler().addRectToDimensionState(5,10, 90, 90);
		dzikirView.getShiftPositionHandler().addRectToDimensionState(5,10, 90, 123);
		
		
		countImageView = ASShiftableImageView.create(canvas, 80, 0, 20, 15, false,true);

		/*
		countImageView = new ASImageView(this);
		countImageView.setScaleType(ScaleType.FIT_XY);
		countImageView.setAdjustViewBounds(true);
		canvas.addViewWithFrame(countImageView, 80, 0, 20, 15);*/
		
		slidingPageSelectorCanvas = canvas.createNewSectionWithFrame("slidingPageSelectorCanvas",0,90,100,100, true);
		slidingPageSelectorCanvas.setBackgroundColor(Color.argb(200, 0, 0, 0));
		slidingPageSelectorCanvas.getShiftPositionHandler().addRectToDimensionState(0, 90, 100, 100);
		slidingPageSelectorCanvas.getShiftPositionHandler().addRectToDimensionState(0, 0, 100, 100);
		slidingPageSelectorCanvas.getShiftPositionHandler().setMinMaxLocationXY(0, 0, 0, 90);
		
		final CanvasSection pageContent = slidingPageSelectorCanvas.addSubSectionWithFrame("pageContent", 0, 10, 100, 90, true).setSectionAsLinearLayout(LinearLayout.VERTICAL);
		
		slidingPageSelectorCanvas.getShiftPositionHandler().setDimensionStateListener(new DimensionStateListener() {
			
			@Override
			public boolean rectForCurrentDimensionState(Rect currentRectState) {
				return false;
			}
			
			@Override
			public boolean indexForCurrentDimensionState(int currentIndexState) {
				if(currentIndexState == 0){
					//close
					navHandler.showNavigationViewWithState();
					pageContent.removeAllViews();
					pageView.setText("Halaman " + (navHandler.getIndex() + 1) + " dari " + navHandler.getCount());
				}else if(currentIndexState == 1){
					//open
					navHandler.hideNavigationView();
					pageView.setText("pilih halaman ...");
					final ListView listV = new ListView(DzikirBoard.this);
					listV.setCacheColorHint(Color.TRANSPARENT);
					
					pageContent.addViewInLinearLayout(listV);

					listV.setAdapter(new PageListAdapter(DzikirBoard.this, bacaanList, subHeaderTitle));
					listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							slidingPageSelectorCanvas.getShiftPositionHandler().changeStateToDimension(0, true);
							navHandler.setIndex(arg2);
						}
					});
					
					//listV.clearFocus();
					listV.postDelayed(new Runnable() {
						@Override
						public void run() {
							listV.setSelection(navHandler.getIndex());
						}
					}, 400);
				}
				return false;
			}
		});
		
		
		// bagian navigasi
		final CanvasSection navView = slidingPageSelectorCanvas.addSubSectionWithFrame("navView", 0, 0, 100, 10, true);
		navView.shiftTarget(slidingPageSelectorCanvas, true, false, true, false);
		
		//final CanvasSection navView = mainSection.addSubSectionWithFrame("navView", 0, 90, 100, 10, true);
		navView.setBackgroundResource(R.drawable.bgheader);

		final ASImageButtonView nextView = new ASImageButtonView(this);
		nextView.setImageDrawable(R.drawable.arrow_right);
		navView.addViewWithFrame(nextView, 80, 10, 20, 100);

		final ASImageButtonView prevView = new ASImageButtonView(this);
		prevView.setImageDrawable(R.drawable.arrow_left);
		navView.addViewWithFrame(prevView, 0, 10, 20, 100);

		pageView = new TextView(this);
		pageView.setText("prev");
		pageView.setTextSize(15);
		pageView.setTypeface(null, Typeface.BOLD);
		pageView.setTextColor(Color.BLACK);
		pageView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		pageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				slidingPageSelectorCanvas.getShiftPositionHandler().changeStateToNextDimension(true);
			}
		});
		navView.addViewWithFrame(pageView, 30, 10, 40, 100);


		delayedAction = new DelayedAction(20 * 60 * 1000, new Runnable() {
			@Override
			public void run() {
				Prefs.with(DzikirBoard.this).save("last-read:"+subHeaderTitle+":"+bacaanList.get(navHandler.getIndex()), new Date().getTime());
				reloadCounterImage();
			}
		});
		
		
		navHandler.setStateListener(new NavigationHandler.NavigationStateListener() {
			@Override
			public boolean navigationStateIndex(View outputView, int index, int counts) {
				if (soundView != null)
					soundView.setImageResource(R.drawable.play);
				if(runText != null){
					runText.setText(runningTextList.get(index));
					runText.startScroll();
				}

				AudioService.stopAudioSound(DzikirBoard.this);
				if(BackSoundService.getLastAction() == BackSoundService.ACTION_PAUSE){
					BackSoundService.resumeBackSound(DzikirBoard.this);
				}

				soundLink = soundList.get(index);

				slidingLayer.closeLayer(true);

				pageView.setText("Halaman " + (index + 1) + " dari " + counts);

				// set new bacaan
				String bacaanString = bacaanList.get(index);
				String[] bacaanArray = bacaanString.split(",");
				if(bacaanArray.length == 4){ //handle dzikir sesudah sholat alhamdu, subhanallah, allahuakbar, laa ilaha
					//Picasso.get().load(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[0])).into(iv);
					//Picasso.get().load(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[1])).into(iv2);
					//Picasso.get().load(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[2])).into(iv3);
					//Picasso.get().load(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[3])).into(iv4);

					iv.setImageResource(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[0]));
					iv2.setImageResource(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[1]));
					iv3.setImageResource(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[2]));
					iv4.setImageResource(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanArray[3]));


					iv2.setVisibility(View.VISIBLE);
					iv3.setVisibility(View.VISIBLE);
					iv4.setVisibility(View.VISIBLE);

				}else {
					//Picasso.get().load(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanString)).into(iv);
					iv.setImageResource(CommonUtil.getIDResource(DzikirBoard.this, "drawable", bacaanString));

					iv2.setImageDrawable(null);
					iv3.setImageDrawable(null);
					iv4.setImageDrawable(null);
					iv2.setVisibility(View.GONE);
					iv3.setVisibility(View.GONE);
					iv4.setVisibility(View.GONE);
				}

				Picasso.get().load(counterList.get(index)).into(countImageView.getImageView());

				tv.setText(terjemahList.get(index));

				dalilFullText.setText(dalilList.get(index));

				dzikirView.setVScrollOnTop();

				reloadCounterImage();
				delayedAction.go();
				return false;
			}
		});

		
		/* experiment code */
		//mainSection.getShiftPositionHandler().addRectToDimensionState(0, 22, 100, 78);
		//mainSection.getShiftPositionHandler().addRectToDimensionState(-80, 22, 100, 78);
		//mainSection.getShiftPositionHandler().setMinMaxLocationXY(-80, 0, 22, 22);
		//mainSection.shiftTarget(mainSection, false, true, true, false);
		/* =================================================  */
		
	
		gotoUp = new GoToTopView(this, new GoToTopView.IGoToTopAction() {
			@Override
			public void goToTopAction() {
				dzikirView.setVScrollOnTop();
			}
		});

		gotoSection = mainSection.addSubSectionWithFrame(80,86,15,15);
		gotoSection.getShiftPositionHandler().addRectToDimensionState(80,86,15,15);
		gotoSection.addViewWithFrame(gotoUp, 0,0,100,100);

		iv = new ASImageView(this);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setAdjustViewBounds(true);
		//iv.getParent().requestDisallowInterceptTouchEvent(true);

		ASGestureListener listener = new ASGestureListener() {
			boolean isReadingMode = false;
			@Override
			public boolean upEventOccurred(float x, float y) {
				return true;
			}

			@Override
			public boolean swipeEventOccured(int swipeType, float x, float y, float dx, float dy) {
				
				return true;
			}

			@Override
			public boolean swipeTypeFinal(int swipeType) {
				if(swipeType == ASGestureDetector.SWIPE_LEFT){
					navHandler.next();
				}else if(swipeType == ASGestureDetector.SWIPE_RIGHT){
					navHandler.prev();
				}

				return false;
			}

			@Override
			public boolean movingSpeed(float xSpeed, float ySpeed) {
				return false;
			}

			@Override
			public boolean longClickEventOccured() {
				return false;
			}

			@Override
			public boolean isSwipeEnabled() {
				return true;
			}

			@Override
			public boolean isLongClickEnabled() {
				return false;
			}

			@Override
			public boolean isDoubleTapEnabled() {
				return true;
			}

			@Override
			public boolean isClickEnabled() {
				return false;
			}

			@Override
			public boolean downEventOccured(float x, float y) {
				return true;
			}

			@Override
			public boolean doubleTapEventOccured() {
				//goto reading mode
				DebugUtil.logE("GESTURE DETECTOR", "double tap");
				if(!isReadingMode){
					//goto reading mode
					isReadingMode = !isReadingMode;
					headerSection.getShiftPositionHandler().changeStateToDimension(1, true);
					subHeaderSection.getShiftPositionHandler().changeStateToDimension(1, true);
					mainSection.getShiftPositionHandler().changeStateToDimension(1, true);
					dzikirView.getShiftPositionHandler().changeStateToDimension(1,true);
					sliderSection.getShiftPositionHandler().changeStateToDimension(1, true);
					countImageView.setPositionLocked(false);
					countImageView.setPositionToRect(new Rect(40,77,20,15),true);
				}else{
					//back to normal mode
					isReadingMode = !isReadingMode;
					headerSection.getShiftPositionHandler().changeStateToDimension(0, true);
					subHeaderSection.getShiftPositionHandler().changeStateToDimension(0, true);
					mainSection.getShiftPositionHandler().changeStateToDimension(0, true);
					dzikirView.getShiftPositionHandler().changeStateToDimension(0,true);
					sliderSection.getShiftPositionHandler().changeStateToDimension(0,true);
					countImageView.setPositionLocked(true);
					countImageView.setPositionToOrigin(true);
				}
				return true;
			}

			@Override
			public boolean deltaMoveOutsideParameter(int swipeType, float x, float y,
													 float dx, float dy, float fromDownDX, float fromDownDY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean deltaMoveInsideParameter(int swipeType, float x, float y,
													float dx, float dy, float fromDownDX, float fromDownDY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean clickEventOccured() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean cancelEventOccured(float x, float y) {
				// TODO Auto-generated method stub
				DebugUtil.logD("","cancel event");
				return false;
			}
		};
		iv.setASGestureListener(listener);

		dzikirView.addViewInLinearLayout(iv);


		iv2 = new ASImageView(this);
		iv2.setScaleType(ScaleType.FIT_XY);
		iv2.setAdjustViewBounds(true);
		iv2.setASGestureListener(listener);

		dzikirView.addViewInLinearLayout(iv2);


		iv3 = new ASImageView(this);
		iv3.setScaleType(ScaleType.FIT_XY);
		iv3.setAdjustViewBounds(true);
		iv3.setASGestureListener(listener);

		dzikirView.addViewInLinearLayout(iv3);


		iv4 = new ASImageView(this);
		iv4.setScaleType(ScaleType.FIT_XY);
		iv4.setAdjustViewBounds(true);
		iv4.setASGestureListener(listener);

		dzikirView.addViewInLinearLayout(iv4);

		dzikirView.setASGestureListener(listener);

		translatePart = new ASTextView(this);
		translatePart.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/albino/albino.ttf"));
		translatePart.setText("Terjemahan");
		translatePart.setTextSize(24);
		translatePart.setTextColor(Color.WHITE);
		translatePart.setGravity(Gravity.CENTER_VERTICAL);
		translatePart.setMinHeight(30);
		translatePart.setASGestureListener(listener);
		dzikirView.addViewInLinearLayout(translatePart);

		tv = new ASTextView(this);
		tv.setText("Default text");
		tv.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/GeosansLight.ttf"));
		tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		tv.setBackgroundColor(Color.WHITE);
		tv.setTextColor(Color.BLACK);
		tv.setTypeface(null, Typeface.BOLD);
		dzikirView.addViewInLinearLayout(tv);



		tv.setASGestureListener(listener);
		
		View v = new View(this);
		dzikirView.addViewInLinearLayout(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mainSection.getPixelRelativeToHeight(14)));


		//set Data from JSON File
		bacaanList = new ArrayList<String>();
	    counterList = new ArrayList<Integer>();
	    terjemahList = new ArrayList<String>();
	    dalilList = new ArrayList<String>();
	    soundList = new ArrayList<String>();
    	runningTextList = new ArrayList<String>();

		List<DzikirModel> data = null;
		int rawJSONFile = 0;
		if (CommonUtil.getStringIntent(getIntent(), "waktu", "pagi").equalsIgnoreCase("pagi")) {
			rawJSONFile = R.raw.dzikirpagi;
			previousSavedPage = Prefs.with(this).getInt("savedPagiPage", 0);
			savedPageKey = "savedPagiPage";
		} else if (CommonUtil.getStringIntent(getIntent(), "waktu", "pagi").equalsIgnoreCase("petang")) {
			rawJSONFile = R.raw.dzikirpetang;
			previousSavedPage = Prefs.with(this).getInt("savedPetangPage", 0);
			savedPageKey = "savedPetangPage";
		} else if (CommonUtil.getStringIntent(getIntent(), "waktu", "pagi").equalsIgnoreCase("subuh")) {
			rawJSONFile = R.raw.dzikirsesudahsholat_subuh;
			previousSavedPage = Prefs.with(this).getInt("savedSholatSubuhPage", 0);
			savedPageKey = "savedSholatSubuhPage";
		} else if (CommonUtil.getStringIntent(getIntent(), "waktu", "pagi").equalsIgnoreCase("dz-a-i")) {
			rawJSONFile = R.raw.dzikirsesudahsholat_dz_a_i;
			previousSavedPage = Prefs.with(this).getInt("savedSholatDzAIPage", 0);
			savedPageKey = "savedSholatDzAIPage";
		} else if (CommonUtil.getStringIntent(getIntent(), "waktu", "pagi").equalsIgnoreCase("maghrib")) {
			rawJSONFile = R.raw.dzikirsesudahsholat_maghrib;
			previousSavedPage = Prefs.with(this).getInt("savedSholatMaghribPage", 0);
			savedPageKey = "savedSholatMaghribPage";
		}

		// set data

		data = new Gson().fromJson(FileUtil.getReaderFromRawFile(this, rawJSONFile), new TypeToken<List<DzikirModel>>(){}.getType());


		Iterator<DzikirModel> iterator = data.iterator();
	    while(iterator.hasNext()){
	    	DzikirModel item = iterator.next();
	    	bacaanList.add(item.getBacaan());
	    	counterList.add(CommonUtil.getIDResource(this, "drawable", item.getCounter()));
	    	terjemahList.add(item.getTerjemah());
	    	dalilList.add(item.getDalil());
	    	soundList.add(item.getSound());
	    	runningTextList.add(item.getRunningtext());
	    }
	
	    
	    
	    
		// create slider for dalil
		sliderSection = mainSection.addSubSectionWithFrame("sliderSection", 5, 10, 90, 90, true);



		sliderSection.getShiftPositionHandler().addRectToDimensionState(5, 10, 90, 90);
		sliderSection.getShiftPositionHandler().addRectToDimensionState(5, 10, 90, 123);

		sliderSection.setUpdateLayoutChildren(false);

		sliderSection.getShiftPositionHandler().setDimensionStateListener(new DimensionStateListener() {
			@Override
			public boolean rectForCurrentDimensionState(Rect currentRectState) {
				return false;
			}

			@Override
			public boolean indexForCurrentDimensionState(int currentIndexState) {
				if(currentIndexState == 1){
					//reading mode is ready
					slidingLayer.getSliderPanel().getShiftPositionHandler().changeVisibilityForDimensionState(0,false);
					slidingLayer.getSliderPanel().getShiftPositionHandler().changeVisibilityForDimensionState(1, false);

					slidingLayer.getSliderPanel().getShiftPositionHandler().addRectToDimensionState(-85, 123, 100, 150);
					slidingLayer.getSliderPanel().getShiftPositionHandler().addRectToDimensionState(0, 0, 100, 150);

					slidingLayer.OPEN_STATE = 3;
					slidingLayer.CLOSE_STATE = 2;


					slidingLayer.getSliderContentPanel().getShiftPositionHandler().addRectToDimensionState(0, 15, 100, 150);
					slidingLayer.getSliderContentPanel().getShiftPositionHandler().changeStateToDimension(1, true);

					gotoSection.getShiftPositionHandler().addRectToDimensionState(80, 120, 15, 15);
					gotoSection.getShiftPositionHandler().changeStateToDimension(1, true);

					slidingLayer.getSliderPanel().getShiftPositionHandler().changeStateToDimension(2,true);

				}else if(currentIndexState == 0){

					slidingLayer.getSliderPanel().getShiftPositionHandler().changeVisibilityForDimensionState(0,true);
					slidingLayer.getSliderPanel().getShiftPositionHandler().changeVisibilityForDimensionState(1,true);

					slidingLayer.getSliderPanel().getShiftPositionHandler().clearRectInDimensionState(3);
					slidingLayer.getSliderPanel().getShiftPositionHandler().clearRectInDimensionState(2);

					slidingLayer.OPEN_STATE = 1;
					slidingLayer.CLOSE_STATE = 0;

					slidingLayer.getSliderContentPanel().getShiftPositionHandler().clearRectInDimensionState(1);
					slidingLayer.getSliderContentPanel().getShiftPositionHandler().changeStateToDimension(0,true);

					gotoSection.getShiftPositionHandler().clearRectInDimensionState(1);
					gotoSection.getShiftPositionHandler().changeStateToDimension(0, true);

					slidingLayer.getSliderPanel().getShiftPositionHandler().changeStateToDimension(0,true);
				}
				return false;
			}
		});



		//slidingLayer = ASSlidingLayer.create(sliderSection, ASSlidingLayer.STICK_TO_LEFT, 100, 100, 10);
		slidingLayer = ASSlidingLayer.create(sliderSection,new Rect(-85,86,15,185),new Rect(0,0,100,123),15,ASSlidingLayer.STICK_TO_CUSTOM, false);
				
		slidingLayer.setClosedHandleStatement("lihat dalil");
		slidingLayer.setOpenedHandleStatement("tutup dalil");
		slidingLayer.getSliderContentPanel().setSectionAsLinearLayout(LinearLayout.VERTICAL);


		slidingLayer.setSlidingListener(new ASSlidingLayer.ASSlidingLayerListener() {
			@Override
			public void slidingState(int state) {
				DebugUtil.logE("ahmad", "state : "+state);
			}
		});


		//dalilFullText = new JustifiedTextView(this);
		dalilFullText = new ASTextView(this);
		dalilFullText.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/GeosansLight.ttf"));
		dalilFullText.setText("dalil");
		dalilFullText.setTextSize(25);
		dalilFullText.setTextColor(Color.WHITE);
		dalilFullText.setGravity(Gravity.CENTER);

		
		slidingLayer.getSliderContentPanel().addViewInLinearLayout(dalilFullText);
	
		// initiate state
		navHandler.setNextPrevView(prevView, nextView);
		navHandler.setCount(bacaanList.size());
		navHandler.setIndex(previousSavedPage);

		setContentView(canvas.getFillParentView());

		
		getMovableMenu().setListener(new ASMovableMenuListener() {

			@Override
			public void menuWillBeOpened() {
				slidingLayer.closeLayer(true);
				slidingPageSelectorCanvas.getShiftPositionHandler().changeStateToDimension(0, true);
			}

			@Override
			public void menuWillBeClosed() {
			}

			@Override
			public void menuHasBeenOpened() {
			}

			@Override
			public void menuHasBeenClosed() {
			}
		});

		Point poin = new Point();
		poin.set(5, 5);
		//getMovableMenu().setAppearDisappearPoint(poin);
		

		//DebugUtil.logW("SIZE", "app size : "+CommonUtil.getScreenWidth(this)+"x"+CommonUtil.getAppHeight(this));
		EventBus.getDefault().register(this);

		countDownSholatReminderUtils = new CountDownSholatReminderUtils();


	}

	private void reloadCounterImage() {
		long lastRead = Prefs.with(DzikirBoard.this).getLong("last-read:"+subHeaderTitle+":"+bacaanList.get(navHandler.getIndex()), 0);
		Date lastReadDate = new Date();
		lastReadDate.setTime(lastRead);
		if(DateStringUtil.compareToDay(lastReadDate,new Date(), Locale.getDefault())==0){
			Resources r = getResources();
			Drawable[] layers = new Drawable[2];
			layers[0] = r.getDrawable(counterList.get(navHandler.getIndex()));
			layers[1] = r.getDrawable(R.drawable.icon_rate_this_app);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			countImageView.getImageView().setImageDrawable(layerDrawable);
		}
	}

	@Override
	public void onCreateMovableMenu(ASMovableMenu menu) {
		super.onCreateMovableMenu(menu);

		Button doneButton = new Button(this);
		doneButton.setText("Tandai sudah dibaca");
		menu.addItemMenu(doneButton, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delayedAction.goNow();
				CommonUtil.showToast(DzikirBoard.this, "Bacaan sudah berhasil ditandai");
			}
		}, null);

		Button doneNextButton = new Button(this);
		doneNextButton.setText("Tandai sudah dibaca & lanjut berikutnya");
		menu.addItemMenu(doneNextButton, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delayedAction.goNow();
				navHandler.next();
				CommonUtil.showToast(DzikirBoard.this, "Bacaan sudah berhasil ditandai");
			}
		}, null);
		
		Button pageSelection = new Button(this);
		pageSelection.setText("Pilih halaman");
		menu.addItemMenu(pageSelection, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				slidingPageSelectorCanvas.getShiftPositionHandler().changeStateToDimension(1, true);
			}
		}, /*new Rect(5, 30, 5+90, 30+menu.getTextHeight("goto page ..."))*/ null);

		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			recreate();
		}else{
			finish();
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		Prefs.with(this).save(savedPageKey, navHandler.getIndex());
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	

	@Override
	protected void onStop() {
		if (soundView != null)
			soundView.setImageResource(R.drawable.play);
		AudioService.stopAudioSound(this);
		if(BackSoundService.getLastAction() == BackSoundService.ACTION_PAUSE){
			BackSoundService.resumeBackSound(DzikirBoard.this);
		}
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(previousSavedPage > 0) {
			CommonUtil.showDialog2Option(this, "Perhatian", "Apakah anda ingin melanjutkan sesi sebelumnya?",
					"Ya", new Runnable() {
						@Override
						public void run() {
							//do nothing
							if(Prefs.with(DzikirBoard.this).getBoolean("standby", false)){
								keepScreenOnSwitch.setChecked(true);
							}
						}
					}, "Tidak", new Runnable() {
						@Override
						public void run() {
							previousSavedPage = 0;
							navHandler.setIndex(0);
						}
					});
		}
	}

	@Override
	public void onBackPressed() {
		if(slidingPageSelectorCanvas.getShiftPositionHandler().getDimensionState() == 1){
			slidingPageSelectorCanvas.getShiftPositionHandler().changeStateToDimension(0, true);
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (AudioService.getLastAction() == AudioService.ACTION_PAUSE) {
			if (soundView != null)
				soundView.setImageResource(R.drawable.stop);
			AudioService.resumeAudioSound(this);
			if(BackSoundService.isPlaying()){
				BackSoundService.pauseBackSound(DzikirBoard.this);
			}
		} else {
			if (soundView != null)
				soundView.setImageResource(R.drawable.play);
		}

		countDownSholatReminderUtils.startCountDown(this,countDownTimerHeaderText);
		delayedAction.go();
	}

	@Override
	protected void onPause() {
		super.onPause();
		delayedAction.pause();
		countDownSholatReminderUtils.stopCountDown();
	}

	@Subscribe
	public void onEvent(AudioServiceCallBack event){
		if(event.getState() == AudioService.ACTION_NONE || event.getState() == AudioService.ACTION_PAUSE || event.getState() == AudioService.ACTION_STOP){
			soundView.setImageResource(R.drawable.play);
		}
	}


	class DelayedAction{
		private int delayedInMS=0;
		private Handler handler = new Handler();
		private Runnable action;

		public DelayedAction(int delayedInMS, Runnable action){
			this.delayedInMS = delayedInMS;
			this.action = action;
		}

		public void go(){
			if(action != null) {
				handler.removeCallbacks(action);
				handler.postDelayed(action, delayedInMS);
			}
		}


		public void pause(){
			if(action != null) {
				handler.removeCallbacks(action);
			}
		}

		public void goNow(){
			if(action != null) {
				handler.removeCallbacks(action);
				action.run();
			}
		}
	}
}
