package com.wesley.wechatnews;

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

	public class Today {

		public String temperature;

		public String weather;

		public String date_y;

		public String week;

	}

	@Override
	public String toString() {
		return "WeatherData [result=" + result + ", resultcode=" + resultcode
				+ "]";
	}

}
