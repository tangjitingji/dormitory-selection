package cn.edu.pku.tangjiting.dormitoryselection;

import android.app.Application;

import cn.edu.pku.tangjiting.dormitoryselection.entity.StudentResponse;


public class MyApplication extends Application {
    private static MyApplication instance = new MyApplication();
    private static StudentResponse.User user;

    private MyApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    public void setUser (StudentResponse.User user){
        this.user = user;
    }

    public StudentResponse.User getUser (){
        return user;
    }
}
