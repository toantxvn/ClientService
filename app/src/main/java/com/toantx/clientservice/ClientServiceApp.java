package com.toantx.clientservice;

import android.app.Application;
import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientServiceApp extends Application {
    private static final String TAG = "TXTX-ClientServiceApp";

    private final Executor executor = Executors.newFixedThreadPool(4);


    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
        Log.i(TAG, "onCreate: done!");
    }

    private void makeLoginRequest(final String jsonBody) throws ExecutionException, InterruptedException, TimeoutException {
        executor.execute(() -> {
            // do somethings
        });

        CompletableFuture<Void> future = new CompletableFuture<>();
        future.get(5, TimeUnit.SECONDS);
        future.complete(null);
    }
}
