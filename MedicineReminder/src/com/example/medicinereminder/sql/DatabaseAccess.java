package com.example.medicinereminder.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccess {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumnsUser = { MySQLiteHelper.COLUMN_ID, 
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_LAST_NAME,
			MySQLiteHelper.COLUMN_AGE,
			MySQLiteHelper.COLUMN_VIRAL,
			MySQLiteHelper.COLUMN_DIAG_DAY, MySQLiteHelper.COLUMN_DIAG_MONTH,
			MySQLiteHelper.COLUMN_DIAG_YEAR,
			MySQLiteHelper.COLUMN_DOCTOR, MySQLiteHelper.COLUMN_DOCTOR_NO};
	private String[] allColumnsMedicine = { MySQLiteHelper.COLUMN_MEDICINE_ID, 
			MySQLiteHelper.COLUMN_MEDICINE};
	private String[] allColumnsSchedule = { MySQLiteHelper.COLUMN_SCHEDULE_ID, 
			MySQLiteHelper.COLUMN_MINUTE,
			MySQLiteHelper.COLUMN_HOUR, MySQLiteHelper.COLUMN_DAY,
			MySQLiteHelper.COLUMN_MONTH, MySQLiteHelper.COLUMN_YEAR};
	private String[] allColumnsAppt = { MySQLiteHelper.COLUMN_APPT_ID,
			MySQLiteHelper.COLUMN_MINUTE,
			MySQLiteHelper.COLUMN_HOUR, MySQLiteHelper.COLUMN_DAY,
			MySQLiteHelper.COLUMN_MONTH, MySQLiteHelper.COLUMN_YEAR};
	private String[] allColumnsLog = {MySQLiteHelper.COLUMN_MESSAGE_ID,
			MySQLiteHelper.COLUMN_MESSAGE,
			MySQLiteHelper.COLUMN_TIME};
	private String[] allColumnsRefill = {MySQLiteHelper.COLUMN_REFILL_ID,
			MySQLiteHelper.COLUMN_MINUTE,
			MySQLiteHelper.COLUMN_HOUR, MySQLiteHelper.COLUMN_DAY,
			MySQLiteHelper.COLUMN_MONTH, MySQLiteHelper.COLUMN_YEAR};		
	
	
	
	public DatabaseAccess(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	
	public CursorHolder addUserData(String name, String last_name,int age,
			int viral_load, int diag_day, int diag_month,
			int diag_year, String doctor,
			String doctor_number){
		if (name==null || last_name==null || age<0 || age>120 || viral_load<0 ||
			diag_day<1 || diag_day >31 || diag_month<1 || diag_month > 12 ||
			diag_year<1910 ||diag_year > 2013 || doctor ==null || doctor_number==null)
			return null;
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_LAST_NAME, last_name);
		values.put(MySQLiteHelper.COLUMN_AGE, age);
		values.put(MySQLiteHelper.COLUMN_VIRAL, viral_load);
		values.put(MySQLiteHelper.COLUMN_DIAG_DAY, diag_day);
		values.put(MySQLiteHelper.COLUMN_DIAG_MONTH, diag_month);
		values.put(MySQLiteHelper.COLUMN_DIAG_YEAR, diag_year);
		values.put(MySQLiteHelper.COLUMN_DOCTOR, doctor);
		values.put(MySQLiteHelper.COLUMN_DOCTOR_NO, doctor_number);
		long insertId = database.insert(MySQLiteHelper.TABLE_USERINFO, null,
				values);
		int insert = ((Long)insertId).intValue();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERINFO,
				allColumnsUser, MySQLiteHelper.COLUMN_ID + " = " + insert,
				null,null,null,null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToData(cursor);
		cursor.close();
		return newData;
	}
	
	public void deleteNameEntry(String name) {
		if( name == null)
			return;
	    System.out.println("UserDataEntry deleted with name: " + name);
	    database.delete(MySQLiteHelper.TABLE_USERINFO, MySQLiteHelper.COLUMN_NAME
	        + " = '" + name + "'", null);
	 }
	
	public CursorHolder getUserData(String name){
		if(name==null)
			return null;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERINFO, allColumnsUser,
				MySQLiteHelper.COLUMN_NAME + " = '" + name +"'", null, null, null, null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToData(cursor);
		cursor.close();
		return newData;
	}
	
	public List<CursorHolder> getAllData(){
		List<CursorHolder> datas = new ArrayList<CursorHolder>();	
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USERINFO,
		        allColumnsUser, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CursorHolder data= cursorToData(cursor);
		      datas.add(data);
		      cursor.moveToNext();
		}
		 // Make sure to close the cursor
		cursor.close(); 
		return datas;
	}
	
	
	public CursorHolder addMedicine(String medicine){
		if(medicine==null)
			return null;
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_MEDICINE, medicine);
		long insertId = database.insert(MySQLiteHelper.TABLE_MEDICINE, null,
				values);
		int insert = ((Long)insertId).intValue();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MEDICINE,
				allColumnsMedicine, MySQLiteHelper.COLUMN_MEDICINE_ID + " = " + insert,
				null,null,null,null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToMedicine(cursor);
		cursor.close();
		return newData;	
	}
	
	public void deleteMedicine(String medicine) {
		if(medicine==null)
			return;
	    System.out.println("Medicine deleted with nane: " + medicine);
	    database.delete(MySQLiteHelper.TABLE_MEDICINE, MySQLiteHelper.COLUMN_MEDICINE
	        + " = '" + medicine + "'", null);
	 }
	
	public CursorHolder getMedicine(String medicine){
		if(medicine==null)
			return null;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MEDICINE, allColumnsMedicine,
				MySQLiteHelper.COLUMN_MEDICINE + " = '" + medicine +"'", null, null, null, null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToMedicine(cursor);
		cursor.close();
		return newData;
	}
	
	public List<CursorHolder> getAllMedicine(){
		List<CursorHolder> meds = new ArrayList<CursorHolder>();	
		Cursor cursor = database.query(MySQLiteHelper.TABLE_MEDICINE,
		        allColumnsMedicine, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CursorHolder medicine= cursorToMedicine(cursor);
		     meds.add(medicine);
		     cursor.moveToNext();
		}
		 // Make sure to close the cursor
		cursor.close(); 
		return meds;
	}
	
	public CursorHolder addSchedule(int minute,
			int hour, int day, int month, int year){
		if( minute <0 || minute > 59 || hour < 1 ||
				hour > 12 || day < 1 || day > 31 || month < 1 ||
				month > 12 || year < 1910 || year > 2013)
			return null;
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_MINUTE, minute);
		values.put(MySQLiteHelper.COLUMN_HOUR, hour);
		values.put(MySQLiteHelper.COLUMN_DAY, day);
		values.put(MySQLiteHelper.COLUMN_MONTH, month);
		values.put(MySQLiteHelper.COLUMN_YEAR, year);
		long insertId = database.insert(MySQLiteHelper.TABLE_SCHEDULE, null,
				values);
		int insert = ((Long)insertId).intValue();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SCHEDULE,
				allColumnsSchedule, MySQLiteHelper.COLUMN_SCHEDULE_ID + " = " + insert,
				null,null,null,null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToSchedule(cursor);
		cursor.close();
		return newData;	
	}
	
	public void deleteSchedule(int schedule_id) {
		if(schedule_id < 1)
			return;
	    System.out.println("Schedule deleted with id: " + schedule_id);
	    database.delete(MySQLiteHelper.TABLE_SCHEDULE, MySQLiteHelper.COLUMN_SCHEDULE_ID
	        + " = " + schedule_id, null);
	 }
	
	public CursorHolder getSchedule(int schedule_id) {
		if(schedule_id < 1)
			return null;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SCHEDULE, allColumnsSchedule,
				MySQLiteHelper.COLUMN_SCHEDULE_ID + " = " + schedule_id, null, null, null, null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToSchedule(cursor);
		cursor.close();
		return newData;
	}
	
	public List<CursorHolder> getAllSchedules(){
		List<CursorHolder> scheds = new ArrayList<CursorHolder>();	
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SCHEDULE,
		        allColumnsSchedule, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CursorHolder schedule= cursorToSchedule(cursor);
		      scheds.add(schedule);
		      cursor.moveToNext();
		}
		 // Make sure to close the cursor
		cursor.close(); 
		return scheds;
	}
	
	public CursorHolder addAppt(int minute,
			int hour, int day, int month, int year){
		if(minute <0 || minute > 59 || hour < 1 ||
				hour > 12 || day < 1 || day > 31 || month < 1 ||
				month > 12 || year < 1910 || year > 2013)
			return null;
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_MINUTE, minute);
		values.put(MySQLiteHelper.COLUMN_HOUR, hour);
		values.put(MySQLiteHelper.COLUMN_DAY, day);
		values.put(MySQLiteHelper.COLUMN_MONTH, month);
		values.put(MySQLiteHelper.COLUMN_YEAR, year);
		long insertId = database.insert(MySQLiteHelper.TABLE_APPT, null,
				values);
		int insert = ((Long)insertId).intValue();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_APPT,
				allColumnsAppt, MySQLiteHelper.COLUMN_APPT_ID + " = " + insert,
				null,null,null,null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToAppt(cursor);
		cursor.close();
		return newData;	
	}
	
	public void deleteAppt(int appt_id) {
		if( appt_id < 1)
			return;
	    System.out.println("Appointment deleted with id: " + appt_id);
	    database.delete(MySQLiteHelper.TABLE_APPT, MySQLiteHelper.COLUMN_APPT_ID
	        + " = " + appt_id, null);
	 }
	
	public CursorHolder getAppt(int appt_id) {
		if( appt_id < 1)
			return null;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_APPT, allColumnsAppt,
				MySQLiteHelper.COLUMN_APPT_ID + " = " + appt_id, null, null, null, null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToAppt(cursor);
		cursor.close();
		return newData;
	}
	
	public List<CursorHolder> getAllAppts(){
		List<CursorHolder> appointments = new ArrayList<CursorHolder>();	
		Cursor cursor = database.query(MySQLiteHelper.TABLE_APPT,
		        allColumnsAppt, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CursorHolder appt= cursorToAppt(cursor);
		      appointments.add(appt);
		      cursor.moveToNext();
		}
		 // Make sure to close the cursor
		cursor.close(); 
		return appointments;
	}
	
	public CursorHolder addLog(String message, String time){
		if(message == null || time ==null)
			return null;
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_MESSAGE, message);
		values.put(MySQLiteHelper.COLUMN_TIME, time);
		long insertId = database.insert(MySQLiteHelper.TABLE_LOG, null,
				values);
		int insert = ((Long)insertId).intValue();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LOG,
				allColumnsLog, MySQLiteHelper.COLUMN_MESSAGE_ID + " = " + insert,
				null,null,null,null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToLog(cursor);
		cursor.close();
		return newData;	
	}
	
	public CursorHolder getLog(int log_id) {
		if(log_id<1)
			return null;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LOG, allColumnsLog,
				MySQLiteHelper.COLUMN_MESSAGE_ID+ " = " + log_id, null, null, null, null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToLog(cursor);
		cursor.close();
		return newData;
	}
	
	public List<CursorHolder> getAllLogs(){
		List<CursorHolder> logs = new ArrayList<CursorHolder>();	
		Cursor cursor = database.query(MySQLiteHelper.TABLE_LOG,
		        allColumnsLog, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CursorHolder log= cursorToLog(cursor);
		      logs.add(log);
		      cursor.moveToNext();
		}
		 // Make sure to close the cursor
		cursor.close(); 
		return logs;
	}
	
	public CursorHolder addRefill(int minute,
			int hour, int day, int month, int year){
		if(minute <0 || minute > 59 || hour < 1 ||
				hour > 12 || day < 1 || day > 31 || month < 1 ||
				month > 12 || year < 1910 || year > 2013)
			return null;
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_MINUTE, minute);
		values.put(MySQLiteHelper.COLUMN_HOUR, hour);
		values.put(MySQLiteHelper.COLUMN_DAY, day);
		values.put(MySQLiteHelper.COLUMN_MONTH, month);
		values.put(MySQLiteHelper.COLUMN_YEAR, year);
		long insertId = database.insert(MySQLiteHelper.TABLE_REFILL, null,
				values);
		int insert = ((Long)insertId).intValue();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_REFILL,
				allColumnsRefill, MySQLiteHelper.COLUMN_REFILL_ID + " = " + insert,
				null,null,null,null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToSchedule(cursor);
		cursor.close();
		return newData;	
	}
	
	public void deleteRefill(int refill_id) {
		if(refill_id < 1)
			return;
	    System.out.println("Schedule deleted with id: " + refill_id);
	    database.delete(MySQLiteHelper.TABLE_REFILL, MySQLiteHelper.COLUMN_REFILL_ID
	        + " = " + refill_id, null);
	 }
	
	public CursorHolder getRefill(int refill_id) {
		if(refill_id < 1)
			return null;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_REFILL, allColumnsRefill,
				MySQLiteHelper.COLUMN_REFILL_ID + " = " + refill_id, null, null, null, null);
		cursor.moveToFirst();
		CursorHolder newData = cursorToSchedule(cursor);
		cursor.close();
		return newData;
	}
	
	public List<CursorHolder> getAllRefills(){
		List<CursorHolder> scheds = new ArrayList<CursorHolder>();	
		Cursor cursor = database.query(MySQLiteHelper.TABLE_REFILL,
		        allColumnsRefill, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CursorHolder schedule= cursorToSchedule(cursor);
		      scheds.add(schedule);
		      cursor.moveToNext();
		}
		 // Make sure to close the cursor
		cursor.close(); 
		return scheds;
	}
	
	//Print contents of the database
	public void printDatabase(){
		List<CursorHolder> temp_list = getAllData();
		for (CursorHolder item : temp_list){
			System.out.println("Id: " + item.getInt(0) + " Name: " +
					item.getString(1) + " Last: " + item.getString(2) +
					" Age: " + item.getInt(3) + " Viral Load: " + 
					item.getInt(4) + 
					" Diag Day " + item.getInt(5) + " Diag Month " +
					item.getInt(6) + " Diag Year " + item.getInt(7) +
					" Provider: " + item.getString(8) + " Provider No.: " +
					item.getString(9));
		}
		temp_list = getAllMedicine();
		for (CursorHolder item : temp_list){
			System.out.println("Medicine Id: " + item.getInt(0) + " Medicine: " +
					item.getString(1));
		}
		temp_list = getAllSchedules();
		for (CursorHolder item : temp_list){
			System.out.println("Schedule Id: " + item.getInt(0) + " Minute: " +
					item.getInt(1) + " Hour: " + item.getInt(2) +
					" Day " + item.getInt(3) + " Month: " + item.getInt(4) +
					" Year: " + item.getInt(5));
		}
		temp_list = getAllAppts();
		for (CursorHolder item : temp_list){
			System.out.println("Appointment Id: " + item.getInt(0) +
					" Minute: " +
					item.getInt(1) + " Hour: " + item.getInt(2) +
					" Day " + item.getInt(3) + " Month: " + item.getInt(4) +
					" Year: " + item.getInt(5));
		}
		temp_list = getAllRefills();
		for (CursorHolder item : temp_list){
			System.out.println("Refill Id: " + item.getInt(0) + " Minute: " +
					item.getInt(1) + " Hour: " + item.getInt(2) +
					" Day " + item.getInt(3) + " Month: " + item.getInt(4) +
					" Year: " + item.getInt(5));
		}
		temp_list = getAllLogs();
		for (CursorHolder item : temp_list){
			System.out.println("Message Id: " + item.getInt(0) + " Log: " +
					item.getString(1) + " Time: " + item.getString(2));
		}
	}
	
	public void clearTables(){
	    database.delete(MySQLiteHelper.TABLE_USERINFO, null,null);
	    database.delete(MySQLiteHelper.TABLE_MEDICINE, null,null);
	    database.delete(MySQLiteHelper.TABLE_SCHEDULE, null,null);
	    database.delete(MySQLiteHelper.TABLE_APPT, null,null);
	    database.delete(MySQLiteHelper.TABLE_LOG, null,null);
	    database.delete(MySQLiteHelper.TABLE_REFILL, null,null);
	    System.out.println("All tables cleared");
	}
	
	
	private CursorHolder cursorToData(Cursor cursor) {
		CursorHolder data = new CursorHolder();
	    data.add(cursor.getInt(0));
	    data.add(cursor.getString(1));
	    data.add(cursor.getString(2));
	    data.add(cursor.getInt(3));
	    data.add(cursor.getInt(4));
	    data.add(cursor.getInt(5));
	    data.add(cursor.getInt(6));
	    data.add(cursor.getInt(7));
	    data.add(cursor.getString(8));
	    data.add(cursor.getString(9));
	    return data;
	}
	  
	private CursorHolder cursorToMedicine(Cursor cursor){
		CursorHolder data = new CursorHolder();
		data.add(cursor.getInt(0));
		data.add(cursor.getString(1));
		return data;
	}
	  
	private CursorHolder cursorToSchedule(Cursor cursor){
		CursorHolder data = new CursorHolder();
		data.add(cursor.getInt(0));
		data.add(cursor.getInt(1));	
		data.add(cursor.getInt(2));
		data.add(cursor.getInt(3));
		data.add(cursor.getInt(4));
		data.add(cursor.getInt(5));
		return data;
	}
	
	private CursorHolder cursorToAppt(Cursor cursor){
		CursorHolder data = new CursorHolder();
		data.add(cursor.getInt(0));	
		data.add(cursor.getInt(1));
		data.add(cursor.getInt(2));
		data.add(cursor.getInt(3));
		data.add(cursor.getInt(4));
		data.add(cursor.getInt(5));
		return data;
	}
	
	private CursorHolder cursorToLog(Cursor cursor){
		CursorHolder data = new CursorHolder();
		data.add(cursor.getInt(0));
		data.add(cursor.getString(1));
		data.add(cursor.getString(2));
		return data;
	}
}