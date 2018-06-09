package king.helper.ui;
import android.os.*;
import android.view.View;
import android.widget.Button;
import king.helper.*;

public class AboutActivity extends BasedActivity
{

	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		back=(Button)findViewById(R.id.activityaboutButtonBack);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				skipToActivity(MainActivity.class,true);
			}
		});

	}
}
