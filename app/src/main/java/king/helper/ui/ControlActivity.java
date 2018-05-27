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
import king.helper.manager.*;
import king.helper.iface.*;
import king.helper.view.*;
import android.app.*;

public class ControlActivity extends BasedActivity implements OnClickListener
{
	private Context context;
	private ServiceConnection serviceConnection;
	private Handler serviceHandler;
	private Handler mHandler;
	private boolean isBinded=false;
	
	private InstructionMaker instructionMaker;
	
	private CircularRod circularRod;
	private Button alarm;
	private Button voice;
	private Button LEDWord;
	private Button light;
	
	private TextView title;
	
	private TextView power;
	
	private ImageButton top;
	private ImageButton bottom;
	private ImageButton left;
	private ImageButton right;
	private Button near;
	private Button far;
	private Button large;
	private Button small;

	private ImageButton directionChange;
    private Button controlChange;
	private TextView showInfo0;
    private TextView showInfo1;
	private Button visibility;
	private Button exit;

	private ViewGroup include0;
	private ViewGroup include1;
	private ViewGroup include2;
    private ViewGroup include3;
	
	private FrameLayout cameraPanel;
	
	private boolean isHide=false;
	private boolean isCameraControlShow=false;
	
	private OnUIListener onUIListener;
	private boolean isLightAlarmOpen=false;
	private boolean isLightSunOpen=false;
	
	private int dialogFlag;
	private int voiceSelectId;
	private int LEDWordSelectId;
	
	public final static int CONTROL_STATUS_OK=0x00;
	private final static String TAG="ControlActivity";

	private final static int DIALOG_VOICE_SELECT=0xD0;
	private final static int DIALOG_LED_WORD_SELECT=0xD1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		IS_PROHIBIT_BACK_BUTTON=true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
        context=this;

		bindAllView();
		
		instructionMaker=new InstructionMaker(this);
		instructionMaker.setOnInstructionMakeListener(new OnInstructionMakeListener(){

				@Override
				public void onTouch(double polarAngle, double polarDiameter, double maxPolarDiameter)
				{
					// TODO: Implement this method
					showInfo1.setText("pA:"+polarAngle+"\npD:"+polarDiameter+"\nmpD:"+maxPolarDiameter);
				}

				@Override
				public void onInstructionMade(Instruction instruction)
				{
					// TODO: Implement this method
				    if(serviceHandler!=null){
						Message msg=serviceHandler.obtainMessage();
						msg.what=CommunicationService.REQUEST_SEND_INSTRUCTION;
						msg.obj=instruction;
						serviceHandler.sendMessage(msg);
					}else{
						//Toast.makeText(context,"serviceHandler is null!",Toast.LENGTH_SHORT).show();
					}
					
					if(instruction.getType()<Type.INSTRUCTION_WALKING){
						showInfo0.setText("云台指令(长效)\n"+instruction.toString());
					}else{
						showInfo0.setText("机身指令(短效)\n"+instruction.toString());
					}
					Log.i(TAG,instruction.toString());
				}
		});
		
        circularRod.setOnCircularRodTouchListener(instructionMaker);
		alarm.setOnClickListener(this);
		light.setOnClickListener(this);
		voice.setOnClickListener(this);
		LEDWord.setOnClickListener(this);
	
		top.setOnTouchListener(instructionMaker);
		bottom.setOnTouchListener(instructionMaker);
		left.setOnTouchListener(instructionMaker);
		right.setOnTouchListener(instructionMaker);
		near.setOnTouchListener(instructionMaker);
		far.setOnTouchListener(instructionMaker);
		large.setOnTouchListener(instructionMaker);
		small.setOnTouchListener(instructionMaker);
		
		directionChange.setOnClickListener(this);
	    controlChange.setOnClickListener(this);
		visibility.setOnClickListener(this);
		exit.setOnClickListener(this);
		
		this.setOnUIListener(instructionMaker);
		
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
				showInfo0.setText("服务中断");
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
						showInfo0.setText("服务准备完毕！");
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

