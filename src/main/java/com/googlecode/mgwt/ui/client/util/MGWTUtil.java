package com.googlecode.mgwt.ui.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.OsDetection;

public class MGWTUtil {
	
	private static Boolean isAndroidLegacyWebview;
	
	public static void forceFullRepaint(){
		OsDetection d = MGWT.getOsDetection();
		if(d.isAndroid()){
			final String zIndex = Document.get().getBody().getStyle().getZIndex();
			Document.get().getBody().getStyle().setZIndex(-1);
			
			new Timer(){

				@Override
				public void run() {
					Document.get().getBody().getStyle().setProperty("zIndex", zIndex);
					
				}
				
			}.schedule(50);
			
		}
	}
	
	public static native void log(String s)/*-{
		$wnd.console.log("MGWT Log: "+s);
	}-*/;
	
	public static native void log(String s, Object obj)/*-{
		$wnd.console.log("MGWT Log: "+s, obj);
	}-*/;

	
	/**
	 * This function is to test if the cssFeature is supported in the current browser
	 * return true if supported.
	 * can be used for old browser like ie9 where many css3 features are not supported
	 * @param css featurename ex. isCSSFeatureSupported("trainsition");
	 * 
	 */
	public static native boolean isCSSFeatureSupported(String featurename)/*-{
		var feature = false,
	    domPrefixes = 'Webkit Moz ms O'.split(' '),
	    elm = document.createElement('div'),
	    featurenameCapital = null;
	
	    featurename = featurename.toLowerCase();
	
	    if( elm.style[featurename] !== undefined ) { feature = true; } 
	
	    if( feature === false ) {
	        featurenameCapital = featurename.charAt(0).toUpperCase() + featurename.substr(1);
	        for( var i = 0; i < domPrefixes.length; i++ ) {
	            if( elm.style[domPrefixes[i] + featurenameCapital ] !== undefined ) {
	              feature = true;
	              break;
	            }
	        }
	    }
	    return feature; 
	}-*/;
	
	public static native boolean is_win_touch_device()/*-{
		 return (($wnd.navigator.maxTouchPoints > 0)||($wnd.navigator.msMaxTouchPoints > 0));
	}-*/;
	
	public static native boolean isChromeOnWindowTouchDevice()/*-{
		var user_agent = $wnd.navigator.userAgent;
		var exclude = /\b(Edge|Trident|android|iphone|ipod|ipad)\b/i.test(user_agent);
		var maxTouchPoints = $wnd.navigator.maxTouchPoints;
		if(!exclude && maxTouchPoints >=1){
			return true;
		}
		return false;
	}-*/;
	
	public static native boolean isPointerEventSupported()/*-{
		return $wnd.PointerEvent || $wnd.MSPointerEvent;
	}-*/;
	
	/**
	 * this is targeting all webkit browsers
	 */
	public static native boolean isSafari()/*-{
		var isSafari = /^((?!chrome|android).)*safari/i.test($wnd.navigator.userAgent);
		return isSafari;
	}-*/;
	
	public static boolean isAndroidWithLegacyWebview() {
		if(isAndroidLegacyWebview == null) {
			isAndroidLegacyWebview = _isAndroidWithLegacyWebview();
		}
		return isAndroidLegacyWebview;
	}
	
	private static native boolean _isAndroidWithLegacyWebview()/*-{
	  var ua = $wnd.navigator.userAgent.toLowerCase();
	    if(ua.indexOf('android') != -1) {
	    	var androidVersion = parseFloat(ua.match(/android\s+([\d\.]+)/)[1]);
	  		if(androidVersion < 4.4) {
	  			return true;
	  		}
	  	}
	  	return false;
	}-*/;
	
}
