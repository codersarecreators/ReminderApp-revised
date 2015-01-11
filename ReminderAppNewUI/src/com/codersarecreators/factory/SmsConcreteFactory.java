package com.codersarecreators.factory;

import com.codersarecreators.myreminder.ReminderObject;
import com.codersarecreators.myreminder.SmsObject;

public class SmsConcreteFactory extends SmsFactory {

	@Override
	public ReminderObject CreateReminderObject(String id, String note,
			String date, String time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SmsObject CreateSmsObject(String id, String date, String time,
			String text, String phoneNumber) {
		// TODO Auto-generated method stub
		SmsObject smsObj = new SmsObject(id,date,time,text,phoneNumber);
		if(null != smsObj)
			return smsObj;
		return null;
	}

}
