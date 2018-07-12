package king.helper.model;

public class Power
{
	/*
	1.机器人返回数据包格式：字节个数（5 byte)
	byte0：0x55(包头)
	byte1: 0x01(地址位)
	byte2: 0-100(电量)
	byte3: xx(保留字节)        
	byte4：0xaa(数据包尾)
	*/
	
	private int value;

	public Power(byte[] data)
	{
		value=parser(data);
	}

	public int getValue()
	{
		return value;
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
