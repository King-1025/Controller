package king.helper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import king.helper.MyApplication;
import king.helper.model.Type;

/**
 * Created by King on 2018/5/30.
 */

public class Save {

    public static void putVideoURL(String url){
        put(Type.KEY_VIDEO_URL,url);
    }

    public static void putHost(String ip,String port){
        SharedPreferences sharedPreferences= MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Type.KEY_HOST,ip);
        editor.putString(Type.KEY_PORT,port);
        editor.commit();
    }

    public static String getIP(){
        return get(Type.KEY_HOST,MyApplication.HOST);
    }

    public static String getPort(){
        return get(Type.KEY_PORT,MyApplication.PORT);
    }

    public static String getVideoURL(){
        return get(Type.KEY_VIDEO_URL,MyApplication.VIDEO_URL);
    }

    public static int getPictureSaveType(){
        return get(Type.KEY_PICTURE_SAVE_TYPE,MyApplication.PICTURE_SAVE_TYPE);
    }

    public static String getPictureSavePath(){
        return get(Type.KEY_PICTURE_SAVE_PATH,MyApplication.PICTURE_PATH);
    }

    public static int getVideoQuality(){
        return get(Type.KEY_VIDEO_QUALITY,MyApplication.VIDEO_QUALITY);
    }

	public static boolean getServiceStatus(){
		return get(Type.KEY_STATUS_SERVICE,MyApplication.IS_ACCESS_SERVICE);
	}
	
	public static boolean getTestServerStatus(){
		return get(Type.KEY_STATUS_TEST_SERVER,MyApplication.IS_START_TEST_SERVER);
	}
	
	public static boolean getReceiveStatus(){
		return get(Type.KEY_STATUS_RECEIVE,MyApplication.IS_ENABLE_RECEIVE);
	}
	
	public static boolean getVideoStatus(){
		return get(Type.KEY_STATUS_VIDEO,MyApplication.IS_ENABEL_VIDEO);
	}
	
    public static void putPictureSaveType(int type){
        put(Type.KEY_PICTURE_SAVE_TYPE,type);
    }

    public static void putPictureSavePath(String path){
        put(Type.KEY_PICTURE_SAVE_PATH,path);
    }

    public static void putVideoQuality(int type){
        put(Type.KEY_VIDEO_QUALITY,MyApplication.VIDEO_QUALITY);
    }

    public static void put(String key,String value){
        SharedPreferences sharedPreferences= MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void put(String key,int value){
        SharedPreferences sharedPreferences= MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }
	
	public static void put(String key,boolean value){
        SharedPreferences sharedPreferences= MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
	

    public static String get(String key,String defaultValue){
        return MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE).
                getString(key,defaultValue);
    }

    public static int get(String key,int defaultValue){
        return MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE).
                getInt(key,defaultValue);
    }
	
	public static boolean get(String key,boolean defaultValue){
		return MyApplication.APP_CONTEXT.getSharedPreferences(null,Context.MODE_PRIVATE).
			getBoolean(key,defaultValue);
	}
}
