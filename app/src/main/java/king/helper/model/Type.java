package king.helper.model;

public class Type
{
	                    /*云台控制*/
	//静止
	public final static int INSTRUCTION_CAMERA=0x000;
	//方向
	public final static int INSTRUCTION_CAMERA_UP=0x001;
	public final static int INSTRUCTION_CAMERA_DOWN=0x002;
	public final static int INSTRUCTION_CAMERA_LEFT=0x003;
	public final static int INSTRUCTION_CAMERA_RIGHT=0x004;
	//变倍
	public final static int INSTRUCTION_CAMERA_ZOOM_IN=0x005;
	public final static int INSTRUCTION_CAMERA_ZOOM_OUT=0x006;
	//聚焦
	public final static int INSTRUCTION_CAMERA_FOCUS_IN=0x007;
	public final static int INSTRUCTION_CAMERA_FOCUS_OUT=0x008;
	
	                    /*机身控制*/
	//静止
	public final static int INSTRUCTION_WALKING=0x100;
	//方向
	public final static int INSTRUCTION_WALKING_DIRECTION=0x101;
	//灯
	public final static int INSTRUCTION_WALKING_LIGHT=0x101;
	//语音
	public final static int INSTRUCTION_WALKING_VOICE=0x102;
	//LED显示器
	public final static int INSTRUCTION_WALKING_LED_SCREEN=0x101;
	
	public final static int INSTRUCTION_WALKING_FUSION=0x101;
}
