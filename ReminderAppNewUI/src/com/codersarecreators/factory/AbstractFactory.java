package com.codersarecreators.factory;

import com.codersarecreators.myreminder.ReminderObject;
import com.codersarecreators.myreminder.SmsObject;

public interface AbstractFactory {
	ReminderObject CreateReminderObject(String id,String date,String time,String note);
	SmsObject CreateSmsObject(String id, String date, String time, String text, String phoneNumber);
}
