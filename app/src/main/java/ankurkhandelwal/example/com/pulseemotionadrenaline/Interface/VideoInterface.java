package ankurkhandelwal.example.com.pulseemotionadrenaline.Interface;

import java.util.HashMap;

import ankurkhandelwal.example.com.pulseemotionadrenaline.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * @author Ankur Khandelwal on 02/04/18.
 */

public interface VideoInterface {
    @Multipart
    @POST(Constants.VIDEO_URL)
    Call<ResponseBody> processVideo(@Part MultipartBody.Part video,
                                    @Part("userId") RequestBody userId,
                                    @Part("timestamp") RequestBody timestamp
    );


    @Multipart
    @POST(Constants.VIDEO_URL)
    Call<ResponseBody> processVideoParts(@Part MultipartBody.Part video,
                                    @Part("userId") RequestBody userId,
                                    @Part("timestamp") RequestBody timestamp
    );

    @GET(Constants.BASE_URL)
    Call<ResponseBody> checkServerResponse();
}