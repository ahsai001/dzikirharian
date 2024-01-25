package com.zaitunlabs.dzikirharian.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.squareup.picasso.Picasso;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.utils.CommonUtil;
import com.zaitunlabs.zlcore.utils.DateStringUtil;
import com.zaitunlabs.zlcore.utils.DebugUtil;
import com.zaitunlabs.zlcore.utils.Prefs;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.TopCropImageView;

public class PageListAdapter extends BaseAdapter{
	ArrayList<String> imageList = null;
	Context context = null;
	int height = LayoutParams.WRAP_CONTENT;
	String subHeaderTitle;

	public PageListAdapter(Context context, ArrayList<String> imageList, String subHeaderTitle) {
		this.imageList = imageList;
		this.context = context;
		/*height = 150;
		switch (CommonUtil.getDisplayMetricsDensityDPIInString(context).toLowerCase()){
			case "ldpi":
				height = 40;
				break;
			case "mdpi":
				height = 60;
				break;
			case "hdpi":
				height = 90;
				break;
			case "xhdpi":
				height = 120;
				break;
			case "xxhdpi":
				height = 180;
				break;
			case "xxxhdpi":
				height = 270;
				break;
		}*/

		this.subHeaderTitle = subHeaderTitle;
	}
	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int position) {
		return imageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return imageList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		boolean isDone = false;
		long lastRead = Prefs.with(context, false).getLong("last-read:"+subHeaderTitle+":"+position, 0);
		Date lastReadDate = new Date();
		lastReadDate.setTime(lastRead);
		if(DateStringUtil.compareToDay(lastReadDate,new Date(), Locale.getDefault())==0){
			isDone = true;
		}

		if(convertView == null){
			holder = new ViewHolder(new ImageView(context), new ASTextView(context), new ImageView(context));
			//holder.getIv().setAdjustViewBounds(true);
			//holder.getIv().setScaleType(ScaleType.FIT_XY);

			holder.getNumber().setTextSize(24);
			holder.getNumber().setGravity(Gravity.CENTER);
			holder.getNumber().setTypeface(null, Typeface.BOLD);

			LinearLayout ll = new LinearLayout(context);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setWeightSum(10);
			ll.setGravity(Gravity.CENTER_VERTICAL);
			//ll.setMinimumHeight(CommonUtil.getAppHeight(context)/6);

			ll.addView(holder.getNumber(), new LinearLayout.LayoutParams(0, height, 2));
			ll.addView(holder.getImageView(), new LinearLayout.LayoutParams(0, height, 2));
			ll.addView(holder.getIv(), new LinearLayout.LayoutParams(0, height, 8));
			ll.setTag(holder);
			convertView = ll;
			Log.e("ahmad", "new view");
		}else{
			holder = (ViewHolder) convertView.getTag();
			Log.e("ahmad", "recycle view");
		}

		//Point dim = CommonUtil.getImageDimension(context, imageList.get(position));
		//Bitmap bmp = BitmapDecoder.from(context.getResources(), imageList.get(position)).region(new Rect(dim.x/2, 0, dim.x/2, dim.y)).decode();

		/*
		if(CommonUtil.isOdd(position)){
			convertView.setBackgroundColor(Color.argb(200, 0, 0, 0));
		}else{
			convertView.setBackgroundColor(Color.argb(50, 0, 0, 0));
		}
		*/

		//DebugUtil.logW("GETVIEW", "position >> "+position);

		//set content
		String bacaanString = imageList.get(position);
		String[] bacaanArray = bacaanString.split(",");

		//Log.e("ahmad", bacaanString);
		//Log.e("ahmad", bacaanArray[0]);

		if(bacaanArray.length == 4){ //handle dzikir sesudah sholat alhamdu, subhanallah, allahuakbar, laa ilaha
			Picasso.get().load(CommonUtil.getIDResource(context, "drawable", bacaanArray[0].replace(".png", "_preview.png"))).into(holder.getIv());
		}else {
			Picasso.get().load(CommonUtil.getIDResource(context, "drawable", bacaanString.replace(".png", "_preview.png"))).into(holder.getIv());
		}


		if(isDone){
			holder.getNumber().setVisibility(View.GONE);
			holder.getImageView().setVisibility(View.VISIBLE);
			Picasso.get().load(R.drawable.icon_rate_this_app).into(holder.getImageView());

		} else {
			holder.getNumber().setVisibility(View.VISIBLE);
			holder.getImageView().setVisibility(View.GONE);
			holder.getNumber().setText("" + (position + 1));
		}
		return convertView;
	}



	public static class ViewHolder{
		ImageView iv = null;
		ASTextView number = null;
		ImageView imageView = null;
		public ImageView getIv() {
			return iv;
		}
		public ASTextView getNumber() {
			return number;
		}
		public ViewHolder(ImageView iv, ASTextView number, ImageView imageView) {
			this.iv = iv;
			this.number = number;
			this.imageView = imageView;

			//
			this.iv.setAdjustViewBounds(true);
			this.imageView.setAdjustViewBounds(true);
		}

		public ImageView getImageView() {
			return imageView;
		}
	}

}
