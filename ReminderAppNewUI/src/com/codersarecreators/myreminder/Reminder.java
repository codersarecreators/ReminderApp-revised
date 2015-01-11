package com.codersarecreators.myreminder;

import java.util.ArrayList;
import java.util.UUID;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.codersarecreators.factory.AbstractFactory;
import com.codersarecreators.factory.ReminderConcreteFactory;
import com.codersarecreators.factory.SmsConcreteFactory;
import com.example.reminderappnewui.R;

public class Reminder extends Activity {

	Button scheduleSMSDialogueContactsBtn, scheduleSMSDialogueDateBtn,
	scheduleSMSDialogueTimeBtn, scheduleSMSDialogueSaveBtn,
	scheduleSMSDialogueCancelBtn;
	TextView addReminderScreenDateTxtView, addReminderScreenTimeTxtView,
	scheduleSMSDialogueDateTxtView, scheduleSMSDialogueTimeTxtView;
	CheckBox addReminderScreenScheduleSMSChkBox;
	EditText scheduleSMSDialogueMessageEditText,
	addReminderScreenNotesEditText, scheduleSMSDialoguePhoneEditText;
	AbstractFactory factoryObject = null;
	public ArrayList<SmsObject> smsObjList = null;
	public SmsObject smsObj;
	ToggleButton tButton;
	static ExpandableListView addReminderScreenSMSDetailsListView;
	public static SMSExpandableListAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminder_screen);
		addReminderScreenDateTxtView = (TextView) findViewById(R.id.addReminderScreen_dateTxtView);
		addReminderScreenTimeTxtView = (TextView) findViewById(R.id.addReminderScreen_timeTxtView);
		addReminderScreenNotesEditText = (EditText) findViewById(R.id.addReminderScreen_notesEditText);
		
		Intent intent = getIntent();
		// here check if reminderid returned to verify edit mode entered
		if (null != intent.getStringExtra("com.codersarecreators.reminderId")) {
			editReminderMode(intent);
		} else {
			getActionBar().setTitle("Add Reminder");
		}
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.Brown)));

	}// end of onCreate Method

	/**
	 * @param view
	 * @author siddharth Method invoked on clicking on Set Button present on the
	 *         Add ReminderScreen to set the reminder and on Edit Reminder
	 *         screen to save the changed values
	 */
	public void saveReminder(View view) {
		String reminderId;
		EditText reminderNote = (EditText) findViewById(R.id.addReminderScreen_notesEditText);
		TextView reminderDate = (TextView) findViewById(R.id.addReminderScreen_dateTxtView);
		TextView reminderTime = (TextView) findViewById(R.id.addReminderScreen_timeTxtView);
		/*
		 * Get the intent and check if reminderId is returned If returned , the
		 * user is in edit mode
		 */
		Intent intent = getIntent();
		reminderId = intent.getStringExtra("com.codersarecreators.reminderId");
		if (null != reminderId) {
			/*
			 * Get the changed values and update in database
			 */
			ContentValues cv = new ContentValues();
			/*
			 * These Fields should be your String values of
			 * actual column names
			 */
			cv.put("Date", reminderDate.getText().toString()); 
			cv.put("Time", reminderTime.getText().toString());
			cv.put("Notes", reminderNote.getText().toString());
			DatabaseGateway.GetDbGateWay().EditReminder(
					intent.getStringExtra("com.codersarecreators.reminderId"),
					cv);
		} else {
			/*
			 * Add Reminder mode entered Create reminder object and insert
			 * values into database
			 */
			factoryObject = new ReminderConcreteFactory();
			ReminderObject reminderObj = factoryObject.CreateReminderObject(
					UUID.randomUUID().toString(), reminderDate.getText()
					.toString(), reminderTime.getText().toString(),
					reminderNote.getText().toString());
			DatabaseGateway.GetDbGateWay().InsertReminder(reminderObj);
			reminderId = reminderObj.getId();
		}
		if (smsObjList != null) {
			// this means that there has been an addition for sms object as well
			insertSMSObject(reminderId);
		}
	}// End of setReminder method

	/**
	 * Insert the sms values into database
	 * 
	 * @param reminderId
	 */
	private void insertSMSObject(String reminderId) {
		/*
		 * SMS object list is already filled
		 */
		for (SmsObject smsObj : smsObjList) {
			// this means that there has been an addition for sms object as well
			DatabaseGateway.GetDbGateWay().InsertSMS(smsObj);
			DatabaseGateway.GetDbGateWay().InsertIntoRemSmsAssocTable(
					reminderId, smsObj.getId());
		}
	}

	/**
	 * @param view
	 * @author fatema Method invoked on clicking on Cancel button present on Add
	 *         Reminder Screen. It will navigate back to the MainActivity.java
	 *         i.e home screen and reminder won't be saved.
	 */
	public void cancelReminder(View view) {
		Intent intent = new Intent(Reminder.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Function called when schedule SMS + sign is clicked
	 * 
	 * @param view
	 */
	public void displayScheduleSMSDialogue(View view) {

		final Dialog scheduleSMSDialog = new Dialog(view.getContext());
		scheduleSMSDialog.setCancelable(true);
		scheduleSMSDialog.setTitle("Schedule SMS");
		scheduleSMSDialog.setContentView(R.layout.schedule_sms_dialogue);

		scheduleSMSDialogueDateTxtView = (TextView) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_dateTxtView);
		scheduleSMSDialogueTimeTxtView = (TextView) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_timeTxtView);
		scheduleSMSDialogueMessageEditText = (EditText) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_messageEditText);
		scheduleSMSDialoguePhoneEditText = (EditText) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_phoneNoEditText);
		scheduleSMSDialogueCancelBtn = (Button) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_cancelBtn);
		/*
		 * Listener on Cancel button. It will close the schedule SMS dialogue
		 */
		scheduleSMSDialogueCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scheduleSMSDialog.dismiss();
			}
		});
		// On Click listener on Contacts image
		scheduleSMSDialogueContactsBtn = (Button) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_contactsBtn);
		scheduleSMSDialogueContactsBtn
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				/*
				 * Contact numbers displayed and user can select number
				 * to which he wants to send schedule sms
				 */
				Toast.makeText(view.getContext(),
						"Clicked on Contacts icon", Toast.LENGTH_LONG)
						.show();
			}
		});

		// On Click Listener on Date Button
		scheduleSMSDialogueDateBtn = (Button) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_dateBtn);
		scheduleSMSDialogueDateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				/*
				 * dateTextView is static text view.
				 * scheduleSMSDialogueDateTxtView is assigned to dateTextView
				 * where date will be displayed after getting selected.
				 */
				DatePickerImplementation.dateTextView = scheduleSMSDialogueDateTxtView;
				new DatePickerImplementation().displayDatePickerDialog(view);
			}
		});

		// On Click Listener on Time Button
		scheduleSMSDialogueTimeBtn = (Button) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_timeBtn);
		scheduleSMSDialogueTimeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				/*
				 * Object of TimePickerImplementation class created. TextView
				 * where selected date is to be displayed is passed as parameter
				 * to the constructor
				 */
				TimePickerImplementation.timeTextView = scheduleSMSDialogueTimeTxtView;
				new TimePickerImplementation().displayTimePickerDialog(view);
			}
		});

		/*
		 * On Click Listener on Save Button. It should save the values entered
		 * in database and also display them dynamically on add_reminder_screen
		 */
		scheduleSMSDialogueSaveBtn = (Button) scheduleSMSDialog
				.findViewById(R.id.scheduleSMSDialogue_saveBtn);
		scheduleSMSDialogueSaveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				/*
				 * Retrieve the user entered values
				 */
				String message = scheduleSMSDialogueMessageEditText.getText()
						.toString();
				String date = scheduleSMSDialogueDateTxtView.getText()
						.toString();
				String time = scheduleSMSDialogueTimeTxtView.getText()
						.toString();
				String phoneNumber = scheduleSMSDialoguePhoneEditText.getText()
						.toString();

				if (message.matches("") || date.matches("") || time.matches("")
						|| phoneNumber.matches("")) {
					MyToast.RaiseToast("Please fill all the fields");
				} else {
					/*
					 * SMS object created which will be required later when user
					 * wants to Set Reminder Object created here as the dialogue
					 * closes and we need the data at a later stage
					 */
					if (null == smsObjList || smsObjList.size() == 0) {
						// Entered when clicked on 1st time on + sign
						smsObjList = new ArrayList<SmsObject>();
						factoryObject = new SmsConcreteFactory();
						smsObj = factoryObject.CreateSmsObject(UUID
								.randomUUID().toString(), date, time, message,
								phoneNumber);
						smsObjList.add(smsObj);
						/*
						 * Make the Expandable List view which contains
						 * scheduleSMS fields visible and populate it
						 * accordingly.
						 */
						addReminderScreenSMSDetailsListView = (ExpandableListView) findViewById(R.id.smsDetails_ListView);
						adapter = new SMSExpandableListAdapter(
								getApplicationContext(), "Recipients",
								smsObjList);
						addReminderScreenSMSDetailsListView.setAdapter(adapter);
						addReminderScreenSMSDetailsListView
						.setVisibility(View.VISIBLE);
						scheduleSMSDialog.dismiss();
					} else {
						/*
						 * this block will be entered when sms object already
						 * created and clicking on + sign for 2nd time and as
						 * the list now has new values notifydatasetchanged is
						 * called.
						 */
						factoryObject = new SmsConcreteFactory();
						smsObj = factoryObject.CreateSmsObject(UUID
								.randomUUID().toString(), date, time, message,
								phoneNumber);
						smsObjList.add(0, smsObj);
						adapter.notifyDataSetChanged();
						scheduleSMSDialog.dismiss();
					}
				}
			}
		});
		scheduleSMSDialog.show();
	}// End of displayScheduleSMSDialogue method

	// Method invoked on clicking on Date Button on Add Reminder Screen
	public void selectDateOnAddReminderScreen(View view) {
		/*
		 * dateTextView is static text view. addReminderScreenDateTxtView is
		 * assigned to dateTextView where date will be displayed after getting
		 * selected.
		 */
		DatePickerImplementation.dateTextView = addReminderScreenDateTxtView;
		new DatePickerImplementation().displayDatePickerDialog(view);
	}

	// Method invoked on clicking on Time Button on Add Reminder Screen
	public void selectTimeOnAddReminderScreen(View view) {
		/*
		 * Object of TimePickerImplementation class created. TextView where
		 * selected time is to be displayed is passed as parameter to the
		 * constructor
		 */
		TimePickerImplementation.timeTextView = addReminderScreenTimeTxtView;
		new TimePickerImplementation().displayTimePickerDialog(view);
	}

	/**
	 * This will be called on deletion of sms from list to update the list
	 * 
	 * @param flag
	 */
	static void notifydatasetchanged(int flag) {
		if (flag == 0) {
			// Flag 0 means there are no sms so make the view invisible
			addReminderScreenSMSDetailsListView.setVisibility(View.INVISIBLE);
		}
		adapter.notifyDataSetChanged();
	}
	
	 /*	 This code is for the Edit functionality of reminder. When edit is
		 * clicked , intent is created and values are passed along with intent
		 * which are to be displayed on edit reminder screen Get the intent and
		 * check if reminderId is returned If returned , the user is in edit
		 * mode and data gets prepopulated
		 */
	public void editReminderMode(Intent intent){

		getActionBar().setTitle("Edit Reminder");
		addReminderScreenDateTxtView.setText(intent
				.getStringExtra("com.codersarecreators.reminderDate"));
		addReminderScreenTimeTxtView.setText(intent
				.getStringExtra("com.codersarecreators.reminderTime"));
		addReminderScreenNotesEditText.setText(intent
				.getStringExtra("com.codersarecreators.reminderNote"));
		/*
		 * Retrieve the SMS related to the reminder id. They will be
		 * displayed on Edit reminder screen if sms exists for that reminder
		 * id
		 */
		smsObjList = DatabaseGateway.GetDbGateWay().GetSMSforReminder(
				intent.getStringExtra("com.codersarecreators.reminderId"));
		if (smsObjList.size() != 0) {
			addReminderScreenSMSDetailsListView = (ExpandableListView) findViewById(R.id.smsDetails_ListView);
			adapter = new SMSExpandableListAdapter(getApplicationContext(),
					"Recipients", smsObjList);
			addReminderScreenSMSDetailsListView.setAdapter(adapter);
			/*
			 * Make the SMS expandablelist view visible as initially it is
			 * invisible if no sms
			 */
			addReminderScreenSMSDetailsListView.setVisibility(View.VISIBLE);
		}
	
	}
}
