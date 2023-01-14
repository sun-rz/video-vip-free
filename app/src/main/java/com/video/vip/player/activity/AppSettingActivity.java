package com.video.vip.player.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.video.vip.player.R;
import com.video.vip.player.bean.SelectItem;
import com.video.vip.player.utils.StringUitls;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppSettingActivity extends AppCompatActivity {

    public final String CONFIG_FILE_KEY = "config.json";
    public static final String DEFAULT_API_KEY = "default_api";
    public static final String APP_CONFIG_FILE_KEY = "WebViewChromiumPrefs";

    private TextView textView;
    private ArrayAdapter<SelectItem> adapterArr;
    private Spinner spinner;
    private String default_api;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        Toolbar mToolbar = this.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        this.setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> AppSettingActivity.this.finish());
        textView = this.findViewById(R.id.third_api);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_CONFIG_FILE_KEY, MODE_PRIVATE);
        default_api = sharedPreferences.getString(DEFAULT_API_KEY, "");

         final SelectItem[] defaultItem = {null};

        String config = getConfig();
        if (!StringUitls.isBlank(config)) {
            textView.setText(config);
            List<SelectItem> selectItems = gson.fromJson(config, new TypeToken<List<SelectItem>>() {
            }.getType());
            //适配器
            adapterArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectItems);
            if (!StringUitls.isBlank(default_api)) {
                Optional<SelectItem> first = selectItems.stream().filter(selectItem -> selectItem.code.equals(default_api)).findFirst();
                first.ifPresent(selectItem -> defaultItem[0] = selectItem);
            } else {
                if (selectItems.size() > 0) {
                    defaultItem[0] = selectItems.get(0);
                }
            }
        } else {
            adapterArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        }
        //设置样式
        adapterArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);
        //加载适配器
        spinner.setAdapter(adapterArr);
        if (null != defaultItem[0]) {
            int position = adapterArr.getPosition(defaultItem[0]);
            spinner.setSelection(position);
        }

        //为spinner设置监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                defaultItem[0] = adapterArr.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button mButton = this.findViewById(R.id.btn_save);
        mButton.setOnClickListener(v -> {
            String apiConfig = textView.getText().toString();
            if (StringUitls.isBlank(apiConfig)) {
                return;
            }
            List<SelectItem> selectItems = saveJsonConfig(apiConfig);
            if (null != selectItems) {
                adapterArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectItems);
                //加载适配器
                spinner.setAdapter(adapterArr);
                if (null != defaultItem[0]) {
                    int position = adapterArr.getPosition(defaultItem[0]);
                    spinner.setSelection(position);
                }
            }
        });
    }

    private String getConfig() {
        FileInputStream inputStream = null;
        ByteArrayOutputStream stream = null;
        try {
            inputStream = this.openFileInput(CONFIG_FILE_KEY);
            stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            return stream.toString();
        } catch (FileNotFoundException e) {
            try {
                String json="[ { \"code\": \"http://pangujiexi.cc/jiexi.php?url=\", \"title\": \"视频解析地址【全网解析(广告)】\" }, { \"code\": \"http://www.ckmov.vip/api.php?url=\", \"title\": \"优先VIP解析引擎系统【全网解析(广告)】\" }, { \"code\": \"http://jx.xmflv.com/?url=\", \"title\": \"虾米视频解析【PC电脑解析】\" }, { \"code\": \"http://jx.aidouer.net/?url=\", \"title\": \"视频云解析【全网解析】\" }, { \"code\": \"http://jx.m3u8.tv/jiexi/?url=\", \"title\": \"无广告视频解析【全网解析】\" }, { \"code\": \"http://z1.im1907.top/?jx=\", \"title\": \"输入片名自动解析【NEW】\" }, { \"code\": \"http://svip.bljiex.cc/?v=\", \"title\": \"VIP极解析【记忆播放】\" }, { \"code\": \"http://z1.m1907.cn/?jx=\", \"title\": \"测试解析【全网解析】\" }, { \"code\": \"http://jiexi.8old.cn/m3u8tv20210705%60/?url=\", \"title\": \"广告超速解析【通用】\" }, { \"code\": \"http://jx.m3u8.tv/jiexi/?url=\", \"title\": \"测试解析【全网解析】\" }, { \"code\": \"http://www.nxflv.com/?url=\", \"title\": \"测试解析二【PC电脑解析】\" }, { \"code\": \"http://www.h8jx.com/jiexi.php?url=\", \"title\": \"测试解析三【全网解析】\" }, { \"code\": \"http://okjx.cc/?url=\", \"title\": \"VIP解析【PC电脑解析】\" }, { \"code\": \"http://www.1717yun.com/jx/ty.php?url=\", \"title\": \"VIP解析二【PC电脑解析】\" }, { \"code\": \"http://parse.123mingren.com/?url=\", \"title\": \"VIP解析三【全网解析】\" }, { \"code\": \"http://www.administratorw.com/index/qqvod.php?url=\", \"title\": \"VIP解析四【全网解析】\" }, { \"code\": \"http://jx.000180.top/jx/?url=\", \"title\": \"观察解析【全网解析】\" }, { \"code\": \"http://jiexi44.qmbo.cn/jiexi/?url=\", \"title\": \"①vip专用解析（推荐）\" }, { \"code\": \"http://okjx.cc/?url=\", \"title\": \"一号通用vip引擎系统 【稳定通用】\" }, { \"code\": \"http://jx.m3u8.tv/jiexi/?url=\", \"title\": \"②通道\" }, { \"code\": \"http://jx.ivito.cn/?url=\", \"title\": \"③蓝光通道\" }, { \"code\": \"http://jx.ivito.cn/?url=\", \"title\": \"视频解析地址【全网解析(广告)】\" }, { \"code\": \"http://www.ckmov.vip/api.php?url=\", \"title\": \"优先VIP解析引擎系统【全网解析(广告)】\" }, { \"code\": \"http://jx.xmflv.com/?url=\", \"title\": \"虾米视频解析【PC电脑解析】\" }, { \"code\": \"http://jx.aidouer.net/?url=\", \"title\": \"视频云解析【全网解析】\" }, { \"code\": \"http://jx.m3u8.tv/jiexi/?url=\", \"title\": \"无广告视频解析【全网解析】\" }, { \"code\": \"http://z1.im1907.top/?jx=\", \"title\": \"输入片名自动解析【NEW】\" }, { \"code\": \"http://www.ckmov.vip/api.php?url=\", \"title\": \"无广告解析【全网解析】\" }, { \"code\": \"http://pangujiexi.cc/jiexi.php?url=\", \"title\": \"盘古视频解析【PC电脑解析】\" }, { \"code\": \"http://z1.m1907.cn/?jx=\", \"title\": \"测试解析【全网解析】\" }, { \"code\": \"http://jiexi.8old.cn/m3u8tv20210705%60/?url=\", \"title\": \"广告超速解析【通用】\" }, { \"code\": \"http://jx.m3u8.tv/jiexi/?url=\", \"title\": \"测试解析【全网解析】\" }, { \"code\": \"http://www.nxflv.com/?url=\", \"title\": \"测试解析二【PC电脑解析】\" }, { \"code\": \"http://55o.co/?url=\", \"title\": \"测试解析三【全网解析】\" }, { \"code\": \"http://jx.ivito.cn/?url=\", \"title\": \"VIP解析【PC电脑解析】\" }, { \"code\": \"http://www.1717yun.com/jx/ty.php?url=\", \"title\": \"VIP解析二【PC电脑解析】\" }, { \"code\": \"http://parse.123mingren.com/?url=\", \"title\": \"VIP解析三【全网解析】\" }, { \"code\": \"http://www.administratorw.com/index/qqvod.php?url=\", \"title\": \"VIP解析四【全网解析】\" }, { \"code\": \"http://www.8090g.cn/?url=\", \"title\": \"观察解析【全网解析(广告)】\" }, { \"code\": \"http://jx.000180.top/jx/?url=\", \"title\": \"观察解析二【全网解析】\" }, { \"code\": \"https://55o.co/?url=\", \"title\": \"视频解析地址【全网解析(弹幕)】\" }, { \"code\": \"http://jx.ivito.cn/?url=\", \"title\": \"视频解析地址【全网解析(广告)】\" }, { \"code\": \"http://www.ckmov.vip/api.php?url=\", \"title\": \"优先VIP解析引擎系统【全网解析(广告)】\" }, { \"code\": \"http://pangujiexi.cc/jiexi.php?url=\", \"title\": \"盘古视频解析【PC电脑解析】\" }, { \"code\": \"http://jx.xmflv.com/?url=\", \"title\": \"虾米视频解析【PC电脑解析】\" }, { \"code\": \"http://jx.aidouer.net/?url=\", \"title\": \"视频云解析【全网解析】\" } ]";
                FileOutputStream outputStream = this.openFileOutput(CONFIG_FILE_KEY, Context.MODE_PRIVATE);
                outputStream.write(json.getBytes());
                outputStream.close();
                return json;
            } catch (IOException ex) {
                Log.i("AppSettingActivity", "配置文件创建失败", ex);
                return null;
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "读取配置文件出错", Toast.LENGTH_LONG);
            toast.show();
            Log.e("AppSettingActivity", "读取配置文件出错", e);
            return null;
        } finally {
            try {
                if (null != stream) {
                    stream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e("AppSettingActivity", "读取配置文件出错", e);
            }
        }
    }

    private List<SelectItem> saveJsonConfig(String apiConfig) {

        SharedPreferences.Editor editor = getSharedPreferences(APP_CONFIG_FILE_KEY, MODE_PRIVATE).edit();
        SelectItem selectedItem = (SelectItem) spinner.getSelectedItem();
        if (null != selectedItem) {
            editor.putString(DEFAULT_API_KEY, selectedItem.code);
            VideoPlayerActivity.default_api = selectedItem.code;
            editor.apply();
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = this.openFileOutput(CONFIG_FILE_KEY, Context.MODE_PRIVATE);
            JsonElement jsonElement = JsonParser.parseString(apiConfig);
            boolean isConfig = jsonElement.isJsonArray();
            if (!isConfig) {
                Toast toast = Toast.makeText(this, "配置信息格式不正确", Toast.LENGTH_LONG);
                toast.show();
                return null;
            }
            JsonArray config = jsonElement.getAsJsonArray();
            if (config.size() == 0) {
                Toast toast = Toast.makeText(this, "配置信息不能为空", Toast.LENGTH_SHORT);
                toast.show();
                return null;
            }
            outputStream.write(config.toString().getBytes());

            Toast toast = Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);
            toast.show();
            Gson gson = new Gson();
            List<SelectItem> selectItems = gson.fromJson(jsonElement, new TypeToken<List<SelectItem>>() {
            }.getType());
            return selectItems;
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "写入配置文件出错", Toast.LENGTH_LONG);
            toast.show();
            Log.e("AppSettingActivity", "写入配置文件出错", e);
            return null;
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("AppSettingActivity", "写入配置文件出错", e);
                }
            }
        }
    }
}
