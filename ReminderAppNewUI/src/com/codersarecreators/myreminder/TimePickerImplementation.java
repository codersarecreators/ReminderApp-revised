package com.codersarecreators.myreminder;

import java.util.Calendar;
import java.util.StringTokenizer;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerImplementation implements
TimePickerDialog.OnTimeSetListener {

	private String timeSet;
	public static TextView timeTextView;
	private TimePickerDialog timePickerDialog = null;
	private String initialHr;
	private String initialMin;
	private int currentHr;
	private int currentMin;

	// Constructor
	public TimePickerImplementation() {

	}

	public String getTimeSet() {
		return timeSet;
	}

	public void setTimeSet(String timeSet) {
		this.timeSet = timeSet;
	}

	// Display Time Picker Dialogue on clicking on Time button on Add reminder
	// screen
	public void displayTimePickerDialog(View view) {
		Calendar calendar = null;

		// If time is already present in text view , it is parsed and displayed
		// on Time Picker
		String preExistingTime = (String) timeTextView.getText().toString();
		if (null != preExistingTime && !preExistingTime.equals("")) {
			StringTokenizer st = new StringTokenizer(preExistingTime, " : ");
			initialHr = st.nextToken();
			initialMin = st.nextToken();
			if (timePickerDialog == null)
				timePickerDialog = new TimePickerDialog(view.getContext(),
						new TimePickerImplementation(),
						Integer.parseInt(initialHr),
						Integer.parseInt(initialMin), true);

		} else {
			// Current time is displayed on Time Picker Dialogue
			calendar = Calendar.getInstance();
			currentHr = calendar.get(Calendar.HOUR);
			currentMin = calendar.get(Calendar.MINUTE);

			if (timePickerDialog == null)
				timePickerDialog = new TimePickerDialog(view.getContext(),
						new TimePickerImplementation(), currentHr, currentMin,
						true);

		}
		timePickerDialog.show();

	}// End of displayTimePickerDialog dialogue

	@Override
	public void onTimeSet(TimePicker view, int hr, int min) {
		timeSet = hr + ":" + min;
		setTimeSet(timeSet);
		// Sets the time selected by user on textview
		timeTextView.setText(hr + ":" + min);

	}
}
