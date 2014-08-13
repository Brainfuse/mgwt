package com.googlecode.mgwt.ui.client.theme.ios7;

import com.google.gwt.resources.client.ClientBundle.Source;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;
import com.googlecode.mgwt.ui.client.theme.base.ButtonBarButtonCss;
import com.googlecode.mgwt.ui.client.theme.base.ButtonBarCss;
import com.googlecode.mgwt.ui.client.theme.base.ButtonCss;
import com.googlecode.mgwt.ui.client.theme.base.CarouselCss;
import com.googlecode.mgwt.ui.client.theme.base.CheckBoxCss;
import com.googlecode.mgwt.ui.client.theme.base.DialogCss;
import com.googlecode.mgwt.ui.client.theme.base.HeaderCss;
import com.googlecode.mgwt.ui.client.theme.base.ListCss;
import com.googlecode.mgwt.ui.client.theme.base.MainCss;
import com.googlecode.mgwt.ui.client.theme.base.SliderCss;

public interface MGWTBundleBaseIOS7 extends MGWTClientBundle{

	
	@Source({"com/googlecode/mgwt/ui/client/theme/base/css/buttonbar.css","css/buttonbar.css"})
	ButtonBarCss getButtonBarCss();
	
	@Source({"com/googlecode/mgwt/ui/client/theme/base/css/buttonbarbutton.css","css/buttonbarbutton.css"})
	ButtonBarButtonCss getButtonBarButtonCss();
	
	@Source({"com/googlecode/mgwt/ui/client/theme/base/css/buttons.css","css/buttons.css"})
	ButtonCss getButtonCss();
	/* "com/googlecode/mgwt/ui/client/theme/base/css/dialog.css",*/
	@Source({"css/dialog.css" })
    DialogCss getDialogCss();
	
	@Source({"css/header.css" })
    HeaderCss getHeaderCss();

	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/list.css"
		,"css/list.css"})
	ListCss getListCss();
	
	@Source({ "com/googlecode/mgwt/ui/client/theme/base/css/main.css","css/main.css" })
    MainCss getMainCss();
	
	@Source({"css/checkbox.css"})
	CheckBoxCss getCheckBoxCss();
	
	@Source({"com/googlecode/mgwt/ui/client/theme/base/css/slider.css","css/slider.css"})
	SliderCss getSliderCss();
	
	@Source({"com/googlecode/mgwt/ui/client/theme/base/css/carousel.css","css/carousel.css"})
	CarouselCss getCarouselCss();

}
