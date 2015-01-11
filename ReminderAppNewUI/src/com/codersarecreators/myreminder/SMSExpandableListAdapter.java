package com.codersarecreators.myreminder;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.reminderappnewui.R;

public class SMSExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private String dataHeader; // header titles
	// child data in format of arraylist of smsobjects
	private ArrayList<SmsObject> listSmsObject;
	LayoutInflater layoutInflater;

	public SMSExpandableListAdapter(Context context, String dataHeader,
			ArrayList<SmsObject> listSmsObject) {
		this.context = context;
		this.dataHeader = dataHeader;
		this.listSmsObject = listSmsObject;
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
		 * this.listDataChild.get(this.listDataHeader.get(groupPosition)):
		 * Returns the values associated with the header. Now to return specific
		 * value we pass the child position
		 */
		return this.listSmsObject.get(childPosition);

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
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final SmsObject smsObject = (SmsObject) getChild(groupPosition,
				childPosition);
		String phoneNumber = smsObject.getPhoneNumber();
		/*
		 * If there is no view initially inflate the view
		 */
		if (null == convertView) {
			convertView = layoutInflater.inflate(
					R.layout.sms_listview_child_item, null);
		}
		/*
		 * Get textview from view and fill it with phone number
		 */
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.AddReminderScreen_childItem_textview);
		txtListChild.setText(phoneNumber);

		// Listener set on Delete icon
		ImageButton menuDelete = (ImageButton) convertView
				.findViewById(R.id.AddReminderScreen_menuDelete);
		menuDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				int flag = 1;
				/*
				 * Delete the SMS from database and remove from the lsit as
				 * well, as UI will be updated from the list by calling
				 * notifydatasetchanged method
				 */
				DatabaseGateway.GetDbGateWay().deleteSMSFromList(
						smsObject.getId());
				listSmsObject.remove(childPosition);
				/*
				 * Set flag to 0 if there are no SMS in the list now. This flag
				 * will be checked in notifydatasetchanged method. If flag will
				 * be 0 it will make the ExpandableListview invisible
				 */
				if (listSmsObject.size() == 0) {
					flag = 0;
				}
				Reminder.notifydatasetchanged(flag);
			}

		});
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
		return this.listSmsObject.size();
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
		return this.dataHeader;
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
		return 1;
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
			convertView = layoutInflater.inflate(
					R.layout.sms_listview_header_item, null);
		}
		// Sets the header value to Recepient
		TextView listHeaderTxtView = (TextView) convertView
				.findViewById(R.id.AddReminderScreen_ListHeader_textView);
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
