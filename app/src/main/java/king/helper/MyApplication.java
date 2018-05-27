package king.helper;
import android.app.*;
import android.widget.*;

public class MyApplication extends Application
{

	public final static boolean IS_ACCESS_SERVICE=true;
	
    public final static boolean IS_START_TEST_SERVER=false;

	public final static String TITLE="测试模式";
	
	public final static String HOST="192.168.12.1";
	
	public final static String PORT="8888";
	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		//Toast.makeText(this,"应用初始化...",Toast.LENGTH_SHORT).show();
	}
	
}
