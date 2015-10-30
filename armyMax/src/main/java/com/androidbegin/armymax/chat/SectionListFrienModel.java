package com.androidbegin.armymax.chat;

public class SectionListFrienModel implements FrienListModel{
	private String txtSection;
	public SectionListFrienModel(String txtSection) {
		// TODO Auto-generated constructor stub
		this.txtSection = txtSection;
	}
	public String getTxtSection() {
		return txtSection;
	}
	public void setTxtSection(String txtSection) {
		this.txtSection = txtSection;
	}
	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
