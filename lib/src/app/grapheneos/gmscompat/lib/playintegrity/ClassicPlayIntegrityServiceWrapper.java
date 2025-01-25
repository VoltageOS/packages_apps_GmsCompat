package app.grapheneos.gmscompat.lib.playintegrity;

import android.os.IBinder;

class ClassicPlayIntegrityServiceWrapper extends PlayIntegrityServiceWrapper {

    ClassicPlayIntegrityServiceWrapper(IBinder base) {
        super(base);
        requestIntegrityTokenTxnCode = 2; // IIntegrityService.Stub.TRANSACTION_requestIntegrityToken
    }
}
