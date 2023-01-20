package model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultListModel;

import bean.musicInfoBean;
import bean.userBean;
import factory.mp3Tagger;

/* 사용자 목록을 위한 ListModel */
public class userListModel extends DefaultListModel{
	private LinkedList<userBean> userData = new LinkedList<userBean>();
	
	public userListModel() {
		super();
	}
	
	@Override
	public void addElement(Object element) {
		userBean data = (userBean) element;
		userData.add(data);	
		
		super.addElement(data.getUserName() + " [" + data.getUserId() + "]");
	}
	
	@Override
	public void insertElementAt(Object element, int index) {
		userBean data = (userBean) element;
		userData.add(index, data);	
		
		super.insertElementAt(data.getUserName() + " [" + data.getUserId() + "]", index);
	}	
	
	public LinkedList<userBean> getElementDataAll(){
		return userData;
	}
	
	public userBean getElementDataAt(int index) {
		return userData.get(index);
	}
	
	@Override
	public void removeElementAt(int index) {
		userData.remove(index);
		super.removeElementAt(index);
	}
	
	@Override
		public void removeAllElements() {
			userData = new LinkedList<userBean>();
			super.removeAllElements();
		}
}
