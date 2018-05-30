package com.perry.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BasicUseActivity extends AppCompatActivity {

    public static final long ONE_SECOND = 1000;
    public static final long TWO_SECOND = 2000;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, BasicUseActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_use);
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
        BasicUseActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BasicUseActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.e("tag", " ------>>> " + msg);
            }
        });
    }

    public void post(View view) {
        Log.e("tag", " ------>>> " + "点击Post按钮 " + getThreadName());
        // 在主线程创建handler
        final Handler handler = new Handler();
        // 创建一个非主线程的新线程
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", " ------>>> " + "执行非主线程runnable " + getThreadName());
                        // handler.post(runnable) 去观察是否回到主线程
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("post runnable" + getThreadName());
                                    }
                                }
                        );
                    }
                }
        );
        // 运行该线程
        thread.start();
    }

    public void postDelayed(View view) {
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        makeToast("postDelayed " + getThreadName());
                    }
                }, ONE_SECOND
        );
    }

    public void postAtTime(View view) {
        Handler handler = new Handler();
//        long atTime = System.currentTimeMillis() + TWO_SECOND;
        long atTime = SystemClock.uptimeMillis() + TWO_SECOND;
        handler.postAtTime(
                new Runnable() {
                    @Override
                    public void run() {
                        makeToast("postAtTime " + getThreadName());
                    }
                }, atTime
        );
    }

    public void postAtFrontOfQueue(View view) {
        Handler handler = new Handler();
        handler.postAtFrontOfQueue(
                new Runnable() {
                    @Override
                    public void run() {
                        makeToast("postAtFrontOfQueue " + getThreadName());
                    }
                }
        );
    }

    public void sendMessage(View view) {
        Log.e("tag", " ------>>> " + "点击sendMessage按钮 " + getThreadName());
        final Handler handler = new Handler() {
            // 此处处理msg需要覆写该方法
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        makeToast("sendMessage" + " 参数 = " + msg.obj + getThreadName());
                        break;
                }

            }
        };
        // 创建一个非主线程的新线程
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", " ------>>> " + "在非主线程执行 handler.sendMessage(msg） " + getThreadName());
                        // handler.sendMessage(msg) 去观察是否回到主线程
                        handler.sendMessage(Message.obtain(handler, 0, "obj参数"));
                    }
                }
        );
        // 运行该线程
        thread.start();
    }

    public void sendEmptyMessage(View view) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        makeToast("sendEmptyMessage" + " 空消息 " + getThreadName());
                        break;
                }
            }
        };
        handler.sendEmptyMessage(0);
    }

    public void sendMessageDelayed(View view) {
        Log.e("tag", " ------>>> " + "sendMessageDelayed " + System.currentTimeMillis());
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        makeToast("sendMessageDelayed" + " 参数 = " + msg.obj + getThreadName());
                        Log.e("tag", " ------>>> " + "sendMessageDelayed " + System.currentTimeMillis());
                        break;
                }
            }
        };
        handler.sendMessageDelayed(Message.obtain(handler, 0, "obj参数"), ONE_SECOND);
    }

    public void sendMessageAtTime(View view) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        makeToast("sendMessageAtTime" + " 参数 = " + msg.obj + getThreadName());
                        break;
                }
            }
        };
        handler.sendMessageAtTime(Message.obtain(handler, 0, "obj参数"), SystemClock.uptimeMillis() + TWO_SECOND);
    }

    public void sendMessageAtFrontOfQueue(View view) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        makeToast("sendMessageAtFrontOfQueue" + " 参数 = " + msg.obj + getThreadName());
                        break;
                }
            }
        };
        handler.sendMessageAtFrontOfQueue(Message.obtain(handler, 0, "obj参数"));
    }

    public void idleHandler(View view) {
        Log.e("tag", " ------>>> " + "点击idleHandler按钮 " + getThreadName());
        Looper.myQueue().addIdleHandler(
                new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        Log.e("tag", " ------>>> " + "执行非主线程runnable " + getThreadName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        makeToast("IdleHandler " + getThreadName());
                        Log.e("tag", " ------>>> " + "idleHandler " + System.currentTimeMillis());
                        return false;
                    }
                }
        );
    }
}
