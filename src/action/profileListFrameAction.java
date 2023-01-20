package action;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;

import app.mainFrame;
import app.profileFrame;
import app.profileListFrame;
import bean.userBean;
import factory.serviceDAO;
import model.playListModel;
import model.userListModel;

public class profileListFrameAction {

	/* 사용자 검색 액션 */
	public static class searchFieldActionListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			JTextField searchField = (JTextField) mainFrame.getComponentByName("PROFILE_SEARCH_FIELD");
			profileListFrame.setModelFilter(searchField.getText());
		}
	}
	
	/* 사용자를 눌렀을 때 Action (Profile로 이동) */
	public static class userListActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JList<userListModel> list = (JList<userListModel>)e.getSource();
			/* 더블클릭을 진행했을 경우, 사용자의 PersonID를 통해 프로필창으로 이동합니다. */
			if (e.getClickCount() == 2) {
				userBean data = profileListFrame.getUserElement(list.getSelectedIndex());
				if(data != null) {	
					/* 프레임을 초기화합니다 */
					menuFrameAction.frameClear();
					/* 프로필 정보로 이동합니다 */
					profileFrame profileFrm = (profileFrame) mainFrame.getComponentByName("PROFILE_FRAME");
					profileFrm.setProfile(data.getPersonId());
					profileFrm.setVisible(true);
				}
			}
		}
	}
}
