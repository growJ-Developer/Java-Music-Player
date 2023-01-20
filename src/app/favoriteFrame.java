package app;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import action.favoriteFrameAction;
import action.favoriteFrameAction.musicDelItemActionListener;
import bean.musicInfoBean;
import components.StripeRenderer;
import factory.serviceDAO;
import model.groupListModel;
import model.playListModel;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;

public class favoriteFrame extends JPanel{
	private int personId = -1;
	private groupListModel groupModel;
	private playListModel musicModel;
	private playListModel searchModel;
	private JTextField searchField;
	private JMenuItem groupEditItem;
	private JMenuItem groupDelItem;
	private JMenuItem musicDelItem;
	private JMenuItem musicPlayItem;
	private JMenuItem groupPlayItem;
	
	
	public favoriteFrame() {
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel favoritePanel = new JPanel();
		favoritePanel.setBackground(Color.WHITE);
		favoritePanel.setBounds(6, 6, 512, 362);
		favoritePanel.setLayout(null);
		
		/* 검색 창 설정 */
		searchField = new JTextField();
		searchField.setBounds(2, 3, 508, 30);
		searchField.setColumns(10);
		searchField.addKeyListener(new favoriteFrameAction.searchFieldActionListener());
		searchField.setName("FAVORITE_SEARCH_FIELD");
		
		/* 검색 전용 리스트 창 설정 */
		searchModel = new playListModel();
		JList<playListModel> searchList = new JList<playListModel>(searchModel);
		searchList.setName("FAVORITE_SEARCH_LIST");
		searchList.setBounds(6, 45, 500, 280);
		searchList.setFixedCellHeight(25);
		searchList.setFixedCellWidth(200);
		searchList.setPreferredSize(new Dimension(500, 280));
		searchList.setCellRenderer(new StripeRenderer());
		searchList.setVisible(false);
		
		/* 검색한 리스트의 PopupMenu 설정 */
		JPopupMenu searchMenu = new JPopupMenu() {
			@Override
			public void show(Component invoker, int x, int y) {
				int row = searchList.locationToIndex(new Point(x, y));
				if(row != -1 && row < searchModel.getSize()) {
					searchList.setSelectedIndex(row);
					super.show(invoker, x, y);
				}
			}
		};
		searchList.setComponentPopupMenu(searchMenu);
		JMenuItem searchPlayItem = new JMenuItem("플레이 리스트 추가");
		searchPlayItem.addActionListener(new favoriteFrameAction.searchPlayItemActionListener());
		searchMenu.add(searchPlayItem);
		
		/* 리스트 창 */
		JSplitPane listPane = new JSplitPane();
		listPane.setBounds(6, 45, 500, 280);
		listPane.setDividerLocation(0.4);
		listPane.setName("FAVORITE_LIST");
		
		/* 좌측 그룹 리스트에 대한 설정 */
		groupModel = new groupListModel();
		JList<groupListModel> groupList = new JList<groupListModel>(groupModel);
		groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupList.setFixedCellHeight(25);
		groupList.setFixedCellWidth(200);
		groupList.setCellRenderer(new StripeRenderer());
		groupList.setName("FAVORITE_GROUP_LIST");
		groupList.addMouseListener(new favoriteFrameAction.groupListActionListener());
		
		/* 좌측 그룹 리스트의 스크롤 설정 */
		JScrollPane groupPane = new JScrollPane(groupList);
		groupPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		groupPane.setBounds(0, 39, 200, 280);
		groupPane.setBackground(Color.LIGHT_GRAY);
		groupPane.setPreferredSize(new Dimension(200, 360));
		listPane.setLeftComponent(groupPane);
		
		/* 좌측 그룹 리스트의 우클릭에 대한 팝업메뉴를 설정합니다 */
		JPopupMenu groupMenu = new JPopupMenu() {
			@Override
			public void show(Component invoker, int x, int y) {
				int row = groupList.locationToIndex(new Point(x, y));
				if(row != -1 && row < groupModel.getSize()) {
					groupList.setSelectedIndex(row);
					super.show(invoker, x, y);
				}
			}
		};
		groupList.setComponentPopupMenu(groupMenu);
		/* groupList - 그룹 재생 */
		groupPlayItem = new JMenuItem("그룹 재생");
		groupPlayItem.addActionListener(new favoriteFrameAction.groupPlayItemActionListener());
		/* groupList - 그룹명 수정 */
		groupEditItem = new JMenuItem("그룹명 수정");
		groupEditItem.addActionListener(new favoriteFrameAction.groupEditItemActionListener());
		groupEditItem.setName("FAVORITE_GROUP_EDIT_ITEM");
		groupEditItem.setVisible(false);
		/* groupList - 그룹 삭제 */
		groupDelItem = new JMenuItem("그룹 삭제");
		groupDelItem.setName("FAVORITE_GROUP_DEL_ITEM");
		groupDelItem.setVisible(false);
		groupDelItem.addActionListener(new favoriteFrameAction.groupDelItemActionListener());
		
		groupMenu.add(groupPlayItem);
		groupMenu.add(groupEditItem);
		groupMenu.add(groupDelItem);
		groupMenu.setName("FAVORITE_GROUP_MENU");
		
		/* 우측 음악 리스트에 대한 설정 */
		musicModel = new playListModel();
		JList<playListModel> musicList = new JList<playListModel>(musicModel);
		musicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		musicList.setFixedCellHeight(25);
		musicList.setFixedCellWidth(3);
		musicList.setCellRenderer(new StripeRenderer());
		musicList.setName("FAVORITE_MUSIC_LIST");
		
		/* 우측 음악 리스트의 스크롤 설정 */
		JScrollPane musicPane = new JScrollPane(musicList);
		musicPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		musicPane.setBounds(0, 39, 250, 280);
		musicPane.setBackground(Color.LIGHT_GRAY);
		musicPane.setPreferredSize(new Dimension(300, 360));
		listPane.setRightComponent(musicPane);
	
		/* 우측 음악 리스트의 우클릭에 대한 팝업메뉴를 설정합니다 */
		JPopupMenu musicMenu = new JPopupMenu() {
			@Override
			public void show(Component invoker, int x, int y) {
				int row = musicList.locationToIndex(new Point(x, y));
				if(row != -1 && row < musicModel.getListSize()){
					musicList.setSelectedIndex(row);
					super.show(invoker, x, y);
				}
			}
		};
		musicList.setComponentPopupMenu(musicMenu);
		/* musicList - 플레이리스트 추가 */
		musicPlayItem = new JMenuItem("플레이리스트 추가");
		musicPlayItem.addActionListener(new favoriteFrameAction.musicPlayItemActionListener());
		/* musicList - 플레이리스트 추가 */
		musicDelItem = new JMenuItem("음악 삭제");
		musicDelItem.addActionListener(new favoriteFrameAction.musicDelItemActionListener());
		musicDelItem.setName("FAVORITE_MUSIC_DEL_ITEM");
		musicDelItem.setVisible(false);
		
		musicMenu.add(musicPlayItem);
		musicMenu.add(musicDelItem);
		musicMenu.setName("FAVORITE_MUSIC_MENU");
					
		/* 음악파일 추가 버튼 설정 */
		JButton addFromDiskBtn = new JButton("파일 추가");
		addFromDiskBtn.setBounds(400, 330, 110, 30);
		addFromDiskBtn.addMouseListener(new favoriteFrameAction.addFromDiskBtnActionListener());
		addFromDiskBtn.setName("FAVORITE_ADD_BTN");
		addFromDiskBtn.setVisible(false);
		addFromDiskBtn.setBackground(Color.white);
		
		/* 그룹 추가 버튼 설정 */
		JButton groupAddBtn = new JButton("그룹 추가");
		groupAddBtn.setBounds(0, 330, 110, 30);
		groupAddBtn.addMouseListener(new favoriteFrameAction.groupAddBtnActionListener());
		groupAddBtn.setName("FAVORITE_GROUP_BTN");
		groupAddBtn.setVisible(false);
		groupAddBtn.setBackground(Color.white);
		
		favoritePanel.add(searchList);
		favoritePanel.add(searchField);
		favoritePanel.add(listPane);
		favoritePanel.add(addFromDiskBtn);
		favoritePanel.add(groupAddBtn);
		
		this.add(favoritePanel);
	}
	
