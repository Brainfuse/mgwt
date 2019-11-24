package com.googlecode.mgwt.ui.client.util;

public class Browser {

	protected Browser() {
	}
	
	private static String browserName = null;
	
	public static boolean isChrome() {
		return getName().equals("chrome");
	}
	
	public static boolean isOpera() {
		return getName().equals("opera");
	}
	
	public static boolean isFirefox() {
		return getName().equals("firefox");
	}
	
	public static boolean isIE() {
		return getName().equals("ie");
	}
	
	public static boolean isEdge() {
		return getName().equals("edge");
	}
	
	public static boolean isSafari() {
		return getName().equals("safari");
	}
	
	private static String getName() {
		if(browserName == null)
			browserName = _getName();
		
		return browserName;
	}
	
	/*
	 * https://developer.mozilla.org/en-US/docs/Web/API/Window/navigator
	 */
	private static native String _getName() /*-{
		var sBrowser, sUsrAg = $wnd.navigator.userAgent;
		// The order matters here, and this may report false positives for unlisted browsers.
		
		if (sUsrAg.indexOf("Firefox") > -1) {
		  sBrowser = "firefox";
		  // "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:61.0) Gecko/20100101 Firefox/61.0"
		} else if (sUsrAg.indexOf("SamsungBrowser") > -1) {
		  sBrowser = "Samsung Internet";
		  // "Mozilla/5.0 (Linux; Android 9; SAMSUNG SM-G955F Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/9.4 Chrome/67.0.3396.87 Mobile Safari/537.36
		} else if (sUsrAg.indexOf("Opera") > -1 || sUsrAg.indexOf("OPR") > -1) {
		  sBrowser = "opera";
		  // "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106"
		} else if (sUsrAg.indexOf("Trident") > -1) {
		  sBrowser = "ie";
		  // "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; Zoom 3.6.0; wbx 1.0.0; rv:11.0) like Gecko"
		} else if (sUsrAg.indexOf("Edge") > -1) {
		  sBrowser = "edge";
		  // "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299"
		} else if (sUsrAg.indexOf("Chrome") > -1) {
		  sBrowser = "chrome";
		  // "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/66.0.3359.181 Chrome/66.0.3359.181 Safari/537.36"
		} else if (sUsrAg.indexOf("Safari") > -1) {
		  sBrowser = "safari";
		  // "Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1 980x1306"
		} else {
		  sBrowser = "unknown";
		}
		return sBrowser;
	}-*/;
	
	public static int getIEVersion() {
		if(isIE()) {
			return _getIEVersion();
		}else {
			return -1;
		}
	}
	
	private static native int _getIEVersion()/*-{
		var ua = $wnd.navigator.userAgent;
	    var ieold = (/MSIE (\d+\.\d+);/.test(ua));
	    if(ieold){
	    	return new Number(RegExp.$1);
	    }
	    if ($wnd.navigator.appVersion.indexOf("MSIE 10") != -1){
	     	return 10;
	    }
	    
	    var trident = !!ua.match(/Trident\/7.0/);
	    var rv=ua.indexOf("rv:11.0");
	    if (trident&&rv!=-1){
	    	return 11;
	    }
	
	    return -1;  
	}-*/;
	
}
