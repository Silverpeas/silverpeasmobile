package com.silverpeas.mobile.android;

import org.apache.cordova.DroidGap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SilverpeasMobileActivity extends DroidGap {

	public static final String PREFS_NAME = "URLPrefsFile";
	private static String URL;

	@Override
	public void onCreate(Bundle state) {
		if(isOnline()==false){
			Deconnexion();
		}
		super.onCreate(state);
		setContentView(R.layout.main);
		super.setBooleanProperty("loadInWebView", true);
		CheckPreferences();
	}

	public void CheckPreferences() {
		final SharedPreferences URLPrefs = getSharedPreferences(PREFS_NAME,
				MODE_PRIVATE);
		String prefsURL = URLPrefs.getString("URL", "http://my.silverpeas.com");
		URL = new String(prefsURL);
		LoadURL();
	}

	public void LoadURL() {
		super.loadUrl(URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.URL:
			ChangeURL();
			return true;
		case R.id.deconnexion:
			CheckPreferences();
			return true;
		case R.id.quitter:
			finish();
			return true;
		}
		return false;
	}

	public void ChangeURL() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(R.layout.alertdialogurl,
				null);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setView(alertDialogView);
		alertDialog.setTitle("Entrez l'URL du serveur : ");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						final SharedPreferences URLPrefs = getSharedPreferences(
								PREFS_NAME, MODE_PRIVATE);
						SharedPreferences.Editor prefsEditor = URLPrefs.edit();
						EditText EditTextURL = (EditText) alertDialogView
								.findViewById(R.id.EditTextURL);
						prefsEditor.putString("URL", EditTextURL.getText()
								.toString());
						prefsEditor.commit();
						URL = EditTextURL.getText().toString();
						String URLenr = new String("URL Enregistree.");
						Toast.makeText(SilverpeasMobileActivity.this, URLenr,
								Toast.LENGTH_SHORT).show();
						LoadURL();
					}
				});

		alertDialog.setNegativeButton("Annuler",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();
	}

	public void Deconnexion() {
		Intent myIntent = new Intent(SilverpeasMobileActivity.this,
				NoConnectionPage.class);
		startActivity(myIntent);
		finish();
	}

	private boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}