package com.toantx.clientservice.outside_messenger_service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.toantx.clientservice.R;
import com.toantx.serverservice.MyPoint;

public class OutSideMessengerServiceActivity extends AppCompatActivity {
    private static final String TAG = "TXTX-OutSideMessengerServiceActivity";
    private static final int SAY_HELLO = 0;
    private static final int REPLY_HELLO = 1;

    private Messenger mSendingMessenger;
    private boolean isBound;

    private Messenger mReceivingMessenger;
    private Looper mLooper;
    private Handler mReceivingHandler;

    private static final String PACKAGE_NAME = "com.toantx.serverservice";
    private static final String CLASS_NAME = "com.toantx.serverservice.MyServerServiceUseMessager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_side_messenger_service);

        isBound = false;
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        mLooper = thread.getLooper();
        mReceivingHandler = new ReceivingHandler(mLooper);
        mReceivingMessenger = new Messenger(mReceivingHandler);
        bindToService();

        initComponent();
    }

    private void bindToService() {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        boolean result = bindService(intent, mConnection, BIND_AUTO_CREATE);
        Log.i(TAG, "bindToService: result: " + result);
    }

    private void unbindToService() {
        unbindService(mConnection);
        mSendingMessenger = null;
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + name + " - " + service);
            mSendingMessenger = new Messenger(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + name);
            isBound = false;
        }
    };

    private void initComponent() {
        findViewById(R.id.btn_send_information).setOnClickListener(v -> sendSayHello());
    }

    private void sendSayHello() {
        if (isBoundToService()) {
            Message msg = Message.obtain();
            msg.what = SAY_HELLO;
            Bundle bundle = new Bundle();
            bundle.putString("NAME", "Tran Xuan Toan");
            msg.obj = bundle;
            msg.replyTo = mReceivingMessenger;

            try {
                mSendingMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ReceivingHandler extends Handler {
        private ReceivingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case REPLY_HELLO: {
                    receiveHello(msg);
                    break;
                }
                default:
                    break;
            }
        }

        private void receiveHello(@NonNull Message msg) {
            Bundle incomingBundle = (Bundle) msg.obj;
            incomingBundle.setClassLoader(getClass().getClassLoader());
            MyPoint myPoint = incomingBundle.getParcelable("MYPOINT");
            Log.i(TAG, "Client received: " + myPoint);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        if (isBound) {
            unbindToService();
            mLooper.quitSafely();
            mReceivingHandler = null;
            mReceivingMessenger = null;
            isBound = false;
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy: done!");
    }

    private boolean isBoundToService() {
        if (!isBound) {
            Log.i(TAG, "Have not bound to service");
            return false;
        }
        return true;
    }
}
