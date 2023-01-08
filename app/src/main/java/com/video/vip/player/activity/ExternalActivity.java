package com.video.vip.player.activity;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;

/**
 * @author cenxiaozhong
 * @date 2019-05-19
 * @since 1.0.0
 */
public class ExternalActivity extends WebActivity {

	public static final String TAG = ExternalActivity.class.getSimpleName();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public String getUrl() {
		String url = getIntent().getData().getQueryParameter("url");
		Log.e(TAG, " url:" + url);
		return url;

	}
}
