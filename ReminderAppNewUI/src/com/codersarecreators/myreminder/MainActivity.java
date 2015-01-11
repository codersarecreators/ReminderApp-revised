package com.codersarecreators.myreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import com.example.reminderappnewui.R;

public class MainActivity extends Activity {

	static ReminderExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, ArrayList<ReminderObject>> listDataChild;
	private static Context contextObj = null;
	private ArrayList<ReminderObject> listReminderObject = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		 * Set color to Action bar
		 */
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.Brown)));
		contextObj = this;
		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.HomeScreen_listViewExpandable);
		/*
		 * divider.xml file has custom divider attributes set Assign these to
		 * the divider of child elements
		 */
		Drawable divider = getResources().getDrawable(R.drawable.divider);
		expListView.setChildDivider(divider);
		expListView.setDividerHeight(5);
		// preparing list data
		prepareListData();
		listAdapter = new ReminderExpandableListAdapter(this, listDataHeader,
				listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);

	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, ArrayList<ReminderObject>>();

		// Adding header Data
		listDataHeader.add("Today's");
		listDataHeader.add("Tomorrow's");
		listDataHeader.add("Upcoming");

		/*
		 * Get the values from database for today, tomorrow and upcoming and put
		 * them in hashmap besides the appropriate header.
		 */
		listReminderObject = new ArrayList<ReminderObject>();
		listReminderObject = DatabaseGateway.GetDbGateWay()
				.GetTodaysReminders();
		if (listReminderObject == null)
			listReminderObject = new ArrayList<ReminderObject>();
		listDataChild.put(listDataHeader.get(0), listReminderObject);

		listReminderObject = DatabaseGateway.GetDbGateWay()
				.GetTomorrowsReminder();
		if (listReminderObject == null)
			listReminderObject = new ArrayList<ReminderObject>();
		listDataChild.put(listDataHeader.get(1), listReminderObject);

		listReminderObject = DatabaseGateway.GetDbGateWay()
				.GetUpcomingReminders();
		if (listReminderObject == null)
			listReminderObject = new ArrayList<ReminderObject>();
		listDataChild.put(listDataHeader.get(2), listReminderObject);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * Inflate the menu; this adds items to the action bar if it is present.
		 * Action bar inflated with + and history buttons
		 */
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			displayAddReminderScreen();
			return true;
		case R.id.action_history:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// GetContext Method
	public static Context GetContext() {
		return contextObj;
	}

	/*
	 * Function called when clicked on + sign
	 */
	public void displayAddReminderScreen() {
		// Launches new Activity AddReminder which has functionalities to add
		// new Reminder
		Intent intent = new Intent(this, Reminder.class);
		startActivity(intent);
	}

	/**
	 * Notify the adapter when values deleted from database
	 */
	static void notifydatasetchanged() {
		listAdapter.notifyDataSetChanged();
	}
}
