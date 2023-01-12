package com.video.vip.player.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.video.vip.player.R;
import com.video.vip.player.utils.StringUitls;

import java.io.*;

public class AppSettingActivity extends AppCompatActivity {

    private TextView textView;

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
        FileInputStream inputStream = null;
        ByteArrayOutputStream stream = null;
        try {
            inputStream = this.openFileInput("config.json");
            stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            textView.setText(stream.toString());
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "读取配置文件出错", Toast.LENGTH_LONG);
            toast.show();
            Log.e("AppSettingActivity", "读取配置文件出错", e);
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

        Button mButton = this.findViewById(R.id.btn_save);
        mButton.setOnClickListener(v -> {
            FileOutputStream outputStream = null;
            try {
                String apiConfig = textView.getText().toString();
                if (StringUitls.isBlank(apiConfig)) {
                    return;
                }
                outputStream = this.openFileOutput("config.json", Context.MODE_PRIVATE);
                JsonElement jsonElement = JsonParser.parseString(apiConfig);
                boolean isJsonObject = jsonElement.isJsonObject();
                if (!isJsonObject) {
                    Toast toast = Toast.makeText(this, "配置信息格式不正确", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.size() == 0) {
                    return;
                }
                outputStream.write(jsonObject.toString().getBytes());

                Toast toast = Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);
                toast.show();
            } catch (Exception e) {
                Toast toast = Toast.makeText(this, "写入配置文件出错", Toast.LENGTH_LONG);
                toast.show();
                Log.e("AppSettingActivity", "写入配置文件出错", e);
            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e("AppSettingActivity", "写入配置文件出错", e);
                    }
                }
            }
        });
    }
}
