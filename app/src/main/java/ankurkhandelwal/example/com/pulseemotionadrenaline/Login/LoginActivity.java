package ankurkhandelwal.example.com.pulseemotionadrenaline.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import ankurkhandelwal.example.com.pulseemotionadrenaline.EmotionApplication;
import ankurkhandelwal.example.com.pulseemotionadrenaline.MainActivity;
import ankurkhandelwal.example.com.pulseemotionadrenaline.Models.User;
import ankurkhandelwal.example.com.pulseemotionadrenaline.PreferencesManager;
import ankurkhandelwal.example.com.pulseemotionadrenaline.R;

//import io.realm.Realm;

public class LoginActivity extends AppCompatActivity{
    String TAG = "LoginActivity";
    ProgressBar progressBar;

    private final static String LOGIN_API_ENDPOINT_URL = "/api/v1/sessions.json";

    private String mUserEmail, mUserPassword;
    TextView forgetPasswordText;
    Button signin_button,signup_button;
    CoordinatorLayout coordinate_login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String device_id="";
    private ProgressDialog mProgressDialog;
    Activity activity;
//    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        forgetPasswordText = (TextView) findViewById(R.id.forgetPasswordText);
        signup_button = (Button) findViewById(R.id.signup_button);
        signin_button = (Button) findViewById(R.id.signin_button);
        coordinate_login = (CoordinatorLayout) findViewById(R.id.coordinate_login);

        activity = this;
        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(forgotIntent);
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                EditText userEmailField = (EditText) findViewById(R.id.signin_email);
                EditText userPasswordField = (EditText) findViewById(R.id.sigin_password);
                mUserEmail = userEmailField.getText().toString();
                mUserPassword = userPasswordField.getText().toString();
                if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
                    Snackbar snack = Snackbar.make(coordinate_login, "Please complete all fields", Snackbar.LENGTH_LONG);
                    snack.show();
                } else {
//                    LoginTask loginTask = new LoginTask();
//                    loginTask.execute(LOGIN_API_ENDPOINT_URL);

                    //fetch user data from realm and compare
                    fetchDataFromRealm(mUserEmail,mUserPassword);
                }
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchDataFromRealm(String mUserEmail, String mUserPassword) {
//        Realm realm = Realm.getDefaultInstance();
//        List<User> payloads= new ArrayList<>();
//        payloads = realm.where(User.class).findAll();
//
//        Log.d(TAG,"payload size= "+payloads.size());
        int userId = PreferencesManager.getInt("userId",0);
        Log.d(TAG,"userId in preference ="+userId);

//        for(int i=0;i<payloads.size();i++){
//            Log.d(TAG,"realm user table userId ="+payloads.get(i).getId() + " &email = "+payloads.get(i).getEmail());
//            if(userId == payloads.get(i).getId()){
//                //user found
//                Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_LONG).show();
//                PreferencesManager.putString("isUserLoggedIn","true");
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                LoginActivity.this.finish();
//            }
//        }


                for(int i = 0; i< EmotionApplication.userArrayList.size(); i++){
                    User user = EmotionApplication.userArrayList.get(i);
            Log.d(TAG," user table userId ="+user.getId() + " &email = "+user.getEmail());
            if(userId == user.getId()){
                //user found
                Toast.makeText(getApplicationContext(),"Welcome "+user.getEmail(),Toast.LENGTH_LONG).show();
                PreferencesManager.putString("isUserLoggedIn","true");
                PreferencesManager.putInt("userId", user.getId());
                PreferencesManager.putString("userEmail",user.getEmail());
                PreferencesManager.putString("userPassword",user.getPassword());
                PreferencesManager.putString("userName",user.getName());
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                LoginActivity.this.finish();
            }
        }



    }


    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private class LoginTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
                try {
                    URL url = new URL(LOGIN_API_ENDPOINT_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("charset", "utf-8");
                    DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                    JSONObject userObj = new JSONObject();
                    userObj.put(URLEncoder.encode("email", "UTF-8"), mUserEmail);
                    userObj.put(URLEncoder.encode("password", "UTF-8"), URLEncoder.encode(mUserPassword, "UTF-8"));
                    dataOutputStream.writeBytes(userObj.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    if(urlConnection.getResponseCode()==401){
                        JSONObject jsonObject= new JSONObject();
                        jsonObject.put("success",false);
                        return jsonObject;
                    }else {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            Log.d(TAG, line);
                            response.append(line);
                        }
                        Log.d(TAG, String.valueOf(response));
                        return new JSONObject(response.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            return null;
            }


        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                progressBar.setVisibility(View.GONE);
                if (json.getBoolean("success")) {
                    Log.d(TAG,"json "+json.toString());
                    int userId = json.getJSONObject("data").getInt("user_id");
                    PreferencesManager.putInt("UserId", userId);
                    PreferencesManager.putString("user_email",mUserEmail);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    LoginActivity.this.finish();
                }else {
                    Log.d(TAG, "false");
                    Snackbar snack = Snackbar.make(coordinate_login, "Email or Password not correct.", Snackbar.LENGTH_LONG);
                    snack.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActivityResult ","requestcode "+requestCode + "resultCode "+resultCode);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

     private void updateUI() {

    }
    //show progressdialog for google signIn
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    //hide progressdialog for google signIn
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
