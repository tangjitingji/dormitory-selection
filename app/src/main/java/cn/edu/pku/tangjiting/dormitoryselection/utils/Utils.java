package cn.edu.pku.tangjiting.dormitoryselection.utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static final Boolean DEBUGGING = true;
    public static  Boolean loginOK = false;
    public static String getMD5(String str) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] bytes = md.digest();
            return byte2HexString(bytes);
        }
        catch (NoSuchAlgorithmException e){
            System.out.println(e);
        }
        return  null;
    }

    private static String byte2HexString(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        int num = 0;
        for(int i = 0; i < bytes.length; i++){
            num = bytes[i];
            if(num < 0) num += 256;
            if(num < 16) sb.append("0");
            sb.append(Integer.toHexString(num));
        }

        return sb.toString();
    }

    public static void log(String flag, String msg){
        if(DEBUGGING){
            Log.i(flag, msg);
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //校验网络请求是否有误
    public static boolean checkResponseCode(Context context, String responseCode){
        Set<Map.Entry<String, String>> entrySet = Constants.errorCodeMap.entrySet();
        for(Map.Entry<String, String> entry:entrySet){
            String key = entry.getKey();
            if(key.trim().equals(responseCode.trim())){
                Looper.getMainLooper();
                Toast.makeText(context,entry.getValue(),Toast.LENGTH_SHORT).show();
                Looper.loop();
                return false;
            }
        }
        return true;
    }
}
