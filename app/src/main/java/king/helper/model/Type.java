package king.helper.model;

public class Type
{
	                    /*云台控制*/
	//静止
	public final static int INSTRUCTION_CAMERA=0x000;
	//方向
	public final static int INSTRUCTION_CAMERA_UP=0x001;
	public final static int INSTRUCTION_CAMERA_DOWN=0x002;
	public final static int INSTRUCTION_CAMERA_LEFT=0x003;
	public final static int INSTRUCTION_CAMERA_RIGHT=0x004;
	//变倍
	public final static int INSTRUCTION_CAMERA_ZOOM_IN=0x005;
	public final static int INSTRUCTION_CAMERA_ZOOM_OUT=0x006;
	//聚焦
	public final static int INSTRUCTION_CAMERA_FOCUS_IN=0x007;
	public final static int INSTRUCTION_CAMERA_FOCUS_OUT=0x008;
	
	                    /*机身控制*/
	//静止
	public final static int INSTRUCTION_WALKING=0x100;
	//方向
	public final static int INSTRUCTION_WALKING_DIRECTION=0x101;
	//灯
	public final static int INSTRUCTION_WALKING_LIGHT=0x102;
	//语音
	public final static int INSTRUCTION_WALKING_VOICE=0x103;
	//LED字幕
	public final static int INSTRUCTION_WALKING_LED_WORD=0x104;
    //混合
	public final static int INSTRUCTION_WALKING_FUSION=0x105;

						/*截图*/
	public final static int FLAG_SCREENSHOT_SAVE_AS_JPEG=0x200;
	public final static int FLAG_SCREENSHOT_SAVE_AS_PNG=0x201;
	public final static int FLAG_SCREENSHOT_SAVE_AS_WEBP=0x202;

						/*视频*/
	public final static int FLAG_VIDEO_QUALITY_FLUENT=0x300;
	public final static int FLAG_VIDEO_QUALITY_STANDAED=0x301;
	public final static int FLAG_VIDEO_QUALITY_HIGH=0x302;

						/*设置*/
	public final static String KEY_HOST="key_host";
	public final static String KEY_PORT="key_port";
	public final static String KEY_VIDEO_URL="key_video_url";
	public final static String KEY_PICTURE_SAVE_TYPE="key_picture_save_type";
	public final static String KEY_VIDEO_QUALITY="key_video_quality";
	public final static String KEY_PICTURE_SAVE_PATH="key_picture_save_path";
	public final static String KEY_STATUS_SERVICE="key_service_status";
	public final static String KEY_STATUS_TEST_SERVER="key_test_server_status";
	public final static String KEY_STATUS_RECEIVE="key_receive_status";
	public final static String KEY_STATUS_VIDEO="key_video_status";
}
