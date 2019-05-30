package ankurkhandelwal.example.com.pulseemotionadrenaline.Service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ankurkhandelwal.example.com.pulseemotionadrenaline.MainActivity;
import ankurkhandelwal.example.com.pulseemotionadrenaline.MarshMallowPermission;
import ankurkhandelwal.example.com.pulseemotionadrenaline.PreferencesManager;
import ankurkhandelwal.example.com.pulseemotionadrenaline.Utils;

/**
 * @author Ankur Khandelwal on 01/04/18.
 */

public class CameraService extends Service {
    private static final String TAG = "RecorderService";
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private static Camera mServiceCamera;
    private boolean mRecordingStatus;
    private MediaRecorder mMediaRecorder;
    private MarshMallowPermission marshMallowPermission;

    @Override
    public void onCreate() {
        Log.d(TAG,"service onCreate");
        mRecordingStatus = false;
        mServiceCamera = Camera.open(1);

        mSurfaceView = MainActivity.mSurfaceView;
        mSurfaceHolder = mSurfaceView.getHolder();

        super.onCreate();
        if (!mRecordingStatus)
            startRecording();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG , "====== onStartCommand =====");
        setCameraParameters();
        if (!mRecordingStatus)
            startRecording();

        return START_STICKY;
    }

    private void setCameraParameters() {
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Log.d(TAG,"orienataion = "+String.valueOf(display.getRotation()));

        Camera.Parameters params = mServiceCamera.getParameters();
        mServiceCamera.setParameters(params);
        Camera.Parameters cameraParameter = mServiceCamera.getParameters();


        final List<Camera.Size> listPreviewSize = cameraParameter.getSupportedPreviewSizes();
        for (Camera.Size size : listPreviewSize) {
            Log.i(TAG, String.format("Supported Preview Size (%d, %d)", size.width, size.height));
        }
        Camera.Size previewSize = listPreviewSize.get(0);

        if (listPreviewSize != null) {
            previewSize  = getOptimalPreviewSize(listPreviewSize, Utils.getScreenWidth(), Utils.getScreenHeight());
        }
        cameraParameter.setPreviewSize(previewSize.width, previewSize.height);
        mServiceCamera.setParameters(cameraParameter);
        mServiceCamera.setDisplayOrientation(90);

        try {
            mServiceCamera.setPreviewDisplay(mSurfaceHolder);
            mServiceCamera.startPreview();
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        stopRecording();
        mRecordingStatus = false;

        super.onDestroy();
    }

    public void startRecording(){
        try {
            Toast.makeText(getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();
            mServiceCamera.unlock();
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mServiceCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setMaxDuration(10000);
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

            File pictureFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Emotion");
            if (!pictureFolder.exists()) {
                if (!pictureFolder.mkdir()) {
                    Log.e(TAG, "Unable to create directory: " + pictureFolder.getAbsolutePath());
                    return;
                }
            }

            String userEmail = PreferencesManager.getString("userEmail", "");
            long timeStamp = System.currentTimeMillis();
            String file_name = userEmail.replace(".","_").concat(String.valueOf(timeStamp)).concat(".mp4");
            File audioFile = new File(pictureFolder, file_name);


            mMediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mRecordingStatus = true;

        } catch (IllegalStateException e) {
            Log.d(TAG, "error ="+e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void stopRecording() {
        Log.d(TAG,"===== stop recording ====");
        Toast.makeText(getBaseContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;

        mServiceCamera.stopPreview();
        mServiceCamera.release();
        mServiceCamera = null;

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.stopServiceAction);
        broadcastIntent.putExtra("isServiceStop", true);
        sendBroadcast(broadcastIntent);

    }
}