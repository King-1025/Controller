package king.helper.service;
import android.content.*;
import android.os.*;
import android.util.*;
import king.helper.ui.*;
import king.helper.manager.*;
import king.helper.model.*;
import king.helper.iface.*;
import android.widget.*;
import king.helper.*;

public class CommunicationService extends BasedService implements OnConnectionListener
{
	private Handler activityHandler;
	private ServerManager sm;
	private ConnectionManager connectionManager;
	private Sender sender;
	private Receiver receiver;
	private final static String TAG="CommunicationService";
	@Override
	public void onCreate()
	{
		super.onCreate();

		connectionManager=new ConnectionManager(this);
		connectionManager.setOnConnectionListener(this);

		sender=new Sender(this,connectionManager);
		receiver=new Receiver(this,connectionManager);
	}


	public void setActivityHandler(Handler activityHandler)
	{
		this.activityHandler = activityHandler;
	}
	
	public void buildConnection(){
	    if(connectionManager!=null){
		   if(!connectionManager.isHasConnection()){
			   if(MyApplication.IS_START_TEST_SERVER){
				   connectionManager.bulid("0.0.0.0",MyApplication.PORT);
			   }else{
				   connectionManager.bulid(MyApplication.HOST,MyApplication.PORT);
			   }
		   }
	    }
	}
	
	@Override
	public IBinder onBind(Intent p1)
	{
		if(MyApplication.IS_START_TEST_SERVER){
			sm=new ServerManager(this,Integer.valueOf(MyApplication.PORT));
			sm.start();
		}
		receiver.start();
		sender.start();
		return new Brige(this);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		receiver.stop();
		sender.stop();
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		receiver.release();
		sender.release();
		connectionManager.release();
		if(MyApplication.IS_START_TEST_SERVER){
			sm.stop();
		}
	}
	
	@Override
	public void onConnect()
	{
		if(activityHandler!=null){
			activityHandler.sendEmptyMessage(ControlActivity.FLAG_CONNECTION_DOING);
		}
	}

	@Override
	public void onReconnect()
	{
		if(activityHandler!=null){
			activityHandler.sendEmptyMessage(ControlActivity.FLAG_CONNECTION_RECONNECT);
		}
	}

	@Override
	public void onClose()
	{
		if(activityHandler!=null){
			activityHandler.sendEmptyMessage(ControlActivity.FLAG_CONNECTION_CLOSE);
		}
	}

	@Override
	public void onSuccess()
	{
		receiver.start();
		sender.start();
		activityHandler.sendEmptyMessage(ControlActivity.FLAG__CONNECTION_SUCCESS);
	}

	@Override
	public void onFaild(String error)
	{
		// TODO: Implement this method
		activityHandler.sendEmptyMessage(ControlActivity.FLAG_CONNECTION_FAILD);
		Log.e(TAG,error);
	}
	
	public int getPower(){
		return receiver.getPowerValue();
	}

	public void directiy(Instruction instruction){
		if(instruction!=null){
			sender.send(instruction);
		}
	}

	public void pause(){
		receiver.stop();
		sender.stop();
		if(MyApplication.IS_START_TEST_SERVER){
			sm.stop();
		}
	}
}
