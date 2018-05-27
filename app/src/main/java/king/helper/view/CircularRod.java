package king.helper.view;
import android.view.*;
import android.graphics.drawable.*;
import android.content.*;
import android.util.*;
import android.content.res.*;
import king.helper.*;
import android.graphics.*;
import king.helper.iface.*;



public class CircularRod extends View {

    private Drawable outerDrawable;
    private Drawable innerDrawable;
    //中心坐标(控件左上角为原点)
    private int centerX;
    private int centerY;
    //内圆坐标(控件左上角为原点)
    private int innerCircleX;
    private int innerCircleY;

    private int innerCircleR;
    private int outerCircleR;

	private final static double WEIGHT=0.18;
	
	private final static int OFFSET=128;
	
	private double MAX_POLAR_DIAMETER;
	
	private OnCircularRodTouchListener onCircularRodTouchListener;
	
    private final String TAG="CircularRod";

    public CircularRod(Context context) {
        super(context);
        init(null, 0);
    }

    public CircularRod(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircularRod(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

	public void setOnCircularRodTouchListener(OnCircularRodTouchListener onCircularRodTouchListener)
	{
		this.onCircularRodTouchListener = onCircularRodTouchListener;
	}

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
			attrs, R.styleable.CircularRod, defStyle, 0);

        if(a.hasValue(R.styleable.CircularRod_bgOuterDrawable)){
            outerDrawable=a.getDrawable(R.styleable.CircularRod_bgOuterDrawable);
            outerDrawable.setCallback(this);
        }
        if(a.hasValue(R.styleable.CircularRod_bgInnerDrawable)){
            innerDrawable=a.getDrawable(R.styleable.CircularRod_bgInnerDrawable);
            innerDrawable.setCallback(this);
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w=getMeasuredWidth();
        int h=getMeasuredHeight();
        centerX =innerCircleX= w/2;
        centerY =innerCircleY= h/2;
        if(w<=h){
            innerCircleR= (int) (w*WEIGHT);
			//innerCircleR=centerX-OFFSET;
            outerCircleR=centerX;
			
        }else{
             innerCircleR= (int) (h*WEIGHT);
			//innerCircleR=centerY-OFFSET;
            outerCircleR=centerY;
        }
		
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (outerDrawable != null) {
            outerDrawable.setBounds(centerX-outerCircleR,centerY-outerCircleR,
									centerX+outerCircleR,centerY+outerCircleR);
            outerDrawable.setAlpha(128);
            outerDrawable.draw(canvas);
        }
        if (innerDrawable != null) {
            innerDrawable.setBounds(innerCircleX-innerCircleR,innerCircleY-innerCircleR,
									innerCircleX+innerCircleR,innerCircleY+innerCircleR);
            outerDrawable.setAlpha(245);
            innerDrawable.draw(canvas);
        }
        MAX_POLAR_DIAMETER=Math.abs(outerCircleR-innerCircleR);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                double x=event.getX()-centerX;
                double y=event.getY()-centerY;
                double z=Math.sqrt(x*x+y*y);
                double d=MAX_POLAR_DIAMETER;
                if(z>0) {
                    double sinY = y / z;
                    double w=Math.toDegrees(Math.asin(sinY));
                    if(x<0){
                        w=180-w;
                    }else if(x>=0&&y<0){
                        w=360+w;
                    }
                    if (z <= d) {
                        innerCircleX = (int) event.getX();
                        innerCircleY = (int) event.getY();
                        onTouch(w,z,d);
                    } else {
                        double sinX = x / z;
                        innerCircleX = (int) (centerX + d * sinX);
                        innerCircleY = (int) (centerY + d * sinY);
                        onTouch(w,d,d);
                    }
                }else{
                    onTouch(0,0,MAX_POLAR_DIAMETER);
                }
                break;
            case MotionEvent.ACTION_UP:
                innerCircleX=centerX;
                innerCircleY=centerY;
                onTouch(0,0,MAX_POLAR_DIAMETER);
                break;
        }
        invalidate();
        return true;
    }

    private void onTouch(double w, double z, double d){
		if(onCircularRodTouchListener!=null){
			onCircularRodTouchListener.onCircularRodTouch(this,w,z,d);
		}
      }
	  
	}

