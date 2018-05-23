package king.helper.ui;
import android.os.*;
import king.helper.*;
import android.content.*;
import king.helper.service.*;
import android.util.*;
import android.widget.*;
import king.helper.model.*;
import android.view.View.*;
import android.view.*;

public class ControlActivity extends BasedActivity implements OnClickListener
{
	private ServiceConnection serviceConnection;
	private Handler serviceHandler;
	private Handler mHandler;
	private boolean isBinded=false;
	private final static String TAG="ControlActivity";
	public final static int CONTROL_STATUS_OK=0x00;

	private Button test0;
	private Button test1;
	private TextView power;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		IS_PROHIBIT_BACK_BUTTON=true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		test0=(Button) findViewById(R.id.activity_main_test_0);
		//test0.setOnClickListener(this);
		
		test1=(Button) findViewById(R.id.activity_main_test_1);
		//test1.setOnClickListener(this);
		
		power=(TextView) findViewById(R.id.activity_control_power);
		
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
						Log.d(TAG,"activityHandler is ok！");
						break;
					case CommunicationService.RESPONSE_POWER:
						power.setText("电量:"+msg.arg1);
						serviceHandler.sendEmptyMessageDelayed(CommunicationService.REQUEST_POWER,1000);
						break;
					case CommunicationService.RESPONSE_INSTRUCTION_STATUS:
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
		mHandler.removeMessages(CommunicationService.RESPONSE_POWER);
		unBind();
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id=p1.getId();
		switch(id){
			case R.id.activity_main_test_0:
				test(Type.INSTRUCTION_WALKING_DIRECTION,"短效命令");
				break;
			case R.id.activity_main_test_1:
				test(Type.INSTRUCTION_WALKING,"长效命令");
				break;
		}
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
	
	
	public void test(int type,String des){
		WalkingInstruction instruction=new WalkingInstruction(type,des);
		if(type>Type.INSTRUCTION_WALKING){
			instruction.setVoiceCommand((byte)0xFF);
			instruction.setLEDCommand((byte)0xFF);
		}
		Message msg=serviceHandler.obtainMessage();
		msg.what=CommunicationService.REQUEST_SEND_INSTRUCTION;
		msg.obj=instruction;
		serviceHandler.sendMessage(msg);
	}
}
