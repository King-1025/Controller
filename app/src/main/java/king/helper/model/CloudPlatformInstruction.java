package king.helper.model;

public class CloudPlatformInstruction extends Instruction
{
	/*
	
                            <<命令串格式>>
	 一个PTZ控制命令为7字节的十六进制代码，格式如下：

	   Word1   Word２   Word３    Word4    Word５	Word６   Word ７
	  同步字节  地址字节  Command1  Command2	Data1	Data2	 校验字节

	 同步字节:固定值0xFF。
	 地址字节:受控制的PG解码器的十六进制地址，1~127。我们用到的是0x01。
	 校验字节:为Word2到Word6的检验和（CheckSum）
	         检验字节 = MOD[（字节2 + 字节3 + 字节4 + 字节5 + 字节6）/100H]
   
	 Data1(Word5)数据码1控制水平方向速度00-3FH
	 Data2(Word6)数据码2控制垂直方向速度00-3FH。
	
	*/

	private final static int BODY_SIZE=7;
	
	private static CloudPlatformInstruction cloudPlatformInstruction;
	
    public CloudPlatformInstruction (int type,String description){
	   super(type,BODY_SIZE,description);
   }
   
	@Override
	protected void initBody()
	{
		// TODO: Implement this method
		body[0]=(byte)0xFF; //同步字节 固定值
		body[1]=(byte)0x01; //地址字节 固定值
		body[BODY_SIZE-1]=(byte)0x01; //最后一个字节 检验字节
	}
	
	public void setFirstCommand(byte command){
		body[2]=command;
	}
	
	public void setSecondCommand(byte command){
		body[3]=command;
	}
   
	public void setFirstData(byte data){
		body[4]=data;
	}
	
	public void setSecondData(byte data){
		body[5]=data;
	}
	
	public void updateCheckSum(){
		byte sum=0x00;
		for(int i=1;i<BODY_SIZE-1;i++){
			sum+=body[i];
		}
		body[6]=(byte)(sum%0x100);
	}
	
	
	public static CloudPlatformInstruction create(int type,byte command1,byte command2,byte data1,byte data2,String description){
	    cloudPlatformInstruction=new CloudPlatformInstruction(type,description);
		cloudPlatformInstruction.setFirstCommand(command1);
		cloudPlatformInstruction.setSecondCommand(command2);
		cloudPlatformInstruction.setFirstData(data1);
		cloudPlatformInstruction.setSecondData(data2);
		cloudPlatformInstruction.updateCheckSum();
		return cloudPlatformInstruction;
	}
}
