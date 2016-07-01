package com.wesley.wechatnews;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wesley.wechatnews.NewsData.Data;

public class WeChatNewsDB {

	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private static WeChatNewsDB mWeChatNewsDB;

	private WeChatNewsDB(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		// 清除数据库中的数据
		// dbHelper.onUpgrade(db, 1, 1);
	}

	// 同步：创建数据库实例
	public synchronized static WeChatNewsDB getInstance(Context context) {
		if (mWeChatNewsDB == null) {
			mWeChatNewsDB = new WeChatNewsDB(context);
		}
		return mWeChatNewsDB;
	}

	/**
	 * 把阅读过的新闻id保存到数据库的news_read表
	 * 
	 * @param news_id
	 */
	public void saveReadID(String news_id) {
		if (news_id != null) {
			ContentValues values = new ContentValues();
			values.put("news_id", news_id);
			db.insert("news_read", null, values);
		}
	}

	/**
	 * 获取度过的所有新闻的id
	 */
	public List<String> loadAllReadID() {
		List<String> newsID_list = new ArrayList<String>();
		Cursor cursor = db.query("news_read", null, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				String news_id = cursor.getString(1);
				newsID_list.add(news_id);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return newsID_list;
	}

	/**
	 * 保存收藏的文章对象到数据库
	 * 
	 * @param news
	 */
	public void saveFavourite(Data news) {
		if (news != null) {
			ContentValues values = new ContentValues();
			values.put("news_id", news.id);
			values.put("news_title", news.title);
			values.put("news_image", news.firstImg);
			values.put("news_source", news.source);
			values.put("news_url", news.url);
			db.insert("news_fav", null, values);
		}
	}

	/**
	 * 获取所有保存的新闻对象列表
	 * 
	 * @return
	 */
	public List<Data> loadFavourite() {
		List<Data> favouriteList = new ArrayList<Data>();
		Cursor cursor = db
				.query("news_fav", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Data news = new NewsData().new Data();
				news.setId(cursor.getString(cursor.getColumnIndex("news_id")));
				news.setTitle(cursor.getString(cursor
						.getColumnIndex("news_title")));
				news.setFirstImg(cursor.getString(cursor
						.getColumnIndex("news_image")));
				news.setSource(cursor.getString(cursor
						.getColumnIndex("news_source")));
				news.setUrl(cursor.getString(cursor.getColumnIndex("news_url")));
				favouriteList.add(news);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return favouriteList;
	}

	/**
	 * 判断新闻是否为已收藏
	 * 
	 * @param news
	 * @return
	 */
	public boolean isFavourite(Data news) {
		Cursor cursor = db.query("news_fav", null, "news_id = ?",
				new String[] { news.getId() }, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除收藏的文章
	 * 
	 * @param news
	 */
	public void deleteFavourite(Data news) {
		if (news != null) {
			db.delete("news_fav", "news_id = ?", new String[] { news.getId() });
		}
	}

	public synchronized void closeDB() {
		if (mWeChatNewsDB != null) {
			db.close();
		}
	}
}
