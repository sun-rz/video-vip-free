package com.video.vip.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.just.agentweb.BaseIndicatorView;

/**
 * Created by cenxiaozhong on 2017/5/26.
 * source code  https://github.com/Justson/AgentWeb
 */

public class CommonIndicator extends BaseIndicatorView {
	public CommonIndicator(Context context) {
		super(context);
	}

	public CommonIndicator(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public CommonIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	public void show() {
		this.setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		this.setVisibility(View.GONE);
	}


	@Override
	public LayoutParams offerLayoutParams() {
		return new LayoutParams(-1, -1);
	}
}
