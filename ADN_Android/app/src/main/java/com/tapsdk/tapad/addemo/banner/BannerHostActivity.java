package com.tapsdk.tapad.addemo.banner;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;


import com.tapsdk.tapad.addemo.R;

public class BannerHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_banner_host);

        bindButton(R.id.portraitBannerAdButton, PortraitBannerActivity.class);
        bindButton(R.id.landscapeBannerAdButton, LandscapeBannerActivity.class);

    }

    private void bindButton(@IdRes int id, final Class<?> clz) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BannerHostActivity.this, clz);
                startActivity(intent);
            }
        });
    }
}
