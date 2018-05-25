package king.helper.model;

public class WalkingInstruction extends Instruction
{
    /*
	1.每100ms发送一次：树莓派发送数据包格式字节个数（8 byte)
	byte0: 0x55(包头)
	byte1: 0x01(地址位)
	摇杆操作控制字解析
	byte2: 摇杆上下指令
	byte3: 摇杆左右指令
	其他功能操作
	byte4: 高4位->照明灯控制(0000:照明灯关闭，1111照明灯打开) 低4位->警灯控制(0000警灯关闭，1111警灯打开)
	byte5: 语音控制
	byte6: LED显示屏节目播放
	byte7：0xaa(数据包尾)  
	*/
	private final static int BODY_SIZE=8;
	
	private static WalkingInstruction walkingInstruction;
	
	public WalkingInstruction(int type,String description){
		super(type,BODY_SIZE,description);
	}
	@Override
	protected void initBody()
	{
		// TODO: Implement this method
		body[0]=(byte)0x55; //包头
		body[1]=(byte)0x01; //地址位
		body[2]=(byte)0x80; //控制杆上下
		body[3]=(byte)0x80; //控制杆左右
		body[BODY_SIZE-1]=(byte)0xAA; //包尾
	}
	
	public void setUpAndDownCommand(byte command){
		body[2]=command;
	}

	public void setLeftAndRightCommand(byte command){
		body[3]=command;
	}
	
	public void setLightCommand(byte command){
		body[4]=command;
	}

	public void setVoiceCommand(byte command){
		body[5]=command;
	}
	
	public void setLEDWordCommand(byte command){
		body[6]=command;
	}
	
	public static WalkingInstruction create(int type,byte upAndownCommand,byte leftAndRightCommand,byte lightCommand,byte voiceCommand,byte LEDWordCommand,String description){
		walkingInstruction=new WalkingInstruction(type,description);
		walkingInstruction.setUpAndDownCommand(upAndownCommand);
		walkingInstruction.setLeftAndRightCommand(leftAndRightCommand);
		walkingInstruction.setLightCommand(lightCommand);
		walkingInstruction.setVoiceCommand(voiceCommand);
		walkingInstruction.setLEDWordCommand(LEDWordCommand);
		return walkingInstruction;
	}
}   
