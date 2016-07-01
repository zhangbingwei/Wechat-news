package com.wesley.wechatnews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wesley.wechatnews.NewsData.Data;

public class MainActivity extends Activity {

	public static final String URL = "http://v.juhe.cn/weixin/query?pno=&ps=&dtype=&key=57a8c271dd6d486d2609c7505106ce2d";

	private WeChatNewsDB weChatNewsDB; // 数据库对象

	private PullToRefreshListView lvNews;

	private NewsData mNewsData; // 获取的数据对象

	private NewsData.Result mNewsResult; // 从接口中获取的数据结果

	private List<Data> list;

	private NewsAdapter adapter; // ListView适配器
	private TextView tvWeek; // 显示星期
	private ImageButton ibSave; // 收藏按钮

	// 当前第几页
	private int currentPage = 1;
	// 每页的条目
	private int itemCount;
	// 总共多少页
	private int totalPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.refresh_activity_main);

		weChatNewsDB = WeChatNewsDB.getInstance(this);
		adapter = new NewsAdapter();

		tvWeek = (TextView) findViewById(R.id.tv_week);
		// 设置当前星期几
		tvWeek.setText(getWeek());

		ibSave = (ImageButton) findViewById(R.id.ib_save);
		// 主页面点击收藏按钮跳转到收藏页面
		ibSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this,
						FavouriteActivity.class));
			}
		});

		lvNews = (PullToRefreshListView) findViewById(R.id.lv_news);

		if (NetworkUtils.checkNetworkConnection(MainActivity.this)) {
			getDataFromServer(URL);
		} else {
			Toast.makeText(MainActivity.this, "网络不可用,请先开启网络!",
					Toast.LENGTH_SHORT).show();
		}

		// ListView的下拉刷新和上拉加载功能
		lvNews.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// Log.e("TAG", "onPullDownToRefresh");

				// 显示下拉刷新时间
				String label = DateUtils.formatDateTime(MainActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// 这里写下拉刷新的任务
				String pullDownUrl = "http://v.juhe.cn/weixin/query?" + "pno="
						+ (++currentPage)
						+ "&ps=&dtype=&key=57a8c271dd6d486d2609c7505106ce2d";
				// System.out.println("下拉刷新：当前是第几页呢：：：" + currentPage +
				// ",总共的页数是："
				// + totalPage);
				if (currentPage <= totalPage) {
					getMoreDataFromServer(pullDownUrl, true);
				} else {
					noDataSleep();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// Log.e("TAG", "onPullUpToRefresh");

				// 显示上拉加载时间
				String label = DateUtils.formatDateTime(MainActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// 这里写上拉加载更多的任务
				String pullUpUrl = "http://v.juhe.cn/weixin/query?" + "pno="
						+ (++currentPage)
						+ "&ps=&dtype=&key=57a8c271dd6d486d2609c7505106ce2d";
				// System.out.println("上拉加载：当前是第几页呢：：：" + currentPage +
				// ",总共的页数是："
				// + totalPage);
				if (currentPage <= totalPage) {
					getMoreDataFromServer(pullUpUrl, false);
				} else {
					noDataSleep();
				}

			}
		});

	}

	// 当下拉刷新或者上拉加载没有更多数据的时候休眠
	public void noDataSleep() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SystemClock.sleep(3000);
			}
		});
		lvNews.onRefreshComplete();
		// System.out.println("页数到达了最大页数，没有数据了。。。");
		Toast.makeText(MainActivity.this, "已经没有更多数据了", Toast.LENGTH_SHORT)
				.show();
	}

	private String getWeek() {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(String url) {
		HttpUtils utils = new HttpUtils();

		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;

				// 解析数据
				parseData(result, false, false);
				// System.out.println("执行的第一次解析数据，不是More。。。");

				if (list != null) {
					lvNews.setAdapter(adapter);
				}

				// 给ListView设置点击事件
				lvNews.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long arg3) {

						// 跳转到新闻详情页的时候，直接把新闻对象传递过去，这样可以在新闻详情页里进行收藏，保存对应的数据到数据库
						Intent intent = new Intent(MainActivity.this,
								NewsDetailActivity.class);
						Data data = list.get(position - 1);
						System.out.println("传递到详情页的position是：" + position);
						System.out.println("主页面ListView传递过去的新闻链接URL是：：："
								+ data.url);

						Bundle bundle = new Bundle();
						bundle.putSerializable("data", data);
						// intent.putExtra("url", list.get(position).url);
						intent.putExtras(bundle);
						startActivity(intent);

						String news_id = data.id;

						// 将阅读过的新闻id保存到数据库中，如果已经存在则不操作
						if (!weChatNewsDB.loadAllReadID().contains(news_id)) {
							weChatNewsDB.saveReadID(news_id);
						}

						// adapter.notifyDataSetChanged();
						// 实现局部界面刷新，这个view就是被点击的item布局对象
						changeReadState(view);
					}
				});

			}

			@Override
			public void onFailure(HttpException error, String msg) {

				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
				error.printStackTrace();
			}

		});
	}

	public void getMoreDataFromServer(String moreUrl, boolean isPullDown) {
		final boolean mIsPullDown = isPullDown;

		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				parseData(result, true, mIsPullDown);
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}
		});

	}

	/**
	 * 改变已读新闻的颜色
	 * 
	 * @param view
	 */
	public void changeReadState(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	/**
	 * 解析json数据
	 * 
	 * @param result
	 */
	protected void parseData(String result, boolean isMore, boolean isPullDown) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(result, NewsData.class);
		// System.out.println("从聚合数据获取到的结果是：" + mNewsData);
		mNewsResult = mNewsData.result;
		// System.out.println("the result is :" + mNewsResult);

		if (!isMore) {
			list = mNewsResult.list;
			currentPage = mNewsResult.pno;
			totalPage = mNewsResult.totalPage;
		} else {
			// 如果是下拉刷新，则将新增加的数据加到ListView的头部
			if (isPullDown) {
				List<Data> pullDownList = mNewsResult.list;
				currentPage = mNewsResult.pno;
				list.addAll(0, pullDownList);
				adapter.notifyDataSetChanged();
				lvNews.onRefreshComplete();
				Toast.makeText(MainActivity.this, "为您更新了20条新闻",
						Toast.LENGTH_SHORT).show();
			} else {
				// 如果是上拉加载，则将新增加的数据添加到ListView的底部
				List<Data> pullUpList = mNewsResult.list;
				currentPage = mNewsResult.pno;
				list.addAll(pullUpList);
				adapter.notifyDataSetChanged();
				lvNews.onRefreshComplete();
				Toast.makeText(MainActivity.this, "为您新加载了20条新闻",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * ListView的适配器
	 * 
	 * @author zhangbingwei
	 * 
	 */
	class NewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Data getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 获取网络图片
			BitmapUtils bitmap = new BitmapUtils(MainActivity.this);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(MainActivity.this,
						R.layout.item_list_view, null);
				holder.ivPhoto = (ImageView) convertView
						.findViewById(R.id.iv_photo);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvFrom = (TextView) convertView
						.findViewById(R.id.tv_from);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Data item = getItem(position);
			if (item != null) {
				String dateString = item.id.split("_")[1].substring(0, 8);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				Date date = null;
				try {
					date = dateFormat.parse(dateString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String time = sdf.format(date);

				if (!TextUtils.isEmpty(item.firstImg)) {
					bitmap.display(holder.ivPhoto, item.firstImg);
				}
				holder.tvTitle.setText(item.title);
				holder.tvFrom.setText(item.source);
				holder.tvDate.setText(time);

				List<String> allReadID = weChatNewsDB.loadAllReadID();
				// System.out.println("读取到的所有id是：" + allReadID);
				if (allReadID.contains(item.id)) {
					// System.out.println("包含在集合里面的id：" + item.id);
					holder.tvTitle.setTextColor(Color.GRAY);
				} else {
					holder.tvTitle.setTextColor(Color.BLACK);
				}
			}

			return convertView;
		}

	}

	class ViewHolder {
		ImageView ivPhoto;
		TextView tvTitle;
		TextView tvFrom;
		TextView tvDate;
	}

	private long exitTime = 0;

	/**
	 * 重写返回键功能
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if (System.currentTimeMillis() - exitTime > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次返回退出",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

}
