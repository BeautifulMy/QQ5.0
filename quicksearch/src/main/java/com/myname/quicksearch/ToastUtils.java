package com.myname.quicksearch;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/28.
 */

public class ToastUtils {
    //    public ToastUtils{
//        throw  new UnsupportedOperationException("cannot be instantiated");
//    }
    public static boolean isShow = true;

    public static void ShowStaticToast(final Activity activity, final String msg
    ) {
        String name = Thread.currentThread().getName();
        if ("main".equals(name)) {
            if (isShow) {
                showToast(activity, msg
                );
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isShow) {
                        showToast(activity, msg);
                    }
                }
            });
        }

    }

    private static Toast toast;

    private static void showToast(Activity activity, String msg) {
        if (toast == null) {
            toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        if (isShow) {
            toast.show();
        }
    }
}
