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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.utils.CommonUtils;
import com.zaitunlabs.zlcore.utils.DateStringUtils;
import com.zaitunlabs.zlcore.utils.DebugUtils;
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
		height = 150;
		switch (CommonUtils.getDisplayMetricsDensityDPIInString(context).toLowerCase()){
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
		}

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
		long lastRead = Prefs.with(context).getLong("last-read:"+subHeaderTitle+":"+imageList.get(position), 0);
		Date lastReadDate = new Date();
		lastReadDate.setTime(lastRead);
		if(DateStringUtils.compareToDay(lastReadDate,new Date(), Locale.getDefault())==0){
			isDone = true;
		}

		if(convertView == null){
			holder = new ViewHolder(new TopCropImageView(context), new ASTextView(context), new ImageView(context));
			//holder.getIv().setAdjustViewBounds(true);
			//holder.getIv().setScaleType(ScaleType.FIT_XY);

			holder.getNumber().setTextSize(24);
			holder.getNumber().setGravity(Gravity.CENTER);
			holder.getNumber().setTypeface(null, Typeface.BOLD);

			LinearLayout ll = new LinearLayout(context);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setWeightSum(10);
			ll.setGravity(Gravity.CENTER_VERTICAL);
			//ll.setMinimumHeight(CommonUtils.getAppHeight(context)/6);

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

		//Point dim = CommonUtils.getImageDimension(context, imageList.get(position));
		//Bitmap bmp = BitmapDecoder.from(context.getResources(), imageList.get(position)).region(new Rect(dim.x/2, 0, dim.x/2, dim.y)).decode();

		/*
		if(CommonUtils.isOdd(position)){
			convertView.setBackgroundColor(Color.argb(200, 0, 0, 0));
		}else{
			convertView.setBackgroundColor(Color.argb(50, 0, 0, 0));
		}
		*/

		//DebugUtils.logW("GETVIEW", "position >> "+position);

		//set content
		String bacaanString = imageList.get(position);
		String[] bacaanArray = bacaanString.split(",");

		Log.e("ahmad", bacaanString);
		Log.e("ahmad", bacaanArray[0]);

		if(bacaanArray.length == 4){ //handle dzikir sesudah sholat alhamdu, subhanallah, allahuakbar, laa ilaha
			//Picasso.get().load(CommonUtils.getIDResource(context, "drawable", bacaanArray[0])).into(holder.getIv());
			ImageLoader.getInstance().displayImage("drawable://"+CommonUtils.getIDResource(context, "drawable", bacaanArray[0]), holder.getIv());
			//holder.getIv().setImageResource(CommonUtils.getIDResource(context, "drawable", bacaanArray[0]));
			//Log.e("ahmad", "picasso in action");
		}else {
			//Picasso.get().load(CommonUtils.getIDResource(context, "drawable", bacaanString)).into(holder.getIv());
			ImageLoader.getInstance().displayImage("drawable://"+CommonUtils.getIDResource(context, "drawable", bacaanString), holder.getIv());
			//holder.getIv().setImageResource(CommonUtils.getIDResource(context, "drawable", bacaanString));
			//Log.e("ahmad", "picasso in action 2");
		}

		/*
		final TopCropImageView a = holder.getIv();
		ImageLoader.getInstance().loadImage("drawable://" + imageList.get(position), new ImageSize(172, 100), new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				a.setImageBitmap(loadedImage);
			}

		});
		*/
		//Picasso.with(context).load(imageList.get(position)).resize(172, 100).into(holder.getIv());
		//Picasso.with(context).load(imageList.get(position)).fit().centerCrop().into(holder.getIv());
		//holder.getIv().setImageResource(imageList.get(position));

		if(isDone){
			holder.getNumber().setVisibility(View.GONE);
			holder.getImageView().setVisibility(View.VISIBLE);
			//holder.getImageView().setImageResource(R.drawable.icon_rate_this_app);
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_rate_this_app, holder.getImageView());

		} else {
			holder.getNumber().setVisibility(View.VISIBLE);
			holder.getImageView().setVisibility(View.GONE);
			holder.getNumber().setText("" + (position + 1));
		}
		return convertView;
	}



	private class ViewHolder{
		TopCropImageView iv = null;
		ASTextView number = null;
		ImageView imageView = null;
		public TopCropImageView getIv() {
			return iv;
		}
		public ASTextView getNumber() {
			return number;
		}
		public ViewHolder(TopCropImageView iv, ASTextView number, ImageView imageView) {
			this.iv = iv;
			this.number = number;
			this.imageView = imageView;
		}

		public ImageView getImageView() {
			return imageView;
		}
	}

}
