package com.example.medicinereminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmPage extends Activity {
	AlarmSet alarmSet;
	String time;
	String photo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_page);

		TextView out = (TextView)findViewById(R.id.alarmMessage);
		out.append(AlarmTracker.getTracker().alarmMessage);

		Spinner spinner = (Spinner) findViewById(R.id.editSleep);
		// 		Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.sleep_array, android.R.layout.simple_spinner_item);
		// 		Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 		Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener((OnItemSelectedListener) new SpinnerActivity());
		MediaPlayer mp = MediaPlayer.create(getBaseContext(),R.raw.lickshot);
		mp.start();
	}


	public class SpinnerActivity extends Activity implements OnItemSelectedListener {


		public void onItemSelected(AdapterView<?> parent, View view,
				int pos, long id) {
			// An item was selected. You can retrieve the selected item using
			time =(String)parent.getItemAtPosition(pos);
		}

		public void onNothingSelected(AdapterView<?> parent) {
			time = "5";
		}
	}

	public void onSleepButtonClick(View view){
		//EditText times = (EditText)findViewById(R.id.editSleep);
		alarmSet = new AlarmSet(this);
		alarmSet.setSleep(time);
		finish();
	}

	public void onIgnoreButtonClick(View view){
		AlarmTracker.getTracker().missedAlarms++;
		Time now = new Time();
		now.setToNow();
		AlarmTracker.getTracker().addRecord(now, "No");
		if (AlarmTracker.getTracker().missedAlarms>3){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Are you OK?");
			builder.setMessage("You haven't taken your medication in a few days. Are you ok?");
			builder.setPositiveButton("Call provider", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String num = MyGuy.getUser().providerPhoneNumber;
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
					startActivity(intent);
					dialog.dismiss();
					finish();
				}
			});
			builder.setNegativeButton("I'm fine", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			});
			builder.create().show();
		}
		else{
			finish();
		}
	}

	public void onTakeButtonClick(View view){
		AlarmTracker.getTracker().missedAlarms = 0;
		Time now = new Time();
		now.setToNow();
		AlarmTracker.getTracker().addRecord(now, "Yes");
		finish();
	}

	public void onPillCamButtonClick(View view){
		Time now = new Time();
		now.setToNow();
		AlarmTracker.getTracker().addRecord(now, "Cam");
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, 0);
		finish();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {
			photo = data.toUri(0);
		}
	}

}

