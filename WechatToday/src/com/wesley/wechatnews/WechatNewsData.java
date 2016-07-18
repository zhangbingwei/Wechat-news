package com.wesley.wechatnews;

import java.io.Serializable;
import java.util.List;

public class WechatNewsData {
	public int error_code;
	public String reason;
	public Result result;

	public class Result {
		public int pno;// 当前的页：1
		public int ps; // 一页显示的数据量是：20
		public int totalPage; // 总的页数是：25
		public List<Data> list;

	}

	public class Data implements Serializable {
		public String id;
		public String firstImg;
		public String title;
		public String source;
		public String url;

		public Data() {
			super();
		}

		public Data(String id, String firstImg, String title, String source,
				String url) {
			this.id = id;
			this.firstImg = firstImg;
			this.title = title;
			this.source = source;
			this.url = url;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getFirstImg() {
			return firstImg;
		}

		public void setFirstImg(String firstImg) {
			this.firstImg = firstImg;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
