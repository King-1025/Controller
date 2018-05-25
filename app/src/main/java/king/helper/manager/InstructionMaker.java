package king.helper.manager;
import android.view.View.*;
import android.view.*;
import king.helper.iface.*;
import android.content.*;
import king.helper.model.*;
import king.helper.*;
import java.util.*;
import android.util.*;

public class InstructionMaker implements OnUIListener,OnCircularRodTouchListener,OnTouchListener
{
    private Context context;
    private OnInstructionMakeListener onInstructionMakeListener;
    
	private CloudPlatformInstruction currentCloudPlatformInstruction;
	private WalkingInstruction currentWalkingInstruction;
	
	private int cameraInstructionType=Type.INSTRUCTION_CAMERA;
	private int walkingInstructionType=Type.INSTRUCTION_WALKING;
	
	private String cameraInstructionDescription;
	private String walkingInstructionDecription;
 
	private byte upAndDownCommand;
	private byte leftAndRightCommand;
	private byte lightCommand;
	private byte voiceCommand;
	private byte LEDWordCommand;
	
	private String walkingDirectionDescription="机身停止运动";
    private String lightDescription="警灯关闭,照明关闭";
	private String voiceDescription="语音关闭";
	private String LEDWordDescription="LED字幕关闭";
	
	//行走控制中心量(10进制:128)
	private final static byte WALKING_DIRECTION_MIDDLE=(byte) 0x80;
	//行走控制偏移量(10进制:20)
	private final static byte WALKING_DIRECTION_OFFSET=(byte) 0x14;
	
	private final static byte WALKING_DIRECTION_START=(byte) (WALKING_DIRECTION_MIDDLE-WALKING_DIRECTION_OFFSET);
	private final static byte WALKING_DIRECTION_END=(byte) (WALKING_DIRECTION_MIDDLE+WALKING_DIRECTION_OFFSET);
	
	private final static int FLAG_MAKE_WALKING_INSTRUCTION=0x00;
	private final static int FLAG_MAKE_CAMERA_INSTRUCTION=0x01;
	
	private final static String TAG="InstructionMaker";
	
	public InstructionMaker(Context context)
	{
		this.context = context;
	}

	public void setOnInstructionMakeListener(OnInstructionMakeListener onInstructionMakeListener)
	{
		this.onInstructionMakeListener = onInstructionMakeListener;
	}

	@Override
	public void onCircularRodTouch(View v, double polarAngle, double polarDiameter, double maxPolarDiameter)
	{
		// TODO: Implement this method
		
		//利用相似性和极坐标计算
		//纵向
		upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE+(0xFF*(-polarDiameter*Math.sin(polarAngle)/maxPolarDiameter)));
		//横向
		//leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE+(0xFF*(polarDiameter*Math.cos(polarAngle)/maxPolarDiameter)));
        
		double offset=0;
		
