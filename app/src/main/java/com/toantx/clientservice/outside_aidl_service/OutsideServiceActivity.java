package com.toantx.clientservice.outside_aidl_service;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.toantx.clientservice.R;
import com.toantx.serverservice.MyPoint;
import com.toantx.serverservice.MyResultListener;
import com.toantx.serverservice.ServerServiceManager;

public class OutsideServiceActivity extends AppCompatActivity {
    private static final String TAG = "TXTX-OutsideServiceActivity";

    private ServerServiceManager mServiceManager;
    private int count; // value for test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_service);

        mServiceManager = new ServerServiceManager(this);
        boolean bindSuccess = mServiceManager.bindService();
        Log.i(TAG, "bindSuccess: " + bindSuccess);

        count = 0;
        findViewById(R.id.btn_generate_id).setOnClickListener(v -> generateId());
        findViewById(R.id.btn_fast_double_point).setOnClickListener(v -> fastDoublePoint());
        findViewById(R.id.btn_low_triple_point).setOnClickListener(v -> lowTriplePoint());
    }

    private void generateId() {
        try {
            String ans = mServiceManager.generateId("Toan", count++);
            runOnUiThread(() -> {
                TextView result = findViewById(R.id.tv_result_content);
                result.setText(ans);
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void fastDoublePoint() {
        MyPoint point = new MyPoint(count, 2 * count);
        try {
            MyPoint returnPoint = mServiceManager.fastDoublePoint(point);
            runOnUiThread(() -> {
                TextView result = findViewById(R.id.tv_result_content);
                result.setText(returnPoint.toString());
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void lowTriplePoint() {
        MyPoint point = new MyPoint(count * 10, 2 * count * 10);
        try {
            // Có thể hiển thị một Loading progress.
            mServiceManager.lowTriplePoint(point, resultListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private final MyResultListener.Stub resultListener = new MyResultListener.Stub() {
        @Override
        public void onResult(MyPoint myPoint) {
            Log.i(TAG, "onResult: " + myPoint);
            // Nhận được kết quả rồi, thì ẩn Loading progress đi.
            runOnUiThread(() -> {
                TextView result = findViewById(R.id.tv_result_content);
                result.setText(myPoint.toString());
            });
        }
    };

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        mServiceManager.unbindService();
        mServiceManager = null;
        super.onDestroy();
    }
}