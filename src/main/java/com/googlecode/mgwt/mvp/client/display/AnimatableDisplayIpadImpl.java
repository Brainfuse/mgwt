/*
 * Copyright 2011 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.mgwt.mvp.client.display;

import com.google.gwt.user.client.ui.Widget;

/**
 * Considered internal
 * 
 * @author Daniel Kurka
 * @version $Id: $
 */
public class AnimatableDisplayIpadImpl extends AnimatableDisplayIphoneImpl {
	
	/**
	 * 
	 */
	@Override
	protected void setZIndex(int zIndexValue) {
		Widget widgetToSet = null;
		if ( showFirst){
			if (!isReverse) {
				widgetToSet = second;
				
			} else {
				widgetToSet = first;
			}
			
		}else{
			if (!isReverse) {
				widgetToSet =  first;
			} else {
				widgetToSet = second;
			}
		}
		widgetToSet.getElement().getStyle().setZIndex(zIndexValue);
	}
}
