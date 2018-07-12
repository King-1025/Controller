package king.helper.view;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.content.res.*;
import king.helper.*;
import android.view.*;
import android.view.View.*;

public class FloatingLayout extends LinearLayout
{

	private int width;
	private int height;
	
	private int floatingX;
	private int floatingY;

	private int defaultFloatingX;
	private int defaultFloatingY;

	private boolean isHasValueX=false;
	private boolean isHasValueY=false;
	
	private boolean isInit=true;
	
	private View floatingView;
	
    public FloatingLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public FloatingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FloatingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
	
	private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
			attrs, R.styleable.FloatingLayout, defStyle, 0);
		    isHasValueX=a.hasValue(R.styleable.FloatingLayout_defaultFloatingX);
		    if(isHasValueX){
			    defaultFloatingX=a.getInteger(R.styleable.FloatingLayout_defaultFloatingX,0);
		    }
		    isHasValueY=a.hasValue(R.styleable.FloatingLayout_defaultFloatingY);
		    if(isHasValueY){
			    defaultFloatingY=a.getInteger(R.styleable.FloatingLayout_defaultFloatingY,0);
		    }
        a.recycle();
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO: Implement this method
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width=getMeasuredWidth();
		height=getMeasuredHeight();
		if(!isHasValueX)defaultFloatingX=width/2;
		if(!isHasValueY)defaultFloatingY=height/2;
	}

	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		// TODO: Implement this method
		super.onLayout(changed, l, t, r, b);
		if(getChildCount()>0){
		    floatingView =getChildAt(0);
			int hvX=floatingView.getMeasuredWidth()/2;
			int hvY=floatingView.getMeasuredHeight()/2;
			if(isInit){
				floatingX=defaultFloatingX;
				floatingY=defaultFloatingY;
			    isInit=false;
			}else{
				floatingX=floatingX < hvX ? hvX:floatingX;
		        floatingY=floatingY < hvY ? hvY:floatingY;
				floatingX=floatingX > width-hvX ? width-hvX:floatingX;
				floatingY=floatingY > height-hvY ? height-hvY:floatingY;
			}
			int left=floatingX-hvX;
			int top= floatingY-hvY;
			int right= floatingX+hvX;
			int bottom=floatingY+hvY;
			floatingView.layout(left,top,right,bottom);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO: Implement this method
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				floatingX=(int) event.getX();
				floatingY=(int) event.getY();
				break;
			case MotionEvent.ACTION_UP:
				floatingX=defaultFloatingX;
				floatingY=defaultFloatingY;
				break;
		}	
		
		if(event.getAction()!=MotionEvent.ACTION_MOVE){
			requestLayout();	
		}
		/*int s=(width<height?width:height)/
		      (floatingView.getWidth() < floatingView.getHeight()?floatingView.getWidth():floatingView.getHeight());
			  */
	    //float sx= (event.getX()<floatingView.getWidth()? event.getX():floatingView.getWidth());
		//float sy= (event.getY()<floatingView.getHeight()?event.getY():floatingView.getHeight());
		//event.setLocation(width-event.getX(),height-event.getY());
		//event.setLocation(event.getX()/3,event.getY()/3);
		/*if(event.getAction()==MotionEvent.ACTION_MOVE){
			event.setAction(MotionEvent.ACTION_DOWN);
		    floatingView.dispatchTouchEvent(event);
		}*/
		return false;
	}

}
