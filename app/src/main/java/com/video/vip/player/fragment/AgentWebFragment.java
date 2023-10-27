package com.video.vip.player.fragment;


import android.content.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.webkit.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import com.codingending.popuplayout.PopupLayout;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.gson.Gson;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.*;
import com.video.vip.player.R;
import com.video.vip.player.activity.AppSettingActivity;
import com.video.vip.player.activity.VideoPlayerActivity;
import com.video.vip.player.app.App;
import com.video.vip.player.bean.ApiURLConfig;
import com.video.vip.player.client.MiddlewareChromeClient;
import com.video.vip.player.client.MiddlewareWebViewClient;
import com.video.vip.player.common.CommonWebChromeClient;
import com.video.vip.player.common.FragmentKeyDown;
import com.video.vip.player.common.UIController;
import com.video.vip.player.db.AppSettingManager;
import com.video.vip.player.filechooser.FileCompressor;
import com.video.vip.player.utils.FileUtils;
import top.zibin.luban.Luban;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by cenxiaozhong on 2017/5/15.
 * source code  https://github.com/Justson/AgentWeb
 */

public class AgentWebFragment extends Fragment implements FragmentKeyDown, FileCompressor.FileCompressEngine {

    private ImageView mBackImageView;
    private View mLineView;
    private ImageView mFinishImageView;
    private TextView mTitleTextView;
    protected AgentWeb mAgentWeb;

    public static final String URL_KEY = "url_key";
    public static final String SHOW_SEARCH_KEY = "show_search";
    public static final String SHOW_PLAYER_KEY = "show_player";
    public static final String SHOW_TP_KEY = "show_tp";
    public static final String WEB_URL_KEY = "web_url";
    public static final String WEB_TITLE_KEY = "web_title";

    private String webTitle = null;
    private ImageView mMoreImageView;
    private PopupMenu mPopupMenu;

    private AppSettingManager appSettingManager;
    private MenuItem chooseApi;
    List<ApiURLConfig> configList;

    /**
     * 用于方便打印测试
     */
    private Gson mGson = new Gson();
    public static final String TAG = AgentWebFragment.class.getSimpleName();
    private MiddlewareWebClientBase mMiddleWareWebClient;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;

    public static AgentWebFragment getInstance(Bundle bundle) {

        AgentWebFragment mAgentWebFragment = new AgentWebFragment();
        if (bundle != null) {
            mAgentWebFragment.setArguments(bundle);
        }

        return mAgentWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agentweb, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView mSearchImageView = view.findViewById(R.id.iv_search);
        ImageView mJxImageView = view.findViewById(R.id.iv_jx);
        ImageView mTpImageView = view.findViewById(R.id.iv_tp);
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
        if (null != mTpImageView) {
            boolean aBoolean = bundle.getBoolean(SHOW_TP_KEY);
            if (aBoolean) {
                mTpImageView.setVisibility(View.VISIBLE);
            }
        }
        super.onViewCreated(view, savedInstanceState);


        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(new CommonWebChromeClient() {
                    @Override
                    public void onShowCustomView(View view, CustomViewCallback callback) {
                        //点击Video标签全屏播放按钮时，会回调WebChromeClient的onShowCustomView()方法
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        super.onShowCustomView(view, callback);
                    }

                    @Override
                    public void onHideCustomView() {
                        //退出全屏时会回调onHideCustomView()方法
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        super.onHideCustomView();
                    }
                }) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setAgentWebUIController(new UIController(getActivity())) //自定义UI  AgentWeb3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                .additionalHttpHeader(getUrl(), "cookie", "41bc7ddf04a26b91803f6b11817a5a1c")
                .useMiddlewareWebClient(getMiddlewareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(getUrl()); //WebView载入该url地址的页面并显示。


        AgentWebConfig.debug();

        initView(view);


        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        //mAgentWeb.getWebCreator().getWebView()  获取WebView .

//		mAgentWeb.getWebCreator().getWebView().setOnLongClickListener();

//		Runtime.getInstance().setFileComparatorFactory(new FileComparator.FileComparatorFactory() {
//			@Override
//			public FileComparator newFileComparator() {
//				return new FileComparator() {
//					@Override
//					public int compare(String url, File originFile, String inputMD5, String originFileMD5) {
//						return FileComparator.COMPARE_RESULT_SUCCESSFUL;
//					}
//				};
//			}
//		});
        Bundle arguments = getArguments();
        String web_title = arguments.getString(AgentWebFragment.WEB_TITLE_KEY);
        mTitleTextView = view.findViewById(R.id.toolbar_title);
        if (null != mTitleTextView) {
            mTitleTextView.setText(web_title);
        }
    }


    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i(TAG, "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
            return false;
        }
    };


    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.download.library:Downloader:4.1.1' ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        new DefaultDownloadImpl(getActivity(),
                                webView,
                                this.mAgentWeb.getPermissionInterceptor()) {

                            @Override
                            protected ResourceRequest createResourceRequest(String url) {
                                return DownloadImpl.getInstance(getContext())
                                        .url(url)
                                        .quickProgress()
                                        .addHeader("", "")
                                        .setEnableIndicator(true)
                                        .autoOpenIgnoreMD5()
                                        .setRetry(5)
                                        .setBlockMaxTime(100000L);
                            }

                            @Override
                            protected void taskEnqueue(ResourceRequest resourceRequest) {
                                resourceRequest.enqueue(new DownloadListenerAdapter() {
                                    @Override
                                    public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                                        super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                                    }

                                    @MainThread
                                    @Override
                                    public void onProgress(String url, long downloaded, long length, long usedTime) {
                                        super.onProgress(url, downloaded, length, usedTime);
                                    }

                                    @Override
                                    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                                        return super.onResult(throwable, path, url, extra);
                                    }
                                });
                            }
                        });
            }
        };
    }

    /**
     * 页面空白，请检查scheme是否加上， scheme://host:port/path?query&query 。
     *
     * @return mUrl
     */
    public String getUrl() {
        String target = "";

        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "http://cw.gzyunjuchuang.com/";
        }

