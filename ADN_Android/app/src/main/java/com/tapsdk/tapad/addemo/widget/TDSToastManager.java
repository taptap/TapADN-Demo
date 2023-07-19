package com.tapsdk.tapad.addemo.widget;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.text.TextUtils;

/**
 * @author xiaoyi@xd.com
 * @date 2020/9/2
 * @description 自定义TdsToast 实例
 */
public enum TDSToastManager {

    INSTANCE;

    private final static String TAG = "TDSToastManager";

    private TDSToast mToast;

    private volatile boolean show;

    private static final int TIME_OUT = 15000;

    private static final int SHORT = 1000;

    private static HandlerThread handlerThread;
    private static Handler handler;

    public static TDSToastManager instance() {
        initHandler();
        return INSTANCE;
    }

    private static void initHandler() {
        if (handlerThread == null) {
            handlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_DISPLAY);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
    }

    public void showMessage(Activity activity, String message) {
        show(activity, message, TIME_OUT);
    }

    public void showShortMessage(Activity activity, String message) {
        show(activity, message, SHORT);
    }

    public void showLoading(Activity activity) {
        show(activity, null, TIME_OUT);
    }

    public void showLoading(Activity activity, int duration) {
        show(activity, null, duration);
    }

    private void show(Activity activity, String message, int duration) {
        if (activity == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (show && mToast != null) {
                    mToast.dismiss();
                }
                mToast = TextUtils.isEmpty(message) ? TDSToast.newInstance() : TDSToast.newInstance(message);

                try {
                    mToast.show(activity.getFragmentManager(), TDSToast.TAG);
                } catch (Exception e) {
                }
                show = true;

            }
        });

        if (duration != Integer.MAX_VALUE) {
            handler.postDelayed(TDSToastManager.this::dismiss, duration);
        }
    }


    public void dismiss() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    try {
                        mToast.dismissAllowingStateLoss();
                    } catch (Exception e) {
                    }
                }
                show = false;
            }
        });
    }
}


