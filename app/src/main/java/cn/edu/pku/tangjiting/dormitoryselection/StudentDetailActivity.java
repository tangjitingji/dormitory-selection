package cn.edu.pku.tangjiting.dormitoryselection;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import cn.edu.pku.tangjiting.dormitoryselection.entity.StudentResponse;
import cn.edu.pku.tangjiting.dormitoryselection.utils.MyOkhttpClient;
import cn.edu.pku.tangjiting.dormitoryselection.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.RESPONSE_OK;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.STUDENT_DETAIL_URL;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.STU_ID;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.USER;


public class StudentDetailActivity extends BaseActivity{
    private StuDetailTask mStuDetailTask = null;
    private String stuid;
    private ProgressBar mProgressView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        setTitleContent("办理住宿");
        stuid = getIntent().getStringExtra(STU_ID);

        mProgressView = (ProgressBar) findViewById(R.id.stu_detail_progress);
        findViewById(R.id.bt_start_deal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDetailActivity.this, SelectDromTypeActivity.class);
                intent.putExtra(USER,MyApplication.getInstance().getUser());
                startActivity(intent);
            }
        });

        if(!TextUtils.isEmpty(stuid)){
            attemptStudentDetail(stuid);
        }else {
            Toast.makeText(StudentDetailActivity.this,"the student id is wrong!",Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(StudentResponse studentResponse) {
        ((TextView)findViewById(R.id.tv_stu_name)).setText(studentResponse.getData().getName());
        ((TextView)findViewById(R.id.tv_stu_num)).setText(studentResponse.getData().getStudentid());
        ((TextView)findViewById(R.id.tv_stu_sex)).setText(studentResponse.getData().getGender());
        ((TextView)findViewById(R.id.tv_stu_code)).setText(studentResponse.getData().getVcode());
    }

    private void attemptStudentDetail(String stuid) {
        if (mStuDetailTask != null) {
            return;
        }

        showProgress(true);
        mStuDetailTask = new StuDetailTask(stuid);
        mStuDetailTask.execute((Void) null);

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public class StuDetailTask extends AsyncTask<Void, Void, StudentResponse> {

        private final String mStuId;
        private OkHttpClient client =new OkHttpClient();

        StuDetailTask(String stuid) {
            mStuId = stuid;
        }

        @Override
        protected StudentResponse doInBackground(Void... params) {
            String login_url =STUDENT_DETAIL_URL +"?stuid=" + mStuId;
            try {
                Response response = MyOkhttpClient.get(login_url);
                if (response.code() == RESPONSE_OK) {
                    String response_json = response.body().string();
                    if(response_json != null){
                        final Object obj = JSON.parseObject(response_json, StudentResponse.class);
                        Utils.log(getClass().getName(), response_json);
                        return (StudentResponse) obj;
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
        protected void onPostExecute(final StudentResponse studentResponse) {
            mStuDetailTask = null;
            showProgress(false);

            if (null != studentResponse) {
                initView(studentResponse);
                StudentResponse.User user = studentResponse.getData();
                MyApplication.getInstance().setUser(user);
//                Toast.makeText(StudentDetailActivity.this, "success!", Toast.LENGTH_SHORT).show();
                Utils.log(StudentDetailActivity.this.getLocalClassName(), "student detail OK");

            } else {
//                Toast.makeText(StudentDetailActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                Utils.log(getLocalClassName(), "student detail Fail");
            }
        }

        @Override
        protected void onCancelled() {
            mStuDetailTask = null;
            showProgress(false);
        }
    }
}
