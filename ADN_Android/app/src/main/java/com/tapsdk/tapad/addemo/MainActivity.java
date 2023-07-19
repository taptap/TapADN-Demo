package com.tapsdk.tapad.addemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



import com.tapsdk.tapad.BuildConfig;
import com.tapsdk.tapad.Callback;
import com.tapsdk.tapad.CustomUser;
import com.tapsdk.tapad.TapAdConfig;
import com.tapsdk.tapad.TapAdCustomController;
import com.tapsdk.tapad.TapAdLocation;
import com.tapsdk.tapad.TapAdManager;
import com.tapsdk.tapad.TapAdSdk;
import com.tapsdk.tapad.UserAction;
import com.tapsdk.tapad.addemo.banner.BannerHostActivity;
import com.tapsdk.tapad.addemo.feed.FeedRecyclerViewActivity;
import com.tapsdk.tapad.addemo.feed.FeedRecyclerViewActivity2;
import com.tapsdk.tapad.addemo.feed.SingleFeedAdActivity;
import com.tapsdk.tapad.addemo.interstitial.InterstitialActivity;
import com.tapsdk.tapad.addemo.interstitial.PortraitInterstitialActivity;
import com.tapsdk.tapad.addemo.reward.RewardHostActivity;
import com.tapsdk.tapad.addemo.splash.SplashHostActivity;
import com.tapsdk.tapad.exceptions.AdException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean enableLocation = true;

    private boolean enableGetAndroidId = true;
    private EditText inputAvatarLevelEditText;
    private EditText inputOaidEditText;
    private TextView versionTextView;
    private String oaid = "asdf12345";

    public void initCN() {

    }

    private void initFormal() {
        initCN();
        final TapAdConfig config = new TapAdConfig.Builder()
                .withMediaId(1000007)
                .withMediaName("联盟正式-Android")
                .withMediaKey("1AjDOjD0F3SDDmgTuBQHbCRULSizYPHV17viZObHvhDjf7Pq1rlarueOX1cYBucn")
                .withMediaVersion("1")
                .withGameChannel("taptap2")
                .withTapClientId("0RiAlMny7jiz086FaU")
                .enableDebug(true)
                .withCustomController(new TapAdCustomController() {
                    @Override
                    public boolean isCanUseLocation() {
                        return enableLocation;
                    }

                    @Override
                    public TapAdLocation getTapAdLocation() {
                        double longitude = 0;
                        double latitude = 0;
                        double accuracy = 0;
                        return new TapAdLocation(longitude, latitude, accuracy);
                    }

                    @Override
                    public boolean isCanUsePhoneState() {
                        return super.isCanUsePhoneState();
                    }

                    @Override
                    public String getDevImei() {
                        return super.getDevImei();
                    }

                    @Override
                    public boolean isCanUseWifiState() {
                        return super.isCanUseWifiState();
                    }

                    @Override
                    public boolean isCanUseWriteExternal() {
                        return super.isCanUseWriteExternal();
                    }

                    @Override
                    public String getDevOaid() {
//                        if (inputOaidEditText != null && inputOaidEditText.getText() != null) {
//                            return inputOaidEditText.getText().toString();
//                        }
                        return "asdf12345";
                    }

                    @Override
                    public boolean alist() {
                        return super.alist();
                    }

                    @Override
                    public boolean isCanUseAndroidId() {
                        return enableGetAndroidId;
                    }

                    @Override
                    public CustomUser provideCustomUser() {
                        int avatarLevel = -1;
                        try {
                            avatarLevel = Integer.parseInt(inputAvatarLevelEditText.getText().toString());
                        } catch (NumberFormatException numberFormatException) {

                        }
                        return new CustomUser.Builder()
                                .withRealAge(1)                   // 年龄
                                .withRealSex(1)                   // 性别 0:男 1：女
                                .withAvatarSex(1)           // 角色性别 0:男 1：女
                                .withAvatarLevel(avatarLevel)            // 角色等级
                                .withNewUserStatus(1)          // 是否新玩家 0:否；1:是
                                .withPayedUserStatus(1)        // 是否付费用户 0:否；1:是
                                .withBeginMissionFinished(1) // 是否通过新手教程 0:否 1:是
                                .withAvatarPayedToolCnt(1)        // 角色当前付费道具数量
                                .build();
                    }
                })
                .build();

        TapAdSdk.init(this, config);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initFormal();

        final List<String> scopeList = new ArrayList<>();

        bindButton(R.id.enterShowRewardButton, RewardHostActivity.class);
        bindButton(R.id.enterShowBannerButton, BannerHostActivity.class);
        bindButton(R.id.enterShowSplashButton, SplashHostActivity.class);
        bindButton(R.id.enterFeedButton, FeedRecyclerViewActivity.class);
        bindButton(R.id.enterShowInterstitialButton, InterstitialActivity.class);

        versionTextView = (TextView) findViewById(R.id.versionTextView);
        versionTextView.setText(BuildConfig.SDK_VERSION_NAME);

        inputAvatarLevelEditText = (EditText)findViewById(R.id.inputAvatarLevelEditText);
        inputOaidEditText = (EditText)findViewById(R.id.inputOaidEditText);
        inputOaidEditText.setText("");

        findViewById(R.id.testUserActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAction[] userActions = new UserAction[3];

                for (int i = 0; i < 3; i++) {
                    UserAction tmp = new UserAction.Builder()
                            .withActionType(i)
                            .withActionTime(System.currentTimeMillis())
                            .withAmount(i * 1000)
                            .withWinStatus(i % 2)
                            .build();
                    userActions[i] = tmp;
                }
                TapAdManager.get().uploadUserAction(userActions, new Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "上报成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(AdException exception) {
                        Toast.makeText(MainActivity.this, "上报出错:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ((ToggleButton)findViewById(R.id.enableGetAndroidLocationToggleButton)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                enableGetAndroidId = checked;
            }
        });
    }

    private void restart() {
        finish();
    }

    private void bindButton(@IdRes int id, final Class<?> clz) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, clz);
                startActivity(intent);
            }
        });
    }
}