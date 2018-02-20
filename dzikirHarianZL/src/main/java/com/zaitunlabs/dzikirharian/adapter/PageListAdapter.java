package com.zaitunlabs.dzikirharian.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zaitunlabs.zlcore.utils.CommonUtils;
import com.zaitunlabs.zlcore.views.ASTextView;
import com.zaitunlabs.zlcore.views.TopCropImageView;

public class PageListAdapter extends BaseAdapter{
	ArrayList<String> imageList = null;
	Context context = null;
	int height = LayoutParams.WRAP_CONTENT;
	public PageListAdapter(Context context, ArrayList<String> imageList) {
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
		if(convertView == null){
			holder = new ViewHolder(new TopCropImageView(context), new ASTextView(context));
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
			ll.addView(holder.getIv(), new LinearLayout.LayoutParams(0, height, 8));
			ll.setTag(holder);
			convertView = ll;
		}else{
			holder = (ViewHolder) convertView.getTag();
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
		if(bacaanArray.length == 4){ //handle dzikir sesudah sholat alhamdu, subhanallah, allahuakbar, laa ilaha
			ImageLoader.getInstance().displayImage("drawable://" +CommonUtils.getIDResource(context, "drawable", bacaanArray[0]), holder.getIv());
		}else {
			ImageLoader.getInstance().displayImage("drawable://" +CommonUtils.getIDResource(context, "drawable", bacaanString), holder.getIv());
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
		
		holder.getNumber().setText(""+(position+1));
		return convertView;
	}
	
	private class ViewHolder{
		TopCropImageView iv = null;
		ASTextView number = null;
		public TopCropImageView getIv() {
			return iv;
		}
		public ASTextView getNumber() {
			return number;
		}
		public ViewHolder(TopCropImageView iv, ASTextView number) {
			this.iv = iv;
			this.number = number;
		}
		
	}

}
