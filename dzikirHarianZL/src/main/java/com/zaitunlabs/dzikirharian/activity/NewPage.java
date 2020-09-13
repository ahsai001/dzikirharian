package com.zaitunlabs.dzikirharian.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.zlcore.core.CanvasActivity;
import com.zaitunlabs.zlcore.views.ASShiftableImageView;
import com.zaitunlabs.zlcore.views.CanvasLayout;
import com.zaitunlabs.zlcore.views.CanvasSection;
import com.zaitunlabs.zlcore.views.TalkyCounterView;

public class NewPage extends CanvasActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		LinearLayout contentPanel = findViewById(R.id.contentPanel);

		final TalkyCounterView tcvCounter = findViewById(R.id.tcvCounter);
		Button btnIncrement = findViewById(R.id.btnIncrement);

		btnIncrement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tcvCounter.increment();
			}
		});

		Button btnReset = findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tcvCounter.reset();
			}
		});

		CanvasLayout canvas = new CanvasLayout(this);
		canvas.embedTo(contentPanel, new CanvasLayout.OnLayoutReady() {
			@Override
			public void ready(CanvasLayout canvasLayout) {
				CanvasSection mainSection =
						canvasLayout.createNewSectionWithFrame("main", 0, 0, 100, 50, false);
				mainSection.setBackgroundColor(Color.BLUE);

				ASShiftableImageView shiftableImageView = ASShiftableImageView.create(canvasLayout,0,0,10,10);
				shiftableImageView.getImageView().setImageResource(R.drawable.masjid);
			}
		});





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
