package king.helper.ui;

import android.os.*;

import king.helper.*;
import android.content.*;
import king.helper.service.*;
import android.widget.*;
import king.helper.model.*;
import android.view.View.*;
import android.view.*;
import king.helper.manager.*;
import king.helper.iface.*;
import king.helper.view.*;
import android.app.*;
import android.widget.SeekBar.*;


public class ControlActivity extends BasedActivity implements OnClickListener
{
	private Context context;
	private ServiceConnection serviceConnection;

	private Handler mHandler;
	private boolean isBind=false;

	//指令生成
	private InstructionMaker instructionMaker;

	//行走控制
	private CircularRod circularRod; //行走控制操作杆

	//底部操作栏
	private Button alarm; //警灯
	private Button voice; //语音
	private Button LEDWord; //LED字幕
	private Button light; //照明
	private Button sound; //音量
	private SeekBar volumeSeekBar; //音量拖动条
	private TextView volumeShow; //音量显示条

	//云台控制
	private ImageButton directionChange;
	private ImageButton top;
	private ImageButton bottom;
	private ImageButton left;
	private ImageButton right;
	private Button controlChange;
	private Button near;
	private Button far;
	private Button large;
	private Button small;

	private TextView title; //标题
	private TextView power; //电量
	private TextView showInfo0; //信息显示0
    private TextView showInfo1; //信息显示1
	private Button visibility; //显示和隐藏
	private Button exit; //离开

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
	
	private int volume=InstructionMaker.DEFAULT_VALUME_VALUE;
	
	public final static int FLAG__CONNECTION_SUCCESS=0x00;
	public final static int FLAG_UPDATE_POWER=0x01;
	public final static int FLAG_CONNECTION_FAILD=0x02;
	public final static int FLAG_CONNECTION_DOING=0x03;
	public final static int FLAG_CONNECTION_RECONNECT=0x04;
	public final static int FLAG_CONNECTION_CLOSE=0x05;
	
	private final static String TAG="ControlActivity";

	private final static int DIALOG_VOICE_SELECT=0xD0;
	private final static int DIALOG_LED_WORD_SELECT=0xD1;
	private final static int DIALOG_VOLUME_RESET=0xD2;
	
	private CommunicationService communicationService;
	private boolean isAccessSend=false;

