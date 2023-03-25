package com.video.vip.player.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.video.vip.player.R;
import com.video.vip.player.bean.ApiURLConfig;
import com.video.vip.player.db.AppSettingManager;

import java.util.ArrayList;
import java.util.List;

public class AppSettingActivity extends AppCompatActivity {

    private TextView textView;
    private ArrayAdapter<ApiURLConfig> adapterArr;
    private Spinner spinner;
    private AppSettingManager appSettingManager;

    final ApiURLConfig[] defaultItem = {null};
    final ApiURLConfig[] defaultItem1 = {null};

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

        List<ApiURLConfig> urlConfigs = getConfig();
        if (null != urlConfigs) {
            //适配器
            adapterArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, urlConfigs);
            for (ApiURLConfig apiURLConfig : urlConfigs) {
                int is_default = apiURLConfig.getIs_default();
                if (is_default == 1) {
                    defaultItem[0] = apiURLConfig;
                    defaultItem1[0] = apiURLConfig;
                    break;
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
            saveConfig();
            Toast toast = Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    private List<ApiURLConfig> getConfig() {
        appSettingManager = new AppSettingManager(getApplicationContext());
        return appSettingManager.query();
    }

    private boolean saveConfig() {

        ContentValues contentValues = new ContentValues();
        contentValues.put("is_default", 1);
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("is_default", 0);
        appSettingManager.update(contentValues, "id = ?", new String[]{defaultItem[0].getId() + ""});
        VideoPlayerActivity.defaultApi = defaultItem[0];
        if (null != defaultItem1[0]) {
            appSettingManager.update(contentValues1, "id = ?", new String[]{defaultItem1[0].getId() + ""});
        }
        return true;
    }
}
