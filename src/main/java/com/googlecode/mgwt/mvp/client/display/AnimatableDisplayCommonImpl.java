package com.googlecode.mgwt.mvp.client.display;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.mvp.client.AnimatableDisplay;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationEndCallback;

public abstract class AnimatableDisplayCommonImpl implements AnimatableDisplay {

	protected FlowPanel main;
	protected SimplePanel first;
	protected SimplePanel second;
	protected AnimationEndCallback lastCallback;
	protected boolean showFirst;
	
	@Override
	public void animate(Animation animation, boolean animateToFirst,
			AnimationEndCallback callback) {
		
		lastCallback = callback;
		showFirst = animateToFirst;
		
		if(!main.isAttached()){
			if(showFirst){
				first.getElement().getStyle().setDisplay(Display.BLOCK);
			}else {
				second.getElement().getStyle().setDisplay(Display.BLOCK);
			}
			onAnimationEnd();
			return;
		}
		
		doAnimate(animation, animateToFirst, callback);
		
	}
	
	@Override
	public Widget asWidget() {
		return main;
	}
	
	@Override
	public void setFirstWidget(IsWidget w) {
		first.setWidget(w);
	}
	
	@Override
	public void setSecondWidget(IsWidget w) {
		second.setWidget(w);
	}
	
	abstract void doAnimate(Animation animation, boolean animateToFirst,
			AnimationEndCallback callback);
	
	abstract void onAnimationEnd();
}
