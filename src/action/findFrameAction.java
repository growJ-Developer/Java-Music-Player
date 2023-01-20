package action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.findFrame;
import app.loginFrame;
import app.mainFrame;
import app.optionFrame;
import app.signUpFrame;
import bean.userBean;
import factory.serviceDAO;

public class findFrameAction{
	/* 찾기 버튼 Action*/
	public static class findBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField idField = (JTextField) mainFrame.getComponentByName("FIND_ID_FIELD");
			JTextField phoneField = (JTextField) mainFrame.getComponentByName("FIND_PHONE_FIELD");
			JTextField hintField = (JTextField) mainFrame.getComponentByName("FIND_HINT_FIELD");
			JComboBox hintCombo = (JComboBox) mainFrame.getComponentByName("FIND_HINT_COMBO");
			String userId = idField.getText();
			String phone = phoneField.getText();
			String hintAns = hintField.getText();
			int hintNum = findFrame.getSelectedHint(hintCombo.getSelectedIndex());
			
			userBean data = new userBean();
			data.setUserId(userId);
			data.setPhoneNumber(phone);
			data.setHintAns(hintAns);
			data.setHintNum(hintNum);
			
			serviceDAO dao = new serviceDAO();
			/* 비밀번호 찾기를 위한 정보가 일치한 경우 적용 버튼을 활성화 합니다 */
			if(dao.findPasswordCheck(data)) {
				JLabel pswLabel = (JLabel) mainFrame.getComponentByName("FIND_PSW_LABEL");
				pswLabel.setVisible(true);
				JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("FIND_PSW_FIELD");
				pswField.setVisible(true);
				JButton applyBtn = (JButton) mainFrame.getComponentByName("FIND_APPLY_BTN");
				applyBtn.setVisible(true);
			} else {
				optionFrame.alertMessageDialog("정보가 일치하지 않습니다.");
			}
		}
	}
	
	/* 변경 버튼 Action */
	public static class applyBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			JTextField idField = (JTextField) mainFrame.getComponentByName("FIND_ID_FIELD");
			JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("FIND_PSW_FIELD");
			
			userBean data = new userBean();
			data.setUserId(idField.getText());
			data.setUserPassword(String.valueOf(pswField.getPassword()));
			
			/* 비밀번호 체크를 진행합니다 */
			if(signUpFrameAction.checkPassword(data.getUserPassword(), data.getUserId()).length() != 0) {
				optionFrame.alertMessageDialog(signUpFrameAction.checkPassword(data.getUserPassword(), data.getUserId()));
				return;
			}
			
			/* 비밀번호에 이상이 없으므로 변경을 진행합니다 */
			serviceDAO dao = new serviceDAO();
			dao.setNewPassword(data);
			
			optionFrame.alertMessageDialog("비밀번호 변경이 완료되었습니다.");
			frameClear();
			
			/* 비밀번호 찾기 창 숨기기 */
			findFrame findFrm = (findFrame) mainFrame.getComponentByName("FIND_FRAME");
			findFrm.setVisible(false);
			
			/* 프레임 초기화 */
			frameClear();
			
			/* 로그인 창 보이기 */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(true);
		}
	}
	
	/* 이전 버튼 Action */
	public static class prevBtnActionListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			/* 비밀번호 찾기 창 숨기기 */
			findFrame findFrm = (findFrame) mainFrame.getComponentByName("FIND_FRAME");
			findFrm.setVisible(false);
			
			/* 프레임 초기화 */
			frameClear();
			
			/* 로그인 창 보이기 */
			loginFrame loginFrm = (loginFrame) mainFrame.getComponentByName("LOGIN_FRAME");
			loginFrm.setVisible(true);
		}
	}
	
	public static void frameClear() {
		JTextField idField = (JTextField) mainFrame.getComponentByName("FIND_ID_FIELD");
		JTextField phoneField = (JTextField) mainFrame.getComponentByName("FIND_PHONE_FIELD");
		JPasswordField pswField = (JPasswordField) mainFrame.getComponentByName("FIND_PSW_FIELD");
		JTextField hintField = (JTextField) mainFrame.getComponentByName("FIND_HINT_FIELD");
		JComboBox hintCombo = (JComboBox) mainFrame.getComponentByName("FIND_HINT_COMBO");
		JLabel pswLabel = (JLabel) mainFrame.getComponentByName("FIND_PSW_LABEL");
		JButton applyBtn = (JButton) mainFrame.getComponentByName("FIND_APPLY_BTN");
		
		pswLabel.setVisible(false);
		pswField.setVisible(false);
		applyBtn.setVisible(false);
		
		idField.setText("");
		pswField.setText("");
		hintField.setText("");
		phoneField.setText("");
		hintCombo.setSelectedIndex(0);
	}

}
