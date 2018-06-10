package king.helper.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import king.helper.iface.OnConnectionListener;
import king.helper.iface.OnTransmissionListener;
import king.helper.utils.Format;
import java.io.*;
import java.net.*;

/**
 * Created by King on 2017/8/7.
 * 控制连接管理
 */

public class ConnectionManager
{
    private Context context;
	private String host;
    private int port;
	private HandlerThread handlerThread;
	private Handler mhandler;
	private Socket socket;
	private InputStream inputStream;
    private OutputStream outputStream;
	private boolean hasConnection=false;
	private boolean isStopReceive=false;
	private boolean isReconnection=false;
	private boolean isReceiving=false;
	private long RECONNECTION_DELAYED_TIME=5000;
	private int SEND_BUFFER_SIZE=1024;
	private int RECEIVE_BUFFER_SIZE=5;
	private int RECEIVED_INTERNAL_TIME=1000;
	private OnConnectionListener onConnectionListener;
    private OnTransmissionListener onTransmissionListener;
	
	private final static int ESTABLISH_CONNECTION=0x00;
	private final static int TRANSMISSION_DATA_RECEIVE=0x03;
	private final static int RELEASE_CONNECTION=0x04;
	
	private final static String TAG="ConnectionManager";
    private final static String FLAG="ConnectionManager_HandlerThread";