	/* personId에 따른 플레이리스트 그룹을 로딩합니다 */
	public void setGroupList(int personId) {
		this.personId = personId;
		groupModel.removeAllElements();
		musicModel.removeAllElements();
		
		serviceDAO dao = new serviceDAO();
		ArrayList<HashMap<Integer, String>> data = dao.getGroupList(personId);
		
		Iterator<HashMap<Integer, String>> it = data.iterator();
		while(it.hasNext()) {
			HashMap<Integer, String> mData = it.next();
			groupModel.addElement(mData);
		}
		
		checkUserProfile();		
	}
	
	/* 프로필과 검증해서 일치 하지 않을 경우 수정 Item을 비활성화 합니다. */
	public void checkUserProfile() {
		JButton groupAddBtn = (JButton) mainFrame.getComponentByName("FAVORITE_GROUP_BTN");
		JButton addFromDiskBtn = (JButton) mainFrame.getComponentByName("FAVORITE_ADD_BTN");
		
		serviceDAO dao = new serviceDAO();
		/* 본인 프로필이 아닌 경우 */
		if(personId != dao.getPersonId() || dao.getPersonId() == -1) {
			groupAddBtn.setVisible(false);
			addFromDiskBtn.setVisible(false);
			groupEditItem.setVisible(false);
			groupDelItem.setVisible(false);
			musicDelItem.setVisible(false);
		} else {
			groupAddBtn.setVisible(true);
			addFromDiskBtn.setVisible(true);
			groupEditItem.setVisible(true);
			groupDelItem.setVisible(true);
			musicDelItem.setVisible(true);
		}
	}
	
	/* groupIndex에 따른 플레이리스트를 로딩합니다 */
	public void setMusicList(int groupIndex) {
		musicModel.removeAllElements();
		
		serviceDAO dao = new serviceDAO();
		ArrayList<musicInfoBean> data = dao.getGroupMusic(groupIndex);
		
		Iterator<musicInfoBean> it = data.iterator();
		while(it.hasNext()) {
			musicModel.addElement(it.next());
		}
	}
}
