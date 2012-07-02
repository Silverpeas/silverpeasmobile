package com.silverpeas.mobile.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NoConnectionPage extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noconnection);
	}
	
	public void Quitter(View view){
		finish();
	}
	
	public void Informations(View view){
		Intent intent = new Intent(NoConnectionPage.this, InformationsPage.class);
		startActivityForResult(intent, 0);
	}
	
	public void onBackPressed() {
	    super.onBackPressed();   
	    Intent intent = new Intent(NoConnectionPage.this, SilverpeasMobileActivity.class);
		startActivityForResult(intent, 0);
		finish();
	}
}
