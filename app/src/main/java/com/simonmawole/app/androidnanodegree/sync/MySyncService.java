package com.simonmawole.app.androidnanodegree.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.simonmawole.app.androidnanodegree.utility.Helpers;

public class MySyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static MySyncAdapter sMySyncAdapter = null;


    /**
     * Thread-safe constructor, creates static {@link MySyncAdapter} instance.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (sMySyncAdapter == null) {
                sMySyncAdapter = new MySyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    /**
     * Logging-only destructor.
     */
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Return Binder handle for IPC communication with {@link MySyncAdapter}.
     *
     * <p>New sync requests will be sent directly to the SyncAdapter using this channel.
     *
     * @param intent Calling intent
     * @return Binder handle for {@link MySyncAdapter}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return sMySyncAdapter.getSyncAdapterBinder();
    }
}