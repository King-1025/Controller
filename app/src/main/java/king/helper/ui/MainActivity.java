package king.helper.ui;

import android.app.*;
import android.os.*;
import king.helper.model.*;
import android.util.*;
import android.widget.*;
import king.helper.*;
import android.view.View.*;
import android.view.*;
import king.helper.manager.*;

public class MainActivity extends BasedActivity implements OnClickListener
{
	private final static String TAG="MainActivity";
	
	private Button control;
	private Button setting;
	private Button about;
	private TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		control=(Button) findViewById(R.id.activity_main_control);
		setting=(Button) findViewById(R.id.activity_main_setting);
		about=(Button) findViewById(R.id.activity_main_about);
		//show=(TextView) findViewById(R.id.mainTextView1);
		
		control.setOnClickListener(this);
		setting.setOnClickListener(this);
		about.setOnClickListener(this);
    }
	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		//TestInstruction();
	}
	
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
	
	
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id=p1.getId();
		switch(id){
			case R.id.activity_main_control:
				skipToActivity(ControlActivity.class,false);
				break;
			case R.id.activity_main_setting:
				skipToActivity(SettingActivity.class,false);
				break;
			case R.id.activity_main_about:
				skipToActivity(AboutActivity.class,false);
				break;
		}
	}
	
	
	
	private void TestInstruction(){
		CloudPlatformInstruction cpi=new CloudPlatformInstruction(Type.INSTRUCTION_CAMERA,"云台静止指令");
		cpi.setFirstCommand((byte)0xFF);
		cpi.setFirstData((byte)0xFF);
		cpi.setSecondCommand((byte)0xEE);
		cpi.setSecondData((byte)0xEE);
		cpi.updateCheckSum();

		WalkingInstruction wi=new WalkingInstruction(Type.INSTRUCTION_WALKING,"机身静止指令");
		wi.setLeftAndRightCommand((byte)0);
		wi.setUpAndDownCommand((byte)0);
		wi.setLightCommand((byte)0xFF);
		wi.setVoiceCommand((byte)29);
		wi.setLEDCommand((byte)29);
		show.setText(wi.toString());
		Log.i(TAG,cpi.toString());
	}
	
}
