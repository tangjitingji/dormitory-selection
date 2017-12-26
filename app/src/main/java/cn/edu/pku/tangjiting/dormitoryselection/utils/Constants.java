package cn.edu.pku.tangjiting.dormitoryselection.utils;

import java.util.HashMap;


public class Constants {
    public static final String STUDENT_DETAIL_URL ="https://api.mysspku.com/index.php/V1/MobileCourse/getDetail";
    public static final String LOGIN_URL ="https://api.mysspku.com/index.php/V1/MobileCourse/Login";
    public static final String GET_ROOM = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom";
    public static final String SELECT_ROOM_URL = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
    public static final String OK = "0";
    public static final int  RESPONSE_OK = 200;
    public static final String  STU_ID = "stuid";
    public static final String  DORM_TYPE = "dormType";
    public static final String  USER = "user";

    //errcode 类别
    public static final HashMap<String, String> errorCodeMap = new HashMap<String, String>(){
        {
            put("40001","学号不存在");
            put("40002","学号或密码错误");
            put("40009","参数错误");
        }
    };
}
