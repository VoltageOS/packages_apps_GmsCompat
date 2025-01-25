package app.grapheneos.gmscompat.lib.playintegrity;

import android.os.IBinder;

class StandardPlayIntegrityServiceWrapper extends PlayIntegrityServiceWrapper {

    StandardPlayIntegrityServiceWrapper(IBinder base) {
        super(base);
        requestIntegrityTokenTxnCode = 3; // IExpressIntegrityService.Stub.TRANSACTION_requestIntegrityToken
    }
}
