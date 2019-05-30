package ankurkhandelwal.example.com.pulseemotionadrenaline;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;

import ankurkhandelwal.example.com.pulseemotionadrenaline.Interface.VideoInterface;
import ankurkhandelwal.example.com.pulseemotionadrenaline.Networking.APIClient;
import ankurkhandelwal.example.com.pulseemotionadrenaline.Service.CameraService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private String TAG = MainActivity.class.getSimpleName();
    private boolean isFrontFacingCameraDetected;
    private ProgressBar progressBar;
    private TextView pleaseWaitTextView;
    private MarshMallowPermission marshMallowPermission;
    private boolean isRecording = false;

    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    private CountDownTimer timer;
    public static final String stopServiceAction = "isServiceStop";
    IntentFilter mIntentFilter;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marshMallowPermission = new MarshMallowPermission(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(stopServiceAction);

        initilizeUI();
        determineCameraAvailability();
        checkPermissions();
    }

    private void startCameraService() {
        if (marshMallowPermission.checkPermissionForCamera()) {
            Intent intent = new Intent(MainActivity.this, CameraService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(intent);
        } else {
            marshMallowPermission.requestPermissionForCamera();
        }
    }

    private void checkPermissions() {
        if (marshMallowPermission.checkPermissionForRecordAudio()) {
            //permission Granted
        } else {
            marshMallowPermission.requestPermissionForRecordAudio();
        }

        if (marshMallowPermission.checkPermissionForExternalStorage()) {
            //permission granted
        } else {
            marshMallowPermission.requestPermissionForExternalStorage();
        }

        if (marshMallowPermission.checkPermissionForCamera()) {
            //premission granted
//            startCameraService();
        } else {
            marshMallowPermission.requestPermissionForCamera();
        }
    }

    private void initilizeUI() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        pleaseWaitTextView = (TextView) findViewById(R.id.please_wait_textview);
        progressBar.setVisibility(View.VISIBLE);

        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

    }

    void determineCameraAvailability() {
        PackageManager manager = getPackageManager();
        isFrontFacingCameraDetected = manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        if (!isFrontFacingCameraDetected) {
            progressBar.setVisibility(View.GONE);
            pleaseWaitTextView.setVisibility(View.INVISIBLE);
            TextView notFoundTextView = (TextView) findViewById(R.id.not_found_textview);
            notFoundTextView.setVisibility(View.VISIBLE);
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(10000, 1000) {
            @Override public void onTick(long millisUntilFinished) {

            }

            @Override public void onFinish() {
                Log.d(TAG," == timer finish ===");
                //stop recording
                stopService(new Intent(MainActivity.this, CameraService.class));
            }
        };
        timer.start();
    }

    private File getSavedExternalFolder(){
        File pictureFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Emotion");
        if (!pictureFolder.exists()) {
            if (!pictureFolder.mkdir()) {
                Log.e(TAG, "Unable to create directory: " + pictureFolder.getAbsolutePath());
                return null;
            }
        }
        return pictureFolder;
    }

    private void getExternalFilesAndSendData() {
        File pictureFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Emotion");
        if (!pictureFolder.exists()) {
            if (!pictureFolder.mkdir()) {
                Log.e(TAG, "Unable to create directory: " + pictureFolder.getAbsolutePath());
                return;
            }
        }

        ArrayList<File> files = getListFiles(pictureFolder);
        Log.d(TAG,"files count in emotion folder = "+files);
        for(int i=0;i<files.size();i++){
            Log.d(TAG,"files name = "+files.get(i).getName() + " & size ="+files.get(i).getPath());
        }

        if(files.size()>0) {
            String userEmail = PreferencesManager.getString("userEmail", "");
            long timeStamp = System.currentTimeMillis();
            uploadVideoToServer(userEmail, timeStamp, files);
        }
    }


    private void emptyExternalFolder() {
        Log.d(TAG,"deleting external folder files");
        File pictureFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Emotion");
        if (!pictureFolder.exists()) {
            if (!pictureFolder.mkdir()) {
                Log.e(TAG, "Unable to create directory: " + pictureFolder.getAbsolutePath());
                return;
            }
        }

        Log.d(TAG,"size before empty ="+pictureFolder.listFiles().length);
        File[] files = pictureFolder.listFiles();
        for(int i=0; i<files.length; i++) {
            files[i].delete();
        }
        Log.d(TAG,"size after empty ="+pictureFolder.listFiles().length);

        //start camera service again
        startServiceAndTimer();

    }

    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        inFiles.addAll(Arrays.asList(files));
        return inFiles;
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override public void surfaceCreated(SurfaceHolder holder) {
        startServiceAndTimer();

    }

    public void startServiceAndTimer(){
        startCameraService();
        startTimer();
    }

    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        Log.d(TAG,"========= start activity for result ========");
        Log.d(TAG,"intent = "+intent.getData().toString() + " requestCode ="+requestCode);
    }

    private void uploadVideoToServer(String userEmail, long timeStamp, ArrayList<File> files){
        Log.d(TAG,"uploadVideoToServer email = "+userEmail + " &timestamp ="+timeStamp + "file ="+files.get(0).getName());
        Log.d(TAG,"url = "+Constants.VIDEO_URL);
        File videoFile = files.get(0);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video_file", videoFile.getName(), videoBody);

        VideoInterface vInterface = APIClient.createService(VideoInterface.class);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody timestamp = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(timeStamp));

        Call<ResponseBody> serverResponse = vInterface.processVideo(vFile,userId ,timestamp);
        serverResponse.enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                if(body!=null) {
                    Log.d(TAG, "Response body =" + body.toString());
                }else{
                    Log.d(TAG, "Response body error");
                }
                emptyExternalFolder();
            }

            @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
                emptyExternalFolder();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(stopServiceAction)) {
                Log.d(TAG,"isServiceStop = "+intent.getBooleanExtra("isServiceStopped",true));
                //send data to server
                getExternalFilesAndSendData();


                //send video to affdex class and get the result from emotion
                startEmotionProcess();

                //start camera service again
//                startServiceAndTimer();
            }
        }
    };

    private void startEmotionProcess() {
        Log.d(TAG,"==== START EMOTION PROCESS ====");
        File emotionFolder = getSavedExternalFolder();
        ArrayList<File> files = getListFiles(emotionFolder);
        Log.d(TAG,"files count in emotion folder = "+files);
        String filePath = files.get(0).getAbsolutePath();
        Log.d(TAG,"filePath = "+filePath);
//        AffDexVideoProcess process = new AffDexVideoProcess();
//        process.init(this);
//        process.startVideoProcess(filePath);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

}
