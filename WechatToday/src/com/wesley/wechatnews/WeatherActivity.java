package com.wesley.wechatnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.wesley.wechatnews.WeatherData.Today;

public class WeatherActivity extends Activity {

	private TextView tvDate;
	private TextView tvTemp;
	private TextView tvWeather;
	private TextView tvWind;
	private TextView tvTravel;
	private TextView tvExe;
	private TextView tvDress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);

		tvDate = (TextView) findViewById(R.id.tv_date);
		tvTemp = (TextView) findViewById(R.id.tv_temp);
		tvWeather = (TextView) findViewById(R.id.tv_weather);
		tvWind = (TextView) findViewById(R.id.tv_wind);
		tvTravel = (TextView) findViewById(R.id.tv_travel);
		tvExe = (TextView) findViewById(R.id.tv_exe);
		tvDress = (TextView) findViewById(R.id.tv_dress);

		Today todayWeather = (Today) getIntent().getSerializableExtra(
				"todayWeather");

		tvDate.setText(todayWeather.date_y + "  " + todayWeather.week);
		tvTemp.setText("气温：" + todayWeather.temperature);
		tvWeather.setText("天气：" + todayWeather.weather);
		tvWind.setText("风力：" + todayWeather.wind);
		tvTravel.setText("旅行：" + todayWeather.travel_index);
		tvExe.setText("锻炼：" + todayWeather.exercise_index);
		tvDress.setText(todayWeather.dressing_advice);
	}
}
