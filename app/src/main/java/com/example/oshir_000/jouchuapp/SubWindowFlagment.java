package com.example.oshir_000.jouchuapp;


import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SubWindowFlagment extends Fragment {
    public View view;
    private MainActivity _activity;
    /*
     * 画面呼び出し
     */
    public View loadView(MainActivity mainActivity) {
        _activity = mainActivity;

        LayoutInflater inflater = LayoutInflater.from(_activity);
        view =  inflater.inflate(R.layout.sub_window_fragment, null);

        // メインウィンドウを閉じるボタン
        Button windowButton = (Button)view.findViewById(R.id.closeMainButton);
        windowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // closeMainWindow();
                KeyBoardUtils.forceShow(_activity);
            }
        });
        Button windoButton = (Button)view.findViewById(R.id.openMainButton);
        windoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // closeMainWindow();
                KeyBoardUtils.hide(_activity);
            }
        });

        // 常駐アプリを閉じるボタン
        Button closeButton = (Button)view.findViewById(R.id.closeSubButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSubWindow();
            }
        });

        return view;
    }

    /*
     * メインウィンドウを閉じる
     */
    private void closeMainWindow() {
        _activity.moveTaskToBack(true);
    }

    /*
     * サブウィンドウを閉じる
     */
    private void closeSubWindow() {
        _activity.closeSubWindow();
    }

    public Context getContext(){
        return getActivity().getApplicationContext();
    }
}