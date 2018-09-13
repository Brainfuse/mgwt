package com.brainfuse.graphics.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeRequestEvent;

public class VetoPlaceChangeRequestEvent extends PlaceChangeRequestEvent {
	
	private boolean vetoed;
	
	public boolean isVetoed() {
		return vetoed;
	}

	public void setVetoed(boolean vetoed) {
		this.vetoed = vetoed;
	}

	public VetoPlaceChangeRequestEvent(Place newPlace) {
		super(newPlace);
	}

}
