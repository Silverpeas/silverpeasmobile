package com.silverpeas.mobile.android;

import com.phonegap.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SilverpeasMobileActivity extends DroidGap {
	
	public static final String PREFS_NAME = "URLPrefsFile";
	private static String URL;
    
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.main);
        
        final SharedPreferences URLPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String prefsURL = URLPrefs.getString("URL", "null");
        
        if(prefsURL=="null"){
        	LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.alertdialogurl, null);
            
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            
            alertDialog.setView(alertDialogView);
            alertDialog.setTitle("Entrez l'URL du serveur : ");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				SharedPreferences.Editor prefsEditor = URLPrefs.edit();
    				EditText EditTextURL = (EditText)alertDialogView.findViewById(R.id.EditTextURL);
    				prefsEditor.putString("URL", EditTextURL.getText().toString());
    				prefsEditor.commit();
    				URL = EditTextURL.getText().toString();
    				
    				String URLenr = new String("URL Enregistree.");	
    				Toast.makeText(SilverpeasMobileActivity.this, URLenr, Toast.LENGTH_SHORT).show();
    			}
    		});
            
            alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {		
    			public void onClick(DialogInterface dialog, int which) {
    				finish();
    			}
    		});
            alertDialog.show();
            super.loadUrl("\""+URL+"\"");
        }
        else{
            super.loadUrl("\""+prefsURL+"\"");
        }
    }
}