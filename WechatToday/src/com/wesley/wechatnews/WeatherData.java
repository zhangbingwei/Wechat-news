package com.wesley.wechatnews;

import java.io.Serializable;

public class WeatherData {

	public Result result;

	public String resultcode;

	public class Result {
		public Today today;

		@Override
		public String toString() {
			return "Result [today=" + today + "]";
		}

	}

	public class Today implements Serializable {

		public String temperature;// 温度

		public String weather;// 天气

		public String date_y;// 日期

		public String week;// 星期

		public String wind;// 风力

		public String dressing_advice;// 穿衣建议

		public String travel_index;// 旅行

		public String exercise_index;// 锻炼

	}

	@Override
	public String toString() {
		return "WeatherData [result=" + result + ", resultcode=" + resultcode
				+ "]";
	}

}
