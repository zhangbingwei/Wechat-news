package com.wesley.wechatnews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wesley.wechatnews.WechatNewsData.Data;

public class NewsDetailActivity extends Activity {

	private WebView wvNews;
	private ProgressBar pbLoading;
	private ImageButton ibBack;
	private ImageButton ibSave;
	private ImageButton ibShare;
	private TextView tvTitle;

	private WeChatNewsDB mWeChatNewsDB;
	private String title;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		// 获取到数据库操作对象
		mWeChatNewsDB = WeChatNewsDB.getInstance(this);

		wvNews = (WebView) findViewById(R.id.wv_news);
		pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
		ibBack = (ImageButton) findViewById(R.id.ib_back);

		// 系统自带的分享功能
		ibShare = (ImageButton) findViewById(R.id.ib_share);
		ibShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				share();
			}
		});

		tvTitle = (TextView) findViewById(R.id.tv_title);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(NewsDetailActivity.this,
						MainActivity.class));
				finish();
			}
		});

		ibSave = (ImageButton) findViewById(R.id.ib_save);

		// 获取到从主页面跳转过来的新闻对象
		final Data data = (Data) getIntent().getSerializableExtra("data");

		// 进入文章详情页，判断是否为收藏的，如果收藏文章设置按钮为红色
		if (mWeChatNewsDB.isFavourite(data)) {
			ibSave.setBackgroundResource(R.drawable.fav_active);
		} else {
			ibSave.setBackgroundResource(R.drawable.fav_normal);
		}

		// 点击收藏按钮，文章的id，
		ibSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isFav = mWeChatNewsDB.isFavourite(data);
				if (isFav) {
					mWeChatNewsDB.deleteFavourite(data);
					ibSave.setBackgroundResource(R.drawable.fav_normal);
					// System.out.println("删除的文章是：" + data);
					Toast.makeText(NewsDetailActivity.this, "删除成功",
							Toast.LENGTH_SHORT).show();
				} else {
					mWeChatNewsDB.saveFavourite(data);
					ibSave.setBackgroundResource(R.drawable.fav_active);
					// System.out.println("收藏的文章是：" + data);
					Toast.makeText(NewsDetailActivity.this, "收藏成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		String source = data.source;
		tvTitle.setText(source);// 设置标题栏文字

		title = data.title;

		// String url = getIntent().getStringExtra("url");
		url = data.url; // 从传递过来的对象里获取到url
		// System.out.println("新闻详情页的链接URL是：：：：" + url);

		wvNews.loadUrl(url);
		wvNews.getSettings().setJavaScriptEnabled(true);

		wvNews.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				pbLoading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pbLoading.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	public void share() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, "微信精选:" + title + " " + url);
		shareIntent.setType("*/*");

		// 设置分享列表的标题，并且每次都显示分享列表
		startActivity(Intent.createChooser(shareIntent, "分享到"));
	}
}
