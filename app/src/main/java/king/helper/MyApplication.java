package king.helper;
import android.app.*;
import android.widget.*;

public class MyApplication extends Application
{

	private final static boolean IS_TEST=true;

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		//Toast.makeText(this,"应用初始化...",Toast.LENGTH_SHORT).show();
	}
	
	public static boolean isTest()
	{
		return IS_TEST;
	}
	
}
