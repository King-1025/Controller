package king.helper.manager;
import android.view.View.*;
import android.view.*;
import king.helper.iface.*;
import android.content.*;
import king.helper.model.*;
import king.helper.*;
import java.util.*;
import android.util.*;
import king.helper.utils.*;

public class InstructionMaker implements OnUIListener,OnTouchListener,OnCircularRodDirectionListener
{

	private Context context;
	private OnInstructionMakeListener onInstructionMakeListener;

	private CloudPlatformInstruction currentCloudPlatformInstruction;
	private WalkingInstruction currentWalkingInstruction;

	private int cameraInstructionType=Type.INSTRUCTION_CAMERA;
	private int walkingInstructionType=Type.INSTRUCTION_WALKING;

	private String cameraInstructionDescription;
	private String walkingInstructionDecription;

	private byte upAndDownCommand=(byte) WALKING_DIRECTION_MIDDLE;
	private byte leftAndRightCommand=(byte) WALKING_DIRECTION_MIDDLE;
	private byte lightCommand;
	private byte voiceCommand;
	private byte LEDWordCommand;
	private byte volume=(byte)0x0F;//(0x01-0x1E)

	private String walkingDirectionDescription="机身停止运动";
	private String lightDescription="警灯关闭,照明关闭";
	private String voiceDescription="语音关闭";
	private String LEDWordDescription="LED字幕关闭";
	private String volumeDescription="音量默认值:"+DEFAULT_VALUME_VALUE;

	public final static int MAX_VOLUME_VALUE=0x1E;
	public final static int DEFAULT_VALUME_VALUE=0x0F;
	public final static int MIN_VOLUME_VALUE=0x01;

	//行走控制中心量
	public final static double WALKING_DIRECTION_MIDDLE=127.5;

	//行走控制偏移
	private final static double WALKING_DIRECTION_OFFSET=2*9;

	private final static int FLAG_MAKE_WALKING_INSTRUCTION=0x00;
	private final static int FLAG_MAKE_CAMERA_INSTRUCTION=0x01;
	
	private Speed speed;
	
	private final static String TAG="InstructionMaker";
    
	public InstructionMaker(Context context)
	{
		this.context = context;
		
		OnSpeedStatusChange(Save.get(Type.KEY_SPEED_TYPE,MyApplication.SPEED_TYPE));
	}

	public void setOnInstructionMakeListener(OnInstructionMakeListener onInstructionMakeListener)
	{
		this.onInstructionMakeListener = onInstructionMakeListener;
	}

	@Override
	public void onDirectionChange(View v, CircularRodAction action)
	{
		
		switch(action.getDirection()){
			case CircularRodAction.DIRECTION_EAST:
				walkingDirectionDescription="向右运动";
				
				leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE+speed.getAxialX());
				
				upAndDownCommand=(byte)WALKING_DIRECTION_MIDDLE;
				
				break;
			case CircularRodAction.DIRECTION_SOUTH_EAST:
				walkingDirectionDescription="向右后方运动";
				
				leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE+speed.getObliqueX());
				
				upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE-speed.getObliqueY());
				
				break;
		    case CircularRodAction.DIRECTION_SOUTH:
				walkingDirectionDescription="向后方运动";
				
				leftAndRightCommand=(byte)WALKING_DIRECTION_MIDDLE;

				upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE-speed.getAxialY());
				
				break;
			case CircularRodAction.DIRECTION_SOUTH_WEST:
				walkingDirectionDescription="向左后方运动";
				
				leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE-speed.getObliqueX());

				upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE-speed.getObliqueY());
				
				break;
			case CircularRodAction.DIRECTION_WEST:
				walkingDirectionDescription="向左运动";
				
				leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE-speed.getAxialX());

				upAndDownCommand=(byte)WALKING_DIRECTION_MIDDLE;
				
				break;
			case CircularRodAction.DIRECTION_NORTH_WEST:
				walkingDirectionDescription="向左前方运动";
				
				leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE-speed.getObliqueX());

				upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE+speed.getObliqueY());
				
				break;
			case CircularRodAction.DIRECTION_NORTH:
				walkingDirectionDescription="向前方运动";
				
				leftAndRightCommand=(byte)WALKING_DIRECTION_MIDDLE;

				upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE+speed.getAxialY());
				
				break;
			case CircularRodAction.DIRECTION_NORTH_EAST:
				walkingDirectionDescription="向右前方运动";
				
				leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE+speed.getObliqueX());

				upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE+speed.getObliqueY());
				
				break;
			case CircularRodAction.DIRECTION_CENTER:
				walkingDirectionDescription="机身停止运动";
				
				leftAndRightCommand=(byte) WALKING_DIRECTION_MIDDLE;
				
				upAndDownCommand=(byte) WALKING_DIRECTION_MIDDLE;
				
				break;
		}
		
		make(FLAG_MAKE_WALKING_INSTRUCTION);
		
	}
	
	@Override
	public void OnSpeedStatusChange(int type)
	{
		if(speed==null){
			speed=new Speed();
		}
		switch(type){
			case Type.FLAG_SPEED_LOW:
				speed.setAxialX(39);
				speed.setAxialY(39);
				speed.setObliqueX(25);
				speed.setObliqueY(20);
				break;
			case Type.FLAG_SPEED_MIDDLE:
				speed.setAxialX(78);
				speed.setAxialY(78);
				speed.setObliqueX(50);
				speed.setObliqueY(40);
				break;
			case Type.FLAG_SPEED_HIGH:
				speed.setAxialX(117);
				speed.setAxialY(117);
				speed.setObliqueX(100);
				speed.setObliqueY(80);
				break;
			case Type.FLAG_SPEED_AUTO:
				speed.setAxialX(Save.get(Type.KEY_SPEED_AUTO_AXIAL_X,0));
				speed.setAxialY(Save.get(Type.KEY_SPEED_AUTO_AXIAL_Y,0));
				speed.setObliqueX(Save.get(Type.KEY_SPEED_AUTO_OBLIQUE_X,0));
				speed.setObliqueY(Save.get(Type.KEY_SPEED_AUTO_OBLIQUE_Y,0));
				break;
			default:
			    Log.w(TAG,"without speed type:"+type);
			    break;
		}
	}
	
	
	/*
	@Override
	public void onCircularRodTouch(View v, double polarAngle, double polarDiameter, double maxPolarDiameter)
	{
		                             //利用相似性和极坐标计算
		//纵向
		//upAndDownCommand=(byte)(WALKING_DIRECTION_MIDDLE*(1-(polarDiameter*Math.sin(polarAngle)/maxPolarDiameter)));
		//横向
		//leftAndRightCommand=(byte)(WALKING_DIRECTION_MIDDLE*(1+(polarDiameter*Math.cos(polarAngle)/maxPolarDiameter)));

//		double dx,dy;
//
//		//逆时针方向
//		if((polarAngle>337.5&&polarAngle<=360)||(polarAngle>=0&&polarAngle<22.5)){
//			//右
//			walkingDirectionDescription="向右运动";
//
//			dx=polarDiameter*Math.cos(polarAngle);
//			dy=0;
//
//			logDirection("右",polarAngle,polarDiameter,maxPolarDiameter);
//
//		}else if(polarAngle>=22.5&&polarAngle<=67.5){
//			//右下
//			walkingDirectionDescription="向右后方运动";
//
//			dx=polarDiameter*absCompareMax(Math.sin(polarAngle),Math.sin(polarAngle));
//			dy=-dx;
//
//			logDirection("右下",polarAngle,polarDiameter,maxPolarDiameter);
//
//		}else if(polarAngle>67.5&&polarAngle<112.5){
//			//下
//			walkingDirectionDescription="向后方运动";
//
//			dx=0;
//			dy=-polarDiameter*Math.sin(polarAngle);
//
//			logDirection("下",polarAngle,polarDiameter,maxPolarDiameter);
//
//
//		}else if(polarAngle>=112.5&&polarAngle<=157.5){
//			//左下
//			walkingDirectionDescription="向左后方运动";
//
//			dx=dy=-polarDiameter*absCompareMax(Math.sin(polarAngle),Math.cos(polarAngle));
//
//			logDirection("左下",polarAngle,polarDiameter,maxPolarDiameter);
//
//
//		}else if(polarAngle>157.5&&polarAngle<202.5){
//			//左
//			walkingDirectionDescription="向左运动";
//
//			dx=polarDiameter*Math.cos(polarAngle);
//			dy=0;
//
//			logDirection("左",polarAngle,polarDiameter,maxPolarDiameter);
//
//		}else if(polarAngle>=202.5&&polarAngle<=247.5){
//			//左上
//			walkingDirectionDescription="向左前方运动";
//
//			dx=-polarDiameter*absCompareMax(Math.sin(polarAngle),Math.cos(polarAngle));
//			dy=-dx;
//
//			logDirection("左上",polarAngle,polarDiameter,maxPolarDiameter);
//
//		}else if(polarAngle>247.5&&polarAngle<292.5){
//			//上
//			walkingDirectionDescription="向前方运动";
//
//			dx=0;
//			dy=-polarDiameter*Math.sin(polarAngle);
//
//			logDirection("上",polarAngle,polarDiameter,maxPolarDiameter);
//
//		}else if(polarAngle>=292.5&&polarAngle<=337.5){
//			//右上
//			walkingDirectionDescription="向右前方运动";
//
//			dx=polarDiameter*absCompareMax(Math.sin(polarAngle),Math.cos(polarAngle));
//			dy=dx;
//
//			logDirection("右上",polarAngle,polarDiameter,maxPolarDiameter);
//
//		}else{
//			//无方向
//			walkingDirectionDescription="机身停止运动";
//
//			dx=dy=0;
//
//			logDirection("无方向",polarAngle,polarDiameter,maxPolarDiameter);
//
//			//Log.w(TAG,"polarAngle is not in [0,360] !");
//		}

		double dx,dy;

		double xf=0.75;

		double yf=0.65;

		//float zf=0.290f;

		if(polarAngle<0){
			//无方向
			walkingDirectionDescription="机身停止运动";

			dx=dy=0;

			logDirection("无方向",polarAngle,polarDiameter,maxPolarDiameter);

			//Log.w(TAG,"polarAngle is not in [0,360] !");
		}else{

			//逆时针方向
			if((polarAngle>337.5&&polarAngle<=360)||(polarAngle>=0&&polarAngle<22.5)){
				//右
				walkingDirectionDescription="向右运动";
				dx=maxPolarDiameter*xf;
				//dx=polarDiameter*Math.cos(polarAngle);
				dy=0;

				logDirection("右",polarAngle,polarDiameter,maxPolarDiameter);


			}else if(polarAngle>=22.5&&polarAngle<=67.5){

				//右下
				walkingDirectionDescription="向右后方运动";

				dx=maxPolarDiameter*xf;
				//dx=polarDiameter*absCompareMax(Math.sin(polarAngle),Math.sin(polarAngle));
				dy=-maxPolarDiameter*yf;

				logDirection("右下",polarAngle,polarDiameter,maxPolarDiameter);

			}else if(polarAngle>67.5&&polarAngle<112.5){
				//下
				walkingDirectionDescription="向后方运动";

				dx=0;
				//dy=-polarDiameter*Math.sin(polarAngle);
				dy=-maxPolarDiameter*yf;

				logDirection("下",polarAngle,polarDiameter,maxPolarDiameter);


			}else if(polarAngle>=112.5&&polarAngle<=157.5){

				//左下
				walkingDirectionDescription="向左后方运动";

				//dx=dy=-polarDiameter*absCompareMax(Math.sin(polarAngle),Math.cos(polarAngle));

				dx=-maxPolarDiameter*xf;
				dy=-maxPolarDiameter*yf;

				logDirection("左下",polarAngle,polarDiameter,maxPolarDiameter);


			}else if(polarAngle>157.5&&polarAngle<202.5){
				//左
				walkingDirectionDescription="向左运动";

				dx=-maxPolarDiameter*xf;
				//dx=polarDiameter*Math.cos(polarAngle);
				dy=0;

				logDirection("左",polarAngle,polarDiameter,maxPolarDiameter);

			}else if(polarAngle>=202.5&&polarAngle<=247.5){
				//左上
				walkingDirectionDescription="向左前方运动";

				dx=-maxPolarDiameter*xf;
				//dx=-polarDiameter*absCompareMax(Math.sin(polarAngle),Math.cos(polarAngle));
				dy=maxPolarDiameter*yf;

				logDirection("左上",polarAngle,polarDiameter,maxPolarDiameter);

			}else if(polarAngle>247.5&&polarAngle<292.5){
				//上
				walkingDirectionDescription="向前方运动";

				dx=0;
				//dy=-polarDiameter*Math.sin(polarAngle);

				dy=maxPolarDiameter*yf;

				logDirection("上",polarAngle,polarDiameter,maxPolarDiameter);

			}else if(polarAngle>=292.5&&polarAngle<=337.5){

				//右上
				walkingDirectionDescription="向右前方运动";

				dx=maxPolarDiameter*xf;
				//dx=polarDiameter*absCompareMax(Math.sin(polarAngle),Math.cos(polarAngle));
				dy=maxPolarDiameter*yf;

				logDirection("右上",polarAngle,polarDiameter,maxPolarDiameter);

			}else{
				//无方向
				walkingDirectionDescription="机身停止运动";

				dx=dy=0;

				logDirection("无方向",polarAngle,polarDiameter,maxPolarDiameter);

				//Log.w(TAG,"polarAngle is not in [0,360] !");
			}
		}

		double dmin=-maxPolarDiameter;
		double dmax=maxPolarDiameter;

		if(dx>=dmin&&dx<0){
			leftAndRightCommand=(byte) (WALKING_DIRECTION_MIDDLE*(1-(dx*dx)/(dmin*dmin)));
		}else if(dx>0&&dx<=dmax){
			leftAndRightCommand=(byte) (WALKING_DIRECTION_MIDDLE*(1+(dx*dx)/(dmax*dmax)));
		}else{
			leftAndRightCommand=(byte) WALKING_DIRECTION_MIDDLE;
		}

		if(dy>=dmin&&dy<0){
			upAndDownCommand=(byte) (WALKING_DIRECTION_MIDDLE*(1-(dy*dy)/(dmin*dmin)));
		}else if(dy>0&&dy<=dmax){
			upAndDownCommand=(byte) (WALKING_DIRECTION_MIDDLE*(1+(dy*dy)/(dmax*dmax)));
		}else{
			upAndDownCommand=(byte) WALKING_DIRECTION_MIDDLE;
		}

		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}
   */
   /*
	private double absCompareMax(double a,double b){
		return Math.abs(a)>Math.abs(b) ? Math.abs(a):Math.abs(b);
	}
   */
	@Override
	public void OnLightStatusChange(int flag, boolean isOpen)
	{
		switch(flag){
			case OnUIListener.FLG_LIGHT_ALARM:
				if(isOpen){
					lightCommand=0x01;
					lightDescription="警灯打开";
				}else{
					lightCommand=0x02;
					lightDescription="警灯关闭";
				}
				break;
			case OnUIListener.FLAG_LIGHT_SUN:
				if(isOpen){
					lightCommand=0x03;
					lightDescription="照明打开";
				}else{
					lightCommand=0x04;
					lightDescription="照明关闭";
				}
				break;
		}
		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}

	@Override
	public void OnVolumeStatusChange(int value)
	{
		if(value>MAX_VOLUME_VALUE){
			value=MAX_VOLUME_VALUE;
			Log.w(TAG,"Reset value:"+value+" Volume max value:"+MAX_VOLUME_VALUE);
		}else if(value<MIN_VOLUME_VALUE){
			value=MIN_VOLUME_VALUE;
			Log.w(TAG,"Reset value:"+value+" Volume min value:"+MIN_VOLUME_VALUE);
		}

		volume=(byte) value;
		volumeDescription="音量值:"+value;

		make(FLAG_MAKE_WALKING_INSTRUCTION);
	}

	@Override
	public void OnVoiceStatusChange(int selectId)
	{
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
		boolean is=true;
		int id=p1.getId();
		switch(p2.getAction()){
			case MotionEvent.ACTION_DOWN:
				switch(id){
					case R.id.top:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_UP;
						cameraInstructionDescription="镜头向上";
						p1.setBackgroundResource(R.drawable.vertical_direction_button_press);
						break;
					case R.id.bottom:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_DOWN;
						cameraInstructionDescription="镜头向下";
						p1.setBackgroundResource(R.drawable.vertical_direction_button_press);
						break;
					case R.id.left:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_LEFT;
						cameraInstructionDescription="镜头向左";
						p1.setBackgroundResource(R.drawable.horizontal_direction_button_press);
						break;
					case R.id.right:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_RIGHT;
						cameraInstructionDescription="镜头向右";
						p1.setBackgroundResource(R.drawable.horizontal_direction_button_press);
						break;
					case R.id.cameracontrolButtonFar:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_FOCUS_OUT;
						cameraInstructionDescription="向远处聚焦";
						p1.setBackgroundResource(R.drawable.rectangle_button_press);
						break;
					case R.id.cameracontrolButtonNear:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_FOCUS_IN;
						cameraInstructionDescription="向近处聚焦";
						p1.setBackgroundResource(R.drawable.rectangle_button_press);
						break;
					case R.id.cameracontrolButtonLarge:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_ZOOM_OUT;
						cameraInstructionDescription="放大倍数";
						p1.setBackgroundResource(R.drawable.rectangle_button_press);
						break;
					case R.id.cameracontrolButtonSmall:
						cameraInstructionType=Type.INSTRUCTION_CAMERA_ZOOM_IN;
						cameraInstructionDescription="缩小倍数";
						p1.setBackgroundResource(R.drawable.rectangle_button_press);
						break;
				}
				make(FLAG_MAKE_CAMERA_INSTRUCTION);
				break;
			case MotionEvent.ACTION_UP:
				cameraInstructionType=Type.INSTRUCTION_CAMERA;
				cameraInstructionDescription="云台停止运动";
				make(FLAG_MAKE_CAMERA_INSTRUCTION);
				switch(id){
					case R.id.top:
					case R.id.bottom:
						p1.setBackgroundResource(R.drawable.vertical_direction_button_normal);
						break;
					case R.id.left:
					case R.id.right:
						p1.setBackgroundResource(R.drawable.horizontal_direction_button_normal);
						break;
					case R.id.cameracontrolButtonSmall:
					case R.id.cameracontrolButtonLarge:
					case R.id.cameracontrolButtonFar:
					case R.id.cameracontrolButtonNear:
						p1.setBackgroundResource(R.drawable.rectangle_button_normal);
						break;
				}
				break;
		}
		return is;
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
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x20,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
					case Type.INSTRUCTION_CAMERA_ZOOM_IN:
						instruction=CloudPlatformInstruction.create(cameraInstructionType,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x00,cameraInstructionDescription);
						break;
				}
				currentCloudPlatformInstruction=(CloudPlatformInstruction) instruction;
				break;
			case FLAG_MAKE_WALKING_INSTRUCTION:
				walkingInstructionType=createType();
				walkingInstructionDecription=createDscription();
				instruction=WalkingInstruction.create(walkingInstructionType,upAndDownCommand,leftAndRightCommand,lightCommand,voiceCommand,LEDWordCommand,volume,walkingInstructionDecription);
				currentWalkingInstruction=(WalkingInstruction) instruction;
				break;
		}
		update(flag,instruction);
	}

	private void update(int flag,Instruction instruction){
		if(onInstructionMakeListener!=null&&instruction!=null){
			onInstructionMakeListener.onInstructionMade(instruction);
		}
	}

	private int createType(){
		int type= 0x00;
		float x=(int) Math.abs(leftAndRightCommand-WALKING_DIRECTION_MIDDLE);
		float y=(int) Math.abs(upAndDownCommand-WALKING_DIRECTION_MIDDLE);
		if(x>WALKING_DIRECTION_OFFSET||y>WALKING_DIRECTION_OFFSET){
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
		str+=LEDWordDescription+":";
		str+=volumeDescription;
		return str;
	}

	private void logDirection(String direction,double polarAngle,double polarDiameter,double maxPolarDiameter){
		//Log.i(TAG,"方向:"+direction+" 极角:"+polarAngle+" 极径:"+polarDiameter+" 最大极径:"+maxPolarDiameter);
	}

}
