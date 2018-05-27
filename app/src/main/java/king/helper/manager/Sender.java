package king.helper.manager;
import king.helper.model.*;
import java.net.*;
import java.io.*;
import android.content.*;
import android.os.*;

public class Sender
{
	private Context context;
	ConnectionManager connectionManager;
	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private final static String FLAG="Sender_HandlerThread";
	private Instruction shortInstruction;
	private Instruction longTimeInstruction;
	private boolean isShortInstructionSending=false;
	private boolean isRecycleSend=true;
	private int interval=100;
	private final static int SEND_SHORT_TIME_INSTRUCTION=0x00;
	private final static int SEND_LONG_TIME_INSTRUCTION=0x01;
	
	private final static int SEND_FLUSH=0x02;
	
	//声明为全局变量，可以达到一定的优化
	private OutputStream outputStream;
	
	public Sender(Context context,ConnectionManager connectionManager){
		this.context=context;
		this.connectionManager=connectionManager;
		mHandlerThread=new HandlerThread(FLAG);
		mHandlerThread.start();
		mHandler=new Handler(mHandlerThread.getLooper()){
			@Override
            public void handleMessage(Message msg) {
				 switch(msg.what){
					 case SEND_SHORT_TIME_INSTRUCTION:
						 isShortInstructionSending=true;
						 doAction(shortInstruction);
						 if(isRecycleSend){
							 mHandler.sendEmptyMessageDelayed(SEND_SHORT_TIME_INSTRUCTION,interval);
						 }
						 break;
					 case SEND_LONG_TIME_INSTRUCTION:
						 doAction(longTimeInstruction);
						 break;
				     case SEND_FLUSH:
						 try
						 {
							 outputStream.flush();
						 }
						 catch (IOException e)
						 {
							 e.printStackTrace();
						 }
						 break;
					 
				 }
			}
		};
		
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	public int getInterval()
	{
		return interval;
	}

	public void release(){
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
		
		if(instruction!=null){
			if(isShortTimeInstruction(instruction)){
				isRecycleSend=true;
				shortInstruction=instruction;
				if(!isShortInstructionSending){
					mHandler.sendEmptyMessage(SEND_SHORT_TIME_INSTRUCTION);
				}
			}else{
				isRecycleSend=false;
				if(instruction.getType()==Type.INSTRUCTION_WALKING){
					shortInstruction=instruction;
					mHandler.removeMessages(SEND_SHORT_TIME_INSTRUCTION);
					isShortInstructionSending=false;
				}
				longTimeInstruction=instruction;
				mHandler.sendEmptyMessage(SEND_LONG_TIME_INSTRUCTION);
			}
		}
		
	}
	
	private void doAction(Instruction instruction){
		if(connectionManager!=null){
			if(outputStream==null){
				outputStream=connectionManager.getOutputStream();
			}
			try
			{
				outputStream.write(instruction.getBody());
				
				outputStream.flush();
				
				//mHandler.sendEmptyMessageDelayed(SEND_FLUSH,500);
				
			}
			catch (IOException e)
		 	{
				e.printStackTrace();
		   	}catch(NullPointerException e){
				e.printStackTrace();
			}
		}
	}
	
	private boolean isShortTimeInstruction(Instruction instruction){
		if(instruction.getType()<Type.INSTRUCTION_WALKING){
			return false;
		}else{
			return true;
		}
	}
	
}
