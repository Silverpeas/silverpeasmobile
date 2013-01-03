package com.silverpeas.mobile.client.common.phonegap;

import com.gwtmobile.phonegap.client.Contacts.Contact;

public class PhoneContact extends Contact {
	
	protected PhoneContact(){
		super();
	}
	
	public final native String getPhoneNumber() /*-{
		return this.PhoneNumber;
	}-*/;

	public final native void setPhonenUmber(String phoneNumber) /*-{
		this.phoneNumber = phoneNumber;
	}-*/;
}
