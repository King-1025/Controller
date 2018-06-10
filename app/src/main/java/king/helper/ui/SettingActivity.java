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

public class SettingActivity extends BasedActivity implements View.OnClickListener
{

	private Context contxt;
	private ListView listView;
	private Button back;
	private Button update;
	private Button reset;
	private FrameLayout content;

	private int flag;

	private final static int FLAG_SETTINTG_BASED=0x00;
	private final static int FLAG_SETTING_VIDEO=0x01;
	private final static int FlAG_SETTING_PICTURE=0x02;

	private EditText etIp;
	private EditText etPort;
	private EditText etVideoURL;

	private View basedPanel;
	private View videoPanel;

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

		basedPanel=findViewById(R.id.base_panel);
		videoPanel=findViewById(R.id.video_setting_panel);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				switch (i){
					case FLAG_SETTINTG_BASED:
						flag=FLAG_SETTINTG_BASED;
						break;
					case FLAG_SETTING_VIDEO:
						flag=FLAG_SETTING_VIDEO;
						break;
					case FlAG_SETTING_PICTURE:
						skipToActivity(ImageSeeActivity.class,true);
						return;
				}
				show(flag);
			}
		});

		etIp.setText(Save.getIP());
		etPort.setText(Save.getPort());
		etVideoURL.setText(Save.getVideoURL());

		flag=FLAG_SETTINTG_BASED;
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
			case FLAG_SETTINTG_BASED:
				basedPanel.setVisibility(View.VISIBLE);
				videoPanel.setVisibility(View.INVISIBLE);
				break;
			case FLAG_SETTING_VIDEO:
				basedPanel.setVisibility(View.INVISIBLE);
				videoPanel.setVisibility(View.VISIBLE);
				break;
		}
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
}