//		return "http://ggzy.sqzwfw.gov.cn/WebBuilderDS/WebbuilderMIS/attach/downloadZtbAttach.jspx?attachGuid=af982055-3d76-4b00-b5ab-36dee1f90b11&appUrlFlag=sqztb&siteGuid=7eb5f7f1-9041-43ad-8e13-8fcb82ea831a";
        return target;
    }

    protected com.just.agentweb.WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            /*if (mTitleTextView != null && !TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10).concat("...");
                }
            }*/
            webTitle = title;
            mTitleTextView.setText(title);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //点击Video标签全屏播放按钮时，会回调WebChromeClient的onShowCustomView()方法
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            //退出全屏时会回调onHideCustomView()方法
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onHideCustomView();
        }
    };
    /**
     * 注意，重写WebViewClient的方法,super.xxx()请务必正确调用， 如果没有调用super.xxx(),则无法执行DefaultWebClient的方法
     * 可能会影响到AgentWeb自带提供的功能,尽可能调用super.xxx()来完成洋葱模型
     */
    protected com.just.agentweb.WebViewClient mWebViewClient = new com.just.agentweb.WebViewClient() {

        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        //
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {

            Log.i(TAG, "view:" + new Gson().toJson(view.getHitTestResult()));
            Log.i(TAG, "mWebViewClient shouldOverrideUrlLoading:" + url);
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone")) {
                return true;
            }
			/*else if (isAlipay(view, mUrl))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
			    return true;*/
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i(TAG, "mUrl:" + url + " onPageStarted  target:" + getUrl());
            timer.put(url, System.currentTimeMillis());
            if (url.equals(getUrl())) {
                pageNavigator(View.GONE);
            } else {
                pageNavigator(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //自动播放
            view.getSettings().setMediaPlaybackRequiresUserGesture(false);

            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
                Log.i(TAG, "  page mUrl:" + url + "  used time:" + (overTime - startTime));
            }

        }
        /*错误页回调该方法 ， 如果重写了该方法， 上面传入了布局将不会显示 ， 交由开发者实现，注意参数对齐。*/
	   /* public void onMainFrameError(AbsAgentWebUIController agentWebUIController, WebView view, int errorCode, String description, String failingUrl) {

            Log.i(TAG, "AgentWebFragment onMainFrameError");
            agentWebUIController.onMainFrameError(view,errorCode,description,failingUrl);

        }*/

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

//			Log.i(TAG, "onReceivedHttpError:" + 3 + "  request:" + mGson.toJson(request) + "  errorResponse:" + mGson.toJson(errorResponse));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

//			Log.i(TAG, "onReceivedError:" + errorCode + "  description:" + description + "  errorResponse:" + failingUrl);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 2.0.0开始 废弃该api ，没有api代替 ,使用 ActionActivity 绕过该方法 ,降低使用门槛,4.0.0 删除该API。
         */
//        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
    }

    private SimpleSearchView mSimpleSearchView;
    private ImageView mSearchImageView;
    private ImageView mJxImageView;
    private ImageView mTpImageView;

    protected void initView(View view) {
        //mBackImageView = view.findViewById(R.id.iv_back);
        //mLineView = view.findViewById(R.id.view_line);
        mFinishImageView = view.findViewById(R.id.iv_finish);
        mTitleTextView = view.findViewById(R.id.toolbar_title);
        //mBackImageView.setOnClickListener(mOnClickListener);
        mFinishImageView.setOnClickListener(mOnClickListener);
        mJxImageView = view.findViewById(R.id.iv_jx);
        mJxImageView.setOnClickListener(mOnClickListener);
        mTpImageView = view.findViewById(R.id.iv_tp);
        mTpImageView.setOnClickListener(mOnClickListener);

        mMoreImageView = view.findViewById(R.id.iv_more);
        mMoreImageView.setOnClickListener(mOnClickListener);
        mSearchImageView = view.findViewById(R.id.iv_search);
        mSearchImageView.setOnClickListener(mOnClickListener);
        mSimpleSearchView = view.findViewById(R.id.search_view);
        pageNavigator(View.GONE);
        mSimpleSearchView.setHint("请输入网址");
        EditText editText = mSimpleSearchView.findViewById(R.id.searchEditText);
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
//        mSimpleSearchView.setSearchBackground(new ColorDrawable(getColorPrimary()));
        mSimpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String completeUrl = s;
                if (!completeUrl.startsWith("http")) {
                    completeUrl = "https://www.baidu.com/s?wd=" + s;
                }
                mAgentWeb.getUrlLoader().loadUrl(completeUrl);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                return false;
            }
        });
        FileCompressor.getInstance().registerFileCompressEngine(this);

    }

    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }


    private void pageNavigator(int tag) {

        //mBackImageView.setVisibility(tag);
        //mLineView.setVisibility(tag);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            /*    case R.id.iv_back:
                    // true表示AgentWeb处理了该事件
                    if (!mAgentWeb.back()) {
                        AgentWebFragment.this.getActivity().finish();
                    }
                    break;*/
                case R.id.iv_finish:
                    AgentWebFragment.this.getActivity().finish();
                    break;
                case R.id.iv_more:
                    showPoPup(v);
                    break;
                case R.id.iv_search:
                    mSimpleSearchView.showSearch();
                    mSimpleSearchView.setQuery(mAgentWeb.getWebCreator().getWebView().getUrl(), false);
                    break;
                case R.id.iv_jx:
                    Intent intent = new Intent(App.mContext, VideoPlayerActivity.class);
                    intent.putExtra(WEB_URL_KEY, mAgentWeb.getWebCreator().getWebView().getUrl());
                    intent.putExtra(WEB_TITLE_KEY, webTitle);
                    startActivity(intent);
                    break;
                case R.id.iv_tp:
                    View view = View.inflate(getContext(), R.layout.layout_right, null);
                    initListView(view);
                    PopupLayout popupLayout = PopupLayout.init(getContext(), view);
                    popupLayout.setUseRadius(true);
                    popupLayout.show(PopupLayout.POSITION_RIGHT);
                    ListView listView = view.findViewById(R.id.device_list);
                    listView.setOnItemClickListener((adapterView, view1, position, id) -> {

                        popupLayout.dismiss();
                    });
                    break;
                default:
                    break;

            }
        }

    };

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            Toast.makeText(this.getContext(), targetUrl + " 该链接无法使用浏览器打开。", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        startActivity(intent);
    }


    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this.getActivity(), view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            Menu menu = mPopupMenu.getMenu();
            MenuItem item = menu.getItem(menu.size() - 1);
            SubMenu subItem = item.getSubMenu();
            configList = getConfig();
            for (int i = 0; i < configList.size(); i++) {
                ApiURLConfig apiURLConfig = configList.get(i);
                MenuItem menuItem = subItem.add(R.id.choose_api, apiURLConfig.getId(), i, apiURLConfig.getTitle());
                menuItem.setTitleCondensed(apiURLConfig.getCode());
                menuItem.setIcon(apiURLConfig.getIs_default() == 1 ? R.drawable.choose_api_checked : R.drawable.choose_api_unchacked);
            }
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }

        if (chooseApi != null) {
            Menu menu = mPopupMenu.getMenu();
            MenuItem menuItem = menu.getItem(menu.size() - 1);
            SubMenu subItem = menuItem.getSubMenu();
            for (int i = 0; i < subItem.size(); i++) {
                MenuItem item = subItem.getItem(i);
                item.setIcon(item.getItemId() == chooseApi.getItemId() ? R.drawable.choose_api_checked : R.drawable.choose_api_unchacked);
            }
        }

        mPopupMenu.show();
    }

    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                /*case R.id.refresh:
                    if (mAgentWeb != null) {
                        mAgentWeb.getUrlLoader().reload(); // 刷新
                    }
                    return true;*/

                case R.id.copy:
                    if (mAgentWeb != null) {
                        toCopy(getContext(), mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.default_browser:
                    if (mAgentWeb != null) {
                        openBrowser(mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.default_clean:
                    toCleanWebCache();
                    return true;
                case R.id.error_website:
                    loadErrorWebSite();
                    // test DownloadingService
//			        LogUtils.i(TAG, " :" + mDownloadingService + "  " + (mDownloadingService == null ? "" : mDownloadingService.isShutdown()) + "  :" + mExtraService);
//                    if (mDownloadingService != null && !mDownloadingService.isShutdown()) {
//                        mExtraService = mDownloadingService.shutdownNow();
//                        LogUtils.i(TAG, "mExtraService::" + mExtraService);
//                        return true;
//                    }
//                    if (mExtraService != null) {
//                        mExtraService.performReDownload();
//                    }

                    return true;
                case R.id.app_setting:
                    Intent intent = new Intent(App.mContext, AppSettingActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    CharSequence titleCondensed = item.getTitleCondensed();
                    if (TextUtils.isEmpty(titleCondensed)) {
                        return false;
                    }
                    if (titleCondensed.toString().startsWith("http")) {
                        if (mAgentWeb != null) {
                            chooseApi = item;
                            Bundle arguments = getArguments();
                            String web_url = arguments.getString(AgentWebFragment.WEB_URL_KEY);
                            mAgentWeb.getUrlLoader().loadUrl(titleCondensed.toString() + web_url); // 刷新
                            return saveConfig();
                        }
                    }
                    return false;
            }

        }
    };

    /**
     * 测试错误页的显示
     */
    private void loadErrorWebSite() {
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().loadUrl("http://www.unkownwebsiteblog.me");
        }
    }

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {

        if (this.mAgentWeb != null) {

            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            Toast.makeText(getActivity(), "已清理缓存", Toast.LENGTH_SHORT).show();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
            AgentWebConfig.clearDiskCache(this.getContext());
        }

    }


    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    private void toCopy(Context context, String text) {

        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));

    }


    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();//恢复
        super.onResume();
    }

    @Override
    public void onPause() {

        mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        if (mSimpleSearchView.onBackPressed()) {
            return true;
        }
        return mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        FileCompressor.getInstance().unregisterFileCompressEngine(this);
        super.onDestroyView();
    }

    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return this.mMiddleWareWebClient = new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading url:" + url);
				/*if (url.startsWith("agentweb")) { // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
					Log.i(TAG, "agentweb scheme ~");
					return true;
				}*/

                if (super.shouldOverrideUrlLoading(view, url)) { // 执行 DefaultWebClient#shouldOverrideUrlLoading
                    return true;
                }
                // do you work
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading request url:" + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame() && error.getErrorCode() != -1) {
                    super.onReceivedError(view, request, error);
                }
            }
        };
    }

    protected MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return this.mMiddleWareWebChrome = new MiddlewareChromeClient() {
        };
    }

    /**
     * 选择文件后回调该方法， 这里可以做文件压缩 / 也可以做图片的方向调整
     *
     * @param type     customize/system  ， customize 表示通过js方式获取文件， 把文件
     *                 转成base64的方式返回给js，这种方式兼容性高，但是存在文件过大转成base64时
     *                 字符串长度过长，导致与js通信失败问题，所以很有必要压缩文件， 尽量控制字符串长度在512kb以内。
     *                 <p>
     *                 system 这种方式，是由input/file 标签触发的文件选择，这种方式缺点是在Android 4.4 不回调
     *                 fileChooser，存在兼容性问题，但是经过升级，基本可以忽略了，api 的兼容性越来越好了， 回调
     *                 返回是于uri形式，所以不存在文件大小问题，作图片预览也很快。(推荐这种方式)
     * @param uri      文件的uri
     * @param callback
     */
    @Override
    public void compressFile(String type, final Uri[] uri, ValueCallback<Uri[]> callback) {
        Log.e(TAG, "compressFile type:" + type);
        if ("system".equals(type)) { // input/file 标签触发的文件选择，这种方式不存在性能问题，可压缩也可以不压缩，具体看自己业务要求
            callback.onReceiveValue(uri);
            return;
        }
        // customize.equals(type)  这种方式强烈建议文件压缩
        if (uri == null || uri.length == 0) {
            callback.onReceiveValue(uri);
        } else {
            final String[] paths = AgentWebUtils.uriToPath(getActivity(), uri);
            if (paths == null || paths.length == 0) {
                callback.onReceiveValue(uri);
                return;
            }

            AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
                try {
                    Uri[] result = new Uri[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        String filePath = paths[i];
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getExtensionByFilePath(filePath));
                        if (TextUtils.isEmpty(mimeType) || !mimeType.startsWith("image")) {
                            result[i] = uri[i];
                        } else {
                            File origin = new File(filePath);
                            File file = Luban.with(App.mContext).ignoreBy(100).setTargetDir(AgentWebUtils.getAgentWebFilePath(App.mContext)).get(filePath);
                            Log.e(TAG, "原文件大小：" + byte2FitMemorySize(origin.length()));
                            Log.e(TAG, "压缩后文件大小：" + byte2FitMemorySize(file.length()));

                            Uri fileUri = AgentWebUtils.getUriFromFile(App.mContext, file);
                            result[i] = fileUri;

                        }
                    }
                    AgentWebUtils.runInUiThread(() -> callback.onReceiveValue(result));
                } catch (IOException e) {
                    e.printStackTrace();
                    AgentWebUtils.runInUiThread(() -> callback.onReceiveValue(uri));
                }
            });

        }

    }

    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.getDefault(), "%.1fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format(Locale.getDefault(), "%.1fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.getDefault(), "%.1fMB", (double) byteNum / 1048576);
        } else {
            return String.format(Locale.getDefault(), "%.1fGB", (double) byteNum / 1073741824);
        }
    }

    //获取示例ListView
    private void initListView(View parent) {
        ListView listView = parent.findViewById(R.id.device_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getDataList());
        listView.setAdapter(adapter);
    }

    //获取列表的演示数据
    private List<String> getDataList() {
        List<String> list = new ArrayList<>();

        return list;
    }

    private List<ApiURLConfig> getConfig() {
        appSettingManager = new AppSettingManager(getContext());
        return appSettingManager.query();
    }

    private boolean saveConfig() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_default", 1);
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("is_default", 0);
        int itemId = chooseApi.getItemId();
        appSettingManager.update(contentValues, "id = ?", new String[]{itemId + ""});
        appSettingManager.update(contentValues1, "id <> ?", new String[]{itemId + ""});

        configList.stream().filter(item -> item.getId() == itemId).findFirst().ifPresent(apiURLConfig -> VideoPlayerActivity.defaultApi = apiURLConfig);
        return true;
    }
}
