package com.googlecode.mgwt.ui.client.dialog;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.mvp.client.MGWTAnimationEndHandler;
import com.googlecode.mgwt.ui.client.dialog.ConfirmDialog.ConfirmCallback;
import com.googlecode.mgwt.ui.client.theme.base.DialogCss;

/**
 * a confirmDialog with ios7 looking
 */
public class BfConfirmDialog extends Widget{
	
	private ConfirmDialog dialog;
	private int timeout;
	private Timer timer;
	private boolean isSetTimeout=false;
	DialogCss css = BfConfirmDialogBundle.INSTANCE.getQuitDialogCss();
	
	/**
	 * using this constructor to show both OK and cancel buttons
	 * @param title
	 * @param message
	 * @param callBack
	 */
	public BfConfirmDialog(String title,String message,ConfirmCallback callBack){
		dialog = new ConfirmDialog(css, title, message, createCallback(callBack));
	}
	
	public BfConfirmDialog(String title, String text, ConfirmCallback callback, String okButtonText,
			String cancelButtonText) {
		dialog = new ConfirmDialog(css, title, text, callback, okButtonText, cancelButtonText);
	}
	
	/**
	 * using this constructor, the dialog will ONLY show the OK button
	 * and this will hide the dialog once it is timeout.
	 * @param title
	 * @param message
	 * @param timeout
	 * @param callBack
	 */
	public BfConfirmDialog(String title,String message,int timeout, ConfirmCallback callBack){
		setTimeoutInSeconds(timeout);
		dialog = new ConfirmDialog(css, title, message, createCallback(callBack));
		dialog.getDialogPanel().showCancelButton(false);
	}
	
	private ConfirmCallback createCallback(final ConfirmCallback callBack){
		
		return new ConfirmCallback(){

			@Override
			public void onOk() {
				cancelTimer();
				callBack.onOk();
			}

			@Override
			public void onCancel() {
				cancelTimer();
				callBack.onCancel();
			}
			
		};
		
	}
	
	/**
	 * timeout in seconds to hide the confirm dialog
	 */
	public void setTimeoutInSeconds(int sec){
		this.timeout = sec;
		isSetTimeout = true;
	}

	public void show(){
		
		if(isSetTimeout){
			
			cancelTimer();
			
			timer = new Timer(){
				@Override
				public void run() {
					dialog.hide();
				}
			};
			timer.schedule( timeout * 1000 );
		}
		
		dialog.show();
	}

	private void cancelTimer() {
		if (timer!=null && timer.isRunning()){
			timer.cancel();
		}
	}
	
	public void hide(){
		dialog.hide();
	}
	
	@Override
	protected void onDetach() {
		cancelTimer();
		super.onDetach();
	}
	
	
	public HandlerRegistration addAnimationEndHandler(MGWTAnimationEndHandler handler){
		return dialog.addAnimationEndHandler(handler);
	}
	
	public interface BfConfirmDialogBundle extends ClientBundle{
		
		@Source("com/googlecode/mgwt/ui/client/theme/base/css/quitDialog.css")
		DialogCss getQuitDialogCss();
		
		public static final BfConfirmDialogBundle INSTANCE = GWT.create(BfConfirmDialogBundle.class);
	}



}