        if(polarDiameter>offset){
			//逆时针方向
			if(polarAngle>=22.5&&polarAngle<=67.5){
				//右下
				walkingDirectionDescription="向右后方运动";
				
				logDirection("右下",polarAngle,polarDiameter,maxPolarDiameter);
				
			}else if(polarAngle>67.5&&polarAngle<112.5){
				//下
				walkingDirectionDescription="向后方运动";
				
				logDirection("下",polarAngle,polarDiameter,maxPolarDiameter);
				

			}else if(polarAngle>=112.5&&polarAngle<=157.5){
				//左下
				walkingDirectionDescription="向左后方运动";
				
				logDirection("左下",polarAngle,polarDiameter,maxPolarDiameter);
				

			}else if(polarAngle>157.5&&polarAngle<202.5){
				//左
				walkingDirectionDescription="向左运动";
				
				logDirection("左",polarAngle,polarDiameter,maxPolarDiameter);

			}else if(polarAngle>=202.5&&polarAngle<=247.5){
				//左上
				walkingDirectionDescription="向左前方运动";
				
				logDirection("左上",polarAngle,polarDiameter,maxPolarDiameter);

			}else if(polarAngle>247.5&&polarAngle<292.5){
				//上
				walkingDirectionDescription="向前方运动";
				
				logDirection("上",polarAngle,polarDiameter,maxPolarDiameter);
				
			}else if(polarAngle>=292.5&&polarAngle<=337.5){
				//右上
				walkingDirectionDescription="向右前方运动";
				
				logDirection("右上",polarAngle,polarDiameter,maxPolarDiameter);

			}else{
				//右
				walkingDirectionDescription="向右运动";
				
				logDirection("右",polarAngle,polarDiameter,maxPolarDiameter);
				
			}
		}else{
			//无方向
			walkingDirectionDescription="机身停止运动";
			
			logDirection("无方向",polarAngle,polarDiameter,maxPolarDiameter);
			
		}
		
		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}

	@Override
	public void OnLightStatusChange(boolean islightAlarmOpen, boolean islightSunOpen)
	{
		// TODO: Implement this method
		if(islightAlarmOpen&&islightSunOpen){
			
			lightCommand=(byte) 0xFF;
			
			lightDescription="警灯打开,照明打开";
			
		}else if(!islightAlarmOpen&&islightSunOpen){
			
			lightCommand=(byte) 0x0F;
			
			lightDescription="警灯关闭,照明打开";
			
		}else if(islightAlarmOpen&&!islightSunOpen){
			
			lightCommand=(byte) 0xF0;
			
			lightDescription="警灯打开,照明关闭";
			
		}else if(!islightAlarmOpen&&!islightSunOpen){
			
			lightCommand=(byte) 0x00;
			
			lightDescription="警灯关闭,照明关闭";
			
		}
		
		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}

	@Override
	public void OnVoiceStatusChange(int selectId)
	{
		// TODO: Implement this method
		if(selectId<=0){
			voiceCommand=(byte) 0x00;
			voiceDescription="语音关闭";
		}else{
			voiceCommand=(byte) selectId;
			voiceDescription="语音打开,播放音频"+selectId;
		}
		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}

	@Override
	public void OnLEDWordStatusChange(int selectId)
	{
		// TODO: Implement this method
		if(selectId<=0){
			LEDWordCommand=(byte) 0x00;
			LEDWordDescription="LED字幕关闭";
		}else{
			LEDWordCommand=(byte) selectId;
			LEDWordDescription="LED字幕打开,显示字幕"+selectId;
		}
		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}
	
	@Override
	public boolean onTouch(View p1, MotionEvent p2)
	{
		// TODO: Implement this method
		switch(p2.getAction()){
			case MotionEvent.ACTION_DOWN:
	             int id=p1.getId();
				 switch(id){
					case R.id.top:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_UP;
						cameraInstructionDescription="镜头向上";
						break;
					case R.id.bottom:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_DOWN;
						cameraInstructionDescription="镜头向下";
						break;
					case R.id.left:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_LEFT;
					    cameraInstructionDescription="镜头向左";
						break;
					case R.id.right:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_RIGHT;
					    cameraInstructionDescription="镜头向右";
						break;
					case R.id.cameracontrolButtonFar:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_FOCUS_OUT;
						cameraInstructionDescription="向远处聚焦";
						break;
					case R.id.cameracontrolButtonNear:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_FOCUS_IN;
					    cameraInstructionDescription="向近处聚焦";
						break;
					case R.id.cameracontrolButtonLarge:
					    cameraInstructionType=Type.INSTRUCTION_CAMERA_ZOOM_OUT;
					    cameraInstructionDescription="放大倍数";
						break;
					case R.id.cameracontrolButtonSmall:
					    cameraInstructionType=Type.INSTRUCTION_CAMERA_ZOOM_IN;
						cameraInstructionDescription="缩小倍数";
						break;
				}
				make(FLAG_MAKE_CAMERA_INSTRUCTION);	
				break;
			case MotionEvent.ACTION_UP:
				cameraInstructionType=Type.INSTRUCTION_CAMERA;
				cameraInstructionDescription="云台停止运动";
				make(FLAG_MAKE_CAMERA_INSTRUCTION);	
				break;
		}
		return false;
	}
	
	private void make(int flag){
		Instruction instruction=null;
		switch(flag){
			case FLAG_MAKE_CAMERA_INSTRUCTION:
				switch(cameraInstructionType){
					case Type.INSTRUCTION_CAMERA:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_UP:
					    instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x08,(byte)0x00,(byte)0x21,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_DOWN:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x10,(byte)0x00,(byte)0x21,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_LEFT:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x04,(byte)0x28,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_RIGHT:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x02,(byte)0x28,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_FOCUS_OUT:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_FOCUS_IN:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_ZOOM_OUT:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_ZOOM_IN:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x20,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
				}
				currentCloudPlatformInstruction=(CloudPlatformInstruction) instruction;
				break;
			case FLAG_MAKE_WALKING_INSTRUCTION:
				walkingInstructionType=createType();
				walkingInstructionDecription=createDscription();
				instruction=WalkingInstruction.create(walkingInstructionType,upAndDownCommand,leftAndRightCommand,lightCommand,voiceCommand,LEDWordCommand,walkingInstructionDecription);
				currentWalkingInstruction=(WalkingInstruction) instruction;
				break;
		}
		update(instruction);
	}
	
	private void update(Instruction instruction){
		if(onInstructionMakeListener!=null&&instruction!=null){
			onInstructionMakeListener.onInstructionMade(instruction);
		}
	}
    
	private int createType(){
		int type=0;
		if((upAndDownCommand<WALKING_DIRECTION_START||upAndDownCommand>WALKING_DIRECTION_END)||
		   (leftAndRightCommand<WALKING_DIRECTION_START||leftAndRightCommand>WALKING_DIRECTION_END)){
			type+=Type.INSTRUCTION_WALKING_DIRECTION;
		}
	    if(lightCommand!=0){
			type+=Type.INSTRUCTION_WALKING_LIGHT;
		}
		if(voiceCommand!=0){
			type+=Type.INSTRUCTION_WALKING_VOICE;
		}
		if(LEDWordCommand!=0){
			type+=Type.INSTRUCTION_WALKING_LED_WORD;
		}
	    if(type==0){
			type=Type.INSTRUCTION_WALKING;
		}else if(type>2*Type.INSTRUCTION_WALKING){
			type=Type.INSTRUCTION_WALKING_FUSION;
		}
		return type;
	}
	
	private String createDscription(){
		String str="";
		str+=walkingDirectionDescription+":";
		str+=lightDescription+":";
		str+=voiceDescription+":";
		str+=LEDWordDescription;
		return str;
	}

	private void logDirection(String direction,double polarAngle,double polarDiameter,double maxPolarDiameter){
		Log.i(TAG,"方向:"+direction+" 极角:"+polarAngle+" 极径:"+polarDiameter+" 最大极径:"+maxPolarDiameter);
	}
	
}
