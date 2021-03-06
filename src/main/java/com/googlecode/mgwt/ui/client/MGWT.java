/*
 * Copyright 2010 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.mgwt.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeHandler;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.util.AddressBarUtil;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

/**
 * The MGWT Object is used to apply settings for an MGWT App. It also provides an instance of
 * {@link OsDetection}, as well a way to determine the device orientation
 * 
 * 
 * 
 * @author Daniel Kurka
 */
public class MGWT {

  private static OsDetection OS_DETECTION;

  private static EventBus manager;

 
  private static Timer timer;

  private static boolean scrollingDisabled;
  private static JavaScriptObject nativeJsFunction;

  private static AddressBarUtil addressBarUtil;
  
  private static OrientationHandler orientationHandler;

  public static OrientationHandler getOrientationHandler() {
	  if ( orientationHandler == null){
		  orientationHandler = GWT.create(OrientationHandler.class);
		  
	  }
	return orientationHandler;
}

/**
   * Add a orientation handler to detect the device orientation
   * 
   * @param handler the handler to add
   *          {@link com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeHandler} .
   * @return a {@link com.google.gwt.event.shared.HandlerRegistration} object.
   */
  public static HandlerRegistration addOrientationChangeHandler(OrientationChangeHandler handler) {
    getOrientationHandler().maybeSetupOrientation(getManager());
    return getManager().addHandler(OrientationChangeEvent.getType(), handler);
  }

  /**
   * Apply settings for this mgwt app. Every app should call this method with the options its wants
   * for their app
   * 
   * The options are documented in @link {@link MGWTSettings}
   * 
   * @param settings the settings for this app
   * 
   */
  public static void applySettings(MGWTSettings settings) {

    // This is a very nasty workaround because GWT CssResource does not
    // support @media correctly!
    StyleInjector.inject(MGWTStyle.getTheme().getMGWTClientBundle().utilTextResource().getText());

    Element head = getHead();

    if (settings.getIconUrl() != null) {

      LinkElement iconUrlLinkElement = Document.get().createLinkElement();
      if (settings.isAddGlosToIcon()) {
        iconUrlLinkElement.setRel("apple-touch-startup-image");
      } else {
        iconUrlLinkElement.setRel("apple-touch-startup-image-precomposed");
      }

      iconUrlLinkElement.setHref(settings.getIconUrl());
      head.appendChild(iconUrlLinkElement);

    }

    if (settings.getStartUrl() != null) {
      LinkElement startUrlLinkElement = Document.get().createLinkElement();
      startUrlLinkElement.setRel("apple-touch-startup-image");
      startUrlLinkElement.setHref(settings.getStartUrl());
      head.appendChild(startUrlLinkElement);
    }

    ViewPort viewPort = settings.getViewPort();

    if (viewPort != null) {
      MetaElement fixViewPortElement = Document.get().createMetaElement();
      fixViewPortElement.setName("viewport");
      fixViewPortElement.setContent(viewPort.getContent());
      head.appendChild(fixViewPortElement);

    }

    if (settings.isFullscreen()) {
      MetaElement fullScreenMetaTag = Document.get().createMetaElement();
      fullScreenMetaTag.setName("apple-mobile-web-app-capable");
      fullScreenMetaTag.setContent("yes");
      head.appendChild(fullScreenMetaTag);

      if (settings.getStatusBar() != null) {
        MetaElement statusBarMetaTag = Document.get().createMetaElement();
        statusBarMetaTag.setName("apple-mobile-web-app-status-bar-style");
        statusBarMetaTag.setContent(settings.getStatusBar());
        head.appendChild(statusBarMetaTag);

      }

    }

    scrollingDisabled = settings.isPreventScrolling();
    if (settings.isPreventScrolling() && getOsDetection().isIOs()) {
      BodyElement body = Document.get().getBody();
      setupPreventScrolling(body);

    }

    if (settings.isDisablePhoneNumberDetection()) {
      MetaElement fullScreenMetaTag = Document.get().createMetaElement();
      fullScreenMetaTag.setName("format-detection");
      fullScreenMetaTag.setContent("telephone=no");
      head.appendChild(fullScreenMetaTag);
    }

    if (settings.getStatusBarStyle() != null) {
      MetaElement statusBarTag = Document.get().createMetaElement();
      statusBarTag.setName("apple-mobile-web-app-status-bar-style");
      switch (settings.getStatusBarStyle()) {

        case BLACK:
          statusBarTag.setContent("black");
          break;
        case BLACK_TRANSLUCENT:
          statusBarTag.setContent("black-translucent");
          break;
        case DEFAULT:
        default:
          statusBarTag.setContent("default");
          break;
      }

      head.appendChild(statusBarTag);
    }

  }

