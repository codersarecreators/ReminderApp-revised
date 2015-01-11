package com.codersarecreators.myreminder;
import android.content.Context;
import android.widget.Toast;

public class MyToast {

	private static Context contextObj = MainActivity.GetContext();
	public static void RaiseToast(String str)
	{
		Toast.makeText(contextObj, str, Toast.LENGTH_SHORT).show();
	}
}
