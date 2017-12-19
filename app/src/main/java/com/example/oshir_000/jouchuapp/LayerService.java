//package com.example.oshir_000.jouchuapp;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.PixelFormat;
//import android.os.IBinder;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
////
//public class LayerService extends Service {
//    View view;
//    WindowManager wm;
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//
//        // Viewからインフレータを作成する
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//
//        // 重ね合わせするViewの設定を行う
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG,
//                WindowManager.LayoutParamks.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT);
//
//        // WindowManagerを取得する
//        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//
//        // レイアウトファイルから重ね合わせするViewを作成する
//        view = layoutInflater.inflate(R.layout.sub_window_fragment, null);
//
//        // Viewを画面上に重ね合わせする
//        wm.addView(view, params);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        // サービスが破棄されるときには重ね合わせしていたViewを削除する
//        wm.removeView(view);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//}
