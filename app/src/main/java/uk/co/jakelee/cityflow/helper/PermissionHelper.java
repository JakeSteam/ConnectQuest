package uk.co.jakelee.cityflow.helper;

import android.Manifest;
import android.os.Build;

import com.aitorvs.android.allowme.AllowMe;
import com.aitorvs.android.allowme.AllowMeCallback;
import com.aitorvs.android.allowme.PermissionResultSet;

public class PermissionHelper {
    private static final int GENERIC_REQUEST_CODE = 1;

    /**
     * A MediaStore insert needs WRITE_EXTERNAL_STORAGE only up to API 28. From 33 it cannot be
     * granted at all, so asking would deny and silently swallow the save.
     */
    public static void runWithImageWriteAccess(final Runnable callback) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            callback.run();
        } else {
            runIfPossible(Manifest.permission.WRITE_EXTERNAL_STORAGE, callback);
        }
    }

    public static void runIfPossible(final String permission, final Runnable callback) {
        if (!AllowMe.isPermissionGranted(permission)) {
            new AllowMe.Builder()
                    .setPermissions(permission)
                    .setCallback(new AllowMeCallback() {
                        @Override
                        public void onPermissionResult(int requestCode, PermissionResultSet result) {
                            if (result.isGranted(permission)) {
                                callback.run();
                            }
                        }
                    })
                    .request(123);
        } else {
            callback.run();
        }
    }
}
