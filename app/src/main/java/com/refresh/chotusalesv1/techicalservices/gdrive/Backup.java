package com.refresh.chotusalesv1.techicalservices.gdrive;

//import android.view.=;

//import android.

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;

public interface Backup {
    void init(@NonNull final Activity activity);

    void start();

    void stop();

    GoogleApiClient getClient();
}