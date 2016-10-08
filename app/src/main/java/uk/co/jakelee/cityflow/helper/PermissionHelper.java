package uk.co.jakelee.cityflow.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

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
}
