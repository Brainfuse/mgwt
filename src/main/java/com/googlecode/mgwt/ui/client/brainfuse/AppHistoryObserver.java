package com.googlecode.mgwt.ui.client.brainfuse;


import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.mvp.client.history.HistoryHandler;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;

public class AppHistoryObserver implements HistoryObserver {
	
	private HistoryHandler mgwtHistoryHandler;

	public AppHistoryObserver() {}
	
	@Override
	public void onPlaceChange(Place place, HistoryHandler handler) {
		this.mgwtHistoryHandler = handler;
	}

	@Override
	public void onHistoryChanged(Place place, HistoryHandler handler) {
		this.mgwtHistoryHandler = handler;
	}

	@Override
	public void onAppStarted(Place place, HistoryHandler historyHandler) {
		this.mgwtHistoryHandler = historyHandler;
	}
	
	public HistoryHandler getMgwtHistoryHandler() {
		return mgwtHistoryHandler;
	}

	@Override
	public HandlerRegistration bind(EventBus eventBus, final HistoryHandler historyHandler) {
		HandlerRegistration register2 = ActionEvent.register(eventBus, ActionEvent.BACK, new ActionEvent.Handler() {

			@Override
			public void onAction(ActionEvent event) {

				History.back();

			}
		});

		HandlerRegistrationCollection col = new HandlerRegistrationCollection();
		col.addHandlerRegistration(register2);
		return col;
	}
}