  /**
   * Detect if a web app is in full screen mode
   * 
   * fullscreen currently only exits on ios
   * 
   * @return true if the web app is in full screen mode, otherwise false
   */
  public static native boolean isFullScreen()/*-{
		if ($wnd.navigator.standalone) {
			return true;
		}
		return false;
  }-*/;

  /**
   * 
   * Considered internal don`t call!
   * <p>
   * fixIOSScrollIssueBlur
   * </p>
   */
  public static void fixIOSScrollIssueBlur() {
    if (!scrollingDisabled) {
      return;
    }
    if (timer != null) {
      timer.cancel();

    }

    timer = new Timer() {

      @Override
      public void run() {
        Window.scrollTo(0, 0);

      }

    };

    timer.schedule(100);
  }

  /**
   * Considered internal don`t call!
   * <p>
   * fixIOSScrollIssueFocus
   * </p>
   */
  public static void fixIOSScrollIssueFocus() {
    if (!scrollingDisabled) {
      return;
    }
    if (timer != null) {
      timer.cancel();

    }
    timer = null;
  }

  /**
   * Get the os detection interface
   * 
   * @return a {@link com.googlecode.mgwt.ui.client.OsDetection} object.
   */
  public static OsDetection getOsDetection() {
    if (OS_DETECTION == null) {
      OS_DETECTION = GWT.create(OsDetection.class);
    }
    return OS_DETECTION;
  }

  


  private static Element getHead() {
    NodeList<Element> elementsByTagName = Document.get().getElementsByTagName("head");

    if (elementsByTagName.getLength() != 1) {
      throw new RuntimeException("there is no head element");
    }

    return elementsByTagName.getItem(0);
  }

  
 
  private static native void setupPreventScrolling(Element el)/*-{
		var func = function(event) {
			event.preventDefault();
			return false;
		};

		el.ontouchmove = func;

  }-*/;

  /**
   * A utility method to hide the soft keyboard
   */
  public static void hideKeyBoard() {
    final Anchor anchor = new Anchor(" ");

    anchor.getElement().getStyle().setWidth(1, Unit.PX);
    anchor.getElement().getStyle().setHeight(1, Unit.PX);
    anchor.getElement().getStyle().setDisplay(Display.INLINE);
    anchor.getElement().getStyle().setFloat(Float.LEFT);

    RootPanel.get().add(anchor);
    anchor.setFocus(true);

    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

      @Override
      public void execute() {
        anchor.removeFromParent();

      }
    });

  }

  public static void showAddressBar(boolean s) {
    if (s) {
      getAddressBarUtil().showAddressBar();
    } else {
      getAddressBarUtil().hideAddressBar();
    }
  }

  private static AddressBarUtil getAddressBarUtil() {
    if (addressBarUtil == null) {
      addressBarUtil = GWT.create(AddressBarUtil.class);
    }
    return addressBarUtil;
  }

  private static EventBus getManager() {
    if (manager == null) {
      manager = new SimpleEventBus();
    }
    return manager;
  }
  
  public static ORIENTATION getOrientation(){
	  return getOrientationHandler().getOrientation();
  }

}