    public ConnectionManager(Context context){
        this.context=context;
        handlerThread=new HandlerThread(FLAG);
        handlerThread.start();
        mhandler=new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case ESTABLISH_CONNECTION:
						establish();
                        break;
					case TRANSMISSION_DATA_RECEIVE:
					    receive(RECEIVED_INTERNAL_TIME);
						break;
					case RELEASE_CONNECTION:
						destroy();
						break;
                }
            }
        };
    }

	public boolean isHasConnection()
	{
		return hasConnection;
	}

	public void setReceivedIntervalTime(int interval)
	{
		RECEIVED_INTERNAL_TIME = interval;
	}

	public int getReceivedIntervalTime()
	{
		return RECEIVED_INTERNAL_TIME;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}

	public OutputStream getOutputStream()
	{
		return outputStream;
	}
	
    public void bulid(String host,String port){
        if(host==null||port==null){
            Toast.makeText(context,"目标无效！",Toast.LENGTH_SHORT).show();
        }else {
            if(!hasConnection)
            {
				this.host=host;
				this.port=Integer.valueOf(port);
				mhandler.sendEmptyMessage(ESTABLISH_CONNECTION);
				Log.d(TAG,"bulid():开始建立连接...");
            }else {
				Log.d(TAG,"bulid():连接已经建立,请先关闭当前连接！");
            }
        }
    }
	
	public void startDataReceived(long delayTime){
		if(!isReceiving){
			if(inputStream!=null){
				isStopReceive=false;
				mhandler.sendEmptyMessageDelayed(TRANSMISSION_DATA_RECEIVE,delayTime);
			}else{
				Log.d(TAG,"startDataReceived()->inputStream is null.");
			}
		}else{
			Log.d(TAG,"startDataReceived():正在接受数据中...");
		}
	}
	
	public void pauseDataReceived(){
		isStopReceive=true;
		mhandler.removeMessages(TRANSMISSION_DATA_RECEIVE);
	}
	
	public void release(){
		mhandler.sendEmptyMessage(RELEASE_CONNECTION);
	}
	
    public void setOnConnectionListener(OnConnectionListener onConnectionListener){
        this.onConnectionListener=onConnectionListener;
    }
	
	public void setOnTransmissionListener(OnTransmissionListener onTransmissionListener)
	{
		this.onTransmissionListener = onTransmissionListener;
	}
	
	private void establish(){
		if(onConnectionListener!=null){
			onConnectionListener.onConnect();
		}

		socket=openSocket(host,port,SEND_BUFFER_SIZE,RECEIVE_BUFFER_SIZE);

		if(socket!=null){
			try
			{
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();

				if(onConnectionListener!=null){
					onConnectionListener.onSuccess();
				}

				if(isReconnection){
					startDataReceived(1000);
					isReconnection=false;
					Log.d(TAG,"重连成功！");
					Toast.makeText(context.getApplicationContext(),"重连成功！", Toast.LENGTH_SHORT).show();
				}else{
					Log.d(TAG,"连接建立成功！");
					Toast.makeText(context.getApplicationContext(),"连接成功！", Toast.LENGTH_SHORT).show();
				}

				hasConnection=true;
			}
			catch (IOException e)
			{
				e.printStackTrace();

				if(onConnectionListener!=null){
					onConnectionListener.onFaild("数据流获取失败!");
				}
				closeSocket(socket);
				hasConnection=false;
				mhandler.removeMessages(ESTABLISH_CONNECTION);
				mhandler.sendEmptyMessageDelayed(ESTABLISH_CONNECTION,2*RECONNECTION_DELAYED_TIME);
				Log.d(TAG,"handleMessage():数据流获取失败,正在尝试...");
				Toast.makeText(context.getApplicationContext(),"数据流获取失败,正在尝试...", Toast.LENGTH_SHORT).show();
			}
		}else{
			hasConnection=false;
			if(onConnectionListener!=null){
				onConnectionListener.onFaild("Socket获取失败!");
			}
			mhandler.removeMessages(ESTABLISH_CONNECTION);
			mhandler.sendEmptyMessageDelayed(ESTABLISH_CONNECTION,2*RECONNECTION_DELAYED_TIME);
			Log.d(TAG,"handleMessage():Socket获取失败,正在尝试...");
			Toast.makeText(context.getApplicationContext(),"Socket获取失败,正在尝试...", Toast.LENGTH_SHORT).show();
		}
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private void destroy(){
		if(onConnectionListener!=null){
			onConnectionListener.onClose();
		}
		pauseDataReceived();
        closeStream();
		closeSocket(socket);
		if(handlerThread!=null){
			handlerThread.quitSafely();
			handlerThread=null;
		}
    }
	
	private void reconnection(){
		pauseDataReceived();
		closeStream();
		closeSocket(socket);
		isReconnection=true;
		if(onConnectionListener!=null){
			onConnectionListener.onReconnect();
		}
		mhandler.removeMessages(ESTABLISH_CONNECTION);
		mhandler.sendEmptyMessageDelayed(ESTABLISH_CONNECTION,RECONNECTION_DELAYED_TIME);
	}
	
	private void closeStream(){
		hasConnection=false;
		if(inputStream!=null){
			try{inputStream.close();}
			catch (IOException e)
			{e.printStackTrace();}
			finally{inputStream=null;}
		}
		if(outputStream!=null){
			try{outputStream.close();}
			catch (IOException e)
			{e.printStackTrace();}
			finally{outputStream=null;}
		}
	}
	
	private void receive(long interval){
		if(isStopReceive){
			isReceiving=false;
			return;
		}
		isReceiving=true;
		byte []data=new byte[RECEIVE_BUFFER_SIZE];
		try
		{
			int count=inputStream.read(data);
			if (count>0)
			{
				if(onTransmissionListener!=null){
					onTransmissionListener.receive(data);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		    reconnection();
		}
		if(!isStopReceive){
			mhandler.sendEmptyMessageDelayed(TRANSMISSION_DATA_RECEIVE,interval);
		}else{
			mhandler.removeMessages(TRANSMISSION_DATA_RECEIVE);
			isReceiving=false;
		}
	}
	
	public Socket openSocket(String host,int port,int sendBufferSize,int receiveBufferSize){
		Socket tempSocket=null;
        if(host!=null&&port>=0&&port<=65535){
			//获取一个Socket对象，建立网络连接。可以尝试调整Socket参数，以优化连接。
			try {
				tempSocket=new Socket(host,port);
				//设置这些属性，可能导致与服务器的通信不稳定
				//tempSocket.setSoTimeout(10000);
				//tempSocket.setSoLinger(true,30);
				//tempSocket.setTcpNoDelay(true);
				tempSocket.setSendBufferSize(sendBufferSize);
				tempSocket.setReceiveBufferSize(receiveBufferSize);
				tempSocket.setKeepAlive(true);
			} catch (SocketException e) {
				e.printStackTrace();
				tempSocket=null;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				tempSocket=null;
			} catch (IOException e) {
				e.printStackTrace();
				tempSocket=null;
			}
		}
        return tempSocket;
    }

    public void closeSocket(Socket socket){
        try {
            if(socket!=null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
