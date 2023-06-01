package com.toantx.clientservice.internal_service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.toantx.clientservice.R;

public class InternalServiceActivity extends AppCompatActivity {
    private static final String TAG = "TXTX-InternalServiceActivity";
    private InternalService mInternalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_service);
        startAService();
        bindAService();

        findViewById(R.id.btn_double_string).setOnClickListener(v -> doubleString());
    }

    private void doubleString() {
        if (isBound()) {
            String ans = mInternalService.doubleString("Toan");
            runOnUiThread(() -> {
                TextView tv = findViewById(R.id.tv_result_content);
                tv.setText(ans);
            });
        }
    }

    private void startAService() {
        InternalService.startService(this);
    }

    private void stopAService() {
        InternalService.stopService(this);
    }

    private void bindAService() {
        InternalService.bindService(this, mConnection, BIND_AUTO_CREATE);
    }

    private void unbindAService() {
        unbindService(mConnection);
        mInternalService = null;
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + name + " - " + service);
            InternalService.MyBinder binder = (InternalService.MyBinder) service;
            mInternalService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + name);
            mInternalService = null;
        }
    };

    private boolean isBound() {
        if (mInternalService == null) {
            Log.i(TAG, "isBound: false");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        unbindAService();
        stopAService();
        super.onDestroy();
        Log.i(TAG, "onDestroy: done!");
    }
}
