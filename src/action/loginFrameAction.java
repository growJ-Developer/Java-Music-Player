package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.chartFrame;
import app.findFrame;
import app.idFindFrame;
import app.loginFrame;
import app.signUpFrame;
import bean.userBean;
import factory.serviceDAO;
import app.mainFrame;
import app.optionFrame;

public class loginFrameAction {
	/* 로그인 버튼에 대한 Action */
	public static class loginBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField idField = (JTextField) mainFrame.getComponentByName("LOGIN_ID_FIELD");
			JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("LOGIN_PASSWORD_FIELD");
			String id = idField.getText();
			String password = String.valueOf(pswField.getPassword());
			
			userBean data = new userBean();
			data.setUserId(id);
			data.setUserPassword(password);
			
			/* 로그인을 진행합니다 */
			serviceDAO dao = new serviceDAO();
			dao.loginUser(data);
			
			if(serviceDAO.isLogin()) {
				/* 프레임 초기화 */
				frameClear();
				/* 로그인 성공 액션 */
				optionFrame.alertMessageDialog("로그인에 성공했습니다.");
				/* 메인 프레임 초기화 */
				menuFrameAction.frameClear();
				/* chart 화면으로 이동 */
				chartFrame chartFrm = (chartFrame) mainFrame.getComponentByName("CHART_FRAME");
				chartFrm.setVisible(true);
			} else {
				optionFrame.alertMessageDialog("ID또는 패스워드가 일치하지 않습니다. 5회 이상 실패 시 계정이 잠깁니다.");
			}
		}
		
	}
	
	/* 회원가입 버튼에 대한 Action */
	public static class signUpBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 로그인 창을 숨깁니다. */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(false);
			
			/* 프레임 초기화 */
			frameClear();
			
			/* 회원가입 창을 표시합니다 */
			signUpFrame signUpFrm = (signUpFrame) mainFrame.getComponentByName("SIGNUP_FRAME");
			signUpFrm.setVisible(true);
		}
	}
	
	/* ID 찾기 버튼에 대한 Aciton */
	public static class idFindBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 로그인 창을 숨깁니다. */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(false);
			
			/* 프레임 초기화 */
			frameClear();
			
			/* ID 찾기 창을 표시합니다 */
			idFindFrame idfindFrm = (idFindFrame) mainFrame.getComponentByName("ID_FIND_FRAME");
			idfindFrm.setVisible(true);
		}
	}
	
	/* 패스워드 찾기 버튼에 대한 Action */
	public static class findBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 로그인 창을 숨깁니다. */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(false);
			
			/* 프레임 초기화 */
			frameClear();
			
			/* 패스워드 찾기 창을 표시합니다 */
			findFrame findFrm = (findFrame) mainFrame.getComponentByName("FIND_FRAME");
			findFrm.setVisible(true);
		}
	}
	
	public static void frameClear() {
		JTextField idField = (JTextField) mainFrame.getComponentByName("LOGIN_ID_FIELD");
		JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("LOGIN_PASSWORD_FIELD");
		
		idField.setText("");
		pswField.setText("");
	}
}
