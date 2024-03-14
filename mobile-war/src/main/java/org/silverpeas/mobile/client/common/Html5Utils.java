/*
 * Copyright (C) 2000 - 2022 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import org.silverpeas.mobile.client.common.event.speech.SpeechErrorEvent;
import org.silverpeas.mobile.client.common.event.speech.SpeechResultEvent;
import org.silverpeas.mobile.client.common.event.speech.SpeechStartEvent;
import org.silverpeas.mobile.client.common.event.speech.SpeechStopEvent;

/**
 * @author: svu
 */
public class Html5Utils {

  public static void disableAnchor(Anchor a) {
    String style = a.getElement().getAttribute("style");
    a.getElement().setAttribute("style",style + "pointer-events: none;");
  }

  public static void enableAnchor(Anchor a) {
    String style = a.getElement().getAttribute("style");
    style = style.replace("pointer-events: none;","");
    a.getElement().setAttribute("style",style);
  }

  public static native void setVideoFullScreen(Element element) /*-{
      if (element.requestFullscreen) {
          element.requestFullscreen();
      } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen();
      } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
      } else if (element.webkitRequestFullscreen) {
          element.webkitRequestFullscreen();
      }
  }-*/;

  public static native void setVideoNotFullScreen(Element element) /*-{
      if (element.exitFullscreen) {
          element.exitFullscreen();
      } else if (element.mozCancelFullScreen) {
          element.mozCancelFullScreen();
      } else if (element.webkitExitFullscreen) {
          element.webkitExitFullscreen();
      }
  }-*/;

  public static native boolean isVideoFullScreen() /*-{
      var fullscreenEnabled = document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled;
      return fullscreenEnabled;
  }-*/;

  public static void vibrate() {
    vibrate(300);
  }

  public static native void vibrate(int duration) /*-{
    $wnd.navigator.vibrate(duration);
  }-*/;
  public static native boolean isSpeechSupported() /*-{
    return ('SpeechRecognition' in window || 'webkitSpeechRecognition' in window);
  }-*/;

  public static native boolean isSpeechSynthesisSupported() /*-{
    return ('speechSynthesis' in window);
  }-*/;

  public static native void startListening() /*-{
    window.recognition.start();
  }-*/;

  public static native void stopListening() /*-{
    window.recognition.stop();
  }-*/;

  public static native boolean initSpeech(boolean continuous, String language) /*-{
    window.recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
    recognition.lang = language;
    recognition.continuous = continuous;

    recognition.onresult = function (event) {
      // Récupère le texte reconnu
      var text = event.results[0][0].transcript;
      @org.silverpeas.mobile.client.common.Html5Utils::onSpeechResult(Ljava/lang/String;)(text);
    }

    recognition.onstart = function () {
      // envoie d'un evenement sur le bus pour mise à jour GUI selon le contexte
      @org.silverpeas.mobile.client.common.Html5Utils::onSpeechStart(*)();
    };

    // Événement déclenché lorsqu'une reconnaissance vocale s'arrête
    recognition.onend = function () {
      // envoie d'un evenement sur le bus pour mise à jour GUI selon le contexte
      @org.silverpeas.mobile.client.common.Html5Utils::onSpeechStop(*)();
    };

    recognition.onerror = function(event) {
      @org.silverpeas.mobile.client.common.Html5Utils::onSpeechError(*)();
    };

  }-*/;
  public static native JsArray<Voice> getVoices() /*-{
    return window.speechSynthesis.getVoices();
  }-*/;

  public static void readText(String[] text) {
    Html5Utils.cancelSpeaking();
    for (int i = 0; i < text.length; i++) {
      String [] sentences = text[i].split("\\.");
      if (sentences.length==0) speak(text[i]);
      for (int j = 0; j < sentences.length; j++) {
        speak(sentences[j]);
      }
    }
  }

  private static native void speak(String text) /*-{
    var msg = new SpeechSynthesisUtterance();
    msg.text = text;
    msg.addEventListener("end", function (){});
    speechSynthesis.speak(msg);

  }-*/;

  public static native void cancelSpeaking() /*-{
    speechSynthesis.cancel();
  }-*/;
  private static native void speak(String text, double volume, double rate, double pitch, String language) /*-{
        var msg = new SpeechSynthesisUtterance();
        msg.volume = volume; // From 0 to 1
        msg.rate = rate; // From 0.1 to 10
        msg.pitch = pitch; // From 0 to 2
        msg.text = text;
        msg.lang = language;
        speechSynthesis.speak(msg);
    }-*/;

  private static native void speak(String text, double volume, double rate, double pitch, String language, String desiredVoice) /*-{
    var msg = new SpeechSynthesisUtterance();
    for (var i = 0; i < speechSynthesis.getVoices().length; i++) {
        if (speechSynthesis.getVoices()[i].name === desiredVoice) {
        msg.voice = speechSynthesis.getVoices()[i];
      }
    }
    msg.volume = volume; // From 0 to 1
    msg.rate = rate; // From 0.1 to 10
    msg.pitch = pitch; // From 0 to 2
    msg.text = text;
    msg.lang = language;
    speechSynthesis.speak(msg);
  }-*/;

  public static void onSpeechResult(String result) {
    EventBus.getInstance().fireEvent(new SpeechResultEvent(result));
  }

  public static void onSpeechStart() {
    EventBus.getInstance().fireEvent(new SpeechStartEvent());
  }

  public static void onSpeechStop() {
    EventBus.getInstance().fireEvent(new SpeechStopEvent());
  }

  public static void onSpeechError() {
    EventBus.getInstance().fireEvent(new SpeechErrorEvent());
  }

  public static native boolean canShare(String title, String text, String url) /*-{
    var shareData = {
      title: title,
      text: text,
      url: url
    };
    return $wnd.navigator.canShare(shareData);
  }-*/;

  public static native void share(String title, String text, String url) /*-{
    var shareData = {
      title: title,
      text: text,
      url: url
    };
    $wnd.navigator.share(shareData);
  }-*/;

}
