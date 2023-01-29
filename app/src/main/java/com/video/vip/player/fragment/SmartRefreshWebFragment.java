package com.video.vip.player.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IWebLayout;
import com.video.vip.player.R;
import com.video.vip.player.common.CustomSettings;
import com.video.vip.player.widget.SmartRefreshWebLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Created by cenxiaozhong on 2017/7/1.
 * source code  https://github.com/Justson/AgentWeb
 */

public class SmartRefreshWebFragment extends BounceWebFragment {

    public static SmartRefreshWebFragment getInstance(Bundle bundle) {

        SmartRefreshWebFragment mSmartRefreshWebFragment = new SmartRefreshWebFragment();
        if (mSmartRefreshWebFragment != null) {
            mSmartRefreshWebFragment.setArguments(bundle);
        }

        return mSmartRefreshWebFragment;
    }

    private SmartRefreshWebLayout mSmartRefreshWebLayout = null;

    @Override
    public String getUrl() {
        return super.getUrl();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView mSearchImageView = view.findViewById(R.id.iv_search);
        ImageView mJxImageView = view.findViewById(R.id.iv_jx);
        Bundle bundle = this.getArguments();
        if (null != mSearchImageView) {
            boolean aBoolean = bundle.getBoolean(SHOW_SEARCH_KEY);
            if (!aBoolean) {
                mSearchImageView.setVisibility(View.GONE);
            }
        }
        if (null != mJxImageView) {
            boolean aBoolean = bundle.getBoolean(SHOW_PLAYER_KEY);
            if (!aBoolean) {
                mJxImageView.setVisibility(View.GONE);
            }
        }
        super.onViewCreated(view, savedInstanceState);

        final SmartRefreshLayout mSmartRefreshLayout = (SmartRefreshLayout) this.mSmartRefreshWebLayout.getLayout();

        final WebView mWebView = this.mSmartRefreshWebLayout.getWebView();
        mSmartRefreshLayout.setOnRefreshListener(refreshlayout -> {
            mAgentWeb.getUrlLoader().reload();

            mSmartRefreshLayout.postDelayed(mSmartRefreshLayout::finishRefresh, 2000);
        });
        mSmartRefreshLayout.autoRefresh();


    }


    @Override
    protected IWebLayout getWebLayout() {
        return this.mSmartRefreshWebLayout = new SmartRefreshWebLayout(this.getActivity());
    }


    @Override
    protected void addBGChild(FrameLayout frameLayout) {

        frameLayout.setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    public IAgentWebSettings getSettings() {
        return new CustomSettings(getActivity());
    }
}
