package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class Ckeditor {
    public static native void createEditor(Element element) /*-{
        $wnd.ClassicEditor.create(element, {
            toolbar: { items : [ 'undo', 'redo', 'heading', 'bold', 'italic','-', 'outdent', 'indent', 'numberedList', 'bulletedList', 'link', 'insertTable' ], shouldNotGroupWhenFull: true}}
        );
    }-*/;

    public static native String getCurrentData() /*-{
        var domEditableElement = $wnd.document.querySelector( '.ck-editor__editable' );
        var editorInstance = domEditableElement.ckeditorInstance;
        return editorInstance.getData();
    }-*/;

    public static native String setCurrentData(String data) /*-{
        var domEditableElement = $wnd.document.querySelector( '.ck-editor__editable' );
        var editorInstance = domEditableElement.ckeditorInstance;
        return editorInstance.setData(data);
    }-*/;

    public static native String destroyEditor() /*-{
        var domEditableElement = $wnd.document.querySelector( '.ck-editor__editable' );
        var editorInstance = domEditableElement.ckeditorInstance;
        return editorInstance.destroy();
    }-*/;
}
