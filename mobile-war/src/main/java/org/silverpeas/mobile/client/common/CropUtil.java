package org.silverpeas.mobile.client.common;

import com.google.gwt.dom.client.Element;

public class CropUtil {
    public static native void initCropTool(Element image, float ratio) /*-{
        if (ratio === 0) {
            $wnd.initCropper(image, 'NaN');
        } else {
            $wnd.initCropper(image, ratio);
        }
    }-*/;

    public static native void destroyCropTool() /*-{
        $wnd.destroyCropper();
    }-*/;

    public static native String getCanvasData() /*-{
        return $wnd.getCanvasData();
    }-*/;

}
