package app.grapheneos.gmscompat.lib.playintegrity;

import android.annotation.Nullable;
import android.content.Context;
import android.content.pm.GosPackageState;
import android.content.pm.GosPackageStateFlag;
import android.os.Binder;
import android.os.BinderWrapper;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.android.internal.os.BackgroundThread;

import static android.app.compat.gms.GmsCompat.appContext;

abstract class PlayIntegrityServiceWrapper extends BinderWrapper {
    final String TAG;
    protected int requestIntegrityTokenTxnCode;

    public PlayIntegrityServiceWrapper(IBinder base) {
        super(base);
        TAG = getClass().getSimpleName();
    }

    @Override
    public boolean transact(int code, Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        if (code == requestIntegrityTokenTxnCode) {
            onIntegrityTokenRequest();
        }
        return super.transact(code, data, reply, flags);
    }

    private void onIntegrityTokenRequest() {
        Runnable r = () -> {
            Context ctx = appContext();
            GosPackageState gosPs = GosPackageState.getForSelf(ctx);
            if (!gosPs.hasFlag(GosPackageStateFlag.PLAY_INTEGRITY_API_USED_AT_LEAST_ONCE)) {
                gosPs.createEditor(ctx.getPackageName(), ctx.getUser())
                        .addFlag(GosPackageStateFlag.PLAY_INTEGRITY_API_USED_AT_LEAST_ONCE)
                        .apply();
            }
        };
        BackgroundThread.getHandler().post(r);
    }
}
