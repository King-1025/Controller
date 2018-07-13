package king.helper.ui;
import android.content.Context;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import king.helper.*;
import king.helper.utils.Save;
import king.helper.R;
import android.widget.*;
import king.helper.model.Type;
import android.widget.SeekBar.*;
import king.helper.manager.*;

public class SettingActivity extends BasedActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,OnSeekBarChangeListener
{
	private Context contxt;
	private ListView listView;
	private Button back;
	private Button update;
	private Button reset;
	private FrameLayout content;

	private int flag;

	private final static int FLAG_SETTING_STATUS=0x00;
	private final static int FLAG_SETTINTG_BASED=0x01;
	private final static int FLAG_SETTING_VIDEO=0x02;
	private final static int FLAG_SETTING_SPEED=0x03;
	private final static int FlAG_SETTING_PICTURE=0x04;
	private final static int FLAG_SETTING_OTHER=0x05;

	private EditText etIp;
	private EditText etPort;
	private EditText etVideoURL;

	private TextView statusIp;
	private TextView statusPort;
	private TextView statusVideoURL;
    private TextView statusService;
	private TextView statusTestServer;
	private TextView statusVideo;
	private TextView statusReceive;
	private TextView statusSpeedType;
	
	private Switch swService;
	private Switch swTestServer;
	private Switch swReceive;
	private Switch swVideo;
	
	private SeekBar seekBarAxialX;
	private SeekBar seekBarAxialY;
	private SeekBar seekBarObliqueX;
	private SeekBar seekBarObliqueY;
	
	private TextView tvAxialX;
	private TextView tvAxialY;
	private TextView tvObliqueX;
	private TextView tvObliqueY;
	
	private View statusPanel;
	private View basedPanel;
	private View videoPanel;
	private View speedPanel;
    private View otherPanel;
	
	private int ax,ay;
	private int ox,oy;
	
	private final static String TAG="SettingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		contxt=this;

		listView=(ListView) findViewById(R.id.setting_list_view);
		back=(Button)findViewById(R.id.activitysettingButtonBack);
		update=(Button)findViewById(R.id.activitysettingButtonUpdate);
		reset=(Button)findViewById(R.id.activitysettingButtonReset);
		content=(FrameLayout)findViewById(R.id.setting_panel);

		back.setOnClickListener(this);
		update.setOnClickListener(this);
		reset.setOnClickListener(this);

		etIp=(EditText)findViewById(R.id.et_ip);
		etPort=(EditText)findViewById(R.id.et_port);
		etVideoURL=(EditText)findViewById(R.id.et_video_url);
		
		statusIp=(TextView) findViewById(R.id.statussettingpanelIP);
        statusPort=(TextView) findViewById(R.id.statussettingpanelPort);
		statusVideoURL=(TextView) findViewById(R.id.statussettingpanelURL);
		statusService=(TextView) findViewById(R.id.statussettingpanelService);
		statusTestServer=(TextView) findViewById(R.id.statussettingpanelTestServer);
		statusReceive=(TextView) findViewById(R.id.statussettingpanelReceive);
		statusVideo=(TextView) findViewById(R.id.statussettingpanelVideo);
		statusSpeedType=(TextView) findViewById(R.id.statussettingpanelSpeedType);
		
		swService=(Switch) findViewById(R.id.othersettingpanelSwitchService);
		swTestServer=(Switch) findViewById(R.id.othersettingpanelSwitchTestServer);
		swReceive=(Switch) findViewById(R.id.othersettingpanelSwitchReceive);
		swVideo=(Switch) findViewById(R.id.othersettingpanelSwitchVideo);
	    
		swService.setOnCheckedChangeListener(this);
		swTestServer.setOnCheckedChangeListener(this);
		swReceive.setOnCheckedChangeListener(this);
		swVideo.setOnCheckedChangeListener(this);
	
	    statusPanel=findViewById(R.id.status_panel);
		basedPanel=findViewById(R.id.base_panel);
		videoPanel=findViewById(R.id.video_setting_panel);
		speedPanel=findViewById(R.id.speed_auto_panel);
		otherPanel=findViewById(R.id.other_setting_panel);

		seekBarAxialX=(SeekBar) findViewById(R.id.speedautopanelSeekBarAxialX);
		seekBarAxialY=(SeekBar) findViewById(R.id.speedautopanelSeekBarAxialY);
		seekBarObliqueX=(SeekBar) findViewById(R.id.speedautopanelSeekBarObliqueX);
		seekBarObliqueY=(SeekBar) findViewById(R.id.speedautopanelSeekBarObliqueY);
		
		seekBarAxialX.setOnSeekBarChangeListener(this);
		seekBarAxialY.setOnSeekBarChangeListener(this);
		seekBarObliqueX.setOnSeekBarChangeListener(this);
		seekBarObliqueY.setOnSeekBarChangeListener(this);
		
