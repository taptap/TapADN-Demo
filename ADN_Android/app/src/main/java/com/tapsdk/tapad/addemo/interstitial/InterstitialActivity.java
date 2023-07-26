package com.tapsdk.tapad.addemo.interstitial;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.tapsdk.tapad.addemo.R;

public class InterstitialActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_main);

        bindButton(R.id.protrait_interstitial_ad,PortraitInterstitialActivity.class);
        bindButton(R.id.landscape_interstitial_ad,LandscapeInterstitialActivity.class);


    }

    private void bindButton(@IdRes int id, final Class<?> clz) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterstitialActivity.this, clz);
                startActivity(intent);
            }
        });
    }
}
