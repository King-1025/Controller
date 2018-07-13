package king.helper;
import android.app.*;
import android.content.Context;
import android.os.Environment;

import king.helper.model.Type;

public class MyApplication extends Application
{

	public final static boolean IS_ACCESS_SERVICE=true;
	
    public final static boolean IS_START_TEST_SERVER=false;

	public final static boolean IS_ENABEL_VIDEO=true;

	public final static boolean IS_ENABLE_RECEIVE=false;
	
	//public final static boolean IS_ENABLE_LOG=false;
	
	public final static String TITLE="一般控制模式";
	
	public final static String HOST="192.168.12.1";
	
	public final static String PORT="8888";

	public final static long SPALASH_TIME=2000;
	
	public final static String VIDEO_URL="rtsp://admin:abcd1234@192.168.1.64:1554/h264/ch1/main/av_stream";

	public final static int PICTURE_SAVE_TYPE= Type.FLAG_SCREENSHOT_SAVE_AS_PNG;

	public final static int VIDEO_QUALITY=Type.FLAG_VIDEO_QUALITY_FLUENT;
	
	public final static int SPEED_TYPE=Type.FLAG_SPEED_LOW;
	
	public static String PICTURE_PATH;

	public static Context APP_CONTEXT;

	@Override
	public void onCreate()
	{
		super.onCreate();

		APP_CONTEXT=this;

		PICTURE_PATH=Environment.getExternalStorageDirectory().getPath()+"/"+getPackageName()+"/截图";

		//Toast.makeText(this,"应用初始化...",Toast.LENGTH_SHORT).show();
	}

}
