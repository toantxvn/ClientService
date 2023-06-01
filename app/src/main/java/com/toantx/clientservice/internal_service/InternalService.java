package com.toantx.clientservice.internal_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class InternalService extends Service {
    private static final String TAG = "TXTX-InternalService";

    public InternalService() {
        Log.i(TAG, "InternalService: constructor()");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + intent + " - " + flags + " - " + startId);
        return START_STICKY;
    }

    String doubleString(String name) {
        Log.i(TAG, "doubleString: received: " + name);
        Log.i(TAG, "doubleString: return: " + name + name);
        return name + name;
    }

    @Override
    public IBinder onBind(Intent intent) {
        MyBinder myBinder = new MyBinder();
        Log.i(TAG, "onBind: " + myBinder + " - " + myBinder.getService());
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        Log.i(TAG, "onDestroy: done!");
    }

    class MyBinder extends Binder {
        InternalService getService() {
            return InternalService.this;
        }
    }

    private static Intent getIntent(Context context) {
        return new Intent(context, InternalService.class);
    }

    public static void startService(Context context) {
        context.startService(getIntent(context));
    }

    public static void stopService(Context context) {
        context.stopService(getIntent(context));
    }

    public static boolean bindService(Context context, ServiceConnection connection, int mode) {
        return context.bindService(getIntent(context), connection, mode);
    }
}