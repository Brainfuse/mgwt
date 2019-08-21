/*
 * Copyright 2010 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.mgwt.ui.client.dialog;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.googlecode.mgwt.mvp.client.MGWTAnimationEndHandler;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.dialog.ConfirmDialogBase.ConfirmDialogCallback;
import com.googlecode.mgwt.ui.client.theme.base.DialogCss;

/**
 * A simple confirm dialog with ok and cancel buttons
 * 
 * @author Daniel Kurka
 */
public class ConfirmDialog implements HasText, HasTitleText, Dialog {
  /**
   * The callback used when buttons are taped
   * 
   * @author Daniel Kurka
   * 
   */
  public interface ConfirmCallback {
    /**
     * Called if ok button is taped
     */
    public void onOk();

    /**
     * called if cancel button is taped
     */
    public void onCancel();
  }

  private ConfirmDialogBase dialog;
  
  /**
   * Construct a Confirmdialg
   * 
   * @param title - the title of the dialog
   * @param text - the text of the dialog
   * @param callback - the callback used when a button of the dialog is taped
   */
  public ConfirmDialog(String title, String text, ConfirmCallback callback) {
    this(MGWTStyle.getTheme().getMGWTClientBundle().getDialogCss(), title, text, callback);
  }

  /**
   * Construct a Confirmdialg
   * 
   * @param css . css to use
   * @param title - the title of the dialog
   * @param text - the text of the dialog
   * @param callback - the callback used when a button of the dialog is taped
   */
  public ConfirmDialog(DialogCss css, String title, String text, ConfirmCallback callback) {
    this(css, title, text, callback, "OK", "Cancel");
  }

  /**
   * Construct a Confirmdialg
   * 
   * @param css . css to use
   * @param title - the title of the dialog
   * @param text - the text of the dialog
   * @param callback - the callback used when a button of the dialog is taped
   */
  public ConfirmDialog(DialogCss css, String title, String text, final ConfirmCallback callback, String okButtonText, String cancelButtonText) {
		String[] buttons = new String[] { okButtonText, cancelButtonText };
		this.dialog = new ConfirmDialogBase(css, title, text,
				new ConfirmDialogCallback() {

					@Override
					public void onConfirm(int button) {
						switch(button) {
						case 0:
							callback.onOk();
							break;
						case 1:
							callback.onCancel();
							break;
						}
					}
				}, buttons);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.googlecode.mgwt.ui.client.dialog.HasTitleText#setTitleText(java.lang.String)
   */
  /** {@inheritDoc} */
  @Override
  public void setTitleText(String title) {
    dialog.setTitleText(title);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  /** {@inheritDoc} */
  @Override
  public void setText(String text) {
    dialog.setText(text);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.googlecode.mgwt.ui.client.dialog.HasTitleText#getTitleText()
   */
  /** {@inheritDoc} */
  @Override
  public String getTitleText() {
	  return dialog.getDialogTitle().getHTML();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  /** {@inheritDoc} */
  @Override
  public String getText() {
	  return dialog.getText();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.googlecode.mgwt.ui.client.dialog.Dialog#show()
   */
  /**
   * <p>
   * show
   * </p>
   */
  public void show() {
    dialog.show();
  }

  @Override
  public void hide() {
    dialog.hide();
  }
  
  public void showCancelButton(boolean show) {
	dialog.getButtons().get(1).setVisible(show);
  }
  
  public HandlerRegistration addAnimationEndHandler(MGWTAnimationEndHandler handler){
	  return dialog.addAnimationEndHandler(handler);
  }
  

}
