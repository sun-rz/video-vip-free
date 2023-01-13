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

    public static final String CONFIG_FILE_KEY = "config.json";
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
                if (first.isPresent()) {
                    defaultItem[0] = first.get();
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
        editor.putString(DEFAULT_API_KEY, selectedItem.code);
        editor.apply();

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
