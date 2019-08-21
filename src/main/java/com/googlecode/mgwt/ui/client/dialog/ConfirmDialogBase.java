package com.googlecode.mgwt.ui.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.mvp.client.MGWTAnimationEndHandler;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.ButtonBaseCss;
import com.googlecode.mgwt.ui.client.theme.base.DialogCss;
import com.googlecode.mgwt.ui.client.widget.base.ButtonBase;

/**
 * A simple confirm dialog with ok and cancel buttons
 * 
 * @author Daniel Kurka
 */
public class ConfirmDialogBase implements HasText, HasTitleText, Dialog{

	  public interface ConfirmDialogCallback {
			void onConfirm(int button);
	  }

	  private PopinDialog popinDialog;
	  private DialogPanel dialogPanel1;
	  private Label textLabel;
	  private ConfirmDialogCallback callback;
	  private List<ButtonBase> buttons;

	  
	  public ConfirmDialogBase(String title, String text, ConfirmDialogCallback callback, String[] labels) {
		  this(MGWTStyle.getTheme().getMGWTClientBundle().getDialogCss(), title, text, callback,labels);
	  }
	
	  public ConfirmDialogBase(DialogCss css, String title, String text, ConfirmDialogCallback callback, String[] labels) {
		this.buttons = new ArrayList<ButtonBase>();
	    this.callback = callback;
	    popinDialog = new PopinDialog(css);
	    dialogPanel1 = new DialogPanel(css);

	    textLabel = new Label();
	    dialogPanel1.getContent().add(textLabel);
	    popinDialog.add(dialogPanel1);
	    
	    setText(text);
	    setTitleText(title);

	    dialogPanel1.setButtons(labels);
	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see com.googlecode.mgwt.ui.client.dialog.HasTitleText#setTitleText(java.lang.String)
	   */
	  /** {@inheritDoc} */
	  @Override
	  public void setTitleText(String title) {
	    dialogPanel1.getDialogTitle().setHTML(title);

	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
	   */
	  /** {@inheritDoc} */
	  @Override
	  public void setText(String text) {
	    textLabel.setText(text);

	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see com.googlecode.mgwt.ui.client.dialog.HasTitleText#getTitleText()
	   */
	  /** {@inheritDoc} */
	  @Override
	  public String getTitleText() {
	    return dialogPanel1.getDialogTitle().getHTML();
	  }

	  /*
	   * (non-Javadoc)
	   * 
	   * @see com.google.gwt.user.client.ui.HasText#getText()
	   */
	  /** {@inheritDoc} */
	  @Override
	  public String getText() {
	    return textLabel.getText();
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
	    popinDialog.center();
	  }

	  @Override
	  public void hide() {
	    popinDialog.hide();

	  }
	  
	  public DialogPanel getDialogPanel(){
		  return this.dialogPanel1;
	  }
	  
	  public HandlerRegistration addAnimationEndHandler(MGWTAnimationEndHandler handler){
		  return popinDialog.addAnimationEndHandler(handler);
	  }
	  
	  public HasHTML getDialogTitle() {
		    return dialogPanel1.getDialogTitle();
	  } 
	  
	  public List<ButtonBase> getButtons(){
		  return buttons;
	  }
	  
	private class DialogPanel extends Composite {
		private FlowPanel main;
		private FlowPanel container;
		private HTML title;
		private FlowPanel content;
		private FlowPanel buttonContainer;
		private final DialogCss css;

		public DialogPanel() {
			this(MGWTStyle.getTheme().getMGWTClientBundle().getDialogCss());
		}

		public DialogPanel(DialogCss css) {
			this.css = css;
			this.css.ensureInjected();
			main = new FlowPanel();
			initWidget(main);

			main.addStyleName(css.getDialogPanel());

			container = new FlowPanel();
			container.addStyleName(css.container());

			main.add(container);

			title = new HTML();
			title.addStyleName(css.title());
			container.add(title);

			content = new FlowPanel();
			content.addStyleName(css.content());
			container.add(content);

			buttonContainer = new FlowPanel();
			buttonContainer.addStyleName(css.footer());
			container.add(buttonContainer);
		}
		
		public void setButtons(String[] labels) {
			for(int i=0; i<labels.length; i++) {
				String label = labels[i];
				DButton button = new DButton(css,label);
				button.addStyleName(css.okbutton());
				buttonContainer.add(button);
				addButtonHandler(button, i);
				buttons.add(button);
				
			}
		}
		
		private void addButtonHandler(DButton b, final int index) {
			b.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
				    popinDialog.hide();
					callback.onConfirm(index);
				}
			});
		}
		
		public HasHTML getDialogTitle() {
			return title;
		}
		
		public HasWidgets getContent() {
		    return content;
		}

	}
	  
	private static class DButton extends ButtonBase {
		public DButton(ButtonBaseCss css, String text) {
			super(css);
			setText(text);
		}
	}

}
