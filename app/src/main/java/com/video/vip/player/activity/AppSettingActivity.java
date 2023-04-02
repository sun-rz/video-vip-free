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
    }

}
