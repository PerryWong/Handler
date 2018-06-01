package com.perry.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class CustomHandlerActivity extends AppCompatActivity {

    private Handler mHandler = new CustomHandler(this);
    private int mMessageWhat = 1;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, CustomHandlerActivity.class));
    }

    private static class CustomHandler extends Handler {
        // 软引用当前activity，当释放资源时，不会因强引用而无法释放
        private WeakReference<CustomHandlerActivity> mActivityRef;

        public CustomHandler(CustomHandlerActivity activity) {
            mActivityRef = new WeakReference<CustomHandlerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 判断发送过程中是否资源已被释放，被释放则不执行
            if (mActivityRef != null) {
                Toast.makeText(mActivityRef.get(), "CustomHandler post msg", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_handler);
    }

    public void post(View view) {
        mHandler.sendMessage(Message.obtain(mHandler, mMessageWhat));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(mMessageWhat); // 界面销毁移除消息
    }
}
