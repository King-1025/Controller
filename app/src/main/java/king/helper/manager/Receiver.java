package king.helper.manager;
import android.content.*;
import android.os.*;
import java.io.*;
import king.helper.iface.*;
import king.helper.model.*;
import android.util.*;
import king.helper.utils.*;

public class Receiver implements OnTransmissionListener
{
	private Context context;
	ConnectionManager connectionManager;
    private byte[]data;
	private final static String TAG="Receiver";

	public Receiver(Context context,ConnectionManager connectionManager){
		this.context=context;
		this.connectionManager=connectionManager;
		connectionManager.setOnTransmissionListener(this);
	}
	
	public byte[] getData()
	{
		return data;
	}

	public void setConnectionManager(ConnectionManager connectionManager)
	{
		this.connectionManager = connectionManager;
		connectionManager.setOnTransmissionListener(this);
	}

	public ConnectionManager getConnectionManager()
	{
		return connectionManager;
	}

	@Override
	public void receive(byte[] data)
	{
		this.data=data;
		//Log.i(TAG,"receive:"+Format.obtainString(data)+" power:"+getPowerValue());
	}
	
	public int getPowerValue(){
		return new Power(data).getValue();
	}
	
	public void setReceivedIntervalTime(int interval)
	{
		if(connectionManager!=null){
			connectionManager.setReceivedIntervalTime(interval);
		}
		
	}

	public int getReceivedIntervalTime()
	{
		if(connectionManager!=null){
			return connectionManager.getReceivedIntervalTime();
		}
		return -1;
	}

	public void start(){
		if(connectionManager!=null){
			connectionManager.startDataReceived(0);
		}
	}

	public void stop(){
		if(connectionManager!=null){
			connectionManager.pauseDataReceived();
		}
	}

	public void release(){
		stop();
		connectionManager=null;
		data=null;
	}
}
