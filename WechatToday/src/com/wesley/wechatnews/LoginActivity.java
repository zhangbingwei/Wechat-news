package com.wesley.wechatnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			}
		});
	}

	public void btnLogin(View view) {
		Toast.makeText(this, "登录成功，功能暂时没做", Toast.LENGTH_SHORT).show();
	}

}
