package com.example.oshir_000.jouchuapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;

public class SubWindowService extends Service {
    private WindowManager _windowManager;
    private SubWindowFlagment _windowFragment;

    /*
     * MainActivity とやりとりをするための Binder
     */
    public class SubWindowServiceBinder extends Binder {
        SubWindowService getService() {
            return SubWindowService.this;
        }
    }
    private final IBinder _binder = new SubWindowServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    /*
     * サブウィンドウの生成
     */
    public void openWindow(MainActivity activity) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, // 画面にタッチできるように SYSTEM_ALERT レイヤーに表示
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | // 下の画面を操作できるようにする
                        WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT; // 左上に表示

        _windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        _windowFragment = new SubWindowFlagment();
        _windowManager.addView(_windowFragment.loadView(activity), params);
    }


    /*
     * サブウィンドウを閉じる
     */
    public void closeSubWindow() {
        if (_windowFragment != null && _windowFragment.view != null) {
            _windowManager.removeView(_windowFragment.view);
            _windowFragment.view = null;
        }
    }
}