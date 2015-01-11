package com.codersarecreators.factory;

import com.codersarecreators.myreminder.ReminderObject;

public abstract class ReminderFactory implements AbstractFactory {

	/*
	 * Take just the one method that is associated with the reminder object from the abstract factory interface.
	 */
	@Override
	public abstract ReminderObject CreateReminderObject(String id,String date,String time,String note);
	
}
