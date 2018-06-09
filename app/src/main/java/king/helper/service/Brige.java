package king.helper.service;
import android.os.*;
import android.app.*;

public class Brige extends Binder
{
	private Service service;
	
	public Brige(Service service){
		this.service=service;
	}
	
	public Service getService(){
		return service;
	}
}
