package com.tapsdk.tapad.addemo.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapsdk.tapad.addemo.R;

/**
 * @author xiaoyi@xd.com
 * @date 2020/8/31
 * @description 自定义Toast
 */
public class TDSToast extends SafeDialogFragment {

    public static TDSToast newInstance(String message, boolean cancelAble) {
        TDSToast tdsToast = new TDSToast();
        Bundle args = new Bundle();
        args.putBoolean(TOAST_LOAD, false);
        args.putBoolean(TOAST_CANCEL, cancelAble);
        args.putString(TOAST_MESSAGE, message);
        tdsToast.setArguments(args);
        return tdsToast;
    }

    public static TDSToast newInstance(boolean cancelAble) {
        TDSToast tdsToast = new TDSToast();
        Bundle args = new Bundle();
        args.putBoolean(TOAST_LOAD, true);
        args.putBoolean(TOAST_CANCEL, cancelAble);
        tdsToast.setArguments(args);
        return tdsToast;
    }

    public static TDSToast newInstance(String message) {
        return newInstance(message, false);
    }

    public static TDSToast newInstance() {
        return newInstance(false);
    }

    public static final String TAG = TDSToast.class.getSimpleName();

    private static final String TOAST_MESSAGE = "TOAST_MESSAGE";

    private static final String TOAST_LOAD = "TOAST_LOADING";

    private static final String TOAST_CANCEL = "TOAST_CANCEL";

    private Animation mAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() == null || getActivity() == null) {
            return null;
        }
        return inflater.inflate(R.layout.tds_toast, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null ||
                getArguments() == null ||
                getActivity() == null ||
                getActivity().getWindowManager() == null ||
                dialog.getWindow() == null) {
            return;
        }
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(getArguments().getBoolean(TOAST_CANCEL));
        dialog.setCanceledOnTouchOutside(getArguments().getBoolean(TOAST_CANCEL));
        dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            boolean cancelAble = getArguments().getBoolean(TOAST_CANCEL);
            if (i == KeyEvent.KEYCODE_BACK) {
                if (cancelAble) {
                    dismiss();
                }
                return true;
            } else {
                return false;
            }
        });

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // 延伸显示区域到刘海
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                window.setAttributes(lp);
            }
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.getDecorView().setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null || getDialog() == null || getActivity() == null) {
            return;
        }

        TextView tvMessage = view.findViewById(R.id.tv_toast_message);
        ImageView iv = view.findViewById(R.id.iv_toast_loading);
        FrameLayout fl = view.findViewById(R.id.fl_toast_loading);
        if (!args.getBoolean(TOAST_LOAD)) {
            tvMessage.setText(args.getString(TOAST_MESSAGE));
            tvMessage.setVisibility(View.VISIBLE);
            fl.setVisibility(View.INVISIBLE);
            return;
        }
        fl.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.INVISIBLE);
        mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.tds_loading);
        mAnimation.setInterpolator(new LinearInterpolator());
        iv.setImageResource(R.drawable.tds_loading_toast);
        iv.startAnimation(mAnimation);
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            if (mAnimation != null) {
                mAnimation.cancel();
                mAnimation = null;
            }
        } catch (Exception e) {
        }
    }
}
