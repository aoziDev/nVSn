package com.aozi.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.example.nvsn.R;

public class DialogManager {
	public static void showDialog(Context context, String title, String msg) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setMessage(msg);
		dialogBuilder.setTitle(title);
		dialogBuilder.setPositiveButton(context.getResources().getString(android.R.string.ok), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialogBuilder.create();
		dialogBuilder.show();
	}
	
	public static void showErrorDialog(Context context, String msg) {
		String errorTitle = context.getResources().getString(R.string.error);
		showDialog(context, errorTitle, msg);
	}
}
