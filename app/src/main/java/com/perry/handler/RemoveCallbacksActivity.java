package com.perry.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RemoveCallbacksActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, RemoveCallbacksActivity.class));
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
        RemoveCallbacksActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RemoveCallbacksActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.e("tag", " ------>>> " + msg);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_callbacks);
    }

    /**
     * handler.removeCallbacks(runnable);
     *
     * @param view
     */
    public void removeCallbacks(View view) {
        Log.e("tag", " ------>>> " + "removeCallbacks按钮 " + getThreadName());
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                makeToast("postDelayed removeCallbacks " + getThreadName());
            }
        };
        handler.postDelayed(runnable, 1000);
        handler.removeCallbacks(runnable);
    }

    /**
     * handler.removeCallbacks(runnable, obj);
     *
     * @param view
     */
    public void removeCallbacksToken(View view) {
        Log.e("tag", " ------>>> " + "removeCallbacks按钮 " + getThreadName() + System.currentTimeMillis());
        final Handler handler = new Handler();
        TokenBean removeTokenBean = new TokenBean("RunnableTokenBeanRemove");
        TokenBean tokenBean = new TokenBean("RunnableTokenBeanNoRemove");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                makeToast("postDelayed removeCallbacks " + getThreadName() + +System.currentTimeMillis());
            }
        };
        handler.postAtTime(runnable, removeTokenBean, SystemClock.uptimeMillis() + 2000);
        handler.postAtTime(runnable, tokenBean, SystemClock.uptimeMillis() + 1500);
        handler.removeCallbacks(runnable, removeTokenBean);
    }

    /**
     * handler.removeMessages(what = 0);
     *
     * @param view
     */
    public void removeMessagesWhat(View view) {
        Log.e("tag", " ------>>> " + "removeMessagesWhat按钮 " + getThreadName() + System.currentTimeMillis());
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        makeToast("handleMessage what = 0 " + getThreadName() + +System.currentTimeMillis());
                        break;
                    case 1:
                        makeToast("handleMessage what = 1 " + getThreadName() + +System.currentTimeMillis());
                        break;
                }
            }
        };
        Message messageWhat = Message.obtain(handler, 0);
        Message message = Message.obtain(handler, 1);
        handler.sendMessageDelayed(messageWhat, 1500);
        handler.sendMessageDelayed(message, 2000);
        handler.removeMessages(0);
    }

    /**
     * handler.removeMessages(what = 0, obj);
     *
     * @param view
     */
    public void removeMessagesWhatToken(View view) {
        Log.e("tag", " ------>>> " + "removeMessagesWhatToken按钮 " + getThreadName() + System.currentTimeMillis());
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (msg.obj != null)
                            makeToast("handleMessage what = 0 obj = " + msg.obj.toString() + getThreadName() + +System.currentTimeMillis());
                        break;
                }
            }
        };
        TokenBean removeTokenBean = new TokenBean("RunnableTokenBeanRemove");
        TokenBean tokenBean = new TokenBean("RunnableTokenBeanNoRemove");
        Message messageWhat = Message.obtain(handler, 0, removeTokenBean);
        Message message = Message.obtain(handler, 0, tokenBean);
        handler.sendMessageDelayed(messageWhat, 1500);
        handler.sendMessageDelayed(message, 2000);
        handler.removeMessages(0, removeTokenBean);
    }

    /**
     * handler.removeCallbacksAndMessages(obj);
     *
     * @param view
     */
    public void removeCallbacksAndMessages(View view) {
        Log.e("tag", " ------>>> " + "removeCallbacksAndMessages按钮 " + getThreadName() + System.currentTimeMillis());
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (msg.obj != null)
                            makeToast("handleMessage what = 0 obj = " + msg.obj.toString() + getThreadName() + +System.currentTimeMillis());
                        break;
                }
            }
        };
        TokenBean removeTokenBean = new TokenBean("RunnableTokenBeanRemove");
        TokenBean tokenBean = new TokenBean("RunnableTokenBeanNoRemove");
        Message messageWhat = Message.obtain(handler, 0, removeTokenBean);
        Message message = Message.obtain(handler, 0, tokenBean);
        handler.sendMessageDelayed(messageWhat, 1500);
        handler.sendMessageDelayed(message, 2000);
        handler.postAtTime(
                new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Runnable TokenBeanRemove " + getThreadName() + +System.currentTimeMillis());
                    }
                },
                removeTokenBean,
                SystemClock.uptimeMillis() + 2500
        );
        handler.postAtTime(
                new Runnable() {
                    @Override
                    public void run() {
                        makeToast("Runnable TokenBeanNoRemove " + getThreadName() + +System.currentTimeMillis());
                    }
                },
                tokenBean,
                SystemClock.uptimeMillis() + 3000
        );
        handler.removeCallbacksAndMessages(removeTokenBean);
    }

    public void handlerThreadQuit(View view) {
        Log.e("tag", " ------>>> " + "handlerThreadQuit按钮 " + getThreadName() + System.currentTimeMillis());
        final HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        // 首先延迟1s发送一条消息
                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postDelayed 1s runnable");
                                    }
                                }, 1000
                        );

                        // handler执行runnable进行退出looper
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        handlerThread.quit();
                                        makeToast("HandlerThread Loop quit");
                                    }
                                }
                        );
                    }
                }
        );
        t.start();
    }

    public void handlerThreadQuitSafely(View view) {
        Log.e("tag", " ------>>> " + "handlerThreadQuitSafely按钮 " + getThreadName() + System.currentTimeMillis());
        final HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        // 首先延迟1s发送一条消息
                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postDelayed 1s runnable");
                                    }
                                }, 1000
                        );

                        // 延迟2s发送一条消息
                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postDelayed 2s runnable");
                                    }
                                }, 1000
                        );
                        // 发送一条消息
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postDelayed 0s runnable1");
                                    }
                                }
                        );
                        // 发送一条消息
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        makeToast("postDelayed 0s runnable2");
                                    }
                                }
                        );

                        // handler执行runnable进行退出looper
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                            handlerThread.quitSafely();
                                            makeToast("HandlerThread Loop quitSafely");
                                        } else {
                                            makeToast("api < 18");
                                        }
                                    }
                                }
                        );
                    }
                }
        );
        t.start();
    }
}
