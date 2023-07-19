package com.tapsdk.tapad.addemo.widget;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ActivityUtils {
    public static boolean isActivityNotAlive(Activity activity) {
        return activity == null || activity.isFinishing() || activity.isDestroyed();
    }

    public static boolean isActivityAlive(Activity activity) {
        return !isActivityNotAlive(activity);
    }

    /**
     * 特殊场景使用（sdk需要获取当前顶部Activity给用户提示）
     * @return currentTopActivity
     */
    @SuppressLint("PrivateApi")
    public static Observable<Activity> getStackTopActivity() {
        return Observable.create(new ObservableOnSubscribe<Activity>() {
            @Override
            public void subscribe(ObservableEmitter<Activity> emitter) throws Exception {
                Class<?> activityThreadClass = null;
                try {
                    activityThreadClass = Class.forName("android.app.ActivityThread");

                    Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
                    Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
                    activitiesField.setAccessible(true);

                    Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
                    if (activities == null) {
                        emitter.onError(new Throwable("can't get top activity"));
                    }
                    boolean hasResult = false;
                    for (Object activityRecord : activities.values()) {
                        Class<?> activityRecordClass = activityRecord.getClass();
                        Field pausedField = activityRecordClass.getDeclaredField("paused");
                        pausedField.setAccessible(true);
                        if (!pausedField.getBoolean(activityRecord)) {
                            Field activityField = activityRecordClass.getDeclaredField("activity");
                            activityField.setAccessible(true);
                            Activity activity = (Activity) activityField.get(activityRecord);
                            emitter.onNext(activity);
                            hasResult = true;
                            break;
                        }
                    }
                    if (hasResult) {
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Throwable("can't get top activity"));
                    }
                } catch (ClassNotFoundException | IllegalAccessException
                        | InvocationTargetException | NoSuchMethodException
                        | NoSuchFieldException e) {
                    e.printStackTrace();
                    emitter.onError(new Throwable("can't get top activity"));
                }
            }
        });
    }
}
