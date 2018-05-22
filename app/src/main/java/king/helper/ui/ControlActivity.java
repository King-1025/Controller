package king.helper.ui;
import android.os.*;
import king.helper.*;
import android.content.*;
import king.helper.service.*;
import android.util.*;
import android.widget.*;

public class ControlActivity extends BasedActivity
{
	private ServiceConnection serviceConnection;
	private Handler serviceHandler;
	private Handler mHandler;
	private boolean isBinded=false;
	private final static String TAG="ControlActivity";
	public final static int CONTROL_STATUS_OK=0x00;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		IS_PROHIBIT_BACK_BUTTON=true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		serviceConnection = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName p1, IBinder p2)
			{
				// TODO: Implement this method
				try
				{
					Message msg=mHandler.obtainMessage();
					msg.what=BasedService.SERVICE_STATUS_OK;
					msg.obj=mHandler;
				    new Messenger(p2).send(msg);
					isBinded=true;
				    
				}
				catch (RemoteException e)
				{
					e.printStackTrace();
				}catch(NullPointerException e){
					e.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName p1)
			{
				// TODO: Implement this method
	            isBinded=false;
			}
			
			
		};
		
		mHandler=new Handler(this.getMainLooper()){

			@Override
			public void handleMessage(Message msg)
			{
				// TODO: Implement this method
				super.handleMessage(msg);
				switch(msg.what){
					case CONTROL_STATUS_OK:
						serviceHandler=(Handler) msg.obj;
						Log.i(TAG,"控制状态良好！");
						Toast.makeText(self.getApplicationContext(),"初始化成功",Toast.LENGTH_SHORT).show();
						break;
				}
				
			}

		};
	}

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		bind(CommunicationService.class);
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
	}
	
	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		super.onStop();
		unBind();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}

	
	private void bind(Class<?> service){
		if(service!=null){
			Intent intent=new Intent();
			intent.setClass(this,service);
			bindService(intent,serviceConnection,BIND_AUTO_CREATE);
		}
	}
	
	private void unBind(){
		if(serviceConnection!=null){
			unbindService(serviceConnection);
			serviceConnection=null;
			isBinded=false;
		}
	}
}
