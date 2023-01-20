package model;

import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import bean.hintBean;

public class hintComboModel extends DefaultComboBoxModel {
	private LinkedList<hintBean> hintData = new LinkedList<hintBean>(); 
	
	public hintComboModel() {
		
	}
	
	@Override
	public void addElement(Object element) {
		hintBean data = (hintBean) element;
		hintData.add(data);
		
		super.addElement(data.getQuestion());
	}
	
	public hintBean getElementDataAt(int index) {
		return hintData.get(index);
	}
	
	@Override
	public void removeElementAt(int index) {
		hintData.remove(index);
		super.removeElementAt(index);
	}

}
