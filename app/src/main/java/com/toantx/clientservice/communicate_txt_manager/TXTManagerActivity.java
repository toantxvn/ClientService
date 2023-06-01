package com.toantx.clientservice.communicate_txt_manager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.toantx.clientservice.R;
import com.toantx.txtmanager.MyData;
import com.toantx.txtmanager.TXTManager;

public class TXTManagerActivity extends AppCompatActivity {
    private static final String TAG = "TXTX-ManagerActivity";

    private TXTManager mTxtManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txtmanager);
        Log.i(TAG, "onCreate()");
        mTxtManager = new TXTManager(this);
        mTxtManager.bindToService();
        findViewById(R.id.set_value).setOnClickListener(v -> setValue());
    }

    private void setValue() {
        MyData data = new MyData("Toan", 29);
        MyData returnedData = mTxtManager.setValue(data);
        runOnUiThread(() -> {
            TextView tv = findViewById(R.id.tv_return_value);
            tv.setText(returnedData.toString());
        });
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        mTxtManager.unbindToService();
        mTxtManager = null;
        super.onDestroy();
        Log.i(TAG, "onDestroy: done!");
    }
}