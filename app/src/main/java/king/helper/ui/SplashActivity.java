package king.helper.ui;

import android.os.*;

import king.helper.MyApplication;
import king.helper.R;

public class SplashActivity extends BasedActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				skipToActivity(MainActivity.class,true);
			}
		}, MyApplication.SPALASH_TIME);
	}
	
}
