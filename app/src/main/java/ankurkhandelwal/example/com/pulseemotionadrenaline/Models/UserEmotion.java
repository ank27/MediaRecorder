package ankurkhandelwal.example.com.pulseemotionadrenaline.Models;

import org.json.JSONObject;

/**
 * @author Ankur Khandelwal on 27/03/18.
 */

public class UserEmotion {
    public UserEmotion(int userId, String userEmail, long timestamp, JSONObject emotion,String finalEmotion) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.timestamp = timestamp;
        this.emotion = emotion;
        this.finalEmotion = finalEmotion;
    }

    private int userId;
    private String userEmail;
    private long timestamp;
    private JSONObject emotion;
    private String finalEmotion;

    public String getFinalEmotion() {
        return finalEmotion;
    }

    public void setFinalEmotion(String finalEmotion) {
        this.finalEmotion = finalEmotion;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public JSONObject getEmotion() {
        return emotion;
    }

    public void setEmotion(JSONObject emotion) {
        this.emotion = emotion;
    }
}
