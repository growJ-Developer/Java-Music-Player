package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTextField;

import app.idFindFrame;
import app.loginFrame;
import app.mainFrame;
import app.optionFrame;
import bean.userBean;
import factory.serviceDAO;

public class idFindFrameAction {

	/* ID 찾기 버튼에 대한 Action */
	public static class findBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField nameField = (JTextField) mainFrame.getComponentByName("IDFIND_NAME_FIELD");
			JTextField phoneField = (JTextField) mainFrame.getComponentByName("IDFIND_PHONE_FIELD");
			
			String name = nameField.getText();
			String phone = phoneField.getText();
			
			userBean data = new userBean();
			data.setUserName(name);
			data.setPhoneNumber(phone);
			
			serviceDAO dao = new serviceDAO();
			String findId = "";
			ArrayList<String> findIds = dao.findUserId(data);
			for(String id : findIds) {
				findId = findId + id + ", ";
			}
			if(findId.length() != 0) findId = findId.substring(0, findId.length() - 2);
			
			if(findId.length() != 0) {
				optionFrame.alertMessageDialog("사용자 ID는 [" + findId + "]입니다.");
				
				/* ID 찾기 창을 숨깁니다 */
				idFindFrame idfindFrm = (idFindFrame) mainFrame.getComponentByName("ID_FIND_FRAME");
				idfindFrm.setVisible(true);
				
				/* 프레임 초기화 */
				frameClear();
				
				/* 로그인 창을 표시합니다. */
				loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
				loginFrm.setVisible(false);
			} else {
				optionFrame.alertMessageDialog("일치하는 ID를 찾을 수 없습니다.");
			}
		}
	}
	
	/* 이전 버튼에 대한 Action */
	public static class prevBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* ID 찾기 창을 숨깁니다 */
			idFindFrame idfindFrm = (idFindFrame) mainFrame.getComponentByName("ID_FIND_FRAME");
			idfindFrm.setVisible(false);
			
			/* 프레임 초기화 */
			frameClear();
			
			/* 로그인 창을 표시합니다. */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(true);
		}
	}
	
	/* 프레임 내 요소 모두 초기화 */
	public static void frameClear() {
		JTextField nameField = (JTextField) mainFrame.getComponentByName("IDFIND_NAME_FIELD");
		JTextField phoneField = (JTextField) mainFrame.getComponentByName("IDFIND_PHONE_FIELD");
		
		nameField.setText("");
		phoneField.setText("");
	}
}
