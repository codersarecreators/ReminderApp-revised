package com.codersarecreators.myreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.reminderappnewui.R;

public class ReminderExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	// header titles
	private List<String> listDataHeader;
	/*
	 * child data in format of header title, child title/data String is 1 header
	 * eg: Today and ArrayList consists of all reminderObjects which are to be
	 * displayed on expanding todays
	 */
	private HashMap<String, ArrayList<ReminderObject>> listReminderObjects;
	LayoutInflater layoutInflater;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listDataHeader
	 * @param listReminderObjects
	 */
	public ReminderExpandableListAdapter(Context context,
			List<String> listDataHeader,
			HashMap<String, ArrayList<ReminderObject>> listReminderObjects) {
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listReminderObjects = listReminderObjects;
		layoutInflater = LayoutInflater.from(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 * groupPosition: The position of the group that contains the child
	 * childPosition: The position of the child within the group returns the
	 * data of the child
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/*
		 * this.listReminderObjects.get(this.listDataHeader.get(groupPosition)):
		 * Returns the values associated with the header. Now to return specific
		 * value we pass the child position
		 */
		return this.listReminderObjects.get(
				this.listDataHeader.get(groupPosition)).get(childPosition);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 * groupPosition: The position of the group that contains the child
	 * childPosition: The position of the child within the group returns the id
	 * of the child
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup) groupPosition: The position of
	 * the group that contains the child childPosition: The position of the
	 * child within the group whose view is to be returned isLastChild: Whether
	 * the child is last child within the group convertView: The old view to
	 * reuse if not null parent: The parent to which the child is going to
	 * attach returns a view corresponding to the child mentioned at specified
	 * position
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition)
				.toString();

		if (null == convertView) {
			convertView = layoutInflater.inflate(R.layout.listview_child_item,
					null);
		}

		/*
		 * This is the text view on which Reminder information will be displayed
		 */
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.HomeScreen_childItem_textview);
		txtListChild.setText(childText);

		// Listener set on edit icon
		ImageButton menuEdit = (ImageButton) convertView
				.findViewById(R.id.HomeScreen_menuEdit);
		menuEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, Reminder.class);
				/*
				 * On Click of Edit icon, Edit Reminder screen opens which has
				 * same UI as AddReminder putExtra is used to pass the values to
				 * the Edit Reminder Screen for that particular reminder These
				 * values will get poppulated on Edit Reminder screen
				 */
				intent.putExtra(
						"com.codersarecreators.reminderId",
						listReminderObjects
						.get(listDataHeader.get(groupPosition))
						.get(childPosition).getId());
				intent.putExtra(
						"com.codersarecreators.reminderDate",
						listReminderObjects
						.get(listDataHeader.get(groupPosition))
						.get(childPosition).getDate());
				intent.putExtra(
						"com.codersarecreators.reminderTime",
						listReminderObjects
						.get(listDataHeader.get(groupPosition))
						.get(childPosition).getTime());
				intent.putExtra(
						"com.codersarecreators.reminderNote",
						listReminderObjects
						.get(listDataHeader.get(groupPosition))
						.get(childPosition).getNote());
				context.startActivity(intent);

			}

		});

		/*
		 * Listener set on Delete icon
		 */
		ImageButton menuDelete = (ImageButton) convertView
				.findViewById(R.id.HomeScreen_menuDelete);
		menuDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * Delete the reminder from database by passing reminder id
				 */
				ArrayList<ReminderObject> listReminderObject;
				DatabaseGateway.GetDbGateWay().DeleteReminder(
						listReminderObjects
						.get(listDataHeader.get(groupPosition))
						.get(childPosition).getId());
				/*
				 * Get the updated list of reminders from database depending on
				 * child of which group was deleted The list required to update
				 * the UI
				 */
				if (groupPosition == 0) {
					listReminderObject = DatabaseGateway.GetDbGateWay()
							.GetTodaysReminders();
				} else if (groupPosition == 1) {
					listReminderObject = DatabaseGateway.GetDbGateWay()
							.GetTomorrowsReminder();
				} else {
					listReminderObject = DatabaseGateway.GetDbGateWay()
							.GetUpcomingReminders();
				}
				/*
				 * After getting the list need to update the hashmap with
				 * updated list for that header/group
				 */
				listReminderObjects.put(listDataHeader.get(groupPosition),
						listReminderObject);
				/*
				 * This is a static method which internally will call adapters
				 * built in NotifyDataSetChanged method It will let the adapter
				 * run the loop again and display the Update UI on screen
				 */
				MainActivity.notifydatasetchanged();

			}
		});

		/*
		 * Set alternate color to the child views
		 */
		if ((childPosition % 2) == 0)
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.White));
		else
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.Wheat));
		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 * groupPosition: The position of the group whose children count is to be
	 * returned returns the number of children in the group
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.listReminderObjects.get(
				this.listDataHeader.get(groupPosition)).size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int) groupPosition:
	 * The position of the group returns child data associated to the group
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.listDataHeader.get(groupPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount() returns the
	 * number of groups
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.listDataHeader.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int) The position of
	 * the group whose ID is wanted
	 */
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup) Gets a view that displays the
	 * given group groupPosition: The position of the group whose view is to be
	 * returned isExapnded: Whether the group is expanded or collapsed
	 * convertView: Retruns the old view if not null parent: The parent to which
	 * the view will be attached
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.listview_header_item,
					null);
		}

		TextView listHeaderTxtView = (TextView) convertView
				.findViewById(R.id.HomeScreen_ListHeader_textView);
		listHeaderTxtView.setTypeface(null, Typeface.BOLD);
		listHeaderTxtView.setText(headerTitle);

		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds() verifies whether
	 * same id's refer to same object
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 * groupPosition: The position of the group that contains the child
	 * childPosition: The position of the child returns whether the child is
	 * selectable or not
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
