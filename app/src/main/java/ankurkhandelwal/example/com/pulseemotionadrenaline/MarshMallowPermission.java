package ankurkhandelwal.example.com.pulseemotionadrenaline;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MarshMallowPermission extends Activity {
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    public static final int READ_CALENDER_PERMISSION_REQUEST_CODE = 7;
    public static final int READ_CALL_LOG_PERMISSION_REQUEST_CODE = 6;
    public static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_SMS_PERMISSION_REQUEST_CODE = 3;
    public static final int RECEIVE_SMS_PERMISSION_REQUEST_CODE = 8;
    public static final int WRITE_CALENDER_PERMISSION_REQUEST_CODE = 9;
    public static final int WRITE_CONTACT_PERMISSION_REQUEST_CODE = 10;
    public static final int GET_LOCATION_PERMISSION_REQUEST_CODE = 11;
    public static final int RECORD_AUDIO = 12;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForExternalStorage() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForCamera() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.CAMERA") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadSMS() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_SMS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReceiveSMS() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.RECEIVE_SMS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadPhoneState() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_PHONE_STATE") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadContact() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_CONTACTS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadPhoneLogs() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_CALL_LOG") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForReadCalendar() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.READ_CALENDAR") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForWriteCalendar() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.WRITE_CALENDAR") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForWriteContact() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.WRITE_CONTACTS") == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForLocation() {
        if (ContextCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) == 0) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionForRecordAudio() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.RECORD_AUDIO") == 0) {
            return true;
        }
        return false;
    }


    public void requestPermissionForExternalStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        }
    }

    public void requestPermissionForCamera() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.CAMERA")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.CAMERA"}, 2);
        }
    }

    public void requestPermissionForReadSMS() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_SMS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_SMS"}, 3);
        }
    }

    public void requestPermissionForReceiveSMS() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.RECEIVE_SMS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.RECEIVE_SMS"}, 8);
        }
    }

    public void requestPermissionForReadPhoneState() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_PHONE_STATE")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_PHONE_STATE"}, 4);
        }
    }

    public void requestPermissionForReadContact() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_CONTACTS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_CONTACTS"}, 5);
        }
    }

    public void requestPermissionForReadPhoneLogs() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_CALL_LOG")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_CALL_LOG"}, 6);
        }
    }

    public void requestPermissionForReadCalender() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.READ_CALENDAR")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_CALENDAR"}, 7);
        }
    }

    public void requestPermissionForWriteCalender() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.WRITE_CALENDAR")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.WRITE_CALENDAR"}, 9);
        }
    }
    public void requestPermissionForWriteContact() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.WRITE_CONTACTS")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.WRITE_CONTACTS"}, 10);
        }
    }

    public void requestPermissionForLocation() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

    public void requestPermissionForRecordAudio() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, "android.permission.RECORD_AUDIO")) {
            ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.RECORD_AUDIO"}, 12);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "External Storage Permission  Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "External Storage Permission granted");
                    return;
                }
            case 2:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Camera Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Camera Permission granted");
                    return;
                }
            case 3:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Read Sms Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Read SmsPermission granted");
                    return;
                }
            case 4:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Phone State Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Phone State Permission granted");
                    return;
                }
            case 5:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Read Contact Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Read Contact Permission granted");
                    return;
                }
            case 6:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Read Phone Call logs Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Read Phone Call logs Permission granted");
                    return;
                }
            case 7:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Read CalendarData logs Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Read CalendarData Permission granted");
                    return;
                }
            case 8:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Receive  Sms Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Receive SmsPermission granted");
                    return;
                }
            case 9:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Write to Calendar Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Write to Calendar granted");
                    return;
                }
            case 10:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Write to Contacts Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Write to Contacts granted");
                    return;
                }

            case 11:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Access location Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Access location permission granted");
                    return;
                }

            case 12:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.d("BusTracker", "Record Audio Permission Denied");
                    return;
                } else {
                    Log.d("BusTracker", "Record Audio Permission Granted");
                    return;
                }
            default:
                return;
        }
    }
}
