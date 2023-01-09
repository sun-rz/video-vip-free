package com.video.vip.player.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.video.vip.player.R;

public class AppSettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        mToolbar = this.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        this.setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> AppSettingActivity.this.finish());
    }
}
