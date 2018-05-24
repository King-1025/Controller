package king.helper.manager;
import android.view.View.*;
import android.view.*;
import king.helper.iface.*;
import android.content.*;

public class InstructionMaker implements OnClickListener,OnCircularRodTouchListener
{
    private Context context;
    private OnInstructionMakeListener onInstructionMakeListener;
    
	public InstructionMaker(Context context)
	{
		this.context = context;
	}

	public void setOnInstructionMakeListener(OnInstructionMakeListener onInstructionMakeListener)
	{
		this.onInstructionMakeListener = onInstructionMakeListener;
	}

	@Override
	public void onTouch(View v, double polarAngle, double polarDiameter, double maxPolarDiameter)
	{
		// TODO: Implement this method
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		
	}
	
	private void make(){
		
	}
}