	private void bindAllView(){
		
		include0=(ViewGroup) this.findViewById(R.id.activitycontrolinclude1);
		include1=(ViewGroup) this.findViewById(R.id.activitycontrolinclude2);
		include2=(ViewGroup) this.findViewById(R.id.activitycontrolinclude3);
		include3=(ViewGroup) this.findViewById(R.id.activitycontrolinclude4);
		
		circularRod=(CircularRod) this.findViewById(R.id.walkingdirectionCircularRod);
		alarm=(Button) this.findViewById(R.id.lightAlarm);
		voice=(Button) this.findViewById(R.id.bottomcontrolButtonVoice);
		LEDWord=(Button) this.findViewById(R.id.bottomcontrolButtonWord);
		light=(Button) this.findViewById(R.id.lightSun);
		power=(TextView) findViewById(R.id.activitycontrolTextViewPower);

		title=(TextView) findViewById(R.id.activitycontrolTextViewTitle);
		title.setText(MyApplication.TITLE);
		
		cameraPanel=(FrameLayout) this.findViewById(R.id.activitycontrolFrameLayout1);
		
		top=(ImageButton) this.findViewById(R.id.top);
		bottom=(ImageButton) this.findViewById(R.id.bottom);
		left=(ImageButton) this.findViewById(R.id.left);
		right=(ImageButton) this.findViewById(R.id.right);
		
		near=(Button) this.findViewById(R.id.cameracontrolButtonNear);
		far=(Button) this.findViewById(R.id.cameracontrolButtonFar);
		large=(Button) this.findViewById(R.id.cameracontrolButtonLarge);
		small=(Button) this.findViewById(R.id.cameracontrolButtonSmall);
		
		directionChange=(ImageButton) this.findViewById(R.id.cameraDirectionSwitch);
		controlChange=(Button) this.findViewById(R.id.cameracontrolButtonSwitch);
		showInfo0=(TextView) this.findViewById(R.id.activitycontrolTextViewShow0);
		showInfo1=(TextView) this.findViewById(R.id.activitycontrolTextViewShow1);
		visibility=(Button) this.findViewById(R.id.activitycontrolButtonVisibility);
		exit=(Button) this.findViewById(R.id.activitycontrolButtonExit);
	}

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		bind(CommunicationService.class);
		showInfo0.setText("");
		showInfo1.setText("");
		changeVisible(true);
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
			case R.id.cameraDirectionSwitch:
			case R.id.cameracontrolButtonSwitch:
				switchCameraPanel();
				break;
			case R.id.activitycontrolButtonVisibility:
				changeVisible(isHide);
				isHide=!isHide;
				break;
			case R.id.activitycontrolButtonExit:
				skipToActivity(MainActivity.class,false);
				break;
			case R.id.lightAlarm:
				if(isLightAlarmOpen){
					alarm.setText("开");
					showInfo1.setText("警灯已关闭");
				}else{
					alarm.setText("关");
					showInfo1.setText("警灯已开启");
				}
				isLightAlarmOpen=!isLightAlarmOpen;
				if(onUIListener!=null){
					onUIListener.OnLightStatusChange(isLightAlarmOpen,isLightSunOpen);
				}
				break;
			case R.id.lightSun:
				if(isLightSunOpen){
					light.setText("开");
					showInfo1.setText("照明已关闭");
				}else{
					light.setText("关");
					showInfo1.setText("照明已开启");
				}
				isLightSunOpen=!isLightSunOpen;
				if(onUIListener!=null){
					onUIListener.OnLightStatusChange(isLightAlarmOpen,isLightSunOpen);
				}
				break;
			case R.id.bottomcontrolButtonVoice:
				dialogFlag=DIALOG_VOICE_SELECT;
			    show();
				break;
			case R.id.bottomcontrolButtonWord:
				dialogFlag=DIALOG_LED_WORD_SELECT;
				show();
				break;
		}
	}
	
	private void bind(Class<?> service){
		if(!MyApplication.IS_ACCESS_SERVICE)return;
		if(service!=null){
			Intent intent=new Intent();
			intent.setClass(this,service);
			bindService(intent,serviceConnection,BIND_AUTO_CREATE);
		}
	}
	
	private void unBind(){
		if(!MyApplication.IS_ACCESS_SERVICE)return;
		if(serviceConnection!=null){
			unbindService(serviceConnection);
			serviceConnection=null;
			isBinded=false;
		}
	}
	
	private void changeVisible(boolean isVisible){
		if(isVisible){
			showInfo0.setVisibility(View.VISIBLE);
			showInfo1.setVisibility(View.VISIBLE);
			include0.setVisibility(View.VISIBLE);
			include1.setVisibility(View.VISIBLE);
			cameraPanel.setVisibility(View.VISIBLE);
			visibility.setText("隐藏");
		}else{
			showInfo0.setVisibility(View.INVISIBLE);
			showInfo1.setVisibility(View.INVISIBLE);
			include0.setVisibility(View.INVISIBLE);
			include1.setVisibility(View.INVISIBLE);
			cameraPanel.setVisibility(View.INVISIBLE);
			visibility.setText("显示");
		}
	}

	private void switchCameraPanel(){
		if(!isCameraControlShow){
			include2.setVisibility(View.INVISIBLE);
			include3.setVisibility(View.VISIBLE);
		}else{
			include2.setVisibility(View.VISIBLE);
			include3.setVisibility(View.INVISIBLE);
		}
		isCameraControlShow=!isCameraControlShow;
	}
	
	public void setOnUIListener(OnUIListener onUIListener)
	{
		this.onUIListener = onUIListener;
	}
	
	private void show(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		String title=null;
		int viewId=-1;
		int selectId=0;
		switch(dialogFlag){
			case DIALOG_VOICE_SELECT:
				title="请选择语音";
				viewId=R.array.voice;
				selectId=voiceSelectId;
				break;
			case DIALOG_LED_WORD_SELECT:
				title="请选择字幕";
				viewId=R.array.LEDWord;
				selectId=LEDWordSelectId;
				break;
		}
		builder.setTitle(title);
		/*builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					switch(dialogFlag){
						case DIALOG_VOICE_SELECT:
							showInfo1.setText("已取消语音选择");
							break;
						case DIALOG_LED_WORD_SELECT:
							showInfo1.setText("已取消字幕选择");
							break;
					}
				}
			});*/
		
		if(viewId!=-1){
			builder.setSingleChoiceItems(viewId, selectId, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						switch(dialogFlag){
							case DIALOG_VOICE_SELECT:
								voiceSelectId=p2;
								break;
							case DIALOG_LED_WORD_SELECT:
								LEDWordSelectId=p2;
								break;
						}
					}
				});
			
		}
	
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					if(onUIListener!=null){
						switch(dialogFlag){
							case DIALOG_VOICE_SELECT:
								if(voiceSelectId<=0){
									voice.setText("无");
									showInfo1.setText("语音已关闭");
								}else{
									voice.setText(voiceSelectId+"");
									showInfo1.setText("选择语音"+voiceSelectId);
								}
								onUIListener.OnVoiceStatusChange(voiceSelectId);
								break;
							case DIALOG_LED_WORD_SELECT:
								if(LEDWordSelectId<=0){
									LEDWord.setText("无");
									showInfo1.setText("字幕已关闭");
								}else{
									LEDWord.setText(LEDWordSelectId+"");
									showInfo1.setText("选择字幕"+LEDWordSelectId);
								}
								onUIListener.OnLEDWordStatusChange(LEDWordSelectId);
								break;
						}
					}
				}
			});
		builder.create().show();
	}
	/*
	private View makePanel(int flag){
		View v=null;
		switch(flag){
			case DIALOG_VOICE_SELECT:
				break;
			case DIALOG_LED_WORD_SELECT:
				break;
		}
		return v;
	}
	*/
}
