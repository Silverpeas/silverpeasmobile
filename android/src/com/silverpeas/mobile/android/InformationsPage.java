package com.silverpeas.mobile.android;

import android.app.Activity;
import android.os.Bundle;

public class InformationsPage extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informations);
	}
	
	public void onBackPressed() {
	    super.onBackPressed();   
	    finish();
	}
}
