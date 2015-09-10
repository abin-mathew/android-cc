package com.example.amathew.codechallenge.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.amathew.codechallenge.IssuesAdapter;

/**
 * Created by amathew on 9/9/15.
 */
public class IssuesSyncService extends Service {

    private static IssuesAdapter mIssuesAdapter = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //return mIssuesAdapter.getSyncAdapterBinder();
    }
}
