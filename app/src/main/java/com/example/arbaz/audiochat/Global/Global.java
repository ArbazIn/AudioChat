package com.example.arbaz.audiochat.Global;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

/**
 * Created by arbaz on 31/1/17.
 */

public class Global {
    private static final int MY_PERMISSIONS_REQUEST = 122;

    public static boolean checkPermission(final Context context) {

        int contact = ContextCompat.checkSelfPermission(context, READ_CONTACTS);
        int audio = ContextCompat.checkSelfPermission(context, RECORD_AUDIO);
        int callPhone = ContextCompat.checkSelfPermission(context, CALL_PHONE);
        int storage = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (contact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_CONTACTS);
        } if (audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(RECORD_AUDIO);
        } if (callPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(CALL_PHONE);
        } if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

}
