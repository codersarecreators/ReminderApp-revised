package com.codersarecreators.myreminder;


public class ReminderObject {

	private String id = null;
	private String note = null;
	private String date = null;
	private String time = null;
	
	public ReminderObject(String id,String date, String time, String note)
	{
		this.id = id;
		this.note = note;
		this.date = date;
		this.time = time;
	}

	public String getNote() {
		return note;
	}

	public String getTime() {
		return time;
	}

	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}
	
	//override the toString method
	public String toString()
	{
		return time + ", " + note;
	}
}
