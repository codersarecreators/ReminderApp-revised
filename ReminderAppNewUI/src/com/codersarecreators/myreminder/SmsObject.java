package com.codersarecreators.myreminder;


public class SmsObject {

	private String id = null;
	private String text = null;
	private String date = null;
	private String time = null;
	private String phoneNumber = null;
	
	public SmsObject(String id, String date, String time, String text, String phoneNumber)
	{
		this.id = id;
		this.text = text;
		this.date = date;
		this.time = time;
		this.phoneNumber = phoneNumber;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
}
