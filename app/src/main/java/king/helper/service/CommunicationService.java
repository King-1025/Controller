package king.helper.service;
import android.content.*;
import android.os.*;
import android.util.*;
import king.helper.ui.*;

public class CommunicationService extends BasedService
{
	private Handler mHandler;
	private Handler activityHandler;
	private final static String TAG="CommunicationService";
	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		
		mHandler=new Handler(this.getMainLooper()){

			@Override
			public void handleMessage(Message msg)
			{
				// TODO: Implement this method
				switch(msg.what){
					case BasedService.SERVICE_STATUS_OK:
						activityHandler=(Handler) msg.obj;
						Message tempMsg=mHandler.obtainMessage();
						tempMsg.what=ControlActivity.CONTROL_STATUS_OK;
						tempMsg.obj=mHandler;
						activityHandler.sendMessage(tempMsg);
						Log.i(TAG,"服务状态良好！");
						break;
				}
			}

		};
	
	}

	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return new Messenger(mHandler).getBinder();
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		// TODO: Implement this method
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}
