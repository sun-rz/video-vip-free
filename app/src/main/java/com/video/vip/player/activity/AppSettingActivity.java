package com.video.vip.player.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;
import com.video.vip.player.R;
import com.video.vip.player.bean.ApiURLConfig;
import com.video.vip.player.bean.ConfigInfo;
import com.video.vip.player.common.GuideItemEntity;
import com.video.vip.player.db.GuideItemManager;
import com.video.vip.player.fragment.AgentWebFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.video.vip.player.activity.MainActivity.*;


public class AppSettingActivity extends AppCompatActivity {

    private Button buttonReadApi;

    public static final String CONFIG_URL = "https://gitee.com/sunrz95/video-vip-free/raw/master/config.json";

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

            MainActivity.threadPoolExecutor.execute(() -> {
                try {
                    ConfigInfo configInfo = getConfigInfo();

                    runOnUiThread(() -> {
                        Toast toast = Toast.makeText(getApplicationContext(), "更新接口配置完成", Toast.LENGTH_SHORT);
                        //显示toast信息
                        toast.show();

                        //刷新数据
                        MainActivity.datas = configInfo.getSites();
                        MainActivity.myGridViewAdapter.notifyDataSetChanged();

                        AgentWebFragment.configList = configInfo.getApi();

                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        Toast toast = Toast.makeText(getApplicationContext(), "获取接口信息失败：请检查网络是否正常", Toast.LENGTH_SHORT);
                        //显示toast信息
                        toast.show();
                    });
                } finally {
                    LOADING = false;
                }
            });
        });

    }

    /**
     * 远程获取配置文件
     *
     * @param uri
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    private static <T> T requestConf(String uri, Class<T> tClass) throws IOException {
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
        Gson gson = new Gson();
        return gson.fromJson(lines.toString(), tClass);
    }

    /**
     * 获取配置信息
     *
     * @throws IOException
     */
    public static ConfigInfo getConfigInfo() throws IOException {
        ConfigInfo configInfo = requestConf(CONFIG_URL, ConfigInfo.class);

        /*appSettingManager.delete("id > ?", new String[]{"0"});

        if (apiURLConfigs.length == 0) {
            return;
        }*/

        for (ApiURLConfig apiURLConfig : configInfo.getApi()) {
            if (!"".equals(default_api)) {
                if (apiURLConfig.getCode().equals(default_api)) {
                    apiURLConfig.setIs_default(1);
                } else {
                    apiURLConfig.setIs_default(0);
                }
            }
        }

        List<GuideItemEntity> defaultList = Arrays.asList(GuideItemManager.guideItemEntityList);
        List<GuideItemEntity> list = new ArrayList<>(defaultList);
        for (GuideItemEntity guideItemEntity : configInfo.getSites()) {
            if (guideItemEntity.getType() == 2) {
                guideItemEntity.setImage(R.mipmap.music);
                guideItemEntity.setGuideDictionary(FLAG_GUIDE_DICTIONARY_FILE_DOWNLOAD);
            } else {
                guideItemEntity.setImage(R.mipmap.sp);
                guideItemEntity.setGuideDictionary(FLAG_GUIDE_DICTIONARY_PULL_DOWN_REFRESH_SEARCH);
            }
            guideItemEntity.setExtra(1);
            list.add(guideItemEntity);
        }

        configInfo.setSites(list);

        return configInfo;
    }
}
