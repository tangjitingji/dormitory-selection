package cn.edu.pku.tangjiting.dormitoryselection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import cn.edu.pku.tangjiting.dormitoryselection.entity.Building;
import cn.edu.pku.tangjiting.dormitoryselection.entity.StudentResponse;
import cn.edu.pku.tangjiting.dormitoryselection.utils.MyOkhttpClient;
import cn.edu.pku.tangjiting.dormitoryselection.utils.Utils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.DORM_TYPE;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.GET_ROOM;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.RESPONSE_OK;
import static cn.edu.pku.tangjiting.dormitoryselection.utils.Constants.SELECT_ROOM_URL;

public class DealDormActivity extends BaseActivity {
    private int type;
    private LinearLayout ll_add_student;
    private TextView tv_people_together;
    private StudentResponse.User user;
    private TextView building_5, building_13, building_14, building_8, building_9,tv_selected_building;
    private ArrayList<RoomMateHolder> roomMateArr = new ArrayList<>();
    private LinkedHashMap<String,Integer> getAvailBuilding;

    private GetRoomTask mGetRoomTask = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(DORM_TYPE, 1);
        setContentView(R.layout.activity_deal_dorm);
        user = MyApplication.getInstance().getUser();

        initView();
        if (type != 1) {
            addLayout(type - 1);
            setTitleContent("多人办理住宿");
        } else {
            setTitleContent("单人办理住宿");
        }
        getRoomRetain(user.getGender());
    }

    private void getRoomRetain(String sex) {
        if (mGetRoomTask != null) {
            return;
        }

        mGetRoomTask = new GetRoomTask(sex);
        mGetRoomTask.execute((Void) null);
    }

    private void initView() {
        ll_add_student = (LinearLayout) findViewById(R.id.ll_add_student);
        tv_people_together = (TextView) findViewById(R.id.tv_people_together);
        ((TextView) findViewById(R.id.tv_stu_name)).setText(user.getName());
        ((TextView) findViewById(R.id.tv_stu_num)).setText(user.getStudentid());
        ((TextView) findViewById(R.id.tv_stu_sex)).setText(user.getGender());
        ((TextView) findViewById(R.id.tv_stu_code)).setText(user.getVcode());
        building_5 = (TextView) findViewById(R.id.tv_5_retain_num);
        building_13 = (TextView) findViewById(R.id.tv_13_retain_num);
        building_14 = (TextView) findViewById(R.id.tv_14_retain_num);
        building_8 = (TextView) findViewById(R.id.tv_8_retain_num);
        building_9 = (TextView) findViewById(R.id.tv_9_retain_num);
        tv_selected_building = (TextView) findViewById(R.id.tv_selected_building);
        findViewById(R.id.bt_start_deal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(tv_selected_building.getText().toString().trim())){
                    Toast.makeText(DealDormActivity.this,"请选择宿舍楼！",Toast.LENGTH_SHORT).show();
                    return;
                }
                SubmitTask task = new SubmitTask();
                if(task.isAvail()){
                    task.isAvail();
                    task.execute((Void) null);
                }else{
                    Toast.makeText(DealDormActivity.this,"同住人信息缺失！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.ll_choose_building).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRadioButton(getAvailBuilding);
            }
        });
    }


    private void initBuilding(Building buildingCollection) {
        building_5.setText(buildingCollection.get5()+"个");
        building_13.setText(buildingCollection.get13()+"个");
        building_14.setText(buildingCollection.get14()+"个");
        building_8.setText(buildingCollection.get8()+"个");
        building_9.setText(buildingCollection.get9()+"个");
    }


    private void addLayout(int num) {
        while (num > 0) {
            tv_people_together.setVisibility(View.VISIBLE);
            ll_add_student.setVisibility(View.VISIBLE);

            LinearLayout linearLayout_stu_id = new LinearLayout(DealDormActivity.this);
            linearLayout_stu_id.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout_stu_id.setOrientation(LinearLayout.HORIZONTAL);
            int pix = Utils.dip2px(DealDormActivity.this, 8);
            linearLayout_stu_id.setPadding(pix, pix, pix, pix);

            TextView title = new TextView(DealDormActivity.this);
            title.setText("学号");
            title.setTextSize(17);
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            title.setTypeface(font);
            title.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams layoutParams_id = new LinearLayout.LayoutParams(Utils.dip2px(DealDormActivity.this, 100), ViewGroup.LayoutParams.WRAP_CONTENT);
            title.setPadding(0, 0, Utils.dip2px(DealDormActivity.this, 10), 0);
            linearLayout_stu_id.addView(title, layoutParams_id);

            EditText content = new EditText(DealDormActivity.this);
            content.setTextSize(16);
            content.setHint("输入同住人学号！");
            content.clearFocus();
            content.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams layoutParams_content = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout_stu_id.addView(content, layoutParams_content);

            View line = new View(DealDormActivity.this);
            line.setBackgroundResource(R.color.colorGrayBackground);
            LinearLayout.LayoutParams layoutParams_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2px(DealDormActivity.this, 1));
            line.setLayoutParams(layoutParams_line);

            LinearLayout linearLayout_stu_code = new LinearLayout(DealDormActivity.this);
            linearLayout_stu_id.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout_stu_code.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout_stu_code.setPadding(pix, pix, pix, pix);

            TextView code_title = new TextView(DealDormActivity.this);
            code_title.setText("验证码");
            code_title.setTextSize(17);
            LinearLayout.LayoutParams layoutParams_code_title = new LinearLayout.LayoutParams(Utils.dip2px(DealDormActivity.this, 100), ViewGroup.LayoutParams.WRAP_CONTENT);
            code_title.setPadding(0, 0, Utils.dip2px(DealDormActivity.this, 10), 0);
            Typeface font2 = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            code_title.setTypeface(font2);
            linearLayout_stu_code.addView(code_title, layoutParams_code_title);

            EditText code_content = new EditText(DealDormActivity.this);
            code_content.setTextSize(16);
            code_content.setHint("输入同住人验证码！");
            code_content.clearFocus();
            code_content.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams layoutParams_code_content = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout_stu_code.addView(code_content, layoutParams_code_content);

            ll_add_student.addView(linearLayout_stu_id);
            ll_add_student.addView(line);
            ll_add_student.addView(linearLayout_stu_code);

            roomMateArr.add(new RoomMateHolder(content, code_content));
            num--;
        }
    }

    public class GetRoomTask extends AsyncTask<Void, Void, Building> {

        private final String sex;
        private OkHttpClient client = new OkHttpClient();

        GetRoomTask(String sex) {
            this.sex = sex;
        }

        @Override
        protected Building doInBackground(Void... params) {
            String login_url = GET_ROOM + "?gender=" + (sex.trim().equals("男") ? 1 : 2);
            try {
                Response response = MyOkhttpClient.get(login_url);
                if (response.code() == RESPONSE_OK) {
                    String response_json = response.body().string();
                    if (response_json != null) {
                        Building buildingInfo = parseStrToBuilding(response_json);
                        getAvailBuilding = buildingInfo.getAvailBuilding(type);
                        Utils.log(getClass().getName(), response_json);
                        return buildingInfo;
                    }
                    return null;
                } else {//网络错误
                    Utils.log(getClass().getName(), "get building info failed");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Building building) {
            mGetRoomTask = null;
            if (null != building) {
                initBuilding(building);
//                Toast.makeText(DealDormActivity.this, "success!", Toast.LENGTH_SHORT).show();
                Utils.log(DealDormActivity.this.getLocalClassName(), "student detail OK");

            } else {
//                Toast.makeText(DealDormActivity.this, "failed!", Toast.LENGTH_SHORT).show();
                Utils.log(getLocalClassName(), "student detail Fail");
            }
        }

        @Override
        protected void onCancelled() {
            mGetRoomTask = null;
        }
    }


    public class SubmitTask extends AsyncTask<Void, Void, Boolean> {

        private FormBody.Builder builder;
        private OkHttpClient client = new OkHttpClient();

        SubmitTask() {

        }

        public boolean isAvail(){
            int buildingNo = getAvailBuilding.get(selectedBuildingName);
            String[]bodyKeys = {"stu1id", "stu2id", "stu3id", "v1code", "v2code", "v3code"};
            builder = new FormBody.Builder();
            builder.add("num",type+"");
            builder.add("buildingNo",buildingNo+"");
            builder.add("stuid",user.getStudentid());
            for (int i = 0;i<roomMateArr.size();i++){
                RoomMateHolder holder = roomMateArr.get(i);
                String id = holder.idView.getText().toString();
                String vCode = holder.codeView.getText().toString();
                if((!TextUtils.isEmpty(id.trim()))&&(!TextUtils.isEmpty(vCode.trim()))){
                    builder.add(bodyKeys[i],id);
                    builder.add(bodyKeys[i+3],id);
                }else {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Response response = MyOkhttpClient.post(SELECT_ROOM_URL,builder.build());
                if (response.code() == RESPONSE_OK) {
                    String response_json = response.body().string();
                    if (response_json != null) {
//                        Looper.prepare();
//                        Toast.makeText(DealDormActivity.this,response_json,Toast.LENGTH_SHORT).show();
//                        Looper.loop();
                        Utils.log(getClass().getName(), response_json);
                        return true;
                    }
                    return false;
                } else {//网络错误
                    Utils.log(getClass().getName(), "Login fail");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                Intent intent = new Intent(DealDormActivity.this,SuccessActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(DealDormActivity.this, "failed!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(DealDormActivity.this, "You have cancelled this process!", Toast.LENGTH_SHORT).show();
        }
    }

    private class RoomMateHolder {
        public TextView idView;
        public TextView codeView;

        RoomMateHolder(TextView idView, TextView codeView) {
            this.idView = idView;
            this.codeView = codeView;
        }
    }

    private Building parseStrToBuilding(String jsonData) {
        Building building = null;
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonData);
            org.json.JSONObject buildingJsonObject = jsonObject.getJSONObject("data");
            building = new Building();
            building.set5(buildingJsonObject.getInt("5"));
            building.set13(buildingJsonObject.getInt("13"));
            building.set14(buildingJsonObject.getInt("14"));
            building.set8(buildingJsonObject.getInt("8"));
            building.set9(buildingJsonObject.getInt("9"));

        } catch (Exception e) {
            e.printStackTrace();

        }
        return building;
    }

    private int selectedIndex,selectedIndexTemp;
    private String selectedBuildingName;
    private void showRadioButton(LinkedHashMap<String, Integer> availBuilding) {

        Collection<String> keys = availBuilding.keySet();
        final String[] buildArr = new String[keys.size()];
        Iterator<String> iterator = keys.iterator();
        int index = 0;
        while (iterator.hasNext()){
            buildArr[index] = iterator.next();
            index++;
        }

        Dialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = selectedIndexTemp;
                        selectedBuildingName = buildArr[selectedIndexTemp];
                        tv_selected_building.setText(selectedBuildingName);
                    }
                })
                .setSingleChoiceItems(buildArr, selectedIndex,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectedIndexTemp = which;
                            }
                        })
                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                }).create();
        dialog.show();
    }
}