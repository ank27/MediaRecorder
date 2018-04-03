package ankurkhandelwal.example.com.pulseemotionadrenaline.Login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
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
import ankurkhandelwal.example.com.pulseemotionadrenaline.Models.User;
import ankurkhandelwal.example.com.pulseemotionadrenaline.PreferencesManager;
import ankurkhandelwal.example.com.pulseemotionadrenaline.R;

public class SignUpActivity extends AppCompatActivity {
    EditText name,password,email,rePassword;
    Button continueButton;
    private String mUserName, mUserEmail, mUserPassword, mUserConfirmPassword;
    String TAG = "SignUpActivity";
    private final static String SIGNUP_API_ENDPOINT_URL = "/api/v1/registrations.json";
    private String inviteCode;
    ProgressBar progressBar;
    CoordinatorLayout coordinate_signup;
//    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        setContentView(R.layout.activity_signup);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        continueButton=(Button) findViewById(R.id.continueButton);
        coordinate_signup=(CoordinatorLayout)findViewById(R.id.coordinate_signup);
        
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                EditText userNameField = (EditText) findViewById(R.id.name);
                EditText userEmailField = (EditText) findViewById(R.id.email);
                EditText userPasswordField = (EditText) findViewById(R.id.password);
                EditText userConfirmPasswordField = (EditText) findViewById(R.id.rePassword);
                mUserName = userNameField.getText().toString();
                mUserEmail = userEmailField.getText().toString();
                mUserPassword = userPasswordField.getText().toString();
                mUserConfirmPassword = userConfirmPasswordField.getText().toString();

                if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
                    Snackbar snack = Snackbar.make(coordinate_signup, "Please fill all fields", Snackbar.LENGTH_LONG);
                    snack.show();
                    return;
                }
                if (!mUserEmail.contains("@") || mUserEmail.indexOf('@') >= mUserEmail.lastIndexOf('.')) {
                    Snackbar snack = Snackbar.make(coordinate_signup, "Email is not valid!!", Snackbar.LENGTH_LONG);
                    snack.show();
                    return;
                }
                if (mUserPassword.length() < 4) {
                    Snackbar snack = Snackbar.make(coordinate_signup, "Password is too short. The minimum length required is 8 characters long.", Snackbar.LENGTH_LONG);
                    snack.show();
                    return;
                }
                if (!(mUserPassword.equals(mUserConfirmPassword))) {
                    Snackbar snack = Snackbar.make(coordinate_signup, "Password fields do not match", Snackbar.LENGTH_LONG);
                    snack.show();
                    return;
                }
                if (mUserName.length() == 0) {
                    Snackbar snack = Snackbar.make(coordinate_signup, "Please enter your name", Snackbar.LENGTH_LONG);
                    snack.show();
                    return;
                }
//                SignUpTask signupTask = new SignUpTask();
//                signupTask.execute(SIGNUP_API_ENDPOINT_URL);

                //save user data to realm
                saveUserDataToRealm(mUserName,mUserEmail,mUserPassword);

                SignUpAPITask signUpAPITask = new SignUpAPITask();
                String sigupUrl = "https://1vck3cls5b.execute-api.eu-central-1.amazonaws.com/prod?temp=5&userId="+mUserEmail.trim()+"&long=2.1&lat=3.1";
                signUpAPITask.execute(sigupUrl);
            }
        });
    }

    private void saveUserDataToRealm(String mUserName, String mUserEmail, String mUserPassword) {
//        realm=Realm.getDefaultInstance();
//        int count = realm.where(User.class).findAll().size();
//        Log.d(TAG,"count of user table ="+count);
//
        int count = EmotionApplication.userArrayList.size();
        User user = new User(count+1,mUserName,mUserEmail,mUserPassword);
        EmotionApplication.userArrayList.add(user);
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(user);
//        realm.commitTransaction();
//        realm.close();
//

        PreferencesManager.putInt("userId", count+1);
        PreferencesManager.putString("userEmail",mUserEmail);
        PreferencesManager.putString("userPassword",mUserPassword);
        PreferencesManager.putString("userName",mUserName);
//        Toast.makeText(getApplicationContext(),"Signup success ",Toast.LENGTH_LONG).show();
//        SignUpActivity.this.finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private class SignUpTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                Log.d(TAG, url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("charset", "utf-8");
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                JSONObject userObj=new JSONObject();
                JSONObject holder = new JSONObject();
                userObj.put("name", mUserName);
                userObj.put("email", mUserEmail);
                userObj.put("password", mUserPassword);
                userObj.put("password_confirmation", mUserPassword);
                holder.put(URLEncoder.encode("invite_code","UTF-8"), inviteCode);
                holder.put(URLEncoder.encode("user", "UTF-8"), userObj);
                Log.d("jsonParam",holder.toString());
                dataOutputStream.writeBytes(holder.toString());
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
                        response.append(line);
                    }
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
                Log.e("PostExecute", json.toString());
                progressBar.setVisibility(View.GONE);
                if (json.getBoolean("success")) {
//                    showAlert();
                }else{
                    Log.e("SUA", "failure returned");
                    JSONArray info = json.getJSONArray("info");
                    Snackbar snack = Snackbar.make(coordinate_signup, info.toString(), Snackbar.LENGTH_LONG);
                    snack.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    private class SignUpAPITask extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                Log.d(TAG, url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("charset", "utf-8");
                if(urlConnection.getResponseCode()==401){
                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("success",false);
                    return jsonObject;
                }else {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
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
                Log.e("PostExecute", json.toString());
                Toast.makeText(getApplicationContext(),"Signup Success",Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                SignUpActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                super.onPostExecute(json);
            }
        }
    }
}
