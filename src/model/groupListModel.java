package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.DefaultListModel;

import bean.musicInfoBean;
import factory.mp3Tagger;

/* 음악 재생목록을 위한 ListModel */
public class groupListModel extends DefaultListModel{
	private LinkedList<Integer> originId = new LinkedList<Integer>();
	
	public groupListModel() {
		super();
	}
	
	@Override
	public void addElement(Object element) {
		HashMap<Integer, String> data = (HashMap<Integer, String>) element;
		
		Set<Integer> keySet = data.keySet();
		for(int key : keySet) {
			originId.add(key);
			String mData = (String) data.get(key);
			super.addElement(mData);
		}
	}
	
	public int getElementDataAt(int index) {
		return originId.get(index);
	}
	
	@Override
	public void removeElementAt(int index) {
		originId.remove(index);
		super.removeElementAt(index);
	}
	
	@Override
		public void removeAllElements() {
			originId = new LinkedList<Integer>();
			super.removeAllElements();
		}
}
