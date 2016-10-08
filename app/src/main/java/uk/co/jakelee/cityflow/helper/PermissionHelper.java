package uk.co.jakelee.cityflow.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.aitorvs.android.allowme.AllowMe;
import com.aitorvs.android.allowme.AllowMeCallback;
import com.aitorvs.android.allowme.PermissionResultSet;

public class PermissionHelper {
    private static final int GENERIC_REQUEST_CODE = 1;
    public static String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static String VIBRATE = Manifest.permission.VIBRATE;
    public static String CAMERA = Manifest.permission.CAMERA;

    public static boolean confirmPermissions(Activity activity, String permission) {
        int hasPermission = ActivityCompat.checkSelfPermission(activity, permission);

        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] { permission },
                    GENERIC_REQUEST_CODE
            );
            return false;
        }
        return true;
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
