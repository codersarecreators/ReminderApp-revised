package com.codersarecreators.myreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListAdapter;
import android.widget.Spinner;

public class DialogSpinner extends Spinner{
	
        public DialogSpinner(Context context) {
            super(context);
        }

        @Override 
        public boolean performClick() {
            new AlertDialog.Builder(getContext()).setAdapter((ListAdapter) getAdapter(), 
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        setSelection(which);
                        dialog.dismiss();
                    }
                }).create().show();
            return true;
        }
}
