package com.codersarecreators.myreminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseGateway extends SQLiteOpenHelper {

	private static Context contextObj = MainActivity.GetContext();
	private static String DB_NAME = "ReminderAppDb";
	private static int DB_VERSION = 14;
	private static String TABLE_REMINDER = "TABLE_REMINDER";
	private static String TABLE_MESSAGESERVICE = "TABLE_SMS";
	private static String TABLE_REMINDER_MESSAGE_ASSOC = "RemSmsAssoc";
	// the singleton object
	private static DatabaseGateway dbGateWayObj = null;
	private static SQLiteDatabase dbObj = null;

	// constructor
	private DatabaseGateway() {
		super(contextObj, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
		// set the foreign key constraint to true
		db.setForeignKeyConstraintsEnabled(true);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		/*
		 * Refer to the tables' design and create the tables accordingly.
		 */
		String CreateTableForReminder = "CREATE TABLE " + TABLE_REMINDER + "("
				+ "ReminderId TEXT NOT NULL," + "Date TEXT NOT NULL,"
				+ "Time TEXT NOT NULL," + "Notes TEXT NOT NULL,"
				+ "PRIMARY KEY(ReminderId)" + ")";
		String CreateTableForSms = "CREATE TABLE " + TABLE_MESSAGESERVICE + "("
				+ "SmsId TEXT NOT NULL," + "Date TEXT NOT NULL,"
				+ "Time TEXT NOT NULL," + "MessageText TEXT NOT NULL,"
				+ "PhoneNumber TEXT NOT NULL," + "PRIMARY KEY(SmsId)" + ")";
		String CreateTableForAssoc = "CREATE TABLE "
				+ TABLE_REMINDER_MESSAGE_ASSOC + "("
				+ "ReminderId TEXT NOT NULL," + "SmsId TEXT NOT NULL,"
				+ "FOREIGN KEY(ReminderId) REFERENCES " + TABLE_REMINDER
				+ "(ReminderId)," + "FOREIGN KEY(SmsId) REFERENCES "
				+ TABLE_MESSAGESERVICE + "(SmsId)" + ")";
		try {
			db.execSQL(CreateTableForReminder);
			db.execSQL(CreateTableForSms);
			db.execSQL(CreateTableForAssoc);
			MyToast.RaiseToast("All tables created successfully");
		} catch (SQLException e) {
			MyToast.RaiseToast("In OnCreate()");
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER_MESSAGE_ASSOC);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGESERVICE);
			MyToast.RaiseToast("In OnUpgrade()");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		onCreate(db);
	}

	public static DatabaseGateway GetDbGateWay() {
		if (null == dbGateWayObj) {
			dbGateWayObj = new DatabaseGateway();
		}
		if (null == dbObj) {
			dbObj = dbGateWayObj.getWritableDatabase();
		}
		return dbGateWayObj;
	}

	public ArrayList<ReminderObject> GetTodaysReminders() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormatter.format(cal.getTime());
		Cursor curObj = null;

		ArrayList<ReminderObject> listRemObj = new ArrayList<ReminderObject>();
		String sql = "SELECT * FROM " + TABLE_REMINDER + " WHERE Date like '%"
				+ today + "%'";
		curObj = dbObj.rawQuery(sql, null);
		if (null != curObj) {
			if (curObj.moveToFirst()) {
				do {

					listRemObj.add(new ReminderObject(curObj.getString(0),
							curObj.getString(1), curObj.getString(2), curObj
							.getString(3)));

				} while (curObj.moveToNext() != false);
			}
		}
		return listRemObj;
	}

	/**
	 * Returns the list of sms objects which were set for the reminderId
	 * 
	 * @param reminderId
	 * @return
	 */
	public ArrayList<SmsObject> GetSMSforReminder(String reminderId) {
		ArrayList<SmsObject> listSmsObj = new ArrayList<SmsObject>();
		String sql = "SELECT * FROM " + TABLE_MESSAGESERVICE
				+ " WHERE SmsId IN (SELECT SmsId FROM "
				+ TABLE_REMINDER_MESSAGE_ASSOC + " WHERE ReminderId = '"
				+ reminderId + "')";
		Cursor curObj = null;
		curObj = dbObj.rawQuery(sql, null);
		if (null != curObj) {
			if (curObj.moveToFirst()) {
				do {
					listSmsObj.add(new SmsObject(curObj.getString(0), curObj
							.getString(1), curObj.getString(2), curObj
							.getString(3), curObj.getString(4)));

				} while (curObj.moveToNext() != false);
			}
		}
		return listSmsObj;
	}

	/*
	 * This method will basically take parameter of the type Reminder. Surround
	 * this method with try catch blocks and raise appropriate toast if the
	 * reminder is added or deleted. this method will be called from the
	 * business logic, with Reminder Obj as the parameter.
	 */
	public void InsertReminder(ReminderObject reminderObj) {

		try {
			String sql = "INSERT INTO " + TABLE_REMINDER + " VALUES(" + "'"
					+ reminderObj.getId() + "'," + "'" + reminderObj.getDate()
					+ "'," + "'" + reminderObj.getTime() + "'," + "'"
					+ reminderObj.getNote() + "')";
			dbObj.execSQL(sql);
			MyToast.RaiseToast("Reminder added successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void DeleteReminders(ArrayList<String> listId) {
		for (int i = 0; i < listId.size(); i++) {
			String Id = "'" + listId.get(i) + "'";
			String sql = " DELETE FROM " + TABLE_REMINDER
					+ " WHERE ReminderId = " + Id;
			dbObj.execSQL(sql);
			MyToast.RaiseToast("Deleted Reminder!");
		}
	}

	/**
	 * Insert sms details into SMS details table
	 * 
	 * @param smsObj
	 */
	public void InsertSMS(SmsObject smsObj) {

		try {
			String sql = "INSERT INTO " + TABLE_MESSAGESERVICE + " VALUES("
					+ "'" + smsObj.getId() + "'," + "'" + smsObj.getDate()
					+ "'," + "'" + smsObj.getTime() + "'," + "'"
					+ smsObj.getText() + "'," + "'" + smsObj.getPhoneNumber()
					+ "')";
			dbObj.execSQL(sql);
			MyToast.RaiseToast("SMS added successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert values into table which associate Reminder and SMS
	 * 
	 * @param reminderId
	 * @param smsId
	 */
	public void InsertIntoRemSmsAssocTable(String reminderId, String smsId) {
		try {
			String sql = "INSERT INTO " + TABLE_REMINDER_MESSAGE_ASSOC
					+ " VALUES(" + "'" + reminderId + "'," + "'" + smsId + "')";
			dbObj.execSQL(sql);
			MyToast.RaiseToast("Values added in Associate table succesfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function which will be called when clicked on Set button on Edit Reminder
	 * screen
	 * 
	 * @param reminderId
	 * @param cv
	 */
	public void EditReminder(String reminderId, ContentValues contentValue) {
		dbObj.update(TABLE_REMINDER, contentValue, "ReminderId = " + "'"
				+ reminderId + "'", null);
		MyToast.RaiseToast("Reminder edited successfully!");
	}

	/**
	 * Function called to delete a reminder from database
	 * 
	 * @param reminderId
	 */
	public void DeleteReminder(String reminderId) {
		/*
		 * Delete reminder from TABLE_REMINDER Delete sms associated with the
		 * reminder from TABLE_MESSAGESERVICE Delete association of Reminder and
		 * sms from TABLE_REMINDER_MESSAGE_ASSOC
		 */
		String sql_delete_reminder = " DELETE FROM " + TABLE_REMINDER
				+ " WHERE ReminderId = " + "'" + reminderId + "'";
		String sql_delete_sms = " DELETE FROM " + TABLE_MESSAGESERVICE
				+ " WHERE SmsId IN (SELECT SmsId FROM "
				+ TABLE_REMINDER_MESSAGE_ASSOC + " WHERE ReminderId = " + "'"
				+ reminderId + "')";
		String sql_delete_association = " DELETE FROM "
				+ TABLE_REMINDER_MESSAGE_ASSOC + " WHERE ReminderId = " + "'"
				+ reminderId + "'";
		dbObj.execSQL(sql_delete_reminder);
		dbObj.execSQL(sql_delete_sms);
		dbObj.execSQL(sql_delete_association);
		MyToast.RaiseToast("Deleted Reminder!");
	}

	/**
	 * Function to delete only SMS from SMS list
	 * 
	 * @param smsId
	 */
	public void deleteSMSFromList(String smsId) {

		/*
		 * Delete SMS from TABLE_MESSAGESERVICE Delete SMS ID from
		 * TABLE_REMINDER_MESSAGE_ASSOC
		 */
		String sql_delete_sms = " DELETE FROM " + TABLE_MESSAGESERVICE
				+ " WHERE SmsId = " + "'" + smsId + "'";
		dbObj.execSQL(sql_delete_sms);
		String sql_delete_smsAssociation = " DELETE FROM "
				+ TABLE_REMINDER_MESSAGE_ASSOC + " WHERE SmsId = " + "'"
				+ smsId + "'";
		dbObj.execSQL(sql_delete_smsAssociation);
		MyToast.RaiseToast("SMS Deleted!");
	}

	/**
	 * Return list of reminder object set for tomorrows date
	 * 
	 * @return
	 */
	public ArrayList<ReminderObject> GetTomorrowsReminder() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrow = dateFormatter.format(cal.getTime());
		Cursor curObj = null;
		ArrayList<ReminderObject> listRemObj = new ArrayList<ReminderObject>();
		String sql = "SELECT * FROM " + TABLE_REMINDER + " WHERE Date like '%"
				+ tomorrow + "%'";
		curObj = dbObj.rawQuery(sql, null);
		if (null != curObj) {
			if (curObj.moveToFirst()) {
				do {
					listRemObj.add(new ReminderObject(curObj.getString(0),
							curObj.getString(1), curObj.getString(2), curObj
							.getString(3)));

				} while (curObj.moveToNext() != false);
			}
		}
		return listRemObj;
	}

	/**
	 * Return list of reminder objects set for date greater than tomorrow
	 * 
	 * @return
	 */
	public ArrayList<ReminderObject> GetUpcomingReminders() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrow = dateFormatter.format(cal.getTime());
		Cursor curObj = null;
		ArrayList<ReminderObject> listRemObj = new ArrayList<ReminderObject>();
		String sql = "SELECT * FROM " + TABLE_REMINDER + " WHERE Date > '"
				+ tomorrow + "'";
		curObj = dbObj.rawQuery(sql, null);
		if (null != curObj) {
			if (curObj.moveToFirst()) {
				do {
					listRemObj.add(new ReminderObject(curObj.getString(0),
							curObj.getString(1), curObj.getString(2), curObj
							.getString(3)));

				} while (curObj.moveToNext() != false);
			}
		}
		return listRemObj;
	}
}
