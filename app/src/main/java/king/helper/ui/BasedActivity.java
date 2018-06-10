package king.helper.ui;
import android.app.*;
import android.os.*;
import android.*;
import android.view.*;
import android.content.res.*;
import android.content.pm.*;
import android.graphics.*;
import android.content.*;
import android.widget.*;

public class BasedActivity extends Activity
{
	protected Context self;
	protected boolean IS_FULL_SCREEN=true;
	protected boolean IS_LANDSCAPE_SCREEN=true;
	protected boolean IS_PROHIBIT_BACK_BUTTON=false;
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		if(IS_FULL_SCREEN){setFullScreen();}
		if(IS_LANDSCAPE_SCREEN){setLandscapeScreen();}
		self=this;
    }
	
	private void setFullScreen(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		
	}

	private void setLandscapeScreen(){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	public boolean iSFullScreen()
	{
		return IS_FULL_SCREEN;
	}
	
	public boolean isLandscapeScreen()
	{
		return IS_LANDSCAPE_SCREEN;
	}
	
	public boolean isProhibitBackButton()
	{
		return IS_PROHIBIT_BACK_BUTTON;
	}
	
	protected void skipToActivity(Class<?> activity,boolean isFinishSelf){
		Intent intent=new Intent();
		intent.setClass(this,activity);
		startActivity(intent);
		if(isFinishSelf){
			finish();
		}
	}
	
	@Override  
	public boolean onKeyDown(int keyCode,KeyEvent event){  
		if(keyCode==KeyEvent.KEYCODE_BACK&&IS_PROHIBIT_BACK_BUTTON){
			Toast.makeText(this,"已禁用返回键",Toast.LENGTH_SHORT).show();
			return IS_PROHIBIT_BACK_BUTTON;
		}
		return super.onKeyDown(keyCode,event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		return super.onKeyLongPress(keyCode, event);
	}  
	
	protected void addLayout(ViewGroup container,int resid,boolean attachedRoot){
		if(container!=null){
			container.addView(LayoutInflater.from(this).inflate(resid,container,attachedRoot));
		}
	}
}
