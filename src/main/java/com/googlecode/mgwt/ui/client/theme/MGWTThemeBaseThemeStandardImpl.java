package com.googlecode.mgwt.ui.client.theme;

import com.google.gwt.core.client.GWT;
import com.googlecode.mgwt.ui.client.theme.base.MGWTClientBundleBaseTheme;

/**
 * Standard mgwt theme implementation.
 *
 * <p>Historically this class branched on device type (Android, iPhone, iPad,
 * Retina, Blackberry, Desktop, …) and instantiated a different
 * {@code MGWTClientBundleBaseTheme*} for each. Now that all supported targets
 * (modern desktop browsers, Chromium-based Cordova WebViews, and Electron) can
 * share a single baseline stylesheet, the dispatch collapses to a single
 * {@link GWT#create(Class)} call.
 */
public class MGWTThemeBaseThemeStandardImpl implements MGWTTheme {

  private final MGWTClientBundle bundle;

  public MGWTThemeBaseThemeStandardImpl() {
    bundle = GWT.create(MGWTClientBundleBaseTheme.class);
  }

  @Override
  public MGWTClientBundle getMGWTClientBundle() {
    return bundle;
  }

}
