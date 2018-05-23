package king.helper.service;
import android.content.*;
import android.os.*;
import android.util.*;
import king.helper.ui.*;
import king.helper.manager.*;
import king.helper.model.*;
import king.helper.iface.*;
import android.widget.*;

public class CommunicationService extends BasedService implements OnConnectionListener
{
	private Handler mHandler;
	private Handler activityHandler;
	private final static String TAG="CommunicationService";
	private ConnectionManager connectionManager;
	private Sender sender;
	private Receiver receiver;
	
	public final static int REQUEST_SEND_INSTRUCTION=0xA00;
	public final static int REQUEST_POWER=0xA01;
	public final static int RESPONSE_POWER=0xB00;
	public final static int RESPONSE_INSTRUCTION_STATUS=0xB01;
	
	
	private ServerManager sm;
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
						Log.d(TAG,"serviceHandler is ok！");
						break;
						case REQUEST_SEND_INSTRUCTION:
						    Instruction ins=(Instruction)msg.obj;
							sender.send(ins);
							break;
						case REQUEST_POWER:
							Message message=activityHandler.obtainMessage();
							message.what=RESPONSE_POWER;
							message.arg1=receiver.getPowerValue();
							activityHandler.sendMessage(message);
							break;
				}
			}

		};
	    
		sm=new ServerManager(this,8888);
		sm.start();
		
		connectionManager=new ConnectionManager(this);
		sender=new Sender(this,connectionManager);
		receiver=new Receiver(this,connectionManager);
		
		connectionManager.setOnConnectionListener(this);
		connectionManager.bulid("0.0.0.0","8888");
	}

	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		connectionManager.startDataReceived(1000);
		if(connectionManager.isHasConnection()){
			mHandler.sendEmptyMessageDelayed(REQUEST_POWER,1000);
		}
		return new Messenger(mHandler).getBinder();
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		// TODO: Implement this method
		mHandler.removeMessages(RESPONSE_POWER);
		connectionManager.pauseDataReceived();
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		sender.release();
		connectionManager.release();
		sm.stop();
	}
	
	@Override
	public void onConnect()
	{
		// TODO: Implement this method
	}

	@Override
	public void onReconnect()
	{
		// TODO: Implement this method
	}

	@Override
	public void onClose()
	{
		// TODO: Implement this method
	}

	@Override
	public void onSuccess()
	{
		// TODO: Implement this method
		connectionManager.startDataReceived(0);
		mHandler.sendEmptyMessageDelayed(REQUEST_POWER,1000);
	}

	@Override
	public void onFaild(String error)
	{
		// TODO: Implement this method
		Log.e(TAG,error);
	}
	
}
