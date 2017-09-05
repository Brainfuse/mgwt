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

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.mgwt.dom.client.event.animation.TransitionEndEvent;
import com.googlecode.mgwt.dom.client.event.animation.TransitionEndHandler;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationEndCallback;
import com.googlecode.mgwt.mvp.client.resources.AnimationSelector;
import com.googlecode.mgwt.mvp.client.resources.TransistionCss;

/**
 * Considered internal
 * 
 * @author Daniel Kurka
 * @version $Id: $
 */
public class AnimatableDisplayTransistionImpl extends AnimatableDisplayCommonImpl {

	protected boolean lastDir;

	protected final TransistionCss css;

	/**
	 * <p>
	 * Constructor for AnimatableDisplayTransistionImpl.
	 * </p>
	 */
	public AnimatableDisplayTransistionImpl() {
		this(AnimationSelector.getBundle().transitionCss());
	}

	/**
	 * <p>
	 * Constructor for AnimatableDisplayTransistionImpl.
	 * </p>
	 * 
	 * @param css a
	 *            {@link com.googlecode.mgwt.mvp.client.resources.TransistionCss}
	 *            object.
	 */
	public AnimatableDisplayTransistionImpl(TransistionCss css) {

		this.css = css;
		css.ensureInjected();

		main = new FlowPanel() {
			protected void onDetach() {
				super.onDetach();
				AnimatableDisplayTransistionImpl.this.onDetach();
			};
		};

		main.setStylePrimaryName(this.css.display());

		first = new SimplePanel();
		first.addStyleName(this.css.displayContainer());

		second = new SimplePanel();
		second.addStyleName(this.css.displayContainer());

		listener = new TransistionEndListener();

		main.add(first);

		main.add(second);

	}

	protected void onDetach() {
		if (animationRunning) {
			onAnimationEnd();
		}

	}

	protected class TransistionEndListener implements TransitionEndHandler {

		@Override
		public void onTransitionEnd(TransitionEndEvent event) {
			AnimatableDisplayTransistionImpl.this.onAnimationEnd();

		}

	}

	/**
	 * <p>
	 * removeAllStyles
	 * </p>
	 */
	protected void removeAllStyles() {

		first.removeStyleName(this.css.in());
		first.removeStyleName(this.css.out());
		first.removeStyleName(this.css.reverse());
		first.removeStyleName(this.css.start());
		first.removeStyleName(this.css.end());

		first.removeStyleName(this.css.dissolve());
		first.removeStyleName(this.css.fade());
		first.removeStyleName(this.css.flip());
		first.removeStyleName(this.css.pop());
		first.removeStyleName(this.css.slide());
		first.removeStyleName(this.css.slideup());
		first.removeStyleName(this.css.swap());

		second.removeStyleName(this.css.in());
		second.removeStyleName(this.css.out());
		second.removeStyleName(this.css.reverse());
		second.removeStyleName(this.css.start());
		second.removeStyleName(this.css.end());

		second.removeStyleName(this.css.dissolve());
		second.removeStyleName(this.css.fade());
		second.removeStyleName(this.css.flip());
		second.removeStyleName(this.css.pop());
		second.removeStyleName(this.css.slide());
		second.removeStyleName(this.css.slideup());
		second.removeStyleName(this.css.swap());

	}

	protected HandlerRegistration animationEnd;

	protected TransistionEndListener listener;

	private boolean animationRunning;

	private Timer onTransitionEndTimer;

	/**
	 * <p>
	 * blurBeforeAnimation
	 * </p>
	 */
	protected native void blurBeforeAnimation() /*-{
		var node = $doc.querySelector(":focus");

		if (node != null) {
			if (typeof (node.blur) == "function") {
				node.blur();
			}

		}
	}-*/;

	/** {@inheritDoc} */
	@Override
	public void doAnimate(Animation animation, boolean currentIsFirst, AnimationEndCallback callback) {

		blurBeforeAnimation();

		if (animation == null) {
			if (showFirst) {
				first.getElement().getStyle().setDisplay(Display.BLOCK);

			} else {
				second.getElement().getStyle().setDisplay(Display.BLOCK);
			}
			onAnimationEnd();
			return;
		}

		animationRunning = true;

		String type = animation.getCssName();

		if (animationEnd != null) {
			animationEnd.removeHandler();
			animationEnd = null;
		}

		animationEnd = main.addDomHandler(listener, TransitionEndEvent.getType());

		first.addStyleName(css.start());
		second.addStyleName(css.start());

		first.addStyleName(type);
		second.addStyleName(type);

		lastDir = animation.isInverse();
		// backwards
		if (animation.isInverse()) {
			first.addStyleName(this.css.reverse());
			second.addStyleName(this.css.reverse());

		}
		if (currentIsFirst) {
			first.addStyleName(this.css.in());
			second.addStyleName(this.css.out());

		} else {
			first.addStyleName(this.css.out());
			second.addStyleName(this.css.in());

		}

		first.getElement().getStyle().setDisplay(Display.BLOCK);
		second.getElement().getStyle().setDisplay(Display.BLOCK);

		new Timer() {

			@Override
			public void run() {
				first.removeStyleName(css.start());
				second.removeStyleName(css.start());
				first.addStyleName(css.end());
				second.addStyleName(css.end());

			}
		}.schedule(10);
		/**
		 * This done due to a bug on Android sometimes does not fire transictionEnd event
		 * 
		 */
		this.onTransitionEndTimer = new Timer() {

			@Override
			public void run() {
				onAnimationEnd();

			}
		};
		this.onTransitionEndTimer.schedule(400);

	}

	/**
	 * <p>
	 * onAnimationEnd
	 * </p>
	 */
	protected void onAnimationEnd() {
		animationRunning = false;
		if ( this.onTransitionEndTimer!= null){
			this.onTransitionEndTimer.cancel();
			this.onTransitionEndTimer = null;
		}
		if (showFirst) {

			second.getElement().getStyle().setDisplay(Display.NONE);
			second.clear();

		} else {

			first.getElement().getStyle().setDisplay(Display.NONE);
			first.clear();

		}
		removeAllStyles();

		if (animationEnd != null) {
			animationEnd.removeHandler();
			animationEnd = null;
		}

		if (lastCallback != null) {
			lastCallback.onAnimationEnd();
			lastCallback = null;
		}
	}

}
