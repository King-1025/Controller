package king.helper.manager;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import android.util.*;
import java.io.*;
import android.widget.*;
import android.content.*;
import king.helper.utils.*;

public class ServerManager
{
	private Context context;
	private int port= 8888;  
    private List<Socket> mList = new ArrayList<Socket>();  
    private ServerSocket server = null;  
    private ExecutorService mExecutorService = null;
	private final static String TAG="ServerManager";
	private boolean isContinueListen=true;
	
	private boolean isStart=false;
	
	
	private Socket client = null;  
	
	public ServerManager(Context context,int bindPort)
	{
		this.context=context;
	    this.port=bindPort;
	}
	
	public void start() {  
	if(isStart)return;
	  new Thread(){
			public void run(){
				try {  
					server = new ServerSocket(port);  
					mExecutorService = Executors.newCachedThreadPool();  //create a thread pool  
					Log.d(TAG,"服务器已经启动..."+server.getLocalSocketAddress());
					while(isContinueListen) {  
						client = server.accept();  
						if(client!=null){
							mList.add(client);  
							mExecutorService.execute(new Service(client)); 
							mExecutorService.execute(new Runnable(){
                                    OutputStream out;
									@Override
									public void run()
									{
										// TODO: Implement this method
										try
										{
											out = client.getOutputStream();
											while (true)
											{
												sendPower(out);
												try
												{
													sleep(1000);
												}
												catch (InterruptedException e)
												{e.printStackTrace();}
											}
										}
										catch (IOException e)
										{
											e.printStackTrace();
										}
										
									}
									
							});
						}
					}  
					isStart=true;
				}catch (Exception e) {  
					e.printStackTrace();  
					Log.d(TAG,"服务器启动失败!");
				}  
			}
		}.start();
    }  
	
	
	public void stop(){
		isContinueListen=false;
		isStart=false;
		Log.d(TAG,"服务器已停止!");
		Toast.makeText(context,"服务器已停止！",Toast.LENGTH_LONG).show();
		
	}
	
    class Service implements Runnable {  
		private Socket socket;  
		private InputStream in = null;  
		private OutputStream out=null;
		private String msg = "";  

		public Service(Socket socket) {  
			this.socket = socket;  
			try {  
				in = socket.getInputStream();  
				out= socket.getOutputStream();
				msg = "客户端:" +this.socket.getInetAddress() + "总数:" +mList.size();  
				Log.i(TAG,msg);
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  

		@Override  
		public void run() {  
			// TODO Auto-generated method stub  
			
			while(true)
	       {  
                byte []data=new byte[8];
				try
				{
				   int count=in.read(data);
		
				   if(count>0){
					   Log.i(TAG,"接受指令:"+Format.obtainString(data));
				   }
				 
			    } catch (Exception e) {  
			    	e.printStackTrace();  
			    } 
	        }     
        }
     }
	 
	 private void sendPower(OutputStream out){
		 if(out==null)return;
		 byte powerValue=(byte) (Math.random()*100);
		 byte []power=new byte[]{0x55,0x01,powerValue,0x00,(byte)0xaa};
		 try
		 {
			 out.write(power);
			 out.flush();
		 }
		 catch (IOException e)
		 {e.printStackTrace();}
		
		// Log.d(TAG,"解析:"+parser(power));
	 }
	
	 
	private int parser(byte[]data){
		if(data!=null){
			for(int i=0;i<data.length;i++){
				if(data[i]==(byte)0x55){
					if(i+4<data.length){
						if(data[i+1]==(byte)0x01&&data[i+4]==(byte)0xAA){
							return data[i+2];
						}
					}
				}
			}
		}
		return -1;
	}
	 
}
