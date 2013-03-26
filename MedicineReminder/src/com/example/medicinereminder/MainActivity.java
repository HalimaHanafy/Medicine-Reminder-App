package com.example.medicinereminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import android.widget.LinearLayout;
import android.widget.ViewFlipper;



@SuppressLint("NewApi")
public class MainActivity extends Activity {
	protected static String date;
	protected static ArrayList<int[]> time;
	protected static EditText lastTimeBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		NumberPicker np = (NumberPicker) findViewById(R.id.np);
		np.setMaxValue(12);
		np.setMinValue(0);
		String[] vals = new String[13];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = Integer.toString(i * 5) + " min";
		}
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(vals);
		time = new ArrayList<int[]>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	
	
	public static class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			date = Integer.toString(year)+Integer.toString(month)+Integer.toString(day);
		}
	}
	
	
	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
		lastTimeBox = (EditText) v.findViewById(R.id.EditTime);
	}
	
	public static class TimePickerFragment extends DialogFragment
	implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
	        int hour = c.get(Calendar.HOUR_OF_DAY);
	        int minute = c.get(Calendar.MINUTE);

	        // Create a new instance of TimePickerDialog and return it
	        return new TimePickerDialog(getActivity(), this, hour, minute,
	                DateFormat.is24HourFormat(getActivity()));
	    }


		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			int [] t_time = {hourOfDay,minute};
			time.add(t_time);
			lastTimeBox.setText(Integer.toString(hourOfDay)+":"+Integer.toString(minute));
		}
	}

	public void toNextPage(View v){
		ViewFlipper flip = (ViewFlipper) findViewById(R.id.view_flipper);
		flip.showNext();
	}

	public void sumbitForm(View v){
		String firstName = ((EditText) findViewById(R.id.EditFirstName)).getText().toString();
		String lastName = ((EditText) findViewById(R.id.EditLastName)).getText().toString();
		String infectionDate = date;
		String viralLoad = ((EditText) findViewById(R.id.EditViralLoad)).getText().toString();
		String phone = ((EditText) findViewById(R.id.EditPhone)).getText().toString();
		String provider = ((EditText) findViewById(R.id.EditProviderPhone)).getText().toString();

		ArrayList<String> medicineNames = new ArrayList<String>();
		LinearLayout medicines = (LinearLayout)findViewById(R.id.medicineBoxes);
		for( int i = 0; i<medicines.getChildCount(); i++ )
			medicineNames.add(((EditText) medicines.getChildAt(i)).getText().toString());

// 		ArrayList<String> medicineTimes = new ArrayList<String>();
// 		LinearLayout times = (LinearLayout)findViewById(R.id.timeBoxes);
// 		for( int i = 0; i<times.getChildCount(); i++ )
// 			medicineTimes.add(((EditText) times.getChildAt(i)).getText().toString());

		
		int remindertime = ((NumberPicker) findViewById(R.id.np)).getValue();
		AlarmSet alarmset = new AlarmSet(this);
		for(int i =0;i<1;i++) //For now just sets the first alarm
			alarmset.setAlarm(time.get(i)[0],time.get(i)[1], remindertime);

		String custommessage = ((EditText) findViewById(R.id.EditReminderMessage)).getText().toString();
		
		//TODO Need to removed deprecated constructor for Date. 
		ArrayList<Date> appointments = new ArrayList<Date>();
		LinearLayout apps = (LinearLayout)findViewById(R.id.appointmentBoxes);
		for( int i = 0; i<apps.getChildCount(); i++ ){
			DatePicker dp = (DatePicker) apps.getChildAt(i);
					appointments.add(new Date(dp.getYear(),dp.getMonth(),dp.getDayOfMonth()));
		}
		
		//TODO Need to removed deprecated constructor for Date.
		ArrayList<Date> refills = new ArrayList<Date>();
		LinearLayout refs = (LinearLayout)findViewById(R.id.refillBoxes);
		for( int i = 0; i<refs.getChildCount(); i++ ){
			DatePicker dp = (DatePicker) refs.getChildAt(i);
					refills.add(new Date(dp.getYear(),dp.getMonth(),dp.getDayOfMonth()));
		}

		Intent intent = new Intent(this, HomePageActivity.class);
		startActivity(intent);


		//Set Static User Attributes based off submission form
		MyGuy.getUser().setFirstName(firstName);
		MyGuy.getUser().setLastName(lastName);
		MyGuy.getUser().setPhoneNumer(phone);
		MyGuy.getUser().setAlarmMessage(custommessage);
	}


	public void toPrevPage(View v){
		ViewFlipper flip = (ViewFlipper) findViewById(R.id.view_flipper);
		flip.showPrevious();
	}

	public void addMedicineBox(View v){
		LinearLayout medicines = (LinearLayout) findViewById(R.id.medicineBoxes);
		EditText medicinebox = new EditText(this);
		medicinebox.setText("Medicine Name");
		medicines.addView(medicinebox);

		LinearLayout times = (LinearLayout) findViewById(R.id.timeBoxes);
		EditText timebox = new EditText(this);
		timebox.setText("Time");
		timebox.setId(R.id.EditTime);
		timebox.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
		timebox.setFocusableInTouchMode(false);
		timebox.setHint(R.string.medicinetime);
		timebox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				showTimePickerDialog(v);
			}	
		});
		times.addView(timebox);

	}

	public void addAppointmentBox(View v){
		LinearLayout appointments = (LinearLayout) findViewById(R.id.appointmentBoxes);
		DatePicker appbox = new DatePicker(this);
		appbox.setCalendarViewShown(false);
		appointments.addView(appbox);
	}

	public void addRefillBox(View v){
		LinearLayout refills = (LinearLayout) findViewById(R.id.refillBoxes);
		DatePicker refbox = new DatePicker(this);
		refbox.setCalendarViewShown(false);
		refills.addView(refbox);
	}
	
	public void selectAvatar(View v){
		v.setBackgroundColor(Color.RED);
		/*TODO Store avatar on click. Also, set other avatars background color to grey, so that
		*only one is selected at a time.
		*/
	}

}
