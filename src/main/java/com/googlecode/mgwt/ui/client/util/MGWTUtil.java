package com.googlecode.mgwt.ui.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.OsDetection;

public class MGWTUtil {
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
}
