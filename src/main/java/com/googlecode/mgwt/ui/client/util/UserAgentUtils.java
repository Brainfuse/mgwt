package com.googlecode.mgwt.ui.client.util;

import com.google.gwt.user.client.Window;

public class UserAgentUtils {

	private static String userAgent;
	
	public static boolean isGecko(){
		return "gecko1_8".equals(getUserAgent());
	}
	
	public static boolean isSafari(){
		return "safari".equals(getUserAgent());
	}
	
	public static boolean isEdge() {
		return Window.Navigator.getUserAgent().toLowerCase().indexOf("edge") > -1;
	}
	
	public static String getUserAgent() {
		if(userAgent == null){
			userAgent = System.getProperty("user.agent").toLowerCase();
		}
		return userAgent;
	}
	
}
