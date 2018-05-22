package king.helper.model;


public abstract class Instruction
{
	private int type; //类型
	private int size; //数据尺寸
	protected byte[] body; //数据主体
	private String description;

	public Instruction(int type,int size,String description)
	{
		this.type = type;
		this.size = size;
		this.body=new byte[size];
		this.description = description;
		
		initBody();
	}

	protected abstract void initBody();
	
	public void setType(int type)
	{
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public byte[] getBody()
	{
		return body;
	} 
	
	public int getSize()
	{
		if(body!=null){
			size=body.length;
		}
		return size;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this. description=description;
	}
	
	@Override
	public String toString()
	{
		// TODO: Implement this method
		String str="type:"+type+"\n"+
		           "size:"+size+
				   "body:"+bytesToHex(body)+
		           "description:"+description;
		return str;
	} 
	
	private  String bytesToHex(byte[] bytes) {  
	    char[] hexArray = "0123456789ABCDEF".toCharArray(); 
		char[] hexChars = new char[bytes.length * 2];  
		for ( int j = 0; j < bytes.length; j++ ) {  
			int v = bytes[j] & 0xFF;  
			hexChars[j * 2] = hexArray[v >>> 4];  
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];  
		}  
		String str=new String(hexChars);
		if(str==null||str.length()<=0){
			str="none";
		}
		return str;
	}  
	
}
