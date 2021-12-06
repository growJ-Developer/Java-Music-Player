package model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultListModel;

import bean.musicInfoBean;
import factory.mp3Tagger;

/* 음악 재생목록을 위한 ListModel */
public class playListModel extends DefaultListModel{
	private LinkedList<musicInfoBean> musicData = new LinkedList<musicInfoBean>();
	
	public playListModel() {
		super();
	}
	
	@Override
	public void addElement(Object element) {
		musicInfoBean data = (musicInfoBean) element;
		musicData.add(data);		
		String musicName = data.getMusicName();
		if(data.getArtist() != null && !data.getArtist().equals("")) {
			musicName = musicName + " - " + data.getArtist();
		}
		super.addElement(musicName);
	}
	
	@Override
	public void insertElementAt(Object element, int index) {
		musicInfoBean data = (musicInfoBean) element;
		musicData.add(index, data);	
		String musicName = data.getMusicName();
		if(data.getArtist() != null && !data.getArtist().equals("")) {
			musicName = musicName + " - " + data.getArtist();
		}		
		super.insertElementAt(musicName, index);
	}	
	
	
	public musicInfoBean getElementDataAt(int index) {
		return musicData.get(index);
	}
	
	@Override
	public void removeElementAt(int index) {
		musicData.remove(index);
		super.removeElementAt(index);
	}
}
