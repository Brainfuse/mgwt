package com.googlecode.mgwt.mvp.client.history;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;

public class PlaceReplaceEvent extends PlaceChangeEvent {

	public PlaceReplaceEvent(Place newPlace) {
		super(newPlace);
	}

}
