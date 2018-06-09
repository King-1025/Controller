package king.helper.ui;

import android.os.*;
import android.widget.*;
import king.helper.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends BasedActivity implements OnClickListener
{

	private Button control;
	private Button setting;
	private Button about;

	private final static String TAG="MainActivity";
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

		control=(Button) findViewById(R.id.activity_main_control);
		setting=(Button) findViewById(R.id.activity_main_setting);
		about=(Button) findViewById(R.id.activity_main_about);
		
		control.setOnClickListener(this);
		setting.setOnClickListener(this);
		about.setOnClickListener(this);
    }

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		int id=p1.getId();
		switch(id){
			case R.id.activity_main_control:
				skipToActivity(ControlActivity.class,true);
				break;
			case R.id.activity_main_setting:
				skipToActivity(SettingActivity.class,true);
				break;
			case R.id.activity_main_about:
				skipToActivity(AboutActivity.class,true);
				break;
		}
	}
}
