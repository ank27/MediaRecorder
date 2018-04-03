package ankurkhandelwal.example.com.pulseemotionadrenaline;

import android.app.Application;

import java.util.ArrayList;

import ankurkhandelwal.example.com.pulseemotionadrenaline.Models.User;
import ankurkhandelwal.example.com.pulseemotionadrenaline.Models.UserEmotion;

/**
 * @author Ankur Khandelwal on 27/03/18.
 */

public class EmotionApplication extends Application {
    public static EmotionApplication instance;
    public static ArrayList<User> userArrayList = new ArrayList<>();
    public static ArrayList<UserEmotion> userEmotionArrayList = new ArrayList<>();
    @Override public void onCreate() {
        super.onCreate();
        PreferencesManager.initialize(this);
    }
}
