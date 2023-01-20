package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.centerFrame;
import app.chartFrame;
import app.favoriteFrame;
import app.findFrame;
import app.loginFrame;
import app.mainFrame;
import app.profileFrame;
import app.profileListFrame;
import app.signUpFrame;
import factory.serviceDAO;

public class menuFrameAction {
	
	/* 즐겨찾기 버튼에 대한 Action */
	public static class favoriteBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			if(serviceDAO.isLogin()) {		// 로그인 한 사용자의 경우
				/* 프레임 초기화 */
				frameClear();
				favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
				favoriteFrm.setVisible(true);
				/* DB로 부터 Group 로딩을 수행합니다 */
				favoriteFrm.setGroupList(new serviceDAO().getPersonId());
			} else {
				/* 프레임 초기화 */
				frameClear();
				/* 로그인 프레임으로 이동 */
				loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
				loginFrm.setVisible(true);
			}
		}
	}

	/* MusicChart 버튼에 대한 Action */
	public static class chartBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 프레임 초기화 */
			frameClear();
			chartFrame chartFrm = (chartFrame) mainFrame.getComponentByName("CHART_FRAME");
			chartFrm.setVisible(true);
			/* 차트 리스트를 DB로 부터 다시 조회합니다 */
			chartFrm.setChartList();
		}
	}
	
	/* 사용자 버튼에 대한 Action */
	public static class userBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			if(serviceDAO.isLogin()) {		// 로그인 한 사용자의 경우
				frameClear();
				profileFrame profileFrm = (profileFrame) mainFrame.getComponentByName("PROFILE_FRAME");
				/* 본인 프로필로 로딩합니다 */
				profileFrm.setProfile(new serviceDAO().getPersonId());
				profileFrm.setVisible(true);
			} else {								//	로그인 하지 않은 사용자
				/* 프레임 초기화 */
				frameClear();
				/* 로그인 프레임으로 이동 */
				loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
				loginFrm.setVisible(true);
			}
		}		
	}
	
	/* 버튼 전환 시 모든 프레임 초기화 작업을 진행합니다 */
	public static void frameClear() {
		chartFrame chartFrm = (chartFrame) mainFrame.getComponentByName("CHART_FRAME");
		loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
		signUpFrame signUpFrm = (signUpFrame) mainFrame.getComponentByName("SIGNUP_FRAME");
		findFrame findFrm = (findFrame) mainFrame.getComponentByName("FIND_FRAME");
		profileFrame profileFrm = (profileFrame) mainFrame.getComponentByName("PROFILE_FRAME");
		profileListFrame profileListFrm = (profileListFrame) mainFrame.getComponentByName("PROFILE_LIST_FRAME");
		favoriteFrame favoriteFrm = (favoriteFrame) mainFrame.getComponentByName("FAVORITE_FRAME");
		
		chartFrm.setVisible(false);
		loginFrm.setVisible(false);
		signUpFrm.setVisible(false);
		findFrm.setVisible(false);
		profileFrm.setVisible(false);
		profileListFrm.setVisible(false);
		favoriteFrm.setVisible(false);
		
		/* 하위 프레임 요소의 초기화 작업도 호출합니다 */
		loginFrameAction.frameClear();						// 로그인 프레임
		signUpFrameAction.frameClear();					// 회원가입 프레임 
		idFindFrameAction.frameClear();						// ID 찾기 프레
		findFrameAction.frameClear();							// PW 찾기 프레임 
		profileFrameAction.frameClear();						// 프로필 프레임
		//profileListFrame.frameClear();							// 프로필 리스트 프레
		favoriteFrameAction.frameClear();					// 즐겨찾기 프레임
	}
}
