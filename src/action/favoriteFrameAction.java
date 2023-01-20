package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.favoriteFrame;
import app.listFrame;
import app.mainFrame;
import app.optionFrame;
import bean.musicInfoBean;
import factory.mp3Player;
import factory.mp3Tagger;
import factory.serviceDAO;
import factory.similarityUtil;
import model.groupListModel;
import model.playListModel;

public class favoriteFrameAction {	
	/* 그룹의 마우스 클릭에 대한 Action */
	public static class groupListActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 그룹을 선택했는지 확인합니다. */
			JList<playListModel> groupList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_GROUP_LIST");
			if(groupList.getSelectedIndex() >= 0) {
				/* 선택한 그룹에 대한 음악 목록을 로딩합니다. */
				groupListModel model = (groupListModel) groupList.getModel();
				int groupIndex = model.getElementDataAt(groupList.getSelectedIndex());
				favoriteFrm.setMusicList(groupIndex);
			}
		}
	}
	
	/* 파일 추가 버튼에 대한 Action */
	public static class addFromDiskBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 그룹을 선택했는지 확인합니다. */
			JList<playListModel> groupList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_GROUP_LIST");
			if(groupList.getSelectedIndex() >= 0) {
				groupListModel model = (groupListModel) groupList.getModel();
				int groupIndex = model.getElementDataAt(groupList.getSelectedIndex());
				
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Media Fules(mp3, wav)", "mp3", "wav");
				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(true);
				chooser.showOpenDialog((JButton)e.getSource());
				File[] files = chooser.getSelectedFiles();
				
				for(File file : files) {
					mp3Tagger tag = new mp3Tagger(file);
					musicInfoBean musicData = tag.getMp3Tag();
					
					serviceDAO dao = new serviceDAO();
					dao.addMusicAtGroup(musicData, groupIndex);
				}
				/* 리스트 설정를 재설정합니다 */
				favoriteFrm.setMusicList(groupIndex);
				if(files.length > 0) {
					optionFrame.alertMessageDialog("추가가 완료되었습니다.");
				}
			} else {
				optionFrame.alertMessageDialog("음악을 추가할 그룹을 먼저 선택해 주세요.");
			}
			
		}
	}

	/* 그룹 추가 버튼에 대한 Action */
	public static class groupAddBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			String groupName = optionFrame.getUserMessageDialog("그룹 추가", "추가할 그룹의 명칭을 지정해 주세요.", 3);
			
			if(groupName != null && groupName.trim().length() > 0) {
				serviceDAO dao = new serviceDAO();
				/* 해당 이름의 재생 목록이 있는지 점검합니다 */
				if (dao.checkGroup(groupName)) {					
					optionFrame.alertMessageDialog("이미 사용하고 있는 그룹명입니다. 다른 명칭을 지정해 주세요.");					
				} else {
					dao.addGroupList(groupName);					
					optionFrame.alertMessageDialog("그룹이 추가되었습니다.");
					int personId = dao.getPersonId();
					favoriteFrm.setGroupList(personId);
				}
			} else {
				optionFrame.alertMessageDialog("그룹 명칭을 공백으로 설정할 수 없습니다.");
			}
		}
	}
	
	/* groupMenu - 그룹 재생에 대한 Action */
	public static class groupPlayItemActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 그룹 리스트를 불러옵니다 */
			JList<playListModel> groupList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_GROUP_LIST");
			groupListModel groupModel = (groupListModel) groupList.getModel();
			int groupId = groupModel.getElementDataAt(groupList.getSelectedIndex());
			
			/* 플레이 리스트 프레임을 로딩합니다 */
			listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
			
			serviceDAO dao = new serviceDAO();
			ArrayList<musicInfoBean> data = dao.getGroupMusic(groupId);
			if(data.size() != 0) {
				/* 원래 리스트를 삭제합니다 */
				int listCnt = listFrm.getListSize();
				for(int i = 0; i < listCnt; i++) {
					listFrm.removeList(0, false);
				}
				
				int nonFileCnt = 0;
				for(musicInfoBean bean : data) {
					/* 파일이 모두 존재하는지 확인합니다 */
					File file = new File(bean.getFilePath());
					if(file.exists()) {
						listFrm.addList(bean);
					} else {
						nonFileCnt++;
					}
				}
				if(nonFileCnt != 0) {
					optionFrame.alertMessageDialog("음악 파일 경로를 확인할 수 없는 파일이 " + nonFileCnt + "개 발생하였습니다.");
				} else {
					/* 첫번째 곡을 재생합니다 */
					playFrameAction.playFisrt();
				}
				
			} else {
				optionFrame.alertMessageDialog("그룹에 음악이 존재하지 않습니다.");
			}
		}		
	}
	/* groupMenu - 그룹명 수정에 대한 Action */
	public static class groupEditItemActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 그룹 리스트를 불러옵니다 */
			JList<playListModel> groupList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_GROUP_LIST");
			groupListModel groupModel = (groupListModel) groupList.getModel();
			int groupId = groupModel.getElementDataAt(groupList.getSelectedIndex());
			
			String groupName = optionFrame.getUserMessageDialog("그룹명 변경", "변경할 그룹명을 입력해 주십시오.", 3);
			
			if(groupName.length() > 0) {
				serviceDAO dao = new serviceDAO();
				/* 해당 이름의 재생 목록이 있는지 점검합니다 */
				if (dao.checkGroup(groupName)) {	
					optionFrame.alertMessageDialog("이미 사용중인 그룹 이름입니다.");
				} else {
					dao.updateGroupName(groupId, groupName);
					favoriteFrm.setGroupList(dao.getPersonId());
					favoriteFrm.setMusicList(-1);
				}
			} else {
				optionFrame.alertMessageDialog("그룹명을 공백으로 설정이 불가능합니다.");
			}
		}
	}
	/* groupMenu - 그룹 삭제에 대한 Action */
	public static class groupDelItemActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 그룹 리스트를 불러옵니다 */
			JList<playListModel> groupList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_GROUP_LIST");
			groupListModel groupModel = (groupListModel) groupList.getModel();
			int groupId = groupModel.getElementDataAt(groupList.getSelectedIndex());
			String groupName = (String) groupModel.getElementAt(groupList.getSelectedIndex());
			
			String checkCode = optionFrame.getUserMessageDialog("그룹 삭제", "삭제 후에는 복구가 불가능합니다. 삭제하려면 [" + groupName + "]를 정확히 입력해 주십시오.", 3);
			if(checkCode != null && checkCode.equals(groupName)) {
				serviceDAO dao = new serviceDAO();
				dao.delGroup(groupId);
				
				favoriteFrm.setGroupList(dao.getPersonId());
				favoriteFrm.setMusicList(-1);
				optionFrame.alertMessageDialog("삭제되었습니다.");
			} else {
				optionFrame.alertMessageDialog("삭제가 취소되었습니다.");
			}
		}
	}
	
	/* musicMenu - 플레이 리스트 추가 */
	public static class musicPlayItemActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 플레이리스트를 불러옵니다 */
			JList<playListModel> musicList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_MUSIC_LIST");
			playListModel musicModel = (playListModel)musicList.getModel();
			int selIndices[] = musicList.getSelectedIndices();
			for(int i = 0; i < selIndices.length; i++) {
				musicInfoBean data = musicModel.getElementDataAt(selIndices[i]);
				/* 플레이 리스트 프레임을 로딩합니다 */
				listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
				
				/* 파일이 모두 존재하는지 확인합니다 */
				File file = new File(data.getFilePath());
				if(file.exists()) {
					listFrm.addList(data);
				} else {
					optionFrame.alertMessageDialog("해당 파일의 경로를 확인할 수 없으므로, 추가할 수 없습니다.");
				}				
			}						
		}
	}
	/* musicMenu - 음원 삭제 */
	public static class musicDelItemActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 그룹 리스트를 불러옵니다 */
			JList<playListModel> groupList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_GROUP_LIST");
			groupListModel groupModel = (groupListModel) groupList.getModel();
			int groupId = groupModel.getElementDataAt(groupList.getSelectedIndex());
			
			/* 플레이리스트를 불러옵니다 */
			JList<playListModel> musicList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_MUSIC_LIST");
			playListModel musicModel = (playListModel)musicList.getModel();
			int selIndices[] = musicList.getSelectedIndices();
			
			for(int i = 0; i < selIndices.length; i++) {
				musicInfoBean data = musicModel.getElementDataAt(selIndices[i]);	
				serviceDAO dao = new serviceDAO();
				dao.delMusicAtGroup(data, groupId);
			}
			optionFrame.alertMessageDialog("삭제되었습니다");
			
			favoriteFrm.setMusicList(groupId);
		}
	}
	
	/* 검색에 대한 Action	 */
	public static class searchFieldActionListener extends KeyAdapter{
		
		@Override
		public void keyTyped(KeyEvent e) {
			JList<playListModel> searchList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_SEARCH_LIST");
			JButton addFromDiskBtn = (JButton) mainFrame.getComponentByName("FAVORITE_ADD_BTN");
			JButton groupAddBtn = (JButton) mainFrame.getComponentByName("FAVORITE_GROUP_BTN");
			JSplitPane listPane = (JSplitPane) mainFrame.getComponentByName("FAVORITE_LIST");
			playListModel searchModel = (playListModel) searchList.getModel();
			searchModel.removeAllElements();
			
			JTextField searchField = (JTextField) e.getSource();
			String searchText = searchField.getText();
			
			if(!searchText.equals("")) {
				serviceDAO dao = new serviceDAO();
				ArrayList<musicInfoBean> data = dao.getGroupMusicSearch(dao.getPersonId(), searchText);
				for(musicInfoBean bean : data) {
					searchModel.addElement(bean);
				}					
				addFromDiskBtn.setVisible(false);
				groupAddBtn.setVisible(false);
				listPane.setVisible(false);
				searchList.setVisible(true);
			} else {
				listPane.setVisible(true);
				searchList.setVisible(false);
				favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
				favoriteFrm.checkUserProfile();
				//addFromDiskBtn.setVisible(true);
				//groupAddBtn.setVisible(true);
			}
		}
	}
	
	/* searchList - 플레이 리스트 추가 */
	public static class searchPlayItemActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* 프레임을 로딩합니다 */
			favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
			
			/* 플레이리스트를 불러옵니다 */
			JList<playListModel> musicList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_SEARCH_LIST");
			playListModel musicModel = (playListModel)musicList.getModel();
			int selIndices[] = musicList.getSelectedIndices();
			for(int i = 0; i < selIndices.length; i++) {
				musicInfoBean data = musicModel.getElementDataAt(selIndices[i]);
				/* 플레이 리스트 프레임을 로딩합니다 */
				listFrame listFrm = (listFrame) mainFrame.getComponentByName("LIST_PANEL");
				/* 플레이 리스트에 선택한 목록을 추가합니다 */
				listFrm.addList(data);
			}						
		}
	}
	
	/* 프레임 초기화 */
	public static void frameClear() {
		JList<playListModel> searchList = (JList<playListModel>) mainFrame.getComponentByName("FAVORITE_SEARCH_LIST");
		JSplitPane listPane = (JSplitPane) mainFrame.getComponentByName("FAVORITE_LIST");
		JTextField searchField =(JTextField) mainFrame.getComponentByName("FAVORITE_SEARCH_FIELD");
		JButton addFromDiskBtn = (JButton) mainFrame.getComponentByName("FAVORITE_ADD_BTN");
		JButton groupAddBtn = (JButton) mainFrame.getComponentByName("FAVORITE_GROUP_BTN");
		playListModel listModel = (playListModel) searchList.getModel();
		listModel.removeAllElements();
		
		JMenuItem groupEditItem = (JMenuItem) mainFrame.getComponentByName("FAVORITE_GROUP_EDIT_ITEM");
		JMenuItem groupDelItem = (JMenuItem) mainFrame.getComponentByName("FAVORITE_GROUP_DEL_ITEM");
		JMenuItem musicDelItem = (JMenuItem) mainFrame.getComponentByName("FAVORITE_MUSIC_DEL_ITEM");
		
		
		searchField.setText("");
		listPane.setVisible(true);
		searchList.setVisible(false);
		
		favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
		favoriteFrm.checkUserProfile();
		/*
		groupEditItem.setVisible(true);
		groupDelItem.setVisible(true);
		musicDelItem.setVisible(true);
		addFromDiskBtn.setVisible(true);
		groupAddBtn.setVisible(true);
		*/
		
	}
}
