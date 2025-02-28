package app.grapheneos.gmscompat.location;

import android.annotation.Nullable;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class StubResolutionActivity extends Activity {
    private static final String TAG = "StubResolutionActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate, callingActivity: " + getCallingActivity());
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        finish();
    }
}
