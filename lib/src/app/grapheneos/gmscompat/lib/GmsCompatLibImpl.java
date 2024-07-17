package app.grapheneos.gmscompat.lib;

import android.content.Context;
import android.util.Log;

import com.android.internal.gmscompat.IGmsCompatLib;

/**
 * GmsCompatLibrary code is loaded into processes of apps that use GmsCompat.
 */
public class GmsCompatLibImpl implements IGmsCompatLib {
    private static final String TAG = "GmcLib";

    @Override
    public void init(Context appContext, String processName) {
        Log.d(TAG, "init: pkg: " + appContext.getPackageName() + ", process: " + processName);
    }
}
