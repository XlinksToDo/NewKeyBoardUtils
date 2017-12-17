package com.example.oshir_000.jouchuapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    private WindowManager _windowManager;
    private SubWindowFlagment _subWindowFragment;
    private SubWindowService _subWindowService;
    private static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1234; // 適当な数字でOK？
    String url="http://google";

    /*
     * メインの画面作成
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setKeepScreenOn(true);
        WebSettings settings = mWebView.getSettings();
        settings.setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setInitialScale(100);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setWebViewClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        mWebView.loadUrl(url);

        Button windowButton = (Button)this.findViewById(R.id.openWindowButton);
        windowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubWindow();
            }
        });
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.e("Error", error.toString());
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

    /*
     * サブウィンドウを作成
     */
    public void openSubWindow() {

        if (Build.VERSION.SDK_INT >= 26) {
            //Oreo 以上の処理
             if (Settings.canDrawOverlays(this)) {
                if (_subWindowFragment != null && _subWindowFragment.view != null) {
                    closeSubWindow();
                }
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT
                );
                params.gravity = Gravity.TOP | Gravity.LEFT;

                _windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                _subWindowFragment = new SubWindowFlagment();
                _windowManager.addView(_subWindowFragment.loadView(this), params);


            } else {
                // サブウィンドウ生成の権限がなかった場合は、下記で権限を取得する
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:com.hoge"));
                this.startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }else if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow 以上用の処理
            if (Settings.canDrawOverlays(this)) {
                if (_subWindowFragment != null && _subWindowFragment.view != null) {
                    closeSubWindow();
                }
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT
                );
                params.gravity = Gravity.TOP | Gravity.LEFT;

                _windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                _subWindowFragment = new SubWindowFlagment();
                _windowManager.addView(_subWindowFragment.loadView(this), params);


            } else {
                // サブウィンドウ生成の権限がなかった場合は、下記で権限を取得する
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:com.hoge"));
                this.startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        } else {
            // Lollipop 以前用の処理
            startService(new Intent(MainActivity.this, SubWindowService.class));
            // Binder によるサブウィンドウとの接続
            bindService(new Intent(MainActivity.this, SubWindowService.class),
                    _connection, Context.BIND_ABOVE_CLIENT);
        }
    }


    // Build.VERSION.SDK_INT < 22 の対応
    private ServiceConnection _connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            _subWindowService = ((SubWindowService.SubWindowServiceBinder)iBinder).getService();
            openWindow();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };
    public void openWindow() {
        _subWindowService.openWindow(this);
    }

    // Build.VERSION.SDK_INT >= 23 の対応
    public void closeSubWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (_subWindowFragment != null && _subWindowFragment.view != null) {
                _windowManager.removeView(_subWindowFragment.view);
                _subWindowFragment.view = null;
            }
        } else {
            _subWindowService.closeSubWindow();
            stopService(new Intent(MainActivity.this, SubWindowService.class));
        }
    }

    /*
     * Lollipop までは、メインウィンドウをバックグランドに入れる際に onDestroy が実行されてしまう
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= 23) {
            closeSubWindow();
        }
    }
    private final int REQUEST_CODE_MAIN_ACTIVITY = 100;
    private final int NOTIFICATION_CLICK = 100;

    private void sendNotification() {


        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
            getApplicationContext());
        PendingIntent contentIntent = PendingIntent.getActivity(
                MainActivity.this, REQUEST_CODE_MAIN_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_small, "アクション1", contentIntent);
        builder.addAction(R.drawable.ic_small, "アクション2", contentIntent);
        builder.addAction(R.drawable.ic_small, "アクション3", contentIntent);

        // NotificationManagerを取得
        NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        // Notificationを作成して通知
        manager.notify(NOTIFICATION_CLICK, builder.build());
    }


}