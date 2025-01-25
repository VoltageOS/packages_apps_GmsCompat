package app.grapheneos.gmscompat.lib;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.UserHandle;
import android.util.Log;

import com.android.internal.gmscompat.IGmsCompatLib;

import app.grapheneos.gmscompat.lib.playintegrity.PlayIntegrityUtils;

/**
 * GmsCompatLibrary code is loaded into processes of apps that use GmsCompat.
 */
public class GmsCompatLibImpl implements IGmsCompatLib {
    private static final String TAG = "GmcLib";

    @Override
    public void init(Context appContext, String processName) {
        Log.d(TAG, "init: pkg: " + appContext.getPackageName() + ", process: " + processName);
    }

    @Override
    public ServiceConnection maybeReplaceServiceConnection(Intent service, long flags, UserHandle user, ServiceConnection orig) {
        ServiceConnection override = PlayIntegrityUtils.maybeReplaceServiceConnection(service, orig);
        return override;
    }
}
