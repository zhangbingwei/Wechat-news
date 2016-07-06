package com.wesley.wechatnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;

public class MyPhotosActivity extends Activity {

	// private GridView gvPhoto;

	// private int[] photos = new int[] { R.drawable.p1, R.drawable.p2,
	// R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6,
	// R.drawable.p7, R.drawable.p8, R.drawable.p9, R.drawable.p10,
	// R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14,
	// R.drawable.p15, R.drawable.p16 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myphoto);

		// gvPhoto = (GridView) findViewById(R.id.gv_photo);
		// gvPhoto.setAdapter(new MyPhotoAdapter());

	}

	// public class MyPhotoAdapter extends BaseAdapter {
	//
	// @Override
	// public int getCount() {
	// return photos.length;
	// }
	//
	// @Override
	// public Object getItem(int arg0) {
	// return photos[arg0];
	// }
	//
	// @Override
	// public long getItemId(int arg0) {
	// return arg0;
	// }
	//
	// @Override
	// public View getView(int arg0, View convertView, ViewGroup arg2) {
	// if (convertView == null) {
	// convertView = View.inflate(MyPhotosActivity.this,
	// R.layout.item_grid_view, null);
	// }
	// ImageView ivPhoto = (ImageView) convertView
	// .findViewById(R.id.iv_photo);
	// Picasso.with(MyPhotosActivity.this).load(photos[arg0])
	// .into(ivPhoto);
	// return convertView;
	// }
	//
	// }

}
