package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import action.listFrameAction;
import bean.musicInfoBean;
import components.StripeRenderer;
import factory.mp3Tagger;
import model.playListModel;
import java.awt.FlowLayout;

public class listFrame extends JPanel{
	private playListModel model;
	private JList<playListModel> musicList;
		
	public listFrame() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		JPanel listPanel = new JPanel();
		model = new playListModel();
		musicList = new JList<playListModel>(model);
		musicList.setVisibleRowCount(100);
		musicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		// 하나만 선택 가능하도록 설정합니다.
		
		musicList.setFixedCellHeight(25);
		musicList.setFixedCellWidth(250);
		musicList.setBackground(new Color(238, 238, 238));
		musicList.setCellRenderer(new StripeRenderer());
		
		/* 플레이리스트에 대한 Action을 지정 */
		musicList.addMouseListener(new listFrameAction.playListActionListener());
		musicList.setName("PLAY_LIST");
		musicList.setBorder(null);
		
		/* 플레이리스트에 대한 PopupMenu 지정 */
		JPopupMenu musicMenu = new JPopupMenu() {
			@Override
			public void show(Component invoker, int x, int y) {
				int row = musicList.locationToIndex(new Point(x, y));
				if(row != -1 && row < model.getListSize()) {
					musicList.setSelectedIndex(row);
					super.show(invoker, x, y);
				}
			}
		};
		musicList.setComponentPopupMenu(musicMenu);
		JMenuItem playMenuItem = new JMenuItem("재생");
		playMenuItem.addActionListener(new listFrameAction.playMenuItemActionListener());
		JMenuItem delMenuItem = new JMenuItem("삭제");
		delMenuItem.addActionListener(new listFrameAction.delMeuItemActionListener());
		musicMenu.add(playMenuItem);
		musicMenu.add(delMenuItem);
		
		
		/* Disk에서 파일을 선택해서 PlayList에 추가하는 Action 지정 */
		JButton loadFBtn = new JButton("open");										
		loadFBtn.setBounds(169, 6, 75, 29);
		loadFBtn.addMouseListener(new listFrameAction.playListFromDiskListener());
		loadFBtn.setBackground(Color.white);
		
		/* PLAY_LIST에 스크롤을 추가합니다 */
		JScrollPane listScroll = new JScrollPane(musicList);
		listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroll.setBounds(0, 39, 250, 326);
		listScroll.setBackground(new Color(238, 238, 238));
		listScroll.setPreferredSize(new Dimension(250, 500));
		listScroll.setBorder(null);
		listPanel.setLayout(null);
		
		listPanel.add(loadFBtn);
		listPanel.add(listScroll);
		
		listPanel.setPreferredSize(new Dimension(250, 500));
		
		this.add(listPanel);
	}
	
	/* List에서 항목을 가져옵니다 */
	public musicInfoBean getList(int index) {
		return model.getElementDataAt(index);
	}
	
	/* List에서 항목을 제거합니다 */
	public void removeList(int index, boolean record) {
		/* 재생 항목 유지 옵션 선택시, 재생 항목을 PrevList에 저장합니다 */
		if(record) {
			ArrayList<musicInfoBean> prevList = mainFrame.prevList;
			if(prevList.size() >= 50) {
				/* prevList의 항목이 추가시 50개일 경우, 처음 한개를 삭제합니다 */
				prevList.remove(0);
			}
			
			musicInfoBean data = model.getElementDataAt(index);
			mainFrame.prevList.add(data);
		}
		model.removeElementAt(index);
	}
	
	/* List에 항목을 추가합니다. (musicInfoBean) */
	public void addList(musicInfoBean musicData) {
		model.addElement(musicData);
	}
	
	/* List에 항목을 추가합니다 (int index, musicInfoBean) */
	public void addListAt(int index, musicInfoBean musicData) {
		model.insertElementAt(musicData, index);
	}
	
	/* List에 항목을 추가합니다 (File) */
	public void addList(File file) {
		mp3Tagger tag = new mp3Tagger(file);
		musicInfoBean data = tag.getMp3Tag();
		data.setFilePath(file.getPath());
		data.setFileName(file.getName());
		addList(data);
	}
	
	/* List에 항목을 추가합니다 (Files) */
	public void addList(File[] files) {
		for(File file : files) {
			addList(file);			
		}
	}
	
	public int getListSize() {
		return model.getListSize();		
	}
}
