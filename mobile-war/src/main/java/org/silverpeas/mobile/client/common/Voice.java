package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.JavaScriptObject;

public class Voice extends JavaScriptObject  {
    protected Voice() {
    }

    public final native String getLang() /*-{ return this.lang; }-*/;
    public final native String getName() /*-{ return this.name; }-*/;
    public final native String getvoiceURI() /*-{ return this.voiceURI; }-*/;


    public final native boolean getLocalService() /*-{ return this.localService; }-*/;

}
