package com.perry.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class OnBackgroundActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, OnBackgroundActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_background);
    }

    /**
     * 判断是否为主线程
     *
     * @return true为主线程，false为非主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 获得线程名称
     *
     * @return 线程名
     */
    public static String getThreadName() {
        return (isMainThread() ? " 主线程 " : " 非主线程 ");
    }

    /**
     * 避免在非主线程弹toast报错
     *
     * @param msg toast消息
     */
    public void makeToast(final String msg) {
        OnBackgroundActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OnBackgroundActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.e("tag", " ------>>> " + msg);
            }
        });
    }

    /**
     * 此方法为测试在非主线程创建handler后执行post方法
     *
     * @param view
     */
    public void postBackgroundThread(View view) {
        Log.e("tag", " ------>>> " + "点击postBackgroundThread按钮 " + getThreadName());
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", " ------>>> " + "执行非主线程runnable " + getThreadName());
                        Handler handler = new Handler();
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postBackgroundThread " + getThreadName());
                                    }
                                }
                        );
                    }
                }
        );
        thread.start();

        /*
         崩溃日志
         05-30 15:26:07.446 711-711/com.perry.handler E/tag:  ------>>> 点击postBackgroundThread按钮  主线程
         05-30 15:26:07.486 711-773/com.perry.handler E/tag:  ------>>> 执行非主线程runnable  非主线程
         05-30 15:26:07.490 711-773/com.perry.handler E/AndroidRuntime: FATAL EXCEPTION: Thread-18868
         Process: com.perry.handler, PID: 711
         java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
         at android.os.Handler.<init>(Handler.java:209)
         at android.os.Handler.<init>(Handler.java:123)
         at com.perry.handler.OnBackgroundActivity$2.run(OnBackgroundActivity.java:71)
         at java.lang.Thread.run(Thread.java:818)
         */
    }

    public void postNoCrash(View view) {
        Log.e("tag", " ------>>> " + "点击postNoCrash按钮 " + getThreadName());
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", " ------>>> " + "执行非主线程runnable " + getThreadName());
                        Looper.prepare();

                        Handler handler = new Handler();
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postNoCrash " + getThreadName());
                                    }
                                }
                        );

                        Looper.loop();
                    }
                }
        );
        thread.start();
    }

    public void handleThread(View view) {
        final HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        Log.e("tag", " ------>>> " + "点击handleThread按钮 " + getThreadName());
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", " ------>>> " + "执行非主线程runnable " + getThreadName());
                        Handler handler = new Handler(handlerThread.getLooper());
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("handleThread " + getThreadName());
                                    }
                                }
                        );
                    }
                }
        );
        thread.start();
    }

}
