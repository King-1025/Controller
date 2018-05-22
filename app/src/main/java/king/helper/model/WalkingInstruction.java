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
	
	2.机器人返回数据包格式：字节个数（5 byte)
	byte0：0x55(包头)
	byte1: 0x01(地址位)
	byte2: 0-100(电量)
	byte3: xx(保留字节)        
	byte4：0xaa(数据包尾)
	*/
	private final static int BODY_SIZE=8;
	
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
	
	public void setFloodLightEnable(boolean is){
		byte b=body[4];
		if(((b>>4)==0xF)&&(!is)){
			body[4]&=0x0F;
		}else if((b>>4==0x0)&&is){
			body[4]|=0xF0;
		}
	}
	
	public void setAlarmLampEnable(boolean is){
		byte b=body[4];
		if(((b<<4)==0xF)&&(!is)){
			body[4]&=0xF0;
		}else if((b<<4==0x0)&&is){
			body[4]|=0x0F;
		}
	}
	
	public void setVoiceCommand(byte command){
		body[5]=command;
	}
	
	public void setLEDCommand(byte command){
		body[6]=command;
	}
	
}   