		tvAxialX=(TextView) findViewById(R.id.speedautopanelTextViewAxialX);
		tvAxialY=(TextView) findViewById(R.id.speedautopanelTextViewAxialY);
		tvObliqueX=(TextView) findViewById(R.id.speedautopanelTextViewObliqueX);
		tvObliqueY=(TextView) findViewById(R.id.speedautopanelTextViewObliqueY);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				switch (i){
					case FLAG_SETTING_STATUS:
						flag=FLAG_SETTING_STATUS;
						break;
					case FLAG_SETTINTG_BASED:
						flag=FLAG_SETTINTG_BASED;
						break;
					case FLAG_SETTING_VIDEO:
						flag=FLAG_SETTING_VIDEO;
						break;
					case FLAG_SETTING_SPEED:
						flag=FLAG_SETTING_SPEED;
						break;
					case FlAG_SETTING_PICTURE:
						skipToActivity(ImageSeeActivity.class,true);
						return;
					case FLAG_SETTING_OTHER:
						flag=FLAG_SETTING_OTHER;
						break;
				}
				show(flag);
			}
		});
		
		etIp.setText(Save.getIP());
		etPort.setText(Save.getPort());
		etVideoURL.setText(Save.getVideoURL());

		swService.setChecked(Save.getServiceStatus());
		swTestServer.setChecked(Save.getTestServerStatus());
		swReceive.setChecked(Save.getReceiveStatus());
		swVideo.setChecked(Save.getVideoStatus());
		
		seekBarAxialX.setMax((int)InstructionMaker.WALKING_DIRECTION_MIDDLE);
		seekBarAxialY.setMax((int)InstructionMaker.WALKING_DIRECTION_MIDDLE);
		seekBarObliqueX.setMax((int)InstructionMaker.WALKING_DIRECTION_MIDDLE);
		seekBarObliqueY.setMax((int)InstructionMaker.WALKING_DIRECTION_MIDDLE);
		
	    ax=Save.get(Type.KEY_SPEED_AUTO_AXIAL_X,0);
		ay=Save.get(Type.KEY_SPEED_AUTO_AXIAL_Y,0);
		ox=Save.get(Type.KEY_SPEED_AUTO_OBLIQUE_X,0);
	    oy=Save.get(Type.KEY_SPEED_AUTO_OBLIQUE_Y,0);
		
		seekBarAxialX.setProgress(ax);
		seekBarAxialY.setProgress(ay);
		seekBarObliqueX.setProgress(ox);
		seekBarObliqueY.setProgress(oy);
		
		tvAxialX.setText(String.valueOf(ax));
		tvAxialY.setText(String.valueOf(ay));
		tvObliqueX.setText(String.valueOf(ox));
		tvObliqueY.setText(String.valueOf(oy));
		
		flag=FLAG_SETTING_STATUS;
		show(flag);
	}

	@Override
	public void onClick(View view) {
		int id=view.getId();
		switch (id){
			case R.id.activitysettingButtonBack:
				skipToActivity(MainActivity.class,true);
				break;
			case R.id.activitysettingButtonUpdate:
				update(flag);
				break;
			case R.id.activitysettingButtonReset:
				reset(flag);
				break;
		}
	}

	private void show(int flag){
		switch (flag){
			case FLAG_SETTING_STATUS:
				checkStatus();
				statusPanel.setVisibility(View.VISIBLE);
				basedPanel.setVisibility(View.INVISIBLE);
				videoPanel.setVisibility(View.INVISIBLE);
				speedPanel.setVisibility(View.INVISIBLE);
				otherPanel.setVisibility(View.INVISIBLE);
				break;
			case FLAG_SETTINTG_BASED:
				statusPanel.setVisibility(View.INVISIBLE);
				basedPanel.setVisibility(View.VISIBLE);
				videoPanel.setVisibility(View.INVISIBLE);
				speedPanel.setVisibility(View.INVISIBLE);
				otherPanel.setVisibility(View.INVISIBLE);
				break;
			case FLAG_SETTING_VIDEO:
				statusPanel.setVisibility(View.INVISIBLE);
				basedPanel.setVisibility(View.INVISIBLE);
				videoPanel.setVisibility(View.VISIBLE);
				speedPanel.setVisibility(View.INVISIBLE);
				otherPanel.setVisibility(View.INVISIBLE);
				break;
			case FLAG_SETTING_SPEED:
				statusPanel.setVisibility(View.INVISIBLE);
				basedPanel.setVisibility(View.INVISIBLE);
				videoPanel.setVisibility(View.INVISIBLE);
				speedPanel.setVisibility(View.VISIBLE);
				otherPanel.setVisibility(View.INVISIBLE);
				break;
			case FLAG_SETTING_OTHER:
				statusPanel.setVisibility(View.INVISIBLE);
				basedPanel.setVisibility(View.INVISIBLE);
				videoPanel.setVisibility(View.INVISIBLE);
				speedPanel.setVisibility(View.INVISIBLE);
				otherPanel.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void checkStatus(){
		statusIp.setText(Save.getIP());
		statusPort.setText(Save.getPort());
		statusVideoURL.setText(Save.getVideoURL());
		if(Save.getServiceStatus()){
			statusService.setText("可用");
		}else{
			statusService.setText("禁用");
		}
		if(Save.getTestServerStatus()){
			statusTestServer.setText("可用");
		}else{
			statusTestServer.setText("禁用");
		}
		if(Save.getReceiveStatus()){
			statusReceive.setText("可用");
		}else{
			statusReceive.setText("禁用");
		}
		if(Save.getVideoStatus()){
			statusVideo.setText("可用");
		}else{
			statusVideo.setText("禁用");
		}

		int flag=Save.get(Type.KEY_SPEED_TYPE,MyApplication.SPEED_TYPE);
		String str="";
		switch(flag){
			case Type.FLAG_SPEED_LOW:
				str="低档位";
				break;
			case Type.FLAG_SPEED_MIDDLE:
				str="中档位";
				break;
			case Type.FLAG_SPEED_HIGH:
				str="高档位";
				break;
			case Type.FLAG_SPEED_AUTO:
				str="自由档位";
				str+=" axialX:"+Save.get(Type.KEY_SPEED_AUTO_AXIAL_X,0);
				str+=" axialY:"+Save.get(Type.KEY_SPEED_AUTO_AXIAL_Y,0);
				str+=" obliqueX:"+Save.get(Type.KEY_SPEED_AUTO_OBLIQUE_X,0);
				str+=" obliqueY:"+Save.get(Type.KEY_SPEED_AUTO_OBLIQUE_Y,0);
				break;
		}
		statusSpeedType.setText(str);
	}
	
	private void update(int flag){
		switch (flag){
			case FLAG_SETTINTG_BASED:
				String ip=etIp.getText().toString();
				String port=etPort.getText().toString();
				if((ip!=null&&ip.length()>0)&&(port!=null&&port.length()>0)){
					Save.putHost(ip,port);
					Toast.makeText(contxt,"IP和端口保存成功！",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(contxt,"IP和端口都不为空！",Toast.LENGTH_SHORT).show();
				}
				break;
			case FLAG_SETTING_VIDEO:
				String url=etVideoURL.getText().toString();
				if(url!=null&&url.length()>0){
					Save.putVideoURL(url);
					Toast.makeText(contxt,"视频URL保存成功！",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(contxt,"URL不能为空！",Toast.LENGTH_SHORT).show();
				}
				break;
			case FLAG_SETTING_SPEED:
				Save.put(Type.KEY_SPEED_AUTO_AXIAL_X,ax);
				Save.put(Type.KEY_SPEED_AUTO_AXIAL_Y,ay);
				Save.put(Type.KEY_SPEED_AUTO_OBLIQUE_X,ox);
				Save.put(Type.KEY_SPEED_AUTO_OBLIQUE_Y,oy);
				Save.put(Type.KEY_SPEED_TYPE,Type.FLAG_SPEED_AUTO);
				Toast.makeText(contxt,"自由档位保存成功！",Toast.LENGTH_SHORT).show();
				break;
		}
	}

	private void reset(int flag){
		switch (flag){
			case FLAG_SETTINTG_BASED:
				Save.putHost(MyApplication.HOST,MyApplication.PORT);
				etIp.setText(Save.getIP());
				etPort.setText(Save.getPort());
				break;
			case FLAG_SETTING_VIDEO:
				Save.putVideoURL(MyApplication.VIDEO_URL);
				etVideoURL.setText(Save.getVideoURL());
				break;
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		// TODO: Implement this method
		int id=p1.getId();
		switch(id){
			case R.id.othersettingpanelSwitchService:
				Save.put(Type.KEY_STATUS_SERVICE,swService.isChecked());
				break;
			case R.id.othersettingpanelSwitchTestServer:
				Save.put(Type.KEY_STATUS_TEST_SERVER,swTestServer.isChecked());
				break;
			case R.id.othersettingpanelSwitchReceive:
				Save.put(Type.KEY_STATUS_RECEIVE,swReceive.isChecked());
				break;
			case R.id.othersettingpanelSwitchVideo:
				Save.put(Type.KEY_STATUS_VIDEO,swVideo.isChecked());
				break;
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar p1, int p2, boolean p3)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.speedautopanelSeekBarAxialX:
				ax=p2;
				tvAxialX.setText(String.valueOf(ax));
				break;
			case R.id.speedautopanelSeekBarAxialY:
				ay=p2;
				tvAxialY.setText(String.valueOf(ay));
				break;
			case R.id.speedautopanelSeekBarObliqueX:
				ox=p2;
				tvObliqueX.setText(String.valueOf(ox));
				break;
			case R.id.speedautopanelSeekBarObliqueY:
				oy=p2;
				tvObliqueY.setText(String.valueOf(oy));
				break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onStopTrackingTouch(SeekBar p1)
	{
		// TODO: Implement this method
	}
	
	
}
