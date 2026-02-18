package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.mgwt.collection.shared.CollectionFactory;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.JavaTouch;
import com.googlecode.mgwt.dom.client.event.touch.Touch;

/**
 * Manages active pointer state to simulate multi-touch behavior from
 * individual pointer events. Pointer events are per-pointer (one event
 * per pointer), while touch events provide arrays of all active touches.
 * This manager bridges that gap by tracking all active pointers and
 * building touch arrays that match the native touch event model.
 * 
 * <p>Usage:
 * <ul>
 *   <li>{@link #pointerDown(int, int, int)} — call on pointerdown</li>
 *   <li>{@link #pointerMove(int, int, int)} — call on pointermove</li>
 *   <li>{@link #pointerUp(int, int, int)} — call on pointerup</li>
 *   <li>{@link #pointerCancel(int)} — call on pointercancel</li>
 * </ul>
 * 
 * <p>After each call, use {@link #getTouches()} to get all active pointers
 * and {@link #getChangedTouches()} to get the pointer that triggered the event,
 * mirroring the native TouchEvent API.
 */
public class PointerTouchManager {

	/**
	 * Represents a tracked pointer with its current position and id.
	 */
	private static class PointerInfo {
		final int pointerId;
		int pageX;
		int pageY;

		PointerInfo(int pointerId, int pageX, int pageY) {
			this.pointerId = pointerId;
			this.pageX = pageX;
			this.pageY = pageY;
		}
	}

	/** Active pointers keyed by pointerId */
	private final Map<Integer, PointerInfo> activePointers = new HashMap<Integer, PointerInfo>();

	/** The pointer that triggered the most recent event */
	private LightArray<Touch> changedTouches;

	/** All currently active pointers */
	private LightArray<Touch> touches;

	/**
	 * Called when a pointer goes down. Adds the pointer to the active set.
	 * 
	 * @param pointerId the pointer's unique id
	 * @param pageX x coordinate
	 * @param pageY y coordinate
	 */
	public void pointerDown(int pointerId, int pageX, int pageY) {
		PointerInfo info = new PointerInfo(pointerId, pageX, pageY);
		activePointers.put(pointerId, info);
		buildTouchArrays(info);
	}

	/**
	 * Called when a pointer moves. Updates the pointer's position.
	 * 
	 * @param pointerId the pointer's unique id
	 * @param pageX new x coordinate
	 * @param pageY new y coordinate
	 */
	public void pointerMove(int pointerId, int pageX, int pageY) {
		PointerInfo info = activePointers.get(pointerId);
		if (info == null) {
			// Pointer not tracked (e.g., move without a prior down). Ignore.
			return;
		}
		info.pageX = pageX;
		info.pageY = pageY;
		buildTouchArrays(info);
	}

	/**
	 * Called when a pointer goes up. The pointer is included in changedTouches
	 * but removed from touches (all active).
	 * 
	 * @param pointerId the pointer's unique id
	 * @param pageX final x coordinate
	 * @param pageY final y coordinate
	 */
	public void pointerUp(int pointerId, int pageX, int pageY) {
		PointerInfo info = activePointers.remove(pointerId);
		if (info == null) {
			info = new PointerInfo(pointerId, pageX, pageY);
		} else {
			info.pageX = pageX;
			info.pageY = pageY;
		}
		// changedTouches = the pointer that went up
		changedTouches = CollectionFactory.constructArray();
		changedTouches.push(toTouch(info));
		// touches = remaining active pointers (the ended one is already removed)
		touches = buildAllTouches();
	}

	/**
	 * Called when a pointer is cancelled.
	 * 
	 * @param pointerId the pointer's unique id
	 */
	public void pointerCancel(int pointerId) {
		PointerInfo info = activePointers.remove(pointerId);
		if (info != null) {
			changedTouches = CollectionFactory.constructArray();
			changedTouches.push(toTouch(info));
		} else {
			changedTouches = CollectionFactory.constructArray();
		}
		touches = buildAllTouches();
	}

	/**
	 * Returns all currently active touches. Mirrors TouchEvent.touches.
	 */
	public LightArray<Touch> getTouches() {
		return touches;
	}

	/**
	 * Returns the touch(es) that changed in the most recent event.
	 * Mirrors TouchEvent.changedTouches.
	 */
	public LightArray<Touch> getChangedTouches() {
		return changedTouches;
	}

	/**
	 * Returns the number of currently active pointers.
	 */
	public int getActivePointerCount() {
		return activePointers.size();
	}

	/**
	 * Returns true if the given pointerId is currently being tracked.
	 */
	public boolean isTracking(int pointerId) {
		return activePointers.containsKey(pointerId);
	}

	/**
	 * Clears all tracked pointers. Useful for reset scenarios.
	 */
	public void clear() {
		activePointers.clear();
		touches = CollectionFactory.constructArray();
		changedTouches = CollectionFactory.constructArray();
	}

	/**
	 * Build both touches and changedTouches arrays for down/move events
	 * where the triggering pointer is still active.
	 */
	private void buildTouchArrays(PointerInfo changed) {
		changedTouches = CollectionFactory.constructArray();
		changedTouches.push(toTouch(changed));
		touches = buildAllTouches();
	}

	/**
	 * Build the array of all active pointers.
	 */
	private LightArray<Touch> buildAllTouches() {
		LightArray<Touch> all = CollectionFactory.constructArray();
		for (PointerInfo pi : activePointers.values()) {
			all.push(toTouch(pi));
		}
		return all;
	}

	/**
	 * Convert a PointerInfo to a Touch.
	 */
	private Touch toTouch(PointerInfo info) {
		return new JavaTouch(info.pageX, info.pageY, info.pointerId);
	}
}
