package com.googlecode.mgwt.ui.client.brainfuse;


import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.history.HistoryHandler;
import com.googlecode.mgwt.mvp.client.history.PlaceReplaceEvent;

/**
 * add updatePlace feature to the original GWT PlaceController
 */
public class BfPlaceController extends PlaceController {

	private static final Logger log = Logger
			.getLogger(BfPlaceController.class.getName());

	private EventBus eventBus;
	private Delegate delegate;
	private Place where = Place.NOWHERE;

	// for the super class
	private static Delegate dummyDelegate = new Delegate() {
		@Override
		public boolean confirm(String message) {
			return false;
		}

		@Override
		public HandlerRegistration addWindowClosingHandler(
				ClosingHandler handler) {
			return null;
		}
	};

	public BfPlaceController(EventBus eventBus) {
		this(eventBus, (Delegate) GWT.create(DefaultDelegate.class));
	}

	public BfPlaceController(com.google.gwt.event.shared.EventBus eventBus) {
		this((EventBus) eventBus);
	}

	public BfPlaceController(EventBus eventBus, Delegate delegate) {
		super(eventBus, dummyDelegate);
		this.eventBus = eventBus;
		this.delegate = delegate;
		delegate.addWindowClosingHandler(new ClosingHandler() {
			public void onWindowClosing(ClosingEvent event) {
				String warning;
				try {
					warning = maybeGoTo(Place.NOWHERE);
				} catch (VetoException e) {
					return;
				}
				if (warning != null) {
					event.setMessage(warning);
				}
			}
		});
	}

	private String maybeGoTo(Place newPlace) throws VetoException {
		VetoPlaceChangeRequestEvent willChange = new VetoPlaceChangeRequestEvent(
				newPlace);
		eventBus.fireEvent(willChange);
		if(willChange.isVetoed())
			throw new VetoException();
		String warning = willChange.getWarning();
		return warning;
	}
	
	public static class VetoException extends Exception{
		
	}

	@Override
	public Place getWhere() {
		return where;
	}

	public void updatePlace(HistoryHandler historyHandler,Place place) {
		
		if (place == null || getWhere().equals(place)) {
			return;
		}
		
		this.where = place;
		if(historyHandler != null){
			historyHandler.replaceCurrentPlace(place);
		}
	}
	
	public void replacePlace(HistoryHandler historyHandler,Place place) {
		
		if (place == null || getWhere().equals(place)) {
			return;
		}
		
		this.where = place;
		if(historyHandler != null){
			eventBus.fireEvent(new PlaceReplaceEvent(place));
		}
	}

	Logger log() {
		return log;
	}

	public void goTo(Place newPlace) {
		log().fine("goTo: " + newPlace);

		if (getWhere().equals(newPlace)) {
			log().fine("Asked to return to the same place: " + newPlace);
			return;
		}

		String warning;
		try {
			warning = maybeGoTo(newPlace);
		} catch (VetoException e) {
			return;
		}
		if (warning == null || delegate.confirm(warning)) {
			where = newPlace;
			eventBus.fireEvent(new PlaceChangeEvent(newPlace));
		}
	}

}