	//视频管理
	private VideoManager videoManager;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//禁用返回键
		IS_PROHIBIT_BACK_BUTTON=true;
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_control);

		context=this;

		if(MyApplication.IS_ENABEL_VIDEO){
			videoManager=new VideoManager(context,(SurfaceView)findViewById(R.id.activity_control_video));
		}

		bindAllView();

		instructionMaker=new InstructionMaker(this);
		instructionMaker.setOnInstructionMakeListener(new OnInstructionMakeListener(){

				@Override
				public void onInstructionMade(Instruction instruction)
				{

                    if((isBind&&isAccessSend)||!MyApplication.IS_ACCESS_SERVICE){
                        if(instruction.getType()<Type.INSTRUCTION_WALKING){
                            showInfo1.setText("云台指令(长效)\n"+instruction.toString());
                        }else{
                            showInfo0.setText("机身指令(短效)\n"+instruction.toString());
                        }
                        if(MyApplication.IS_ACCESS_SERVICE){
                            communicationService.directiy(instruction);
                        }
                    }else{
                        showInfo0.setText("请等待状态初始化完毕！");
                    }

                    //Log.i(TAG,instruction.toString());
				}
		});

		bindAllListener();

		serviceConnection = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName p1, IBinder p2)
			{
				communicationService=(CommunicationService) ((Brige)p2).getService();
					
				if(communicationService!=null){
					isBind=true;
					showInfo0.setText("服务连接成功！开始创建连接...");
					communicationService.setActivityHandler(mHandler);
					communicationService.buildConnection();
				}else{
					showInfo0.setText("服务回调成功，但是Service获取失败！");
				}
				
			}

			@Override
			public void onServiceDisconnected(ComponentName p1)
			{
				showInfo0.setText("服务中断");
	            isBind=false;
			}
			
		};
		
		mHandler=new Handler(this.getMainLooper()){

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				switch(msg.what){
					case FLAG__CONNECTION_SUCCESS:
						isAccessSend=true;
						showInfo0.setText("");
						showInfo1.setText("连接建立成功！");
					    mHandler.sendEmptyMessage(FLAG_UPDATE_POWER);
						break;
					case FLAG_UPDATE_POWER:
						power.setText("电量:"+communicationService.getPower()+"%");
						mHandler.sendEmptyMessageDelayed(FLAG_UPDATE_POWER,1000);
						break;
					case FLAG_CONNECTION_FAILD:
						showInfo1.setText("连接建立失败！");
						break;
					case FLAG_CONNECTION_RECONNECT:
						isAccessSend=false;
						showInfo1.setText("重新建立连接！");
						break;
					case FLAG_CONNECTION_DOING:
						showInfo1.setText("正在建立连接...");
						break;
					case FLAG_CONNECTION_CLOSE:
						showInfo1.setText("连接关闭");
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
		sound=(Button) this.findViewById(R.id.bottomcontrolButtonVolume);

		sound.setText(String.valueOf(volume));
		
		cameraPanel=(FrameLayout) this.findViewById(R.id.activitycontrolFrameLayout1);

        directionChange=(ImageButton) this.findViewById(R.id.cameraDirectionSwitch);
		top=(ImageButton) this.findViewById(R.id.top);
		bottom=(ImageButton) this.findViewById(R.id.bottom);
		left=(ImageButton) this.findViewById(R.id.left);
		right=(ImageButton) this.findViewById(R.id.right);

        controlChange=(Button) this.findViewById(R.id.cameracontrolButtonSwitch);
		near=(Button) this.findViewById(R.id.cameracontrolButtonNear);
		far=(Button) this.findViewById(R.id.cameracontrolButtonFar);
		large=(Button) this.findViewById(R.id.cameracontrolButtonLarge);
		small=(Button) this.findViewById(R.id.cameracontrolButtonSmall);

        power=(TextView) findViewById(R.id.activitycontrolTextViewPower);
        title=(TextView) findViewById(R.id.activitycontrolTextViewTitle);

        title.setText(MyApplication.TITLE);

        showInfo0=(TextView) this.findViewById(R.id.activitycontrolTextViewShow0);
        showInfo1=(TextView) this.findViewById(R.id.activitycontrolTextViewShow1);
        visibility=(Button) this.findViewById(R.id.activitycontrolButtonVisibility);
        exit=(Button) this.findViewById(R.id.activitycontrolButtonExit);
	}

	private void bindAllListener(){
		circularRod.setOnCircularRodTouchListener(instructionMaker);
		alarm.setOnClickListener(this);
		light.setOnClickListener(this);
		voice.setOnClickListener(this);
		LEDWord.setOnClickListener(this);
		sound.setOnClickListener(this);

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

		setOnUIListener(instructionMaker);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		if(!isBind){
			bind(CommunicationService.class);
		}
		showInfo0.setText("");
		showInfo1.setText("");
		changeVisible(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(isBind){
			communicationService.pause();
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if(isBind){
			unBind();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(videoManager!=null){
			videoManager.realse();
		}
	}

	@Override
	public void onClick(View p1)
	{
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
				skipToActivity(MainActivity.class,true);
				mHandler.removeMessages(FLAG_UPDATE_POWER);
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
					onUIListener.OnLightStatusChange(InstructionMaker.FLG_LIGHT_ALARM,isLightAlarmOpen);
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
					onUIListener.OnLightStatusChange(InstructionMaker.FLAG_LIGHT_SUN,isLightSunOpen);
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
			case R.id.bottomcontrolButtonVolume:
				dialogFlag=DIALOG_VOLUME_RESET;
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
			isBind=false;
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
				if(voiceSelectId>=10){
					selectId=voiceSelectId-9;
				}else{
					selectId=voiceSelectId;
				}
				break;
			case DIALOG_LED_WORD_SELECT:
				title="请选择字幕";
				viewId=R.array.LEDWord;
				selectId=LEDWordSelectId;
				break;
			case DIALOG_VOLUME_RESET:
				title="请调节音量";
				View view=LayoutInflater.from(this).inflate(R.layout.volume_reset,null);
				volumeSeekBar=(SeekBar)view.findViewById(R.id.volumeresetSeekBar);
				volumeShow=(TextView) view.findViewById(R.id.volumeresetTextViewShow);
				volumeSeekBar.setMax(InstructionMaker.MAX_VOLUME_VALUE);
				volumeSeekBar.setProgress(volume);
				volumeShow.setText("音量:"+String.valueOf(volume));
				volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

						@Override
						public void onProgressChanged(SeekBar p1, int p2, boolean p3)
						{
							volume=p2;
					        volumeShow.setText("音量:"+String.valueOf(volume));
						}

						@Override
						public void onStartTrackingTouch(SeekBar p1)
						{
							// TODO: Implement this method
						}

						@Override
						public void onStopTrackingTouch(SeekBar p1)
						{
							// TODO: Implement this method
						}
					});
				builder.setView(view);
				break;
		}
		builder.setTitle(title);
		
		if(viewId!=-1){
			builder.setSingleChoiceItems(viewId, selectId, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						switch(dialogFlag){
							case DIALOG_VOICE_SELECT:
								if(p2>=1){
									voiceSelectId=p2+9;
								}else{
									voiceSelectId=p2;
								}
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
					if(onUIListener!=null){
						switch(dialogFlag){
							case DIALOG_VOICE_SELECT:
								updateVoiceLable();
								onUIListener.OnVoiceStatusChange(voiceSelectId);
								break;
							case DIALOG_LED_WORD_SELECT:
							    updateLEDWordLable();
								onUIListener.OnLEDWordStatusChange(LEDWordSelectId);
								break;
							case DIALOG_VOLUME_RESET:
								sound.setText(String.valueOf(volume));
								showInfo1.setText("调节音量:"+ volume);
								onUIListener.OnVolumeStatusChange(volume);
								break;
						}
					}
				}
			});

		builder.create().show();
	}
	
	
	private void updateVoiceLable(){
		if(voiceSelectId<=0){
			voice.setText("无");
			showInfo1.setText("语音已关闭");
		}else if(voiceSelectId>=10){
			voice.setText((voiceSelectId-9)+"");
			showInfo1.setText("选择语音"+(voiceSelectId-9));
		}
	}
	
	private void updateLEDWordLable(){
		if(LEDWordSelectId<=0){
			LEDWord.setText("无");
			showInfo1.setText("字幕已关闭");
		}else{
			LEDWord.setText(LEDWordSelectId+"");
			showInfo1.setText("选择字幕"+LEDWordSelectId);
		}
	}
}
