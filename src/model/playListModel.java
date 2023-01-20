package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.DefaultListModel;

import bean.musicInfoBean;
import factory.mp3Tagger;

/* 음악 재생목록을 위한 ListModel */
public class playListModel extends DefaultListModel{
	private LinkedList<musicInfoBean> musicData = new LinkedList<musicInfoBean>();
	
	public playListModel() {
		super();
		for(int i = 0; i < 50; i++) {
			super.addElement(" ");
		}
	}
	
	@Override
	public void addElement(Object element) {
		this.insertElementAt(element, musicData.size());
	}
	
	public int getListSize() {
		return musicData.size();
	}
	
	@Override
	public void insertElementAt(Object element, int index) {
		musicInfoBean data = (musicInfoBean) element;
		/* 데이터에 중복 검사를 진행합니다 */
		int checkIndex = checkDuplicate(data);
		/* 데이터가 중복된 경우, 해당 데이터를 지우고 이동합니다 */
		if(checkIndex != -1) {
			musicData.remove(checkIndex);
			super.remove(checkIndex);
		}
		
		if(checkIndex != -1 && checkIndex < index) {
			index--;
		}
		
		musicData.add(index, data);	
		String musicName = data.getMusicName();
		if(data.getArtist() != null && !data.getArtist().equals("")) {
			musicName = musicName + " - " + data.getArtist();
		}
				
		super.insertElementAt(musicName, index);
	}	
	
	public int checkDuplicate(musicInfoBean data) {
		int index = -1;
		for(int i = 0; i < musicData.size(); i++) {
			musicInfoBean bean = musicData.get(i);
			/* 음악명과 아티스트가 동일하다면, true를 반환 */
			if (data.getMusicName().equals(bean.getMusicName()) && data.getArtist().equals(bean.getArtist())) {
				index = i;
				return index;
			}
		}
		return index;
	}
	
	
	public musicInfoBean getElementDataAt(int index) {
		return musicData.get(index);
	}
	
	@Override
	public void removeElementAt(int index) {
		musicData.remove(index);
		super.removeElementAt(index);
	}
	
	@Override
	public void removeAllElements() {
		musicData = new LinkedList<musicInfoBean>();
		super.removeAllElements();
	}
	
	
}
