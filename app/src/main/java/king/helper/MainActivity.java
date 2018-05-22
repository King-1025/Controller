package king.helper;

import android.app.*;
import android.os.*;
import king.helper.model.*;
import android.util.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	private final static String TAG="MainActivity";
	
	private TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		show=(TextView) findViewById(R.id.show);
    }

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		CloudPlatformInstruction cpi=new CloudPlatformInstruction(Type.INSTRUCTION_CAMERA,"云台静止指令");
		show.setText(cpi.toString());
		Log.i(TAG,cpi.toString());
	}
	
	
}
