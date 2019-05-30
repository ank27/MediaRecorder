package ankurkhandelwal.example.com.pulseemotionadrenaline;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectiva.android.affdex.sdk.detector.VideoFileDetector;

import java.util.List;

/**
 * @author Ankur Khandelwal on 05/04/18.
 */

public class AffDexVideoProcess implements Detector.ImageListener,Detector.FaceListener{
    private String TAG = AffDexVideoProcess.class.getSimpleName();
    Context context;
    VideoFileDetector detector;

    public void init(Context context){
        this.context = context;
    }

    public void startVideoProcess(String fileName){
        detector = new VideoFileDetector(context,fileName);
        detector.setImageListener(this);
        detector.setFaceListener(this);
        detector.setDetectAllExpressions(false);
        detector.setDetectAllEmotions(true);
        detector.setDetectAllEmojis(false);
        detector.setDetectAllAppearances(false);
    }

    @Override public void onImageResults(List<Face> faces, Frame frame, float v) {
        Log.d(TAG, " === onImageResults ===");
        if (faces == null)
            return; //frame was not processed

        if (faces.size() == 0)
            return; //no face found

        //For each face found
        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(i);

            //Appearance
            Face.GENDER genderValue = face.appearance.getGender();
            Log.d(TAG,"face gender value ="+genderValue);

            //Some Emotions
            float joy = face.emotions.getJoy();
            float anger = face.emotions.getAnger();
            float disgust = face.emotions.getDisgust();

            Log.d(TAG, " face emotion joy = "+joy + " & anger ="+anger + " &disgust ="+disgust);
        }
    }

    @Override public void onFaceDetectionStarted() {
        Log.d(TAG, "face detection started");
    }

    @Override public void onFaceDetectionStopped() {
        Log.d(TAG, "face detection stopped");
    }
}
