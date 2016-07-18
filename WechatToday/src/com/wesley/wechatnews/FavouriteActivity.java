package com.wesley.wechatnews;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.wesley.wechatnews.WechatNewsData.Data;

public class FavouriteActivity extends Activity {

	// 返回按钮
	private ImageButton ibBack;

	private ListView lvFavourite;

	private WeChatNewsDB mWeChatNewsDB;
	private FavouriteAdapter adapter;

	private List<Data> favouriteList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favourite);

		mWeChatNewsDB = WeChatNewsDB.getInstance(this);

		favouriteList = mWeChatNewsDB.loadFavourite();

		ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(FavouriteActivity.this,
						MainActivity.class));
				finish();
			}
		});

		lvFavourite = (ListView) findViewById(R.id.lv_favourite);

		adapter = new FavouriteAdapter();
		lvFavourite.setAdapter(adapter);

		lvFavourite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Data news = favouriteList.get(position);
				Intent intent = new Intent(FavouriteActivity.this,
						FavouriteDetailActivity.class);
				// 将数据传递到收藏文章的详情页
				intent.putExtra("url", news.url);
				intent.putExtra("image", news.firstImg);
				intent.putExtra("id", news.id);
				intent.putExtra("source", news.source);
				intent.putExtra("title", news.title);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		favouriteList = mWeChatNewsDB.loadFavourite();
		adapter.notifyDataSetChanged();
	}

	class FavouriteAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return favouriteList.size();
		}

		@Override
		public Data getItem(int position) {
			return favouriteList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BitmapUtils bitmap = new BitmapUtils(FavouriteActivity.this);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(FavouriteActivity.this,
						R.layout.item_list_fav_view, null);
				holder.ivPhoto = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvFrom = (TextView) convertView
						.findViewById(R.id.tv_from);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Data item = getItem(position);
			if (item != null) {
				if (!TextUtils.isEmpty(item.firstImg)) {
					bitmap.display(holder.ivPhoto, item.firstImg);
				}
				holder.tvTitle.setText(item.title);
				holder.tvFrom.setText(item.source);
			}

			return convertView;
		}

	}

	class ViewHolder {
		ImageView ivPhoto;
		TextView tvTitle;
		TextView tvFrom;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
