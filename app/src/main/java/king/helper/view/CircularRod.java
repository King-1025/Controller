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

	private final static double WEIGHT=0.15 ;
	
	//private final static float OFFSET=127.5f;
	
	private double MAX_POLAR_DIAMETER;
	
	private OnCircularRodTouchListener onCircularRodTouchListener;
	
	private OnCircularRodDirectionListener onCircularRodDirectionListener;
	
    private final String TAG="CircularRod";

    private Paint paint;

    private int lineColor=Color.WHITE;

    private float lineWidth=8;

    private int LINE_OFFSET=5;

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

	public void setOnCircularRodDirectionListener(OnCircularRodDirectionListener onCircularRodDirectionListener)
	{
		this.onCircularRodDirectionListener = onCircularRodDirectionListener;
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

        paint=new Paint();
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
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
			//innerCircleR=(int) (centerX-OFFSET);
            outerCircleR=centerX;
			
        }else{
            innerCircleR= (int) (h*WEIGHT);
			//innerCircleR=(int) (centerY-OFFSET);
            outerCircleR=centerY;
        }
		
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //X轴方向划线
        canvas.drawLine(LINE_OFFSET,centerY,2*centerX-LINE_OFFSET,centerY,paint);
        //Y轴方向划线
        canvas.drawLine(centerX,LINE_OFFSET,centerX,2*centerY-LINE_OFFSET,paint);

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
                    onTouch(-1,0,MAX_POLAR_DIAMETER);
                }
                break;
            case MotionEvent.ACTION_UP:
                innerCircleX=centerX;
                innerCircleY=centerY;
                onTouch(-1,0,MAX_POLAR_DIAMETER);
                break;
        }
        invalidate();
        return true;
    }

    private void onTouch(double polarAngle, double polarDiameter, double maxPolarDiameter){
		if(onCircularRodTouchListener!=null){
			onCircularRodTouchListener.onCircularRodTouch(this,polarAngle,polarDiameter,maxPolarDiameter);
		}
      }
	  
	 /*
	if(polarAngle<0){
		//无方向
		
		//Log.w(TAG,"polarAngle is not in [0,360] !");
	}else{
		//逆时针方向
		if((polarAngle>337.5&&polarAngle<=360)||(polarAngle>=0&&polarAngle<22.5)){
			//右
			
		}else if(polarAngle>=22.5&&polarAngle<=67.5){
           //右下

		}else if(polarAngle>67.5&&polarAngle<112.5){
			//下
			
		}else if(polarAngle>=112.5&&polarAngle<=157.5){
			//左下
			
		}else if(polarAngle>157.5&&polarAngle<202.5){
			//左
			
		}else if(polarAngle>=202.5&&polarAngle<=247.5){
			//左上
			
		}else if(polarAngle>247.5&&polarAngle<292.5){
			//上
			
		}else if(polarAngle>=292.5&&polarAngle<=337.5){
			//右上
		
		}else{
			//无方向
			walkingDirectionDescription="机身停止运动";

			dx=dy=0;

			logDirection("无方向",polarAngle,polarDiameter,maxPolarDiameter);

			//Log.w(TAG,"polarAngle is not in [0,360] !");
		}
	}*/
	
}

