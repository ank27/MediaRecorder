package ankurkhandelwal.example.com.pulseemotionadrenaline.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ankurkhandelwal.example.com.pulseemotionadrenaline.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    Toolbar toolbar_forgot;
    EditText email;
    Button doneButton;
    String mUserEmail;
    private final static String FORGOT_API_ENDPOINT_URL = "/api/v1/reset_password.json";
    String TAG="ForgotPassword";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        }
        setContentView(R.layout.activity_forgot_password);
        
        email = (EditText) findViewById(R.id.email);
        doneButton = (Button) findViewById(R.id.doneButton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                EditText userEmailField = (EditText) findViewById(R.id.email);
                mUserEmail = userEmailField.getText().toString();

                if (mUserEmail.length() == 0 || !mUserEmail.contains("@")) {
                    Snackbar snack = Snackbar.make(findViewById(R.id.coordinate_forgot), "Email address is not valid!!!", Snackbar.LENGTH_LONG);
                    snack.show();
                    return;
                } else {
                    
                }
//        });

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private class ResetPasswordTask extends AsyncTask<String, Void, JSONObject> {
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
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("charset", "utf-8");
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());

                Log.d("jsonParam",urls[1]);
                dataOutputStream.writeBytes(urls[1]);
                dataOutputStream.flush();
                dataOutputStream.close();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                return new JSONObject(response.toString());
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
//                    Snackbar snack = Snackbar.make(findViewById(R.id.coordinate_forgot), "A confirmation link has been sent your id. You will be able to sign in once you comfirm.", Snackbar.LENGTH_LONG);
//                    snack.show();
//                    Toast.makeText(getApplicationContext(),"A password change link has been sent to your email id",Toast.LENGTH_LONG).show();
                    showAlert();
//                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    ForgotPasswordActivity.this.finish();
                }else{
                    Log.d("SUA", "failure returned");
                    JSONArray info = json.getJSONArray("info");
                    Snackbar snack = Snackbar.make(findViewById(R.id.coordinate_forgot), info.toString(), Snackbar.LENGTH_LONG);
                    snack.show();
                }
            } catch (Exception e) {
                Snackbar snack = Snackbar.make(findViewById(R.id.coordinate_forgot), "An Error occured, Try again later!!!", Snackbar.LENGTH_LONG);
                snack.show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    private void showAlert() {
            Log.d(TAG,"Show alert!!!");
            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
            builder.setTitle("Change Password Email");
            builder.setMessage("A link to change your password has been sent to your email id.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    ForgotPasswordActivity.this.finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
    }
}
