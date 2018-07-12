package king.helper.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import io.vov.vitamio.Vitamio;

/**
 * Created by King on 2018/5/30.
 */

public class VideoManager implements SurfaceHolder.Callback{

    private Context context;
    private VideoPlyer videoPlyer;
    private SurfaceHolder surfaceHolder;
    private boolean isPrepareAsync=true;
    private final static String TAG="VideoManager";

    public VideoManager(Context context, SurfaceView surfaceView){
        this(context,surfaceView.getHolder());
    }

    public VideoManager(Context context,SurfaceHolder surfaceHolder) {
        this.context=context;
        this.surfaceHolder=surfaceHolder;

        if(this.surfaceHolder!=null){

            this.surfaceHolder.setFormat(PixelFormat.RGBA_8888);
            this.surfaceHolder.setKeepScreenOn(true);
            this.surfaceHolder.addCallback(this);

            if(Vitamio.isInitialized(this.context)){
                videoPlyer=new VideoPlyer(this.context,this.surfaceHolder,isPrepareAsync);
            }else{
                Log.e(TAG,"vitamio is not initialized!");
                Toast.makeText(context,"Error:vitamio is not initialized!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Log.d(TAG,"surfaceHolder is null!");
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(videoPlyer!=null){
            videoPlyer.start();
            Log.d(TAG,"surfaceCreated():视频播放器开始工作...");
        }else {
            Toast.makeText(context,"videoPlayer is null!",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(videoPlyer!=null){
            videoPlyer.destroy();
        }
    }

    public VideoPlyer getVideoPlyer() {
        return videoPlyer;
    }

    public void realse(){
        if(videoPlyer!=null){
            videoPlyer.destroy();
        }
    }
}
