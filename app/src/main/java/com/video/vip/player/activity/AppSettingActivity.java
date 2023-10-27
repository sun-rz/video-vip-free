package com.video.vip.player.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;
import com.video.vip.player.R;
import com.video.vip.player.bean.ApiURLConfig;
import com.video.vip.player.common.GuideItemEntity;
import com.video.vip.player.db.AppSettingManager;
import com.video.vip.player.db.GuideItemManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.video.vip.player.activity.MainActivity.FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH;


public class AppSettingActivity extends AppCompatActivity {

    private Button buttonReadApi;

    private AppSettingManager appSettingManager;
    private GuideItemManager guideItemManager;

    public static final String APIURI = "https://github.com/sun-rz/video-vip-free/blob/master/app/src/main/assets/conf/api.json";
    public static final String GUIDEURI = "https://github.com/sun-rz/video-vip-free/blob/master/app/src/main/assets/conf/guide.json";
    private static final String REGX = ".*<script type=\"application/json\" data-target=\"react-app\\.embeddedData\">(.*)</script>.*";

    private static boolean LOADING = false;

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

        appSettingManager = new AppSettingManager(getApplicationContext());
        guideItemManager = new GuideItemManager(getApplicationContext());

        LOADING = false;

        buttonReadApi = this.findViewById(R.id.btn_read_api);
        buttonReadApi.setOnClickListener(v -> {
            if (LOADING) {
                Toast toast = Toast.makeText(getApplicationContext(), "正在更新配置，请稍后......", Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
                return;
            }

            LOADING = true;

            // Android 4.0 之后不能在主线程中请求HTTP请求
            new Thread(() -> {
                updateApi();
                updateHomeConf();

                Toast toast = Toast.makeText(getApplicationContext(), "更新接口配置完成", Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
            }).start();
        });

    }

    private <T> T readConf(String uri, Class<T> tClass) {
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            //设置编码,否则中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = new String(line.getBytes(), StandardCharsets.UTF_8);
                lines.append(line);
            }
            reader.close();

            String json = lines.toString().replaceAll(REGX, "$1");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                String str = jsonArray.getString(i);
                sb.append(str);
            }
            Gson gson = new Gson();

            return gson.fromJson(sb.toString(), tClass);
        } catch (Exception e) {
            runOnUiThread(() -> {
                Toast toast = Toast.makeText(getApplicationContext(), "获取接口信息失败：" + e.getMessage(), Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
            });
            return null;
        } finally {
            LOADING = false;
        }
    }

    private void updateApi() {
        ApiURLConfig[] apiURLConfigs = readConf(APIURI, ApiURLConfig[].class);
        if (null == apiURLConfigs) {
            return;
        }
        if (apiURLConfigs.length == 0) {
            return;
        }

        appSettingManager.delete("id > ?", new String[]{"0"});
        for (ApiURLConfig apiURLConfig : apiURLConfigs) {
            appSettingManager.add(apiURLConfig);
        }
    }

    private void updateHomeConf() {
        GuideItemEntity[] guideItemEntities = readConf(APIURI, GuideItemEntity[].class);
        if (null == guideItemEntities) {
            return;
        }
        if (guideItemEntities.length == 0) {
            return;
        }
        guideItemManager.delete("extra = ?", new String[]{"1"});
        for (GuideItemEntity guideItemEntity : guideItemEntities) {
            guideItemEntity.setImage(R.mipmap.sp);
            guideItemEntity.setGuideDictionary(FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH);
            guideItemManager.add(guideItemEntity);
        }
    }
}
