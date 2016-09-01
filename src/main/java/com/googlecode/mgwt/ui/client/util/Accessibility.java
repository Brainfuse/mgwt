package com.googlecode.mgwt.ui.client.util;

public class Accessibility {

	public enum Selected {
		SELECTED("aria-selected"),
		CHECKED("aria-checked"),
		NOT_SELECTABLE("");
	
		private String text;
		Selected(String text){
			this.text = text;
		}
		
		public String getString(){
			return text;
		}
	}
	
	public enum ChildRole{
		OPTION("option"),
		RADIO("radio"),
		LIST_ITEM("listitem");
		
		private String text;

		ChildRole(String text) {
			this.text = text;
		}

		public String getString() {
			return text;
		}
		
	}
	
	public enum Role {
		
		LIST_BOX("listbox", Selected.SELECTED, ChildRole.OPTION),
		RADIO_GROUP("radiogroup", Selected.CHECKED, ChildRole.RADIO),
		LIST("list", Selected.NOT_SELECTABLE, ChildRole.LIST_ITEM);
		
		private String role;
		private Selected selected;
		private ChildRole childRole;
		
		Role(String role, Selected selected, ChildRole childRole){
			this.role = role;
			this.selected = selected;
			this.childRole = childRole;
		}
		
		public String getRole(){
			return role;
		}
		
		public String getSelected(){
			return selected.getString();
		}
		
		public String getChildRole(){
			return childRole.getString();
		}
		
	}
	
}
