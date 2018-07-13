package king.helper.manager;
import king.helper.model.*;
import java.net.*;
import java.io.*;

import android.annotation.TargetApi;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.widget.*;

public class Sender
{
	private Context context;
	private ConnectionManager connectionManager;
	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private final static String FLAG="Sender_HandlerThread";
	
	private final static int FLAG_DO_ACTION=0x00;
	
	private boolean isSending=false;
	
	private Instruction lastShortTimeInstruction;
	
	private OutputStream outputStream;
	
	private InstructionLine line;
	
	private final static int LINE_SIZE=20;
	
	private long interval=100;

	private boolean isPause=false;

	private final static String TAG="Sender";
	public Sender(Context context,ConnectionManager connectionManager){
		this.context=context;
		this.connectionManager=connectionManager;
		mHandlerThread=new HandlerThread(FLAG);
		mHandlerThread.start();
		mHandler=new Handler(mHandlerThread.getLooper()){
			@Override
            public void handleMessage(Message msg) {
				 switch(msg.what){
					 case FLAG_DO_ACTION:
						 doAction();
						 break;				 
				 }
			}
		};
		line=new InstructionLine(LINE_SIZE);
	}

	public void setInterval(long interval)
	{
		this.interval = interval;
	}

	public long getInterval()
	{
		return interval;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void release(){
		stop();
		mHandlerThread.quitSafely();
	}
	
	public void setConnectionManager(ConnectionManager connectionManager)
	{
		this.connectionManager = connectionManager;
	}

	public ConnectionManager getConnectionManager()
	{
		return connectionManager;
	}
	
	public void send(Instruction instruction){
		if(isPause){
			isSending=false;
			mHandler.removeMessages(FLAG_DO_ACTION);
		}else{
			if(instruction!=null){

				if(line.length()<LINE_SIZE){
					try
					{
						line.insert(instruction);
						if(!isSending){
							isSending=true;
							mHandler.sendEmptyMessage(FLAG_DO_ACTION);
						}
					}
					catch (Exception e)
					{e.printStackTrace();}
				}else{
					Toast.makeText(context,"队列已满，请等待指令处理完毕！",Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void start(){
		isPause=false;
	}

	public void stop(){
		isPause=true;
	}

	private void doAction(){
		Instruction ins=null;
		if(line.length()==0){
			if(lastShortTimeInstruction==null){
				ins=new WalkingInstruction(Type.INSTRUCTION_WALKING,"心跳命令");
			}else{
				ins=lastShortTimeInstruction;
			}
			//ins=new WalkingInstruction(Type.INSTRUCTION_WALKING,"心跳命令");
		}else{
			try
			{
				ins = (Instruction) line.poll();
			}
			catch (Exception e)
			{e.printStackTrace();}
		}
		    if(connectionManager!=null){
			   if(outputStream==null){
				    outputStream=connectionManager.getOutputStream();
			}
			try
			{
				if(ins!=null){
					outputStream.write(ins.getBody());
					outputStream.flush();

					if(isShortTimeInstruction(ins)){
						lastShortTimeInstruction=ins;
					}
				}else {
					Log.w(TAG,"ins is null !");
				}

			}
			catch (IOException e)
		 	{
				e.printStackTrace();
		   	}catch(NullPointerException e){
				e.printStackTrace();
			}finally{
		   		if(isPause){
		   			isSending=false;
		   			mHandler.removeMessages(FLAG_DO_ACTION);
				}else{
					mHandler.sendEmptyMessageDelayed(FLAG_DO_ACTION,interval);
				}
			}
	     }
   
	}
	
	private boolean isShortTimeInstruction(Instruction instruction){
		return (instruction.getType()>=Type.INSTRUCTION_WALKING);
	}
	
}
