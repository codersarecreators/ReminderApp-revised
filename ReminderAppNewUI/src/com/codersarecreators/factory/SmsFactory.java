package com.codersarecreators.factory;

import com.codersarecreators.myreminder.SmsObject;

public abstract class SmsFactory implements AbstractFactory {

	@Override
	public abstract SmsObject CreateSmsObject(String id, String date, String time, String text, String phoneNumber);
	
}
