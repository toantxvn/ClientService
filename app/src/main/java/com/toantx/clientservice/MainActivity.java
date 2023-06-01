package com.toantx.clientservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.toantx.clientservice.communicate_txt_manager.TXTManagerActivity;
import com.toantx.clientservice.internal_service.InternalServiceActivity;
import com.toantx.clientservice.outside_aidl_service.OutsideServiceActivity;
import com.toantx.clientservice.outside_messenger_service.OutSideMessengerServiceActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TXTX-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bind_outside_aidl_service_activity).setOnClickListener(v ->
                startActivity(new Intent(this, OutsideServiceActivity.class)));

        findViewById(R.id.bind_outside_messenger_service_activity).setOnClickListener(v ->
                startActivity(new Intent(this, OutSideMessengerServiceActivity.class)));

        findViewById(R.id.internal_service).setOnClickListener(v ->
                startActivity(new Intent(this, InternalServiceActivity.class)));

        findViewById(R.id.connect_to_txtmanager).setOnClickListener(v ->
                startActivity(new Intent(this, TXTManagerActivity.class)));
    }
}