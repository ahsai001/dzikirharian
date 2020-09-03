package com.zaitunlabs.dzikirharian.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.services.DataIntentService;
import com.zaitunlabs.zlcore.utils.CommonUtil;
import com.zaitunlabs.zlcore.utils.HttpClientUtil;
import com.zaitunlabs.zlcore.utils.NotificationProgressUtil;
import com.zaitunlabs.zlcore.utils.PermissionUtil;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;

import org.json.JSONObject;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Map;

import id.web.michsan.praytimes.Configuration;
import id.web.michsan.praytimes.Location;
import id.web.michsan.praytimes.Method;
import id.web.michsan.praytimes.PrayTimes;
import id.web.michsan.praytimes.Util;

public class NewPage extends CanvasActivity {
	private String selectedImage;
	PermissionUtil permissionUtils;
	TextView jadwalView;
	EditText tagEditText;
	EditText titleEditText;
	EditText descEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final CanvasLayout canvas = new CanvasLayout(this);
		//create main section
		final CanvasSection mainSection = canvas.createNewSectionWithFrame("main", 0, 0, 100, 100, false)
				.setSectionAsLinearLayout(LinearLayout.VERTICAL);
		mainSection.setBackgroundColor(Color.BLUE);

		jadwalView = new TextView(this);

		mainSection.addViewInLinearLayout(jadwalView);

		Button button = new Button(this);
		button.setText("upload");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				permissionUtils = PermissionUtil.checkPermissionAndGo(NewPage.this, 1003,
						new Runnable() {
							@Override
							public void run() {
								Intent pickPhoto = new Intent(Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(pickPhoto , 1000);
							}
						}, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}
		});


		mainSection.addViewInLinearLayout(button);

		tagEditText = new EditText(this);
		tagEditText.setHint("tag");
		mainSection.addViewInLinearLayout(tagEditText);

		titleEditText = new EditText(this);
		titleEditText.setHint("title");
		mainSection.addViewInLinearLayout(titleEditText);


		descEditText = new EditText(this);
		descEditText.setHint("desc");
		mainSection.addViewInLinearLayout(descEditText);

		setContentView(canvas.getFillParentView());
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}



	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}



	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case 1000:
				if(resultCode == RESULT_OK){
					selectedImage = getRealPathFromURI(this,data.getData());

					DataIntentService.HeaderParts headerParts= new DataIntentService.HeaderParts();
					headerParts.addItem("x-device",CommonUtil.getMeid(this));

					DataIntentService.BodyParts bodyParts = new DataIntentService.BodyParts();
					bodyParts.addItem("title",titleEditText.getText().toString());
					bodyParts.addItem("tags",tagEditText.getText().toString());
					bodyParts.addItem("desc",descEditText.getText().toString());

					DataIntentService.FileParts fileParts = new DataIntentService.FileParts();
					fileParts.addItem("image", new File(selectedImage));

					DataIntentService.startUpload(this, "http://gallery.ayaindonesia.com/api/saveGallery",R.drawable.icon,"Upload Image","Progress",
							24,fileParts,headerParts,bodyParts,null,false,"upload");


				}
				break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
