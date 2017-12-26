package cn.edu.pku.tangjiting.dormitoryselection;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import cn.edu.pku.tangjiting.dormitoryselection.entity.LoginResponse;
import cn.edu.pku.tangjiting.dormitoryselection.utils.MyOkhttpClient;
import cn.edu.pku.tangjiting.dormitoryselection.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.LOGIN_URL;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.RESPONSE_OK;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.STU_ID;

public class LoginActivity extends BaseActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitleContent(R.string.action_sign_in);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, LoginResponse> {

        private final String username;
        private final String mPassword;
        private  OkHttpClient client =new OkHttpClient();

        UserLoginTask(String email, String password) {
            username = email;
            mPassword = password;
        }

        @Override
        protected LoginResponse doInBackground(Void... params) {
            String login_url =LOGIN_URL +"?username=" + username + "&password=" + mPassword;
            try {
                Response response = MyOkhttpClient.get(login_url);
                if (response.code() == RESPONSE_OK) {
                    String response_json = response.body().string();
                    if(response_json != null){
                        final LoginResponse loginResponse = JSON.parseObject(response_json, LoginResponse.class);
                        Utils.log(getClass().getName(), response_json);
                        return loginResponse;
                    }
                    return null;
                }else{//网络错误
                    Utils.log(getClass().getName(), "Login fail");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final LoginResponse response) {
            mAuthTask = null;
            showProgress(false);
            if (Utils.checkResponseCode(LoginActivity.this , response.getErrcode())) {
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                Utils.log(LoginActivity.this.getLocalClassName(), "login OK");
                Intent intent = new Intent(LoginActivity.this, StudentDetailActivity.class);
                intent.putExtra(STU_ID, username);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

