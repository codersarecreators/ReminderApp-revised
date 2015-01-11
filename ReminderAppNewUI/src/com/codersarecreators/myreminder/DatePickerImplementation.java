package com.codersarecreators.myreminder;

import java.util.Calendar;
import java.util.StringTokenizer;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

//Date Picker Listener
public class DatePickerImplementation implements
DatePickerDialog.OnDateSetListener {

	private String dateSet;
	private String initialMonth;
	private String initialDate;
	private String initialYear;
	private DatePickerDialog datePickerDialog = null;
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	public static TextView dateTextView;

	public DatePickerImplementation() {

	}

	public String getDateSet() {
		return dateSet;
	}


	public void setDateSet(String dateSet) {
		this.dateSet = dateSet;
	}
	
	
	/**
	 * Display datepicker dialogue on clicking on Date button on Reminder screen
	 * @param view
	 */
	public void displayDatePickerDialog(View view) {
		Calendar calendar = null;

		/* If a date already exists on screen it gets the date and parses it and
		 * displays that date on datepicker dialogue
		 */
		String preExistingDate = (String) dateTextView.getText().toString();
		if (null != preExistingDate && !preExistingDate.equals("")) {
			StringTokenizer st = new StringTokenizer(preExistingDate, "-");
			initialYear = st.nextToken();
			initialMonth = st.nextToken();
			initialDate = st.nextToken();
			
			if (datePickerDialog == null)
				datePickerDialog = new DatePickerDialog(view.getContext(),
						new DatePickerImplementation(),
						Integer.parseInt(initialYear),
						Integer.parseInt(initialMonth) - 1,
						Integer.parseInt(initialDate));

			/* Sets min date in date picker dialogue as current date so past
			 * date cannot be selected.
			 */
			datePickerDialog.getDatePicker().setMinDate(
					System.currentTimeMillis() - 1000);

		} else {

			// Set the current date in date picker dialogue
			calendar = Calendar.getInstance();
			currentYear = calendar.get(Calendar.YEAR);
			currentMonth = calendar.get(Calendar.MONTH);
			currentDay = calendar.get(Calendar.DAY_OF_MONTH);

			if (datePickerDialog == null)
				datePickerDialog = new DatePickerDialog(view.getContext(),
						new DatePickerImplementation(), currentYear,
						currentMonth, currentDay);

			// Sets min date in date picker dialogue as current date so past
			// date cannot be selected.
			datePickerDialog.getDatePicker().setMinDate(
					System.currentTimeMillis() - 1000);

		}

		datePickerDialog.show();

		// End of displayDatePickerDialog method
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// as month is returned from 0 so January = 0, February = 1
		monthOfYear = monthOfYear + 1; 
	   // Convert int into String
		String formattedDay = (String.valueOf(dayOfMonth));     
		String formattedMonth = (String.valueOf(monthOfYear));
		// If day <10 then prefix the day with 0
		if(dayOfMonth < 10)
		{
		    formattedDay = "0" + dayOfMonth;
		}
		// If month <10 then prefix the month with 0
		if(monthOfYear < 10)
		{
		    formattedMonth = "0" + monthOfYear;
		}
	
		dateSet = year + "-" + formattedMonth + "-" + formattedDay;
		// Sets the date selected by user in text view
		dateTextView.setText(dateSet);
		setDateSet(dateSet);
	}

}