package king.helper;
import android.app.*;
import android.content.Context;
import android.os.Environment;
import android.widget.*;

import king.helper.model.Type;

public class MyApplication extends Application
{
	//xxx

	public final static boolean IS_ACCESS_SERVICE=true;
	
    public final static boolean IS_START_TEST_SERVER=false;

	public final static String TITLE="测试模式";
	
	public final static String HOST="192.168.12.1";
	
	public final static String PORT="8888";

	public final static long SPALASH_TIME=2000;

	public final static boolean IS_ENABEL_VIDEO=false;

	public final static String VIDEO_URL="rtsp://";

	public final static int PICTURE_SAVE_TYPE= Type.FLAG_SCREENSHOT_SAVE_AS_PNG;

	public final static int VIDEO_QUALITY=Type.FLAG_VIDEO_QUALITY_FLUENT;

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
