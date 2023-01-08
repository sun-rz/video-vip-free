package com.video.vip.player.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebSettingsImpl;
import com.just.agentweb.DefaultWebClient;
import com.video.vip.player.widget.CoolIndicatorLayout;

/**
 * Created by cenxiaozhong on 2017/5/26.
 * source code  https://github.com/Justson/AgentWeb
 */

public class CustomIndicatorFragment extends AgentWebFragment {
	public static CustomIndicatorFragment getInstance(Bundle bundle) {
		CustomIndicatorFragment mCustomIndicatorFragment = new CustomIndicatorFragment();
		if (bundle != null){
			mCustomIndicatorFragment.setArguments(bundle);
		}
		return mCustomIndicatorFragment;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

//        CommonIndicator mCommonIndicator=new CommonIndicator(this.getActivity());
//        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(-2,-2);
//        lp.gravity= Gravity.CENTER;
//        ProgressBar mProgressBar=new ProgressBar(this.getActivity());
//        mProgressBar.setBackground(this.getResources().getDrawable(R.drawable.indicator_shape));
//        mCommonIndicator.addView(mProgressBar,lp);


		CoolIndicatorLayout mCoolIndicatorLayout = new CoolIndicatorLayout(this.getActivity());
		this.mAgentWeb = AgentWeb.with(this)//
				.setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
				.setCustomIndicator(mCoolIndicatorLayout)
				.setAgentWebWebSettings(AgentWebSettingsImpl.getInstance())
				.setWebViewClient(mWebViewClient)
				.setPermissionInterceptor(mPermissionInterceptor)
				.setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
				.interceptUnkownUrl()
				.setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
				.createAgentWeb()//
				.ready()//
				.go(getUrl());


		initView(view);
	}
}
