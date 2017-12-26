package cn.edu.pku.tangjiting.dormitoryselection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.DORM_TYPE;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.USER;


public class SelectDromTypeActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dorm_type);
        setTitleContent("办理住宿");

        findViewById(R.id.ll_one).setOnClickListener(this);
        findViewById(R.id.ll_two).setOnClickListener(this);
        findViewById(R.id.ll_three).setOnClickListener(this);
        findViewById(R.id.ll_four).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_one:  startDealDormActivity(1); break;
            case R.id.ll_two:  startDealDormActivity(2); break;
            case R.id.ll_three:  startDealDormActivity(3); break;
            case R.id.ll_four:  startDealDormActivity(4); break;
        }
    }

    private void startDealDormActivity(int num){
        Intent intent = new Intent(SelectDromTypeActivity.this,DealDormActivity.class);
        intent.putExtra(DORM_TYPE, num);
        intent.putExtra(USER,MyApplication.getInstance().getUser());
        startActivity(intent);
    }
}